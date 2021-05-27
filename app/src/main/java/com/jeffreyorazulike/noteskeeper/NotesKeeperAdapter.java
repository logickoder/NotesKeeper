package com.jeffreyorazulike.noteskeeper;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;
import static com.jeffreyorazulike.noteskeeper.NotesKeeperInterfaces.*;

public class NotesKeeperAdapter<T, V> extends RecyclerView.Adapter<NotesKeeperAdapter.ViewHolder<T, V>>{

    private final List<T> mItems;
    private final Function<ViewGroup, ViewBinding> mItemBinding;
    private final Consumer<T, V> mBinder;
    private final Function<Integer, View.OnClickListener> mListener;

    public NotesKeeperAdapter(final List<T> items,
                              final Function<ViewGroup, ViewBinding> itemBinding,
                              final Consumer<T, V> binder, final Function<Integer, View.OnClickListener> listener) {
        mItems = items;
        mItemBinding = itemBinding;
        mBinder = binder;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder<T, V> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ViewHolder<>(mItemBinding.fun(parent), mBinder);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(mItems.get(position));
        holder.mBinding.getRoot().setOnClickListener(mListener.fun(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder<T, V> extends RecyclerView.ViewHolder{
        @NonNull private final ViewBinding mBinding;
        @NonNull private final Consumer<T, V> mBinder;

        ViewHolder(@NonNull final ViewBinding binding, @NonNull final Consumer<T, V> binder) {
            super(binding.getRoot());
            mBinding = binding;
            mBinder = binder;
        }

        void bind(T t){
            mBinder.consume(t, (V) mBinding);
        }
    }
}
