package com.mobile.rxjava2andretrofit2.custom_view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.mobile.rxjava2andretrofit2.manager.ViewAttributeManager;

public class ColorTextView extends AppCompatTextView implements ColorUiInterface {


    private int attr_drawable = -1;
    private int attr_textAppearance = -1;
    private int attr_textColor = -1;
    private int attr_textLinkColor = -1;

    public ColorTextView(Context context) {
        super(context);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.attr_textAppearance = ViewAttributeManager.getTextApperanceAttribute(attrs);
        this.attr_drawable = ViewAttributeManager.getBackgroundAttibute(attrs);
        this.attr_textColor = ViewAttributeManager.getTextColorAttribute(attrs);
        this.attr_textLinkColor = ViewAttributeManager.getTextLinkColorAttribute(attrs);
    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.attr_textAppearance = ViewAttributeManager.getTextApperanceAttribute(attrs);
        this.attr_drawable = ViewAttributeManager.getBackgroundAttibute(attrs);
        this.attr_textColor = ViewAttributeManager.getTextColorAttribute(attrs);
        this.attr_textLinkColor = ViewAttributeManager.getTextLinkColorAttribute(attrs);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme themeId) {
        Log.d("COLOR", "id = " + getId());
        if (attr_drawable != -1) {
            ViewAttributeManager.applyBackgroundDrawable(this, themeId, attr_drawable);
        }
//        if(attr_textAppearance != -1) {
//            ViewAttributeManager.applyTextAppearance(this, themeId, attr_textAppearance);
//        }
        if (attr_textColor != -1) {
            ViewAttributeManager.applyTextColor(this, themeId, attr_textColor);
        }
        if (attr_textLinkColor != -1) {
            ViewAttributeManager.applyTextLinkColor(this, themeId, attr_textLinkColor);
        }
    }
}
