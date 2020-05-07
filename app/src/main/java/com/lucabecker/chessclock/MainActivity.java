package com.lucabecker.chessclock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String gamemode;
    private static long GAME_TIME = 300000;
    private static long timeIncrease = 0;
    private Player player1;
    private Player player2;

    private RelativeLayout player1parent;
    private RelativeLayout player2parent;


    private ImageButton settingsButton;
    private ImageButton stopButton;
    private ImageButton resetButton;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("game_options", Context.MODE_PRIVATE);
        setPreferences();


        initViews();
        setListeners();
        initModsSDK();
    }

    private void initModsSDK() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

    }

    private void setPreferences() {
        gamemode = mPrefs.getString("selected_gamemode", "3 | 0");
        setGameTimes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setPreferences();
        resetGame();
    }

    private void setGameTimes() {
        String[] times = gamemode.split("\\|");
        GAME_TIME = TimeUnit.MINUTES.toMillis(Integer.parseInt(times[0].trim()));
        timeIncrease = TimeUnit.SECONDS.toMillis(Integer.parseInt(times[1].trim()));

        if (player1 != null && player2 != null) {
            if (player1.isRunning()) {
                player1.cancelTimer();
            }
            if (player2.isRunning()) {
                player2.cancelTimer();
            }

            player1.setTimeLeft(GAME_TIME);
            player2.setTimeLeft(GAME_TIME);

            player1.setTimeIncrease(timeIncrease);
            player2.setTimeIncrease(timeIncrease);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetButton:
                resetGame();
                break;

            case R.id.stopButton:
                stopGame();
                break;
            case R.id.settingsButton:
                resetGame();
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
    }


    private void resetGame() {
        if (player1.isRunning()) {
            player1.cancelTimer();
        }
        if (player2.isRunning()) {
            player2.cancelTimer();
        }
        player1.setTimeLeft(GAME_TIME);
        player2.setTimeLeft(GAME_TIME);
        player1.writeTime(GAME_TIME);
        player2.writeTime(GAME_TIME);

        stopButton.setImageDrawable(getDrawable(R.drawable.ic_pause));
    }

    private void initViews() {
        player1parent = findViewById(R.id.player1);
        player2parent = findViewById(R.id.player2);


        player1parent.setBackground(getBackgroundDrawable(R.color.colorDefaultPlayer1));
        player2parent.setBackground(getBackgroundDrawable(R.color.colorDefaultPlayer2));

        TextView player1TV = findViewById(R.id.player1TimeTV);
        TextView player2TV = findViewById(R.id.player2TimeTV);

        player1 = new Player(player1TV, GAME_TIME, timeIncrease, this);
        player2 = new Player(player2TV, GAME_TIME, timeIncrease, this);

        settingsButton = findViewById(R.id.settingsButton);
        resetButton = findViewById(R.id.resetButton);
        stopButton = findViewById(R.id.stopButton);
        stopButton.setImageDrawable(getDrawable(R.drawable.ic_pause));
    }

    public void stopGame() {
        if (player1.isRunning() || player2.isRunning()) {
            if (stopButton.getDrawable().getConstantState()
                    .equals(getDrawable(R.drawable.ic_pause).getConstantState())) {
                if (player1.isRunning()) {
                    player1.cancelTimer();
                    player1.setRunning(true);
                }
                if (player2.isRunning()) {
                    player2.cancelTimer();
                    player2.setRunning(true);
                }
                stopButton.setImageDrawable(getDrawable(R.drawable.ic_play));
            } else {
                if (player1.isRunning()) {
                    player1.startTimer();
                }
                if (player2.isRunning()) {
                    player2.startTimer();
                }
                stopButton.setImageDrawable(getDrawable(R.drawable.ic_pause));
            }
        }
    }

    private void setListeners() {
        player1parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Case 1: No Clock is running : start Clock of player 1
                // Case 2: Player 1 Clock is running: stop clock and start player 2 Clock
                // Case 3: Player 2 Clock is running: do nothing
                if (!player1.isRunning()) {
                    if (!player2.isRunning()) {
                        //Case 1
                        player1.startTimer();
                    }
                    //Case 2
                } else {
                    // Case 3
                    player1.cancelTimer();
                    player2.startTimer();
                }
            }
        });

        player2parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!player2.isRunning()) {
                    if (!player1.isRunning()) {
                        player2.startTimer();
                    }
                } else {
                    player2.cancelTimer();
                    player1.startTimer();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGame();
    }

    private GradientDrawable getBackgroundDrawable(int rID) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50f);
        drawable.setColor(getColor(rID));

        return drawable;
    }
}
