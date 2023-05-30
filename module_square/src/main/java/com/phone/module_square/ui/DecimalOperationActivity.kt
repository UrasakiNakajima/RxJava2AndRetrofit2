package com.phone.module_square.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.phone.library_common.base.BaseRxAppActivity
import com.phone.library_common.manager.BigDecimalManager.additionDouble
import com.phone.library_common.manager.BigDecimalManager.additionDoubleCompatible
import com.phone.library_common.manager.BigDecimalManager.divisionDouble
import com.phone.library_common.manager.BigDecimalManager.divisionDoubleCompatible
import com.phone.library_common.manager.BigDecimalManager.getDoubleKeepDecimals
import com.phone.library_common.manager.BigDecimalManager.getDoubleKeepDecimalsCompatible
import com.phone.library_common.manager.BigDecimalManager.getDoubleKeepDecimalsCompatibleToStr
import com.phone.library_common.manager.BigDecimalManager.getDoubleKeepDecimalsToStr
import com.phone.library_common.manager.BigDecimalManager.multiplicationDouble
import com.phone.library_common.manager.BigDecimalManager.multiplicationDoubleCompatible
import com.phone.library_common.manager.BigDecimalManager.multiplicationDoubleCompatibleToStr
import com.phone.library_common.manager.BigDecimalManager.multiplicationDoubleToStr
import com.phone.library_common.manager.BigDecimalManager.subtractionDouble
import com.phone.library_common.manager.BigDecimalManager.subtractionDoubleCompatible
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_square.R

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 如果计算的数值总和很大很大，超过50 0000，请使用带有ToStr的方法，不然double数字会转成科学计数法显示
 */
class DecimalOperationActivity : BaseRxAppActivity() {

    private val TAG = DecimalOperationActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var tevDecimalOperationRounding: TextView? = null
    private var tevDecimalOperationRejection: TextView? = null

    override fun initLayoutId() = R.layout.square_activity_decimal_operation

    override fun initData() {
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        tevDecimalOperationRounding =
            findViewById<View>(R.id.tev_decimal_operation_rounding) as TextView
        tevDecimalOperationRejection =
            findViewById<View>(R.id.tev_decimal_operation_rejection) as TextView
        setToolbar(false, R.color.color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.white))
        layoutBack?.setOnClickListener { view: View -> finish() }
        tevDecimalOperationRounding?.setOnClickListener { v: View ->
            val m1 = 986790.278576897
            val m2 = 1887906.795768
            val numAdd = additionDouble(m1, m2, 5)
            LogManager.i(TAG, "numAdd*****$numAdd")
            val n1 = 1870689.79557790
            val n2 = 987900.27876876656
            val numSub = subtractionDouble(n1, n2, 5)
            LogManager.i(TAG, "numSub*****$numSub")
            val o1 = 9860.2785667
            val o2 = 1000.0
            //这个因为超过了50 0000，已经出现问题了
            val numMul = multiplicationDouble(o1, o2, 5)
            LogManager.i(TAG, "numMul*****$numMul")
            val r1 = "98679007.27798867"
            val r2 = "1000"
            val numMulStr = multiplicationDoubleToStr(r1, r2, 0)
            LogManager.i(TAG, "numMulStr*****$numMulStr")
            val s1 = "98679007.27798867"
            val s2 = "1000.55859767"
            val numMulStr2 = multiplicationDoubleToStr(s1, s2, 5)
            LogManager.i(TAG, "numMulStr2*****$numMulStr2")
            val p1 = 9867900.278676575
            val p2 = 18790689.795565
            val numDiv = divisionDouble(p1, p2, 5)
            LogManager.i(TAG, "numDiv*****$numDiv")
            val q1 = 9867900.278590876
            val numKeepDecimals = getDoubleKeepDecimals(q1, 5)
            LogManager.i(TAG, "numKeepDecimals*****$numKeepDecimals")
            val z1 = 98679689078000.278590876596890
            val numKeepDecimalsStr =
                getDoubleKeepDecimalsToStr(z1, 5)
            LogManager.i(TAG, "numKeepDecimalsStr*****$numKeepDecimalsStr")
        }
        tevDecimalOperationRejection?.setOnClickListener { v: View ->
            val mCompatible1 = 986790.2785
            val mCompatible2 = 1887906.79501
            //这个因为超过了50 0000，可能会出现问题
            val numAddCompatible =
                additionDoubleCompatible(mCompatible1, mCompatible2, 5)
            LogManager.i(TAG, "numAddCompatible*****$numAddCompatible")
            val nCompatible1 = 1870689.795572
            val nCompatible2 = 987900.278768
            //这个因为超过了50 0000，已经出现问题了
            val numSubCompatible =
                subtractionDoubleCompatible(nCompatible1, nCompatible2, 5)
            LogManager.i(TAG, "numSubCompatible*****$numSubCompatible")
            val oCompatible1 = 9860.2785667
            val oCompatible2 = 1000.0
            //这个因为超过了50 0000，可能会出现问题
            val numMulCompatible =
                multiplicationDoubleCompatible(oCompatible1, oCompatible2, 5)
            LogManager.i(TAG, "numMulCompatible*****$numMulCompatible")
            val rCompatible1 = "98679007.27798867"
            val rCompatible2 = "1000"
            val numMulCompatibleStr =
                multiplicationDoubleCompatibleToStr(rCompatible1, rCompatible2, 0)
            LogManager.i(TAG, "numMulCompatibleStr*****$numMulCompatibleStr")
            val sCompatible1 = "98679007.27798"
            val sCompatible2 = "1000.55859"
            //这个因为超过了900 0000 0000，可能会出现问题
            val numMulCompatibleStr2 =
                multiplicationDoubleCompatibleToStr(sCompatible1, sCompatible2, 5)
            LogManager.i(TAG, "numMulCompatibleStr2*****$numMulCompatibleStr2")
            val pCompatible1 = 9867900.278676575
            val pCompatible2 = 18790689.795565
            val numDivCompatible =
                divisionDoubleCompatible(pCompatible1, pCompatible2, 5)
            LogManager.i(TAG, "numDivCompatible*****$numDivCompatible")
            val qCompatible1 = 9867900.278590876
            //这个因为超过了50 0000，可能会出现问题
            val numKeepDecimalsCompatible =
                getDoubleKeepDecimalsCompatible(qCompatible1, 5)
            LogManager.i(TAG, "numKeepDecimalsCompatible*****$numKeepDecimalsCompatible")
            val z1 = 98679689078000.278590876596890
            //这个因为超过了900 0000 0000，已经出现问题了
            val numKeepDecimalsCompatibleStr =
                getDoubleKeepDecimalsCompatibleToStr(z1, 5)
            LogManager.i(TAG, "numKeepDecimalsCompatibleStr*****$numKeepDecimalsCompatibleStr")
        }
    }

    override fun initLoadData() {
    }

}