package com.phone.common_library.manager;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phone.common_library.custom_view.GlideRoundTransform;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2021/3/31 10:55
 * introduce :
 */

public class GlideRoundManager {

    public static void setRoundCorner(final View view, final Drawable resourceId, final float cornerDipValue) {
        if (cornerDipValue == 0) {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        view.removeOnLayoutChangeListener(this);
                        Glide.with(view)
                                .asDrawable()
                                .load(resourceId)
                                .transform(new CenterCrop())
                                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        view.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }
                });
            } else {
                Glide.with(view)
                        .asDrawable()
                        .load(resourceId)
                        .transform(new CenterCrop())
                        .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                view.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        } else {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        view.removeOnLayoutChangeListener(this);
                        Glide.with(view)
                                .load(resourceId)
                                .transform(new CenterCrop(), new RoundedCorners((int) cornerDipValue))
                                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                                .into(new CustomTarget<Drawable>() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        view.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }
                });
            } else {
                Glide.with(view)
                        .load(resourceId)
                        .transform(new CenterCrop(), new RoundedCorners((int) cornerDipValue))
                        .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                view.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        }
    }

    public static void setCorners(final View view, final Drawable resourceId, final float leftTop_corner, final float leftBottom_corner, final float rightTop_corner, final float rightBottom_corner) {
        if (leftTop_corner == 0 && leftBottom_corner == 0 && rightTop_corner == 0 && rightBottom_corner == 0) {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        view.removeOnLayoutChangeListener(this);
                        Glide.with(view)
                                .load(resourceId)
                                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                                .into(new CustomTarget<Drawable>() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        view.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }
                });
            } else {
                Glide.with(view)
                        .load(resourceId)
                        .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                view.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }

        } else {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        view.removeOnLayoutChangeListener(this);
                        GlideRoundTransform transform = new GlideRoundTransform(view.getContext(), leftTop_corner, leftBottom_corner, rightTop_corner, rightBottom_corner);
                        Glide.with(view)
                                .load(resourceId)
                                .transform(transform)
                                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                                .into(new CustomTarget<Drawable>() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        view.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }
                });
            } else {
                GlideRoundTransform transform = new GlideRoundTransform(view.getContext(), leftTop_corner, leftBottom_corner, rightTop_corner, rightBottom_corner);
                Glide.with(view)
                        .load(resourceId)
                        .transform(transform)
                        .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                view.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        }
    }

}
