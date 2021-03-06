package com.phone.common_library.manager;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phone.common_library.R;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderManager {
	
	public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url).placeholder(placeholder)
			.error(error).into(imageView);
	}
	
	public static void display(Context context, ImageView imageView, String url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop()
			.placeholder(R.mipmap.ic_image_loading)
			.error(R.mipmap.ic_empty_picture)
			.into(imageView);
	}
	
	public static void display(Context context, ImageView imageView, File url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop()
			.placeholder(R.mipmap.ic_image_loading)
			.error(R.mipmap.ic_empty_picture)
			.into(imageView);
	}
	
	public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.placeholder(R.mipmap.ic_image_loading)
			.error(R.mipmap.ic_empty_picture)
			.thumbnail(0.5f)
			.into(imageView);
	}
	
	public static void displayBigPhoto(Context context, ImageView imageView, String url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.format(DecodeFormat.PREFER_ARGB_8888)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.placeholder(R.mipmap.ic_image_loading)
			.error(R.mipmap.ic_empty_picture)
			.into(imageView);
	}
	
	public static void display(Context context, ImageView imageView, int url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop()
			.placeholder(R.mipmap.ic_image_loading)
			.error(R.mipmap.ic_empty_picture)
			.into(imageView);
	}
	
	public static void displayRound(Context context, ImageView imageView, String url) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(url)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.error(R.mipmap.toux2)
			.centerCrop().transform(new GlideRoundTransformUtil()).into(imageView);
	}
	
	public static void displayRound(Context context, ImageView imageView, int resId) {
		if (imageView == null) {
			throw new IllegalArgumentException("argument error");
		}
		Glide.with(context).load(resId)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.error(R.mipmap.toux2)
			.centerCrop().transform(new GlideRoundTransformUtil()).into(imageView);
	}
	
}
