package com.example.gif;

import android.support.annotation.NonNull;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.resource.drawable.DrawableResource;

/**
 * Created by Administrator on 2018\11\18 0018.
 */

public class FrameSequenceDrawableResource extends DrawableResource<FrameSequenceDrawable> {
    public FrameSequenceDrawableResource(FrameSequenceDrawable drawable) {
        super(drawable);
    }

    @NonNull
    @Override
    public Class<FrameSequenceDrawable> getResourceClass() {
        return FrameSequenceDrawable.class;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void recycle() {
        drawable.stop();
        drawable.destroy();
    }
}
