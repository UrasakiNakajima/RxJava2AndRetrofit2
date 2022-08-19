package com.phone.square_module.ui;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.common.DecimalInputFilter;
import com.phone.common_library.common.DecimalTextWatcher;
import com.phone.common_library.manager.MineInputMethodManager;
import com.phone.square_module.R;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
public class EditTextInputLimitsActivity extends BaseRxAppActivity {

    private static final String TAG = EditTextInputLimitsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevShowInput;
    private TextView tevStartInput;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_edit_text_input_limits;
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
        tevShowInput = (TextView) findViewById(R.id.tev_show_input);
        tevStartInput = (TextView) findViewById(R.id.tev_start_input);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ContextCompat.getColor(rxAppCompatActivity, R.color.white));

        tevStartInput.setOnClickListener(v -> {
            showDecimalAlertDialog();
        });
    }

    @Override
    protected void initLoadData() {

    }

    /**
     * 注意：如果输入特殊小数或整数，如：.或.15或10.或00035或输入21153.67589，然后删除中间小数点，整数就是10位了（整数不能大于5位），
     * 则要在点击Dialog确认按钮之前进行提示，这样就不会填入不符合规范的整数或小数了。
     */
    private void showDecimalAlertDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_edit_text_input_limits, null, false);
        EditText edtInput = (EditText) view.findViewById(R.id.edt_input);
        TextView tevCancel = (TextView) view.findViewById(R.id.tev_cancel);
        TextView tevConfirm = (TextView) view.findViewById(R.id.tev_confirm);
        //小数点前边几位（修改这里可以自定义）
        int beforeDecimalNum = 5;
        //小数点后边几位（修改这里可以自定义）
        int afterDecimalNum = 5;
        //最大长度是多少位（修改这里可以自定义）
        int maxLength = 11;
        //输入的类型可以是整数或小数
        edtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        DecimalInputFilter decimalInputFilter = new DecimalInputFilter(beforeDecimalNum, afterDecimalNum);
        //输入总长度多少位，小数几位（修改这里可以自定义）
        InputFilter[] inputFilter = {new InputFilter.LengthFilter(maxLength), decimalInputFilter};
        edtInput.setFilters(inputFilter);
        edtInput.addTextChangedListener(new DecimalTextWatcher(edtInput, afterDecimalNum));

        @SuppressLint("RestrictedApi")
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog_decimal_style)
                .setView(view, 0, 0, 0, 0)
                .show();

//        int widthPx = ScreenManager.getScreenWidth(this);
//        int widthDp = ScreenManager.pxToDp(this, widthPx);
//        int heightPx = ScreenManager.getScreenHeight(this);
//        int heightDp = ScreenManager.pxToDp(this, heightPx);
//        LogManager.i(TAG, "widthDp*****" + widthDp);
//        LogManager.i(TAG, "heightDp*****" + heightDp);

        tevCancel.setOnClickListener(v -> {
            MineInputMethodManager.hideInputMethod(this);
            alertDialog.dismiss();
        });
        tevConfirm.setOnClickListener(v -> {
            String afterData = edtInput.getText().toString();
            if (!"".equals(afterData)) {
                if (afterData.contains(".")) {
                    String[] afterDataArr = afterData.split("\\.");
                    if ("".equals(afterDataArr[0])) {
                        Toast.makeText(this, "请输入正常整数或小数", Toast.LENGTH_SHORT).show();
                    } else if (afterDataArr.length == 1) {//当afterData是这种类型的小数时（0. 100.）
                        Toast.makeText(this, "请输入正常整数或小数", Toast.LENGTH_SHORT).show();
                    } else {
                        this.tevShowInput.setText(edtInput.getText().toString());
                        alertDialog.dismiss();
                    }
                } else {
                    if (afterData.length() <= beforeDecimalNum) {
                        String[] afterDataArr = afterData.split("");
                        if (afterDataArr.length > 1 && "0".equals(afterDataArr[1])) {
                            Toast.makeText(this, "请输入正常整数或小数", Toast.LENGTH_SHORT).show();
                        } else {
                            this.tevShowInput.setText(edtInput.getText().toString());
                            alertDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(this, "整数长度不能大于" + beforeDecimalNum + "位", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "请输入整数或小数", Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        edtInput.setFocusable(true);
        edtInput.setFocusableInTouchMode(true);
        edtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MineInputMethodManager.showInputMethod(EditTextInputLimitsActivity.this, edtInput);
                } else {

                }
            }
        });
        edtInput.requestFocus();
    }


}