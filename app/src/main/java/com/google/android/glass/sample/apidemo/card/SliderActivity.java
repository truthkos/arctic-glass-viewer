package com.google.android.glass.sample.apidemo.card;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.google.android.glass.widget.Slider;
import com.google.android.glass.sample.apidemo.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity that demonstrates the slider API.
 */
public final class SliderActivity extends Activity {

    private CardScrollView mCardScroller;

    private CardAdapter mCardAdapter;

    private Slider mSlider;

    private Slider.Indeterminate mIndeterminate;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Ensure screen stays on during demo.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCardScroller = new CardScrollView(this);
        mCardAdapter = new CardAdapter(createLoadingCard(this));
        mCardScroller.setAdapter(mCardAdapter);
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
            }
        });
        setContentView(mCardScroller);
        mSlider = Slider.from(mCardScroller);
        startSlider();
        String[] url = {"http://edc.tfs.alaska.edu/webcam/2015/lake/lake_20150606_1200.jpg"};
        new DownloadImageTask().execute(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    private void startSlider() {
        mIndeterminate = mSlider.startIndeterminate();
    }

    private void stopSlider() {
        if (mIndeterminate != null) {
            mIndeterminate.hide();
            mIndeterminate = null;
        }
    }

    private List<CardBuilder> createLoadingCard(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        cards.add(new CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.loading));
        return cards;
    }

    private List<CardBuilder> createCard(Context context, Drawable drawable) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        cards.add(new CardBuilder(context, CardBuilder.Layout.TEXT)
                .addImage(drawable));
        return cards;
    }

    private Drawable drawableFromUrl(String url) {
        try {
            Bitmap bitmap;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(getResources(), bitmap);
        } catch (IOException e) {
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... urls) {
            String url = urls[0];
            return drawableFromUrl(url);
        }

        protected void onPostExecute(Drawable result) {
            stopSlider();
            if (result != null) {
                mCardAdapter.mCards.add(new CardBuilder(getBaseContext(), CardBuilder.Layout.TEXT)
                        .addImage(result));
           //     mCardAdapter = new CardAdapter(createCard(getBaseContext(), result));
                mCardAdapter.notifyDataSetChanged();
            }
        }
    }



}


//        mCardScroller.setAdapter(new CardAdapter(createCards(this)));