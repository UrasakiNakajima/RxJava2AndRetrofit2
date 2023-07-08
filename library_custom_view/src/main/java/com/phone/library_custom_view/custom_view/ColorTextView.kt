package com.phone.library_custom_view.custom_view

import android.content.Context
import android.content.res.Resources.Theme
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.phone.library_custom_view.ViewAttributeManager

class ColorTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs),
    ColorUiInterface {

    private val TAG = ColorTextView::class.java.simpleName
    private var attr_drawable = -1

    //    private int attr_textAppearance = -1;
    private var attr_textColor = -1
    private var attr_textLinkColor = -1

    init {
        //        this.attr_textAppearance = ViewAttributeManager.getTextApperanceAttribute(attrs);
        attr_drawable = ViewAttributeManager.getBackgroundAttibute(attrs)
        attr_textColor = ViewAttributeManager.getTextColorAttribute(attrs)
        attr_textLinkColor = ViewAttributeManager.getTextLinkColorAttribute(attrs)
    }

    override fun getView(): View {
        return this
    }

    override fun setTheme(themeId: Theme) {
        Log.d("COLOR", "id = $id")
        if (attr_drawable != -1) {
            ViewAttributeManager.applyBackgroundDrawable(this, themeId, attr_drawable)
        }
        //        if(attr_textAppearance != -1) {
//            ViewAttributeManager.applyTextAppearance(this, themeId, attr_textAppearance);
//        }
        if (attr_textColor != -1) {
            ViewAttributeManager.applyTextColor(this, themeId, attr_textColor)
        }
        if (attr_textLinkColor != -1) {
            ViewAttributeManager.applyTextLinkColor(this, themeId, attr_textLinkColor)
        }
    }
}