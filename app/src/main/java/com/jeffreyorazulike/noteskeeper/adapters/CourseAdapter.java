package com.jeffreyorazulike.noteskeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.jeffreyorazulike.noteskeeper.db.dao.ModuleInfo;
import com.jeffreyorazulike.noteskeeper.databinding.ItemModuleListBinding;

import java.util.List;

public class CourseAdapter extends NotesKeeperAdapter<ModuleInfo>{
    public CourseAdapter(final List<ModuleInfo> items, final Context context) {
        super(items, context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ViewHolder(ItemModuleListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mBinding = holder.getBinding();
        final ItemModuleListBinding binding = (ItemModuleListBinding) mBinding;
        final ModuleInfo module = mItems.get(holder.getAdapterPosition());

        binding.tvModuleId.setText(module.getModuleId());
        binding.tvModuleTitle.setText(module.getTitle());
        binding.tvIsCompleted.setText(String.valueOf(module.isComplete()));

        // delete the module when the delete button is clicked
        binding.ivDelete.setOnClickListener(view -> {
            mItems.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

        mBinding.getRoot().setOnClickListener(view -> {
            mObservableViewHolder.setValue(holder);
        });
    }

    @Override
    public void onClick(final View view) { }
}
