package com.zhang.shaon.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017-11-19.
 */

public class BeatBox {
    public static final float MIN_PLAYBACK_SPEED = 0.5f;
    public static final float MAX_PLAYBACK_SPEED = 2.0f;

    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS=5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;
    private float mPlaybackSpeed;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        if(Build.VERSION.SDK_INT>=21){
            mSoundPool=new SoundPool.Builder()
                    .setMaxStreams(MAX_SOUNDS)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setLegacyStreamType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .build();
        }else{
            mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }
        mPlaybackSpeed = 1.0f;
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;

        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.i(TAG, "Cound not list assets ", e);
            return;
        }

        for(String filename: soundNames){

                /*String assetPath=SOUNDS_FOLDER+"/"+filename;
                Sound sound = new Sound(assetPath);
                //loadSounds();
                mSounds.add(sound);*/
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                Log.e(TAG, "Could not load sound " + filename, e);
            }
        }
    }

    private void load(Sound sound) throws  IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }

        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, mPlaybackSpeed);
    }

    public void release(){
        mSoundPool.release();
    }

    public float getPlaybackSpeed() {
        return mPlaybackSpeed;
    }


    public void setPlaybackSpeed(float playbackSpeed) {
        if (playbackSpeed > MAX_PLAYBACK_SPEED) {
            mPlaybackSpeed = MAX_PLAYBACK_SPEED;
        } else if (playbackSpeed < MIN_PLAYBACK_SPEED) {
            mPlaybackSpeed = MIN_PLAYBACK_SPEED;
        } else {
            mPlaybackSpeed = playbackSpeed;
        }

    }

    public List getSounds(){
        return  mSounds;
    }
}
