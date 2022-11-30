package com.phone.module_main.main.presenter;

import com.phone.library_common.base.BasePresenter;
import com.phone.library_common.base.IBaseView;
import com.phone.library_common.callback.OnCommonSingleParamCallback;
import com.phone.library_common.manager.RetrofitManager;
import com.phone.module_main.main.model.MainModelImpl;
import com.phone.module_main.main.view.IMainView;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.Map;
import java.util.Objects;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class MainPresenterImpl extends BasePresenter<IBaseView>
        implements IMainPresenter {

    private static final String TAG = "MainPresenterImpl";
    //    private IResourceChildView mainView;//P需要与V 交互，所以需要持有V的引用
    private final MainModelImpl model = new MainModelImpl();

    public MainPresenterImpl(IBaseView baseView) {
        attachView(baseView);
    }

    @Override
    public void mainData(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IMainView) {
                IMainView mainView = (IMainView) baseView;
                mainView.showLoading();

                Objects.requireNonNull(RetrofitManager.Companion.get())
                        .responseString3(rxAppCompatActivity, model.mainData(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {

                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
//                compositeDisposable.add(disposable);

//                disposable = model.mainData(bodyParams)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<JSONObject>() {
//                            @Override
//                            public void accept(JSONObject jsonObject) throws Exception {
//                                String responseString = jsonObject.toJSONString();
//                                LogManager.i(TAG, "responseString*****" + responseString);
//                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                if (baseResponse.getCode() == 200) {
////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
//                                    mainView.mainDataSuccess(baseResponse.getMessage());
//                                } else {
//                                    mainView.mainDataError(BaseApplication.get().getResources().getString(R.string.data_in_wrong_format));
//                                }
//                                mainView.hideLoading();
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                                // 异常处理
//                                mainView.mainDataError(BaseApplication.get().getResources().getString(R.string.request_was_aborted));
//                                mainView.hideLoading();
//                            }
//                        });
            }
        }
    }

}
