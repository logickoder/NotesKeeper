package com.jeffreyorazulike.noteskeeper.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.jeffreyorazulike.noteskeeper.NotesKeeperAdapter;
import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.dao.CourseInfo;
import com.jeffreyorazulike.noteskeeper.dao.DataManager;
import com.jeffreyorazulike.noteskeeper.dao.NoteInfo;
import com.jeffreyorazulike.noteskeeper.databinding.ItemCourseListBinding;
import com.jeffreyorazulike.noteskeeper.databinding.ItemNoteListBinding;

public class HomeViewModel extends ViewModel {

    private NotesKeeperAdapter mNotesKeeperAdapter;
    HomeFragment.ARGUMENTS.SHOW_VALUES currentScreen = HomeFragment.ARGUMENTS.SHOW_VALUES.NOTES;

    void initNotes(final Activity activity){
        mNotesKeeperAdapter = new NotesKeeperAdapter<NoteInfo, ItemNoteListBinding>(
            DataManager.getInstance().getNotes(),
            parent -> ItemNoteListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
            (note, binding) -> {
                binding.tvTitle.setText(note.getTitle());
                binding.tvCourse.setText(note.getCourse().toString());
            },
            position -> view -> {
                Bundle bundle = new Bundle(1);
                bundle.putInt(HomeFragment.ARGUMENTS.NOTE_POSITION.name(), position);
                final NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_list_to_nav_note, bundle);
        });
    }

    void initCourses(){
        mNotesKeeperAdapter = new NotesKeeperAdapter<CourseInfo, ItemCourseListBinding>(
            DataManager.getInstance().getCourses(),
            parent -> ItemCourseListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
            (course, binding) -> binding.tvCourse.setText(course.toString()),
            position -> view -> {
                Snackbar.make(view,DataManager.getInstance().getCourses().get(position).toString(),Snackbar.LENGTH_LONG);
        });
    }

    public NotesKeeperAdapter getAdapter(){
        return mNotesKeeperAdapter;
    }
}