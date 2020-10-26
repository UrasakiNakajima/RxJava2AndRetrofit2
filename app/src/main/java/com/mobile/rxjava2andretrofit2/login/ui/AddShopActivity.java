package com.mobile.rxjava2andretrofit2.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.login.presenter.LoginPresenterImpl;
import com.mobile.rxjava2andretrofit2.login.view.IAddShopView;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.manager.UpdateFileNameManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddShopActivity extends BaseMvpAppActivity<IBaseView, LoginPresenterImpl>
        implements IAddShopView {

    private static final String TAG = "AddShopActivity";
    @BindView(R.id.tev_select_photos1)
    TextView tevSelectPhotos1;
    @BindView(R.id.tev_select_photos2)
    TextView tevSelectPhotos2;
    @BindView(R.id.tev_select_photos3)
    TextView tevSelectPhotos3;
    @BindView(R.id.tev_add_shop)
    TextView tevAddShop;
    @BindView(R.id.tev_select_photos5)
    TextView tevSelectPhotos5;
    @BindView(R.id.tev_select_photos6)
    TextView tevSelectPhotos6;

    private Map<String, File> fileMap;
    private Map<String, List<File>> filesMap;
    //店铺照片
    private List<String> shopPhotoPathList;
    private List<File> fileList;

    private String shopId;
    private boolean isUploadBusinessLicense;
    private boolean isUploadShopFrontPhoto;
    private boolean isUploadShopPhotoList;

    //选择照片
    private List<LocalMedia> selectPhotoList;
    private List<File> fileSelectPhotoList;
    private String picturePath1;
    private String picturePath2;
    private String picturePath3;
    private String picturePath5;

    private List<File> mineFiles;

    //选择照片标记
    private int selectPhotosFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_perfect_info;
    }

    @Override
    protected void initData() {
        fileMap = new HashMap<>();
        filesMap = new HashMap<>();

        shopPhotoPathList = new ArrayList<>();
        fileList = new ArrayList<>();

        selectPhotoList = new ArrayList<>();
        fileSelectPhotoList = new ArrayList<>();
        mineFiles = new ArrayList<>();

        shopId = mineApplication.getShopId();

        isUploadBusinessLicense = true;
        isUploadShopFrontPhoto = true;
        isUploadShopPhotoList = true;
    }

    @Override
    protected void initViews() {
        addContentView(loadView, layoutParams);
        setToolbar(true, R.color.color_FFFFFFFF);
    }

    @Override
    protected void initLoadData() {
    }

    @Override
    protected LoginPresenterImpl attachPresenter() {
        return new LoginPresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (loadView != null && !loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.start();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView != null && loadView.isShown()) {
            loadView.stop();
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void addShopSuccess(String success) {
        showToast(success, true);
    }

    @Override
    public void addShopError(String error) {
        showToast(error, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogManager.i(TAG, "requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectPhotoList.clear();
                    selectPhotoList.addAll((PictureSelector.obtainMultipleResult(data)));
                    LogManager.i(TAG, selectPhotoList.get(0).getPath());
                    LogManager.i(TAG, selectPhotoList.get(0).toString());
                    fileSelectPhotoList.clear();
                    for (int i = 0; i < selectPhotoList.size(); i++) {
                        fileSelectPhotoList.add(new File(selectPhotoList.get(i).getCompressPath()));
                    }

                    try {
                        List<File> files = UpdateFileNameManager.updateFilesListNames(this, fileSelectPhotoList);
                        if (selectPhotosFlag == 1) {
                            if (files != null && files.size() > 0) {
                                picturePath1 = files.get(0).getAbsolutePath();
                            }
                        } else if (selectPhotosFlag == 2) {
                            if (files != null && files.size() > 0) {
                                picturePath2 = files.get(0).getAbsolutePath();
                            }
                        } else if (selectPhotosFlag == 3) {
                            mineFiles.clear();
                            mineFiles.addAll(files);
                        } else if (selectPhotosFlag == 5) {
                            mineFiles.clear();
                            mineFiles.addAll(files);
                        } else if (selectPhotosFlag == 6) {
                            if (files != null && files.size() > 0) {
                                picturePath5 = files.get(0).getAbsolutePath();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void addShop() {
        bodyParams.clear();
        bodyParams.put("fullname", "fullname2");
        bodyParams.put("contactName", "contactName2");
        bodyParams.put("contactPhone", "13513313210");
        bodyParams.put("province", "province2");
        bodyParams.put("city", "city2");
        bodyParams.put("county", "county2");
        bodyParams.put("address", "address2");
        bodyParams.put("longitude", "100");
        bodyParams.put("latitude", "100");
//        bodyParams.put("businessTime", "businessTime");
        bodyParams.put("shopPhone", "13513313210");
        bodyParams.put("technicianNum", "10");
        bodyParams.put("operationStation", "10");

//        shopLabelsList.clear();
//        shopLabelsList.addAll(success.getShopLabelsList());
//        if (shopLabelsList != null && shopLabelsList.size() > 0) {
//            String shopLabels = "";
//            for (int i = 0; i < shopLabelsList.size(); i++) {
//                if (i == shopLabelsList.size() - 1) {
//                    shopLabels = shopLabels + shopLabelsList.get(i);
//                } else {
//                    shopLabels = shopLabels + shopLabelsList.get(i) + ",";
//                }
//            }
//
//            LogManager.i(TAG, "shopLabels=" + shopLabels);
//            bodyParams.put("tags", shopLabels);
//        } else {
        bodyParams.put("tags", "");
//        }

//        industryTypesList.clear();
//        industryTypesList.addAll(success.getIndustryTypesList());
//        if (industryTypesList != null && industryTypesList.size() > 0) {
//            String industryTypes = "";
//            for (int i = 0; i < industryTypesList.size(); i++) {
//                if (i == industryTypesList.size() - 1) {
//                    industryTypes = industryTypes + industryTypesList.get(i);
//                } else {
//                    industryTypes = industryTypes + industryTypesList.get(i) + ",";
//                }
//            }
//
//            LogManager.i(TAG, "industryTypes=" + industryTypes);
//            bodyParams.put("shopTrade", industryTypes);
//        } else {
        bodyParams.put("shopTrade", "");
//        }
        bodyParams.put("shopDescription", "shopDescription");
        bodyParams.put("deviceDescription", "deviceDescription");
        bodyParams.put("shopkeeperId", mineApplication.getUserId());
        fileMap.clear();
        if (isUploadBusinessLicense) {
            fileMap.put("businessLicense", new File(picturePath1));
        }
        if (isUploadShopFrontPhoto) {
            fileMap.put("doorPhoto", new File(picturePath2));
        }

//        filesMap.clear();
//        if (isUploadShopPhotoList) {
//            for (int i = 0; i < mineFiles.size(); i++) {
//                shopPhotoPathList.clear();
//                shopPhotoPathList.add(mineFiles.get(i).getAbsolutePath());
//            }
//            fileList.clear();
//            if (shopPhotoPathList != null && shopPhotoPathList.size() > 0) {
//                for (int i = 0; i < shopPhotoPathList.size(); i++) {
//                    fileList.add(new File(shopPhotoPathList.get(i)));
//                }
//                filesMap.put("shopImages", fileList);
//            }
//        }
        if (!isEmpty(shopId)) {
            bodyParams.put("shopId", shopId);
        }

        for (int i = 0; i < mineFiles.size(); i++) {
            shopPhotoPathList.clear();
            shopPhotoPathList.add(mineFiles.get(i).getAbsolutePath());
        }

        fileList.clear();
        shopPhotoPathList.clear();
        for (int i = 0; i < mineFiles.size(); i++) {
            shopPhotoPathList.add(mineFiles.get(i).getAbsolutePath());
        }
        if (shopPhotoPathList != null && shopPhotoPathList.size() > 0) {
            for (int i = 0; i < shopPhotoPathList.size(); i++) {
                fileList.add(new File(shopPhotoPathList.get(i)));
            }
            filesMap.put("agreement", fileList);
        }
        bodyParams.put("account", "12345678901234567");
        bodyParams.put("accountUsername", "123");
        fileMap.put("accountReceive", new File(picturePath5));
        presenter.addShop(bodyParams, fileMap, filesMap);
    }

    @OnClick({R.id.tev_select_photos1, R.id.tev_select_photos2, R.id.tev_select_photos3,
            R.id.tev_select_photos5, R.id.tev_select_photos6, R.id.tev_add_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tev_select_photos1:
                selectPhotosFlag = 1;
                PictureSelector.create(AddShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
//                                        .theme(themeId)
                        .maxSelectNum(1)
                        .minSelectNum(0)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false)
                        .enableCrop(false)
                        .compress(true)
//                                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.tev_select_photos2:
                selectPhotosFlag = 2;
                PictureSelector.create(AddShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
//                                        .theme(themeId)
                        .maxSelectNum(1)
                        .minSelectNum(0)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false)
                        .enableCrop(false)
                        .compress(true)
//                                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.tev_select_photos3:
                selectPhotosFlag = 3;
                PictureSelector.create(AddShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
//                                        .theme(themeId)
                        .maxSelectNum(1)
                        .minSelectNum(0)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false)
                        .enableCrop(false)
                        .compress(true)
//                                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.tev_select_photos5:
                selectPhotosFlag = 5;
                PictureSelector.create(AddShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
//                                        .theme(themeId)
                        .maxSelectNum(3)
                        .minSelectNum(0)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false)
                        .enableCrop(false)
                        .compress(true)
//                                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.tev_select_photos6:
                selectPhotosFlag = 6;
                PictureSelector.create(AddShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
//                                        .theme(themeId)
                        .maxSelectNum(1)
                        .minSelectNum(0)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(false)
                        .enableCrop(false)
                        .compress(true)
//                                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.tev_add_shop:
                addShop();
                break;
        }
    }
}
