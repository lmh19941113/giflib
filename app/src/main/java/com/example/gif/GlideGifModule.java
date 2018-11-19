package com.example.gif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by Administrator on 2018\11\18 0018.
 */

@GlideModule
public class GlideGifModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //第一个参数任意填写
        registry.prepend(Registry.BUCKET_GIF, InputStream.class, FrameSequenceDrawable.class, new FrameSequenceDecoder(glide.getBitmapPool()));
    }
}
