package com.jeffreyorazulike.noteskeeper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.jeffreyorazulike.noteskeeper.R;

import java.util.List;

public abstract class NotesKeeperAdapter<T> extends RecyclerView.Adapter<NotesKeeperAdapter.ViewHolder> implements View.OnClickListener {

    int mNavigateTo;

    final List<T> mItems;
    final MutableLiveData<ViewHolder> mObservableViewHolder = new MutableLiveData<>();
    final Context mContext;
    ViewBinding mBinding;

    View.OnClickListener baseListener;

    NotesKeeperAdapter(List<T> items, final Context context){
        mItems = items;
        mContext = context;
        baseListener = view -> {
            Bundle bundle = new Bundle(1);
            // if this view is a recycler view item,
            // pass the position of the item that was clicked
            if(view.getClass().equals(mBinding.getRoot().getClass()))
                bundle.putInt(mContext.getString(R.string.position), mObservableViewHolder.getValue().getAdapterPosition());
            Navigation.findNavController(view).navigate(mNavigateTo, bundle);
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ViewBinding mBinding;
        ViewHolder(@NonNull final ViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewBinding getBinding() {
            return mBinding;
        }
    }

    public LiveData<ViewHolder> getObservable() {
        return mObservableViewHolder;
    }

    public List<T> getItems() {
        return mItems;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
