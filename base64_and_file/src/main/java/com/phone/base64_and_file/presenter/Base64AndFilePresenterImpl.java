package com.phone.base64_and_file.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.phone.base64_and_file.IBase64AndFileView;
import com.phone.base64_and_file.model.Base64AndFileModelImpl;
import com.phone.common_library.base.BasePresenter;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;

import java.util.List;

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
                                      String dirsPath,
                                      String dirsPath2) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showCompressedPicture(context, dirsPath, dirsPath2, new OnCommonBothParamCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap success, String data) {
                        base64AndFileView.showCompressedPictureSuccess(success, data);
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
    public void showPictureToBase64(String filePath) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showPictureToBase64(filePath, new OnCommonBothParamCallback<List<String>>() {
                    @Override
                    public void onSuccess(List<String> success, String data) {
                        base64AndFileView.showPictureToBase64Success(success, data);
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
    public void showBase64ToPicture(String filePath,
                                    String base64Str) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IBase64AndFileView) {
                IBase64AndFileView base64AndFileView = (IBase64AndFileView) baseView;
                model.showBase64ToPicture(filePath, base64Str, new OnCommonSingleParamCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap success) {
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
