package com.igrow.android.recyclerview;

import android.arch.lifecycle.ViewModel;

/**
 * Created by jsr on 7/02/18.
 */

public abstract class ItemViewModel<ITEM_T> extends ViewModel {

    public ItemViewModel() {
        super();
    }

    public abstract void setItem(ITEM_T item);
}