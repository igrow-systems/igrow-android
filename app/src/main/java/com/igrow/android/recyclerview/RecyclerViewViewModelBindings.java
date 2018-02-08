package com.igrow.android.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

public class RecyclerViewViewModelBindings {

    @BindingAdapter("app:recyclerViewViewModel")
    public static void setRecyclerViewViewModel(RecyclerView recyclerView,
                                                RecyclerViewViewModel viewModel) {
        viewModel.setupRecyclerView(recyclerView);
    }
}
