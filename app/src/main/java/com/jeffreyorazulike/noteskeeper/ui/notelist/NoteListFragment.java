package com.jeffreyorazulike.noteskeeper.ui.notelist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.databinding.ContentNoteListBinding;
import com.jeffreyorazulike.noteskeeper.databinding.FragmentNoteListBinding;

public class NoteListFragment extends Fragment {
    public enum ARGUMENTS{
        NOTE_POSITION,
        SHOW;

        public enum SHOW_VALUES{
            NOTES, COURSES
        }
    }

    private NoteListViewModel mNoteListViewModel;
    private ContentNoteListBinding mContentBinding;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private FragmentNoteListBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNoteListViewModel = new ViewModelProvider(this).get(NoteListViewModel.class);

        mBinding = FragmentNoteListBinding.inflate(inflater, container, false);
        mContentBinding = mBinding.iRvNotes;
        mLinearLayoutManager = new LinearLayoutManager(inflater.getContext());
        mGridLayoutManager = new GridLayoutManager(inflater.getContext(), 2);

        mBinding.fabAddNote.setOnClickListener(view -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_note_list_to_nav_note);
        });

        if(getArguments() != null){
            ARGUMENTS.SHOW_VALUES argument = (ARGUMENTS.SHOW_VALUES) getArguments().getSerializable(ARGUMENTS.SHOW.name());
            switch (argument){
                case COURSES:
                    displayCourses();
                    break;
                case NOTES:
                default:
                    displayNotes();
            }
        }

        selectMenu(R.id.nav_notes);
        return mBinding.getRoot();
    }

    private void displayNotes(){
        mBinding.fabAddNote.setVisibility(View.VISIBLE);
        mContentBinding.rvNotes.setLayoutManager(mLinearLayoutManager);
        mNoteListViewModel.initNotes(requireActivity());
        mContentBinding.rvNotes.setAdapter(mNoteListViewModel.getAdapter());
    }

    private void displayCourses(){
        mBinding.fabAddNote.setVisibility(View.GONE);
        mContentBinding.rvNotes.setLayoutManager(mGridLayoutManager);
        mNoteListViewModel.initCourses();
        mContentBinding.rvNotes.setAdapter(mNoteListViewModel.getAdapter());
    }

    private void selectMenu(final int id){
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        MenuItem item = navigationView.getMenu().findItem(id);
        item.setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteListViewModel.getAdapter().notifyDataSetChanged();
    }
}