package com.jeffreyorazulike.noteskeeper.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.databinding.ContentNoteListBinding;
import com.jeffreyorazulike.noteskeeper.databinding.FragmentHomeBinding;
import com.jeffreyorazulike.noteskeeper.ui.MainActivity;

import static com.jeffreyorazulike.noteskeeper.db.dao.DataManager.INSTANCE;

public class HomeFragment extends Fragment {

    public enum SHOW_VALUES{NOTES, COURSES}

    private HomeViewModel mHomeViewModel;
    private ContentNoteListBinding mContentBinding;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private FragmentHomeBinding mBinding;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mContentBinding = mBinding.iRvNotes;
        mLinearLayoutManager = new LinearLayoutManager(inflater.getContext());
        mGridLayoutManager = new GridLayoutManager(inflater.getContext(), requireActivity().getResources().getInteger(R.integer.course_grid_span));

        ((MainActivity) requireActivity()).getHomeFragmentLiveData()
                .observeForever(this::changeDisplay);
        changeDisplay(mHomeViewModel.getCurrentScreen());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeViewModel.getAdapter().notifyDataSetChanged();
    }

    private void changeDisplay(final SHOW_VALUES show_values) {
        mHomeViewModel.setCurrentScreen(show_values);
        if(!isAdded())
            Navigation.findNavController(
                    requireActivity(), R.id.nav_host_fragment).navigateUp();


        mHomeViewModel.init(mBinding.fabAdd, INSTANCE.get());
        switch (show_values){
            case NOTES: mContentBinding.rvNotes.setLayoutManager(mLinearLayoutManager);
                break;
            case COURSES: mContentBinding.rvNotes.setLayoutManager(mGridLayoutManager);
                break;
        }
        mContentBinding.rvNotes.setAdapter(mHomeViewModel.getAdapter());
    }
}