package com.dawn.java.ui;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import com.dawn.java.R;

import java.util.Locale;

public class MyApplication extends Application {
    public static final String TAG = "Pulsar";
    public static final String BUNDLE_KEY_CODE_NAME = "codeName";
    public static final String BUNDLE_KEY_ATTR_NAME = "attrHelpBean";
    public static final String BUNDLE_KEY_CUR_FRAGMENT = "curFragment";
    public static final int CODE_SETTING_REQUEST_CODE = 1001;
    public static final int PARAM_SETTING_REQUEST_CODE = 2001;
    public static final int CODE_SETTING_SUCCESS_RESULT_CODE = 1;

    public static final String ATTR_HELP_BEAM_TYPE_SWITCH = "switch";
    public static final String ATTR_HELP_BEAM_TYPE_EDIT = "edit";
    public static final String ATTR_HELP_BEAM_TYPE_RADIO = "radio";
    public static final String ATTR_HELP_BEAM_TYPE_DROPLIST = "droplist";

    public static SoundPool mSoundPool;
    public static Vibrator vibrator;
    public static int soundId = 1;
    public static Context mContext;

    public static String nlscanSdcardPath;
    public static String lastImagesPath;
    public static String accuracyErrorImagesPath;


    @Override
    public void onCreate()
    {
        super.onCreate();
        initSound();
        mContext = getApplicationContext();
    }

    private void initSound(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            mSoundPool = new SoundPool.Builder().setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 10);
        }
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
       // soundId = mSoundPool.load(getApplicationContext(), R.raw.dingdj5, 1);
        nlscanSdcardPath = getExternalFilesDir(null).getAbsolutePath();
        lastImagesPath = getExternalFilesDir("LastImages").getAbsolutePath();
        accuracyErrorImagesPath = getExternalFilesDir("AccuracyErrorImages").getAbsolutePath();
    }
}
