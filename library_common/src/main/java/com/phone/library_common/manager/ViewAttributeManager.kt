package com.phone.library_common.manager

import android.content.res.Resources.Theme
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.phone.library_common.custom_view.ColorUiInterface

object ViewAttributeManager {

    @JvmStatic
    private val TAG = ViewAttributeManager::class.java.simpleName

    @JvmStatic
    fun getAttributeValue(attr: AttributeSet, paramInt: Int): Int {
        var value = -1
        val count = attr.attributeCount
        for (i in 0 until count) {
            if (attr.getAttributeNameResource(i) == paramInt) {
                val str = attr.getAttributeValue(i)
                if (null != str && str.startsWith("?")) {
                    value = str.substring(1, str.length).toInt()
                    return value
                }
            }
        }
        return value
    }

    @JvmStatic
    fun getBackgroundAttibute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.background)
    }

    @JvmStatic
    fun getCheckMarkAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.checkMark)
    }

    @JvmStatic
    fun getSrcAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.src)
    }

    @JvmStatic
    fun getTextApperanceAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.textAppearance)
    }

    @JvmStatic
    fun getDividerAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.divider)
    }

    @JvmStatic
    fun getTextColorAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.textColor)
    }

    @JvmStatic
    fun getTextLinkColorAttribute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.textColorLink)
    }

    @JvmStatic
    fun applyBackgroundDrawable(ci: ColorUiInterface?, theme: Theme, paramInt: Int) {
        val ta = theme.obtainStyledAttributes(intArrayOf(paramInt))
        val drawable = ta.getDrawable(0)
        if (null != ci) {
            ci.getView()!!.setBackgroundDrawable(drawable)
        }
        ta.recycle()
    }

    @JvmStatic
    fun applyImageDrawable(ci: ColorUiInterface?, theme: Theme, paramInt: Int) {
        val ta = theme.obtainStyledAttributes(intArrayOf(paramInt))
        val drawable = ta.getDrawable(0)
        if (null != ci && ci is ImageView) {
            (ci.getView() as ImageView?)!!.setImageDrawable(drawable)
        }
        ta.recycle()
    }

    @JvmStatic
    fun applyTextAppearance(ci: ColorUiInterface, theme: Theme, paramInt: Int) {
        val ta = theme.obtainStyledAttributes(intArrayOf(paramInt))
        val resourceId = ta.getResourceId(0, 0)
        if (ci is TextView) {
            (ci.getView() as TextView?)!!.setTextAppearance(ci.getView()!!.context, resourceId)
        }
        ta.recycle()
    }

    @JvmStatic
    fun applyTextColor(ci: ColorUiInterface, theme: Theme, paramInt: Int) {
        val ta = theme.obtainStyledAttributes(intArrayOf(paramInt))
        val resourceId = ta.getColor(0, 0)
        if (ci is TextView) {
            (ci.getView() as TextView?)!!.setTextColor(resourceId)
        }
        ta.recycle()
    }

    @JvmStatic
    fun applyTextLinkColor(ci: ColorUiInterface, theme: Theme, paramInt: Int) {
        val ta = theme.obtainStyledAttributes(intArrayOf(paramInt))
        val resourceId = ta.getColor(0, 0)
        if (ci is TextView) {
            (ci.getView() as TextView?)!!.setLinkTextColor(resourceId)
        }
        ta.recycle()
    }

}