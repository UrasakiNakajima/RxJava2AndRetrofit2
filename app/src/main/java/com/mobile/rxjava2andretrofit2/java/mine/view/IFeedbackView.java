package com.mobile.rxjava2andretrofit2.java.mine.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;

public interface IFeedbackView extends IBaseView {

    void submitFeedbackSuccess(String success);

    void submitFeedbackError(String error);

}
