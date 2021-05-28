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

import com.jeffreyorazulike.noteskeeper.NotesKeeperInterfaces;
import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.databinding.ContentNoteListBinding;
import com.jeffreyorazulike.noteskeeper.databinding.FragmentHomeBinding;

import static com.jeffreyorazulike.noteskeeper.ui.home.HomeFragment.ARGUMENTS.SHOW_VALUES.NOTES;

public class HomeFragment extends Fragment implements NotesKeeperInterfaces.Observable<HomeFragment.ARGUMENTS.SHOW_VALUES> {
    public enum ARGUMENTS{
        NOTE_POSITION;

        public enum SHOW_VALUES{
            NOTES, COURSES
        }
    }

    private HomeViewModel mHomeViewModel;
    private ContentNoteListBinding mContentBinding;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private FragmentHomeBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mContentBinding = mBinding.iRvNotes;
        mLinearLayoutManager = new LinearLayoutManager(inflater.getContext());
        mGridLayoutManager = new GridLayoutManager(inflater.getContext(), requireActivity().getResources().getInteger(R.integer.course_grid_span));

        mBinding.fabAddNote.setOnClickListener(view -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_note_list_to_nav_note);
        });

        ((NotesKeeperInterfaces.Observer<ARGUMENTS.SHOW_VALUES>) requireActivity()).bind(this);
        work(NOTES);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void displayNotes(){
        mBinding.fabAddNote.setVisibility(View.VISIBLE);
        mContentBinding.rvNotes.setLayoutManager(mLinearLayoutManager);
        mHomeViewModel.initNotes(requireActivity());
        mContentBinding.rvNotes.setAdapter(mHomeViewModel.getAdapter());
    }

    private void displayCourses(){
        mBinding.fabAddNote.setVisibility(View.GONE);
        mContentBinding.rvNotes.setLayoutManager(mGridLayoutManager);
        mHomeViewModel.initCourses();
        mContentBinding.rvNotes.setAdapter(mHomeViewModel.getAdapter());
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeViewModel.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void work(final ARGUMENTS.SHOW_VALUES show_values) {
        switch (show_values){
            case COURSES:
                displayCourses();
                break;
            case NOTES:
                displayNotes();
                break;
        }
    }
}