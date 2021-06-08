package com.jeffreyorazulike.noteskeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.db.dao.CourseInfo;
import com.jeffreyorazulike.noteskeeper.databinding.ItemCourseListBinding;

import java.util.List;

/**
 * The adapter class for the courses fragment screen (embedded in the home screen)
 **/
public class CoursesAdapter extends NotesKeeperAdapter<CourseInfo>{
    public CoursesAdapter(final List<CourseInfo> items, final Context context) {
        super(items, context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ViewHolder(ItemCourseListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mBinding = holder.getBinding();
        ((ItemCourseListBinding) mBinding).tvCourse.setText(mItems.get(position).toString());

        mBinding.getRoot().setOnClickListener(view -> {
            mObservableViewHolder.setValue(holder);
            onClick(view);
        });
    }

    @Override
    public void onClick(final View view) {
        mNavigateTo = R.id.action_nav_note_list_to_nav_course;
        baseListener.onClick(view);
    }
}
