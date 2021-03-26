package com.mobile.main_module.main.presenter;

import com.mobile.common_library.base.BasePresenter;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.callback.OnCommonSingleParamCallback;
import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.main_module.main.model.MainModelImpl;
import com.mobile.main_module.main.view.IMainView;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class MainPresenterImpl extends BasePresenter<IBaseView>
        implements IMainPresenter {

    private static final String TAG = "MainPresenterImpl";
    //    private IResourceChildView mainView;//P需要与V 交互，所以需要持有V的引用
    private MainModelImpl model = new MainModelImpl();

    public MainPresenterImpl(IBaseView baseView) {
        attachView(baseView);
    }

    @Override
    public void mainData(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IMainView) {
                IMainView mainView = (IMainView) baseView;
                mainView.showLoading();

                disposable = RetrofitManager.getInstance()
                        .responseString(model.mainData(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {

                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                compositeDisposable.add(disposable);

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
//                                    mainView.mainDataError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
//                                }
//                                mainView.hideLoading();
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                                // 异常处理
//                                mainView.mainDataError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                                mainView.hideLoading();
//                            }
//                        });
            }
        }
    }

}
