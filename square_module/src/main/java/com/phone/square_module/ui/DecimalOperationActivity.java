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
    private TextView tevDecimalOperation;

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
        tevDecimalOperation = (TextView) findViewById(R.id.tev_decimal_operation);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ContextCompat.getColor(rxAppCompatActivity, R.color.white));

        tevDecimalOperation.setOnClickListener(v -> {
            double m1 = 986790.27857;
            double m2 = 1887906.795;
            double numAdd = BigDecimalManager.additionDouble(m1, m2, 3);
            LogManager.i(TAG, "numAdd*****" + numAdd);

            double n1 = 1870689.79557;
            double n2 = 987900.278;
            double numSub = BigDecimalManager.subtractionDouble(n1, n2, 3);;
            LogManager.i(TAG, "numSub*****" + numSub);

            double o1 = 9860.278;
            double o2 = 1000;
            double numMul = BigDecimalManager.multiplicationDouble(o1, o2, 3);
            LogManager.i(TAG, "numMul*****" + numMul);


            String r1 = "98679007.27798867";
            String r2 = "1000";
            String numMulStr = BigDecimalManager.multiplicationDoubleToStr(r1, r2, 0);
            LogManager.i(TAG, "numMulStr*****" + numMulStr);


            String s1 = "98679007.27798867";
            String s2 = "1000.35859767";
            String numMulStr2 = BigDecimalManager.multiplicationDoubleToStr(s1, s2, 5);
            LogManager.i(TAG, "numMulStr2*****" + numMulStr2);


            double p1 = 9867900.278;
            double p2 = 18790689.795;
            double numDiv = BigDecimalManager.divisionDouble(p1, p2, 3);
            LogManager.i(TAG, "numDiv*****" + numDiv);

//            double q1 = 9867900.278590876;
//            double numKeepDecimals = BigDecimalManager.getDoubleKeepDecimals(q1, 3);
//            LogManager.i(TAG, "numKeepDecimals*****" + numKeepDecimals);





        });
    }

    @Override
    protected void initLoadData() {

    }
}