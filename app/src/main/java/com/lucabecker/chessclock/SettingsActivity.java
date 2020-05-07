package com.lucabecker.chessclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mSaveButton;
    private RadioGroup mTimeOptions;
    private SharedPreferences mPrefs;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        initViews();
        configureToolbar();
        mPrefs = getSharedPreferences("game_options", Context.MODE_PRIVATE);
        setCheckedRadioBtn();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int optionID = mTimeOptions.getCheckedRadioButtonId();
                if(optionID != -1) {
                    RadioButton checkedButton = findViewById(optionID);
                    mPrefs.edit().putString("selected_gamemode", checkedButton.getText().toString()).apply();
                    finish();
                }
            }
        });

        loadAds();
    }

    private void setCheckedRadioBtn() {
        String gamemode = mPrefs.getString("selected_gamemode", "3 | 0");
        for (int i = 0; i < mTimeOptions.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mTimeOptions.getChildAt(i);
            if (radioButton.getText().toString().equals(gamemode)) {
                radioButton.setChecked(true);
            }
        }
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        mSaveButton = findViewById(R.id.saveButton);
        mTimeOptions = findViewById(R.id.timeOptions);
        mAdView = findViewById(R.id.adView);
    }

    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadAds() {
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
