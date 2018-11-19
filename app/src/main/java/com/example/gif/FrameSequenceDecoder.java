package com.example.gif;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;

/**
 * Created by Administrator on 2018\11\18 0018.
 */

public class FrameSequenceDecoder implements ResourceDecoder<InputStream, FrameSequenceDrawable> {


    private BitmapPool bitmapPool;

    public FrameSequenceDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        return true;
    }

    @Nullable
    @Override
    public FrameSequenceDrawableResource decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        FrameSequence fs = FrameSequence.decodeStream(source);
        FrameSequenceDrawable drawable = new FrameSequenceDrawable(fs, mProvider);
        return new FrameSequenceDrawableResource(drawable);
    }

    // This provider is entirely unnecessary, just here to validate the acquire/release process
    private class CheckingProvider implements FrameSequenceDrawable.BitmapProvider {

        @Override
        public Bitmap acquireBitmap(int minWidth, int minHeight) {
            return bitmapPool.getDirty(minWidth, minHeight, Bitmap.Config.ARGB_8888);
        }

        @Override
        public void releaseBitmap(Bitmap bitmap) {
            bitmapPool.put(bitmap);
        }
    }

    private CheckingProvider mProvider = new CheckingProvider();

}
