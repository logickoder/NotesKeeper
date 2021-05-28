package com.jeffreyorazulike.noteskeeper.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.dao.CourseInfo;
import com.jeffreyorazulike.noteskeeper.dao.DataManager;
import com.jeffreyorazulike.noteskeeper.dao.NoteInfo;
import com.jeffreyorazulike.noteskeeper.databinding.ContentNoteBinding;
import com.jeffreyorazulike.noteskeeper.databinding.FragmentNoteBinding;
import com.jeffreyorazulike.noteskeeper.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteFragment extends Fragment {
    public static final int POSITION_NOT_SET = -1;

    private NoteInfo mNote;
    private boolean mIsNewNote;
    private boolean mIsCancelling;
    private int mNotePosition;

    private NoteViewModel mViewModel;
    private ContentNoteBinding                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             mBinding;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        final FragmentNoteBinding binding = FragmentNoteBinding.inflate(inflater, container, false);
        mBinding = binding.iNote;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDisplayContent();
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.saveState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mIsCancelling)
            if(mIsNewNote)
                DataManager.getInstance().removeNote(mNotePosition);
            else restoreOriginalNoteValues();
        else saveNote();
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

        if(id == R.id.action_mail)
            sendNoteAsEmail();
        else if(id == R.id.action_cancel) {
            mIsCancelling = true;
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack();
        }else if(id == R.id.action_next)
            nextNote();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NotNull final Menu menu) {
        final MenuItem next = menu.findItem(R.id.action_next);
        final int lastIndex = DataManager.getInstance().getNotes().size() - 1;
        next.setEnabled(mNotePosition < lastIndex);
    }

    private void initializeDisplayContent(){
        mViewModel.mIsNewlyCreated = false;

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> listAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courses);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerCourses.setAdapter(listAdapter);

        if(getArguments() != null){
            mNotePosition = getArguments().getInt(HomeFragment.ARGUMENTS.NOTE_POSITION.name(), POSITION_NOT_SET);
        }
        mIsNewNote = mNotePosition == POSITION_NOT_SET;

        if(mIsNewNote){
            createNewNote();
        }else{
            mNote = DataManager.getInstance().getNotes().get(mNotePosition);
            displayNote();
            saveOriginalNoteValues();
        }
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    private void displayNote() {
        mBinding.spinnerCourses.setSelection(
                DataManager.getInstance().getCourses().indexOf(mNote.getCourse()));
        mBinding.editTextTitle.setText(mNote.getTitle());
        mBinding.editTextNote.setText(mNote.getText());
    }

    private void nextNote() {
        saveNote();

        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();
        displayNote();

        requireActivity().invalidateOptionsMenu();
    }

    private void saveOriginalNoteValues() {
        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    private void restoreOriginalNoteValues() {
        mNote.setCourse(DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId));
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void sendNoteAsEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");

        intent.putExtra(Intent.EXTRA_SUBJECT, mBinding.editTextTitle.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT,
                "Checkout what I learnt in the Plurasight course \""
                        + ((CourseInfo) mBinding.spinnerCourses.getSelectedItem()).getTitle() + "\"\n"
                        + mBinding.editTextNote.getText().toString());

        startActivity(intent);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mBinding.spinnerCourses.getSelectedItem());
        mNote.setTitle(mBinding.editTextTitle.getText().toString());
        mNote.setText(mBinding.editTextNote.getText().toString());
    }
}