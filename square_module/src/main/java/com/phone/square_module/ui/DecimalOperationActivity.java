package com.phone.square_module.ui;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.manager.BigDecimalManager;
import com.phone.common_library.manager.LogManager;
import com.phone.square_module.R;

/**
 * 如果计算的数值总和很大很大，超过100 0000 0000，请使用带有ToStr的方法，不然double数字会转成科学计数法显示
 */
public class DecimalOperationActivity extends BaseRxAppActivity {

    private static final String TAG = DecimalOperationActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevDecimalOperationRounding;
    private TextView tevDecimalOperationRejection;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_decimal_operation;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevDecimalOperationRounding = (TextView) findViewById(R.id.tev_decimal_operation_rounding);
        tevDecimalOperationRejection = (TextView) findViewById(R.id.tev_decimal_operation_rejection);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ContextCompat.getColor(rxAppCompatActivity, R.color.white));


        tevDecimalOperationRounding.setOnClickListener(v -> {
            double m1 = 986790.278576897;
            double m2 = 1887906.795768;
            double numAdd = BigDecimalManager.additionDouble(m1, m2, 5);
            LogManager.i(TAG, "numAdd*****" + numAdd);

            double n1 = 1870689.79557790;
            double n2 = 987900.27876876656;
            double numSub = BigDecimalManager.subtractionDouble(n1, n2, 5);;
            LogManager.i(TAG, "numSub*****" + numSub);

            double o1 = 9860.2785667;
            double o2 = 1000;
            //这个因为超过了50 0000，已经出现问题了
            double numMul = BigDecimalManager.multiplicationDouble(o1, o2, 5);
            LogManager.i(TAG, "numMul*****" + numMul);

            String r1 = "98679007.27798867";
            String r2 = "1000";
            String numMulStr = BigDecimalManager.multiplicationDoubleToStr(r1, r2, 0);
            LogManager.i(TAG, "numMulStr*****" + numMulStr);

            String s1 = "98679007.27798867";
            String s2 = "1000.55859767";
            String numMulStr2 = BigDecimalManager.multiplicationDoubleToStr(s1, s2, 5);
            LogManager.i(TAG, "numMulStr2*****" + numMulStr2);

            double p1 = 9867900.278676575;
            double p2 = 18790689.795565;
            double numDiv = BigDecimalManager.divisionDouble(p1, p2, 5);
            LogManager.i(TAG, "numDiv*****" + numDiv);

            double q1 = 9867900.278590876;
            double numKeepDecimals = BigDecimalManager.getDoubleKeepDecimals(q1, 5);
            LogManager.i(TAG, "numKeepDecimals*****" + numKeepDecimals);

            double z1 = 98679689078000.278590876596890;
            String numKeepDecimalsStr = BigDecimalManager.getDoubleKeepDecimalsToStr(z1, 5);
            LogManager.i(TAG, "numKeepDecimalsStr*****" + numKeepDecimalsStr);

        });



        tevDecimalOperationRejection.setOnClickListener(v -> {
            double mCompatible1 = 986790.278576897;
            double mCompatible2 = 1887906.795768;
            double numAddCompatible = BigDecimalManager.additionDoubleCompatible(mCompatible1, mCompatible2, 5);
            LogManager.i(TAG, "numAddCompatible*****" + numAddCompatible);

            double nCompatible1 = 1870689.79557790;
            double nCompatible2 = 987900.27876876656;
            //这个因为超过了50 0000，已经出现问题了
            double numSubCompatible = BigDecimalManager.subtractionDoubleCompatible(nCompatible1, nCompatible2, 5);;
            LogManager.i(TAG, "numSubCompatible*****" + numSubCompatible);

            double oCompatible1 = 9860.2785667;
            double oCompatible2 = 1000;
            //这个因为超过了50 0000，已经出现问题了
            double numMulCompatible = BigDecimalManager.multiplicationDoubleCompatible(oCompatible1, oCompatible2, 5);
            LogManager.i(TAG, "numMulCompatible*****" + numMulCompatible);

            String rCompatible1 = "98679007.27798867";
            String rCompatible2 = "1000";
            String numMulCompatibleStr = BigDecimalManager.multiplicationDoubleCompatibleToStr(rCompatible1, rCompatible2, 0);
            LogManager.i(TAG, "numMulCompatibleStr*****" + numMulCompatibleStr);

            String sCompatible1 = "98679007.27798867";
            String sCompatible2 = "1000.55859767";
            String numMulCompatibleStr2 = BigDecimalManager.multiplicationDoubleCompatibleToStr(sCompatible1, sCompatible2, 5);
            LogManager.i(TAG, "numMulCompatibleStr2*****" + numMulCompatibleStr2);

            double pCompatible1 = 9867900.278676575;
            double pCompatible2 = 18790689.795565;
            double numDivCompatible = BigDecimalManager.divisionDoubleCompatible(pCompatible1, pCompatible2, 5);
            LogManager.i(TAG, "numDivCompatible*****" + numDivCompatible);

            double qCompatible1 = 9867900.278590876;
            double numKeepDecimalsCompatible = BigDecimalManager.getDoubleKeepDecimalsCompatible(qCompatible1, 5);
            LogManager.i(TAG, "numKeepDecimalsCompatible*****" + numKeepDecimalsCompatible);

            double z1 = 98679689078000.278590876596890;
            //这个因为超过了900 0000 0000，已经出现问题了
            String numKeepDecimalsCompatibleStr = BigDecimalManager.getDoubleKeepDecimalsCompatibleToStr(z1, 5);
            LogManager.i(TAG, "numKeepDecimalsCompatibleStr*****" + numKeepDecimalsCompatibleStr);

        });
    }

    @Override
    protected void initLoadData() {

    }
}