package com.phone.common_library.callback;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnItemViewClick2Listener<T> {

    void onItemClickListener(int position, @Nullable View view, @Nullable T success);
}
