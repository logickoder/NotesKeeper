package com.jeffreyorazulike.noteskeeper.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.adapters.CourseAdapter;
import com.jeffreyorazulike.noteskeeper.db.dao.CourseInfo;
import com.jeffreyorazulike.noteskeeper.db.dao.DataManager;
import com.jeffreyorazulike.noteskeeper.db.dao.ModuleInfo;
import com.jeffreyorazulike.noteskeeper.databinding.DialogModuleBinding;
import com.jeffreyorazulike.noteskeeper.databinding.FragmentCourseBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CourseFragment extends Fragment{
    private CourseInfo mCourse;
    private ModuleInfo mModule;
    private int mCoursePosition;
    private boolean mIsNewCourse;

    private CourseViewModel mViewModel;
    private FragmentCourseBinding mBinding;
    private DialogModuleBinding mDialogBinding;

    private CourseAdapter mCourseAdapter;
    private AlertDialog mDialog;
    private boolean mIsCancelling;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mBinding = FragmentCourseBinding.inflate(inflater, container, false);
        mDialogBinding = DialogModuleBinding.inflate(inflater, mBinding.getRoot(), false);
        mDialog = new AlertDialog.Builder(requireContext())
                .setView(mDialogBinding.getRoot()).create();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDisplayContent();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mIsCancelling)
            if(mIsNewCourse)
                deleteCourse();
            else DataManager.getInstance().getCourses().set(mCoursePosition, mViewModel.getCourse());
        // saves the course but deletes the course if all fields are empty
        else if(!saveCourse()) deleteCourse();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if(id == R.id.action_cancel) mIsCancelling = true;
            else if (id == R.id.action_delete) deleteCourse();

        Navigation.findNavController(mBinding.getRoot()).popBackStack();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NotNull final Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_mail).setVisible(false);
        menu.findItem(R.id.action_next).setVisible(false);
    }

    private void initializeDisplayContent(){
        // get the position of the course to be displayed
        final int POSITION_NOT_SET = requireContext().getResources().getInteger(R.integer.position_not_set);
        if(getArguments() != null){
            mCoursePosition = getArguments().getInt(requireContext().getString(R.string.position), POSITION_NOT_SET);
        }
        mIsNewCourse = mCoursePosition == POSITION_NOT_SET;

        if(mIsNewCourse)createNewCourse();
        else{
            mCourse = DataManager.getInstance().getCourses().get(mCoursePosition);
        }

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recyclerView.setAdapter(mCourseAdapter = new CourseAdapter(mCourse.getModules(), requireContext()));

        // save the original course in the view model
        mViewModel.saveCourse(mCourse);
        // set on click listeners for the buttons
        mBinding.ivAdd.setOnClickListener(view -> addNewModule());
        mDialogBinding.btnUpdate.setOnClickListener(view -> {
            updateModule(mModule);
            mDialog.dismiss();
        });
        //update the module ids whenever the course id is changed
        Objects.requireNonNull(mBinding.tilCourseId.getEditText())
                .setOnFocusChangeListener((view, focus) -> {
            if(!focus) updateModulesId();
        });
        displayCourse();
        // listen for changes in the View holder observable
        // and update the modules whenever a change is detected
        mCourseAdapter.getObservable().observe(getViewLifecycleOwner(), holder -> {
            displayModuleInfo(mCourse.getModules().get(holder.getAdapterPosition()));
        });
    }

    private void createNewCourse() {
        DataManager dm = DataManager.getInstance();
        mCourse = dm.getCourses().get(mCoursePosition = dm.createNewCourse());
    }

    private void displayModuleInfo(ModuleInfo moduleInfo){
        mDialog.show();
        mModule = moduleInfo;
        Objects.requireNonNull(mDialogBinding.tilModuleId.getEditText())
                .setText(moduleInfo.getModuleId());
        Objects.requireNonNull(mDialogBinding.tilModuleTitle.getEditText())
                .setText(moduleInfo.getTitle());
        mDialogBinding.spIsCompleted.setSelection(Arrays.asList(
                getResources().getStringArray(R.array.true_false))
                .indexOf(String.valueOf(moduleInfo.isComplete())));
    }

    private void displayCourse() {
        Objects.requireNonNull(mBinding.tilCourseId.getEditText())
                .setText(mCourse.getCourseId());
        Objects.requireNonNull(mBinding.tilCourseName.getEditText())
                .setText(mCourse.getTitle());
    }

    /**
     * Saves the current course
     *
     * @return a boolean indicating if all fields required to save the course are filled
     *
     * */
    private boolean saveCourse(){
        final Date date = new Date();
        final String courseId = Objects.requireNonNull(
                mBinding.tilCourseId.getEditText()).getText().toString();
        final String title = Objects.requireNonNull(
                mBinding.tilCourseName.getEditText()).getText().toString();
        final String genCourseId = String.format(Locale.getDefault(),
                "%d_%d_%d", date.getSeconds(), date.getMinutes(), date.getHours());
        final String genTitle = String.format(Locale.getDefault(),
                "(%d/%d/%d)", date.getDay(), date.getMonth(), date.getYear());

        // saves the course id and title if the user provided them
        // else saves a generated id/title
        mCourse.setCourseId(courseId.isEmpty() ? genCourseId : correctedId(courseId));
        mCourse.setTitle(title.isEmpty() ? genTitle : title);

        return !(courseId.isEmpty() && title.isEmpty() && mCourse.getModules().isEmpty());
    }

    /**Deletes the course at mCoursePosition*/
    private void deleteCourse(){
        DataManager.getInstance().removeCourseAt(mCoursePosition);
    }

    /**
     * Creates a new empty module and adds it to the list of modules the course has
     * */
    private void addNewModule() {
        mModule = new ModuleInfo("", "", false);
        mCourse.getModules().add(mModule);
        // notify the recycler view that a new item has been added to the
        // end of the list
        mCourseAdapter.notifyItemInserted(mCourseAdapter.getItemCount() - 1);
    }

    /**Updates the values in the module dialog with this new module*/
    private void updateModule(ModuleInfo moduleInfo){
        moduleInfo.setModuleId(Objects.requireNonNull(
                mDialogBinding.tilModuleId.getEditText()).getText().toString());
        moduleInfo.setTitle(Objects.requireNonNull(
                mDialogBinding.tilModuleTitle.getEditText()).getText().toString());
        moduleInfo.setComplete(Boolean.parseBoolean(
                mDialogBinding.spIsCompleted.getSelectedItem().toString()));
    }

    private void updateModulesId(){
        final String courseId = correctedId(Objects.requireNonNull(
                mBinding.tilCourseId.getEditText()).getText().toString());
        if(!courseId.isEmpty()){
            mBinding.tilCourseId.getEditText().setText(courseId);
            // walk through all the modules and give them a new module containing
            // the course id and the position of the module in the list
            for (int count = 0; count < mCourseAdapter.getItemCount(); ++count) {
                mCourseAdapter.getItems().get(count).setModuleId(
                        courseId.concat(String.format(Locale.getDefault(),"_m%02d",count + 1)));
            }
            mCourseAdapter.notifyDataSetChanged();
        }
    }

    private String correctedId(String id){
        return id.toLowerCase().replaceAll("_"," ")
                .trim().replaceAll("\\s+","_");
    }
}