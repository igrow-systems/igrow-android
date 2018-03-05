package com.igrow.android.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.reflect.TypeToken;
import com.igrow.android.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsr on 7/02/18.
 */

public abstract class RecyclerViewAdapter<ITEM_T, VIEW_MODEL_T extends ItemViewModel<ITEM_T>>
        extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder<ITEM_T, VIEW_MODEL_T>> {

    public static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    protected final ArrayList<ITEM_T> items;

    protected interface OnItemClickListener<ITEM_T> {
        void onItemClick(ITEM_T item);
    }

    protected OnItemClickListener mListener;


    public RecyclerViewAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public ItemViewHolder<ITEM_T, VIEW_MODEL_T> onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);

        final TypeToken<VIEW_MODEL_T> type = new TypeToken<VIEW_MODEL_T>(getClass()) {};
        ItemViewHolder<ITEM_T, VIEW_MODEL_T> viewHolder = null;

        try {
            VIEW_MODEL_T viewModel = (VIEW_MODEL_T) type.getRawType().newInstance();

            binding.setVariable(BR.viewmodel, viewModel);

            viewHolder = new ItemViewHolder<>(binding.getRoot(), binding, viewModel);

        } catch (InstantiationException ie) {
            Log.e(TAG, "Couldn't construct ViewModel from type argument", ie);
        } catch (IllegalAccessException iae) {
            Log.e(TAG, "Couldn't construct ViewModel from type argument", iae);
        }
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder<ITEM_T, VIEW_MODEL_T> holder, int position) {
        final ITEM_T item = items.get(position);
        holder.setItem(item);
        ItemUserActionsListener userActionsListener = new ItemUserActionsListener() {
            @Override
            public void onClick() {
                mListener.onItemClick(item);
            }
        };
        holder.binding.setVariable(BR.listener, userActionsListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ITEM_T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<ITEM_T> getItems() {
        return items;
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public static class ItemViewHolder<T, VT extends ItemViewModel<T>>
            extends RecyclerView.ViewHolder {

        protected final VT viewModel;
        private final ViewDataBinding binding;

        public ItemViewHolder(View itemView, ViewDataBinding binding, VT viewModel) {
            super(itemView);
            this.binding = binding;
            this.viewModel = viewModel;
        }

        void setItem(T item) {
            viewModel.setItem(item);
            binding.executePendingBindings();
        }
    }
}