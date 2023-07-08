package com.phone.library_custom_view.rclayout

interface RCAttrs {

    fun setClipBackground(clipBackground: Boolean)

    fun setRoundAsCircle(roundAsCircle: Boolean)

    fun setRadius(radius: Int)

    fun setTopLeftRadius(topLeftRadius: Int)

    fun setTopRightRadius(topRightRadius: Int)

    fun setBottomLeftRadius(bottomLeftRadius: Int)

    fun setBottomRightRadius(bottomRightRadius: Int)

    fun setStrokeWidth(strokeWidth: Int)

    fun setStrokeColor(strokeColor: Int)

    fun isClipBackground(): Boolean

    fun isRoundAsCircle(): Boolean

    fun getTopLeftRadius(): Float

    fun getTopRightRadius(): Float

    fun getBottomLeftRadius(): Float

    fun getBottomRightRadius(): Float

    fun getStrokeWidth(): Int

    fun getStrokeColor(): Int
}