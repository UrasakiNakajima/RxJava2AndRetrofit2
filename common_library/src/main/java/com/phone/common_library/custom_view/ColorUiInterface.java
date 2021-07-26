package com.phone.common_library.custom_view;

import android.content.res.Resources;
import android.view.View;

/**
 * 换肤接口
 * Created by chengli on 15/6/8.
 */
public interface ColorUiInterface {

    View getView();

    void setTheme(Resources.Theme themeId);
}
