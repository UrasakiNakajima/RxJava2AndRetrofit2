package com.phone.common_library.callback;

import android.view.View;

public interface OnItemViewClick2Listener<T> {

    void onItemClickListener(int position, View view, T success);
}
