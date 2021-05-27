package com.jeffreyorazulike.noteskeeper.ui.note;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {

    public static final String ORIGINAL_COURSE_ID = "com.jeffreyorazulike.noteskeeper.ui.note.ORIGINAL_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.jeffreyorazulike.noteskeeper.ui.note.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.jeffreyorazulike.noteskeeper.ui.note.ORIGINAL_NOTE_TEXT";

    public String mOriginalCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;

    public boolean mIsNewlyCreated = true;

    public void saveState(final Bundle outState) {
        outState.putString(ORIGINAL_COURSE_ID, mOriginalCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    public void restoreState(final Bundle  inState){
        mOriginalCourseId = (String) inState.get(ORIGINAL_COURSE_ID);
        mOriginalNoteTitle = (String) inState.get(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = (String) inState.get(ORIGINAL_NOTE_TEXT);
    }
}