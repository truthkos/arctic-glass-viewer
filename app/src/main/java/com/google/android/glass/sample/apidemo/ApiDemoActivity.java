/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.glass.sample.apidemo;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.sample.apidemo.card.CardAdapter;
import com.google.android.glass.sample.apidemo.card.CardBuilderActivity;
import com.google.android.glass.sample.apidemo.card.CardScrollViewActivity;
import com.google.android.glass.sample.apidemo.voicemenu.VoiceMenuActivity;
import com.google.android.glass.sample.apidemo.card.SliderActivity;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a card scroll view with examples of different GDK APIs.
 *
 * <ol>
 * <li> CardBuilder API
 * <li> CardScrollView API
 * <li> VoiceMenu
 * </ol>
 */
public class ApiDemoActivity extends Activity {

    private static final String TAG = ApiDemoActivity.class.getSimpleName();

    // Index of api demo cards.
    // Visible for testing.
    static final int CHECK_SENSORS = 0;
    static final int CHECK_TRAPS = 1;
    static final int GALLERY = 2;
    static final int IMAGE = 3;

    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    // Visible for testing.
    CardScrollView getScroller() {
        return mCardScroller;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mAdapter = new CardAdapter(createCards(this));
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener();
    }

    /**
     * Create list of API demo cards.
     */
    private List<CardBuilder> createCards(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        cards.add(CHECK_SENSORS, new CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_builder));
        cards.add(CHECK_TRAPS, new CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_scroll_view));
        cards.add(GALLERY, new CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText("Image Gallery"));
        return cards;
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

    /**
     * Different type of activities can be shown, when tapped on a card.
     */
    private void setCardScrollerListener() {
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;
                switch (position) {
                    case CHECK_SENSORS:
                        startActivity(new Intent(ApiDemoActivity.this, CardBuilderActivity.class));
                        break;

                    case CHECK_TRAPS:
                        startActivity(new Intent(ApiDemoActivity.this,
                                CardScrollViewActivity.class));
                        break;

                    case GALLERY:
                        startActivity(new Intent(ApiDemoActivity.this, VoiceMenuActivity.class));
                        break;

                    default:
                        soundEffect = Sounds.ERROR;
                        Log.d(TAG, "Don't show anything");
                }

                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }
}
