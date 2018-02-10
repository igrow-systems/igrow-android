package com.igrow.android.recyclerview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by jsr on 7/02/18.
 */

public abstract class RecyclerViewViewModel extends AndroidViewModel {

    RecyclerView.LayoutManager layoutManager;
    private Parcelable savedLayoutManagerState;

    protected abstract RecyclerViewAdapter getAdapter();
    protected abstract RecyclerView.LayoutManager createLayoutManager();

    public RecyclerViewViewModel(@Nullable Application application) {
        super(application);
//        if (savedInstanceState instanceof RecyclerViewViewModelState) {
//            savedLayoutManagerState =
//                    ((RecyclerViewViewModelState) savedInstanceState).layoutManagerState;
//        }
    }
//
//    @Override
//    public RecyclerViewViewModelState getInstanceState() {
//        return new RecyclerViewViewModelState(this);
//    }
//
    public final void setupRecyclerView(RecyclerView recyclerView) {
        layoutManager = createLayoutManager();
        if (savedLayoutManagerState != null) {
            layoutManager.onRestoreInstanceState(savedLayoutManagerState);
            savedLayoutManagerState = null;
        }
        recyclerView.setAdapter(getAdapter());
        recyclerView.setLayoutManager(layoutManager);
    }
//
//    protected static class RecyclerViewViewModelState extends State {
//
//        private final Parcelable layoutManagerState;
//
//        public RecyclerViewViewModelState(RecyclerViewViewModel viewModel) {
//            super(viewModel);
//            layoutManagerState = viewModel.layoutManager.onSaveInstanceState();
//        }
//
//        public RecyclerViewViewModelState(Parcel in) {
//            super(in);
//            layoutManagerState = in.readParcelable(
//                    RecyclerView.LayoutManager.class.getClassLoader());
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest, flags);
//            dest.writeParcelable(layoutManagerState, flags);
//        }
//
//        public static Parcelable.Creator<RecyclerViewViewModelState> CREATOR =
//                new Parcelable.Creator<RecyclerViewViewModelState>() {
//                    @Override
//                    public RecyclerViewViewModelState createFromParcel(Parcel source) {
//                        return new RecyclerViewViewModelState(source);
//                    }
//
//                    @Override
//                    public RecyclerViewViewModelState[] newArray(int size) {
//                        return new RecyclerViewViewModelState[size];
//                    }
//                };
//    }
}