package com.gu.toolargetool.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO
 */

public class BitmapActivity extends AppCompatActivity {

    private static final String STATE_BITMAP = "bitmap";

    public static void start(Context context) {
        context.startActivity(new Intent(context, BitmapActivity.class));
    }

    private ImageView imageView;
    private TextView descriptionView;
    @Nullable private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bitmap);
        imageView = (ImageView) findViewById(R.id.image);
        descriptionView = (TextView) findViewById(R.id.description);

        if (savedInstanceState != null) {
            bitmap = savedInstanceState.getParcelable(STATE_BITMAP);
            updateViews();
        } else {
            loadStarryNight();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bitmap != null) {
            outState.putParcelable(STATE_BITMAP, bitmap);
        }
    }

    private void loadStarryNight() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.starry_night, options);
        updateViews();
    }

    @SuppressLint("StringFormatMatches")
    private void updateViews() {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            descriptionView.setText(getString(
                    R.string.bitmap_description_fmt,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    bitmap.getConfig().name(),
                    bitmap.getByteCount(),
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? bitmap.getAllocationByteCount() : null
            ));
        }
    }
}
