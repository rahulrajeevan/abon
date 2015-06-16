package ru.macrobit.abonnews.controller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.webkit.JavascriptInterface;

import java.io.IOException;

public class AudioInterface {
    Context mContext;

    private MediaPlayer mp = null;

    public AudioInterface(Context c) {
        mContext = c;
        mp = new MediaPlayer();
    }
    @JavascriptInterface
    public void playAudio(String aud) {
        if (mp.isPlaying() == true) {
            mp.stop();
            mp.release();
        }
        try {
            AssetFileDescriptor fileDescriptor = mContext.getAssets().openFd(aud);
            mp.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            fileDescriptor.close();
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}