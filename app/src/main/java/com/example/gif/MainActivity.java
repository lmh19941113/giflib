package com.example.gif;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;

import java.io.InputStream;
import java.util.HashSet;

import static android.support.rastermill.FrameSequenceDrawable.LOOP_FINITE;

public class MainActivity extends AppCompatActivity {
    FrameSequenceDrawable mDrawable;
    int mResourceId;

    // This provider is entirely unnecessary, just here to validate the acquire/release process
    private class CheckingProvider implements FrameSequenceDrawable.BitmapProvider {
        HashSet<Bitmap> mBitmaps = new HashSet<Bitmap>();

        @Override
        public Bitmap acquireBitmap(int minWidth, int minHeight) {
            Bitmap bitmap =
                    Bitmap.createBitmap(minWidth + 1, minHeight + 4, Bitmap.Config.ARGB_8888);
            mBitmaps.add(bitmap);
            return bitmap;
        }

        @Override
        public void releaseBitmap(Bitmap bitmap) {
            if (!mBitmaps.contains(bitmap)) throw new IllegalStateException();
            mBitmaps.remove(bitmap);
            bitmap.recycle();
        }

        public boolean isEmpty() {
            return mBitmaps.isEmpty();
        }
    }

    final CheckingProvider mProvider = new CheckingProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResourceId = getIntent().getIntExtra("resourceId", R.raw.animated_gif);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawable.start();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawable.stop();
            }
        });
        findViewById(R.id.vis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawable.setVisible(true, true);
            }
        });
        findViewById(R.id.invis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawable.setVisible(false, true);
            }
        });
        findViewById(R.id.circle_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawable.setCircleMaskEnabled(!mDrawable.getCircleMaskEnabled());
            }
        });
        findViewById(R.id.glide_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GlideApp.with(MainActivity.this).as(FrameSequenceDrawable.class).load(mResourceId).into(drawableView);//采用Giflib来解码GIF
                GlideApp.with(MainActivity.this).asGif2().load(mResourceId).into(drawableView);//采用Giflib来解码GIF
//                Glide.with(MainActivity.this).asGif().load(mResourceId).into(drawableView);
            }
        });
    }

    private ImageView drawableView;

    @Override
    protected void onResume() {
        super.onResume();

        drawableView = findViewById(R.id.drawableview);
        InputStream is = getResources().openRawResource(mResourceId);

        FrameSequence fs = FrameSequence.decodeStream(is);
        mDrawable = new FrameSequenceDrawable(fs, mProvider);
        mDrawable.setLoopBehavior(LOOP_FINITE);
        mDrawable.setOnFinishedListener(new FrameSequenceDrawable.OnFinishedListener() {
            @Override
            public void onFinished(FrameSequenceDrawable drawable) {
                Toast.makeText(getApplicationContext(),
                        "The animation has finished", Toast.LENGTH_SHORT).show();
            }
        });
        drawableView.setBackgroundDrawable(mDrawable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        View drawableView = findViewById(R.id.drawableview);

        mDrawable.destroy();
        if (!mProvider.isEmpty()) throw new IllegalStateException("All bitmaps not recycled");

        mDrawable = null;
        drawableView.setBackgroundDrawable(null);
    }
}
