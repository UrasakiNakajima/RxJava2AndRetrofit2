package com.phone.library_common.callback;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnItemViewClick2Listener<T> {

    void onItemClickListener(int position, View view, T success);
}
