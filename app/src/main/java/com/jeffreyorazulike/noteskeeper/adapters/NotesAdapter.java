package com.jeffreyorazulike.noteskeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.jeffreyorazulike.noteskeeper.R;
import com.jeffreyorazulike.noteskeeper.db.dao.NoteInfo;
import com.jeffreyorazulike.noteskeeper.databinding.ItemNoteListBinding;

import java.util.List;

public class NotesAdapter extends NotesKeeperAdapter<NoteInfo>{

    public NotesAdapter(final List<NoteInfo> items, Context context) {
        super(items, context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ViewHolder(ItemNoteListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mBinding = holder.getBinding();
        final ItemNoteListBinding binding = (ItemNoteListBinding) mBinding;
        final NoteInfo note = mItems.get(position);

        binding.tvTitle.setText(note.getTitle());
        binding.tvCourse.setText(note.getCourse().toString());

        mBinding.getRoot().setOnClickListener(view -> {
            mObservableViewHolder.setValue(holder);
            onClick(view);
        });
    }

    @Override
    public void onClick(final View view) {
        mNavigateTo = R.id.action_nav_note_list_to_nav_note;
        baseListener.onClick(view);
    }
}
