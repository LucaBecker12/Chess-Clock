package com.lucabecker.chessclock;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Player {
    private static final int COUNTDOWN_INTERVALL = 1000;
    private CountDownTimer mTimer;
    private TextView mTextView;
    private long mTimeLeft;
    private long mTimeIncrease = 0L;
    private boolean mRunning;

    private MediaPlayer mediaPlayer;

    public Player(TextView mTextView, long mTimeLeft, long mTimeIncrease, Context context) {
        this.mTimer = null;
        this.mTextView = mTextView;
        this.mTimeLeft = mTimeLeft;
        this.mTimeIncrease = mTimeIncrease;
        this.mRunning = false;
        mediaPlayer = MediaPlayer.create(context, R.raw.clock_tick);
    }

    public CountDownTimer getTimer() {
        return mTimer;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    public long getTimeLeft() {
        return mTimeLeft;
    }

    public void setTimeLeft(long mTimeLeft) {
        this.mTimeLeft = mTimeLeft;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void setRunning(boolean mRunning) {
        this.mRunning = mRunning;
    }

    public void cancelTimer() {
        this.mTimer.cancel();
        setTimeLeft(getTimeLeft() + mTimeIncrease);
        writeTime(getTimeLeft());
        setRunning(false);
    }

    public void writeTime(long time) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        mTextView.setText(String.format(Locale.getDefault(),
                "%02d:%02d", seconds / 60, seconds % 60));
    }

    public void startTimer() {
        mTimer = new CountDownTimer(mTimeLeft, COUNTDOWN_INTERVALL) {
            @Override
            public void onTick(long l) {
                mediaPlayer.start();
                setTimeLeft(l);
                writeTime(l);
            }

            @Override
            public void onFinish() {

            }
        }.start();
        setRunning(true);
    }

    public void setTimeIncrease(long timeIncrease) {
        this.mTimeIncrease = timeIncrease;
    }
}
