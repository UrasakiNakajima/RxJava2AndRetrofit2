package com.phone.base64_and_file.presenter;

import android.content.Context;

import com.phone.base64_and_file.IBase64AndFileView;
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.base64_and_file.model.Base64AndFileModelImpl;
import com.phone.common_library.base.BasePresenter;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonSingleParamCallback;

public class Base64AndFilePresenterImpl extends BasePresenter<IBaseView>
        implements IBase64AndFilePresenter {

    private static final String TAG = Base64AndFilePresenterImpl.class.getSimpleName();
    private Base64AndFileModelImpl model;
    private Object IBase64AndFileView;

    public Base64AndFilePresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new Base64AndFileModelImpl();
    }

    @Override
    public void showCompressedPicture(Context context,
                                      Base64AndFileBean base64AndFileBean) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showCompressedPicture(context, base64AndFileBean, new OnCommonSingleParamCallback<Base64AndFileBean>() {
                    @Override
                    public void onSuccess(Base64AndFileBean success) {
                        base64AndFileView.showCompressedPictureSuccess(success);
                    }

                    @Override
                    public void onError(String error) {
                        base64AndFileView.showCompressedPictureError(error);
                    }
                });
            }
        }
    }

    @Override
    public void showPictureToBase64(Base64AndFileBean base64AndFileBean) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showPictureToBase64(base64AndFileBean, new OnCommonSingleParamCallback<Base64AndFileBean>() {
                    @Override
                    public void onSuccess(Base64AndFileBean success) {
                        base64AndFileView.showPictureToBase64Success(success);
                    }

                    @Override
                    public void onError(String error) {
                        base64AndFileView.showPictureToBase64Error(error);
                    }
                });
            }
        }
    }

    @Override
    public void showBase64ToPicture(Base64AndFileBean base64AndFileBean) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showBase64ToPicture(base64AndFileBean, new OnCommonSingleParamCallback<Base64AndFileBean>() {
                    @Override
                    public void onSuccess(Base64AndFileBean success) {
                        base64AndFileView.showBase64ToPictureSuccess(success);
                    }

                    @Override
                    public void onError(String error) {
                        base64AndFileView.showBase64ToPictureError(error);
                    }
                });
            }
        }
    }
}
