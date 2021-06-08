package com.jeffreyorazulike.noteskeeper.ui.home;

import androidx.lifecycle.ViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jeffreyorazulike.noteskeeper.adapters.CoursesAdapter;
import com.jeffreyorazulike.noteskeeper.adapters.NotesAdapter;
import com.jeffreyorazulike.noteskeeper.adapters.NotesKeeperAdapter;
import com.jeffreyorazulike.noteskeeper.db.dao.DataManager;

public class HomeViewModel extends ViewModel {

    private NotesKeeperAdapter mNotesKeeperAdapter;
    private NotesAdapter mNotesAdapter;
    private CoursesAdapter mCoursesAdapter;

    private HomeFragment.SHOW_VALUES currentScreen = HomeFragment.SHOW_VALUES.NOTES;

    void init(FloatingActionButton floatingActionButton){
        switch (currentScreen){
            case NOTES:
                if(mNotesAdapter == null)
                    mNotesAdapter = new NotesAdapter(
                            DataManager.getInstance().getNotes(), floatingActionButton.getContext());
                floatingActionButton.setOnClickListener(mNotesAdapter);
                mNotesKeeperAdapter = mNotesAdapter;
                break;
            case COURSES:
                if(mCoursesAdapter == null)
                    mCoursesAdapter = new CoursesAdapter(
                            DataManager.getInstance().getCourses(), floatingActionButton.getContext());
                floatingActionButton.setOnClickListener(mCoursesAdapter);
                mNotesKeeperAdapter = mCoursesAdapter;
                break;
        }
    }

    NotesKeeperAdapter getAdapter(){
        return mNotesKeeperAdapter;
    }

    void setCurrentScreen(HomeFragment.SHOW_VALUES currentScreen) {
        this.currentScreen = currentScreen;
    }

    HomeFragment.SHOW_VALUES getCurrentScreen() {
        return currentScreen;
    }
}