package com.mobile.rxjava2andretrofit2.mine.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;

public interface IFeedbackView extends IBaseView {

    void submitFeedbackSuccess(String success);

    void submitFeedbackError(String error);

}
