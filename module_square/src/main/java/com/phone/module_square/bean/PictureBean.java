package com.phone.module_square.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * 一句话描述该类功能.
 *
 * @author Urasaki
 * @version 1.0
 * @date 2024-02-24$ 16:42:00$
 **/
public class PictureBean implements Cloneable {

    public String id;
    public int picture;

    public PictureBean(String id, int picture) {
        this.id = id;
        this.picture = picture;
    }

    @NonNull
    @Override
    public PictureBean clone() throws CloneNotSupportedException {
        return (PictureBean) super.clone();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        // 使用getClass()判断对象是否属于该类
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        PictureBean other = (PictureBean) obj;
        return Objects.equals(this.id, other.id) && this.picture == other.picture;
    }

}
