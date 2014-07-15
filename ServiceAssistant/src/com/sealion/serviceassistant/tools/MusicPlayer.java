package com.sealion.serviceassistant.tools;

import java.io.File;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

/**
 * 音乐播放工具
 */
public class MusicPlayer {
	private final static String TAG = "MusicPlayer";
	private static MediaPlayer mMediaPlayer;
	private Context mContext;

	public MusicPlayer(Context context){
		mContext = context;
	}

    /**
     * 播放一个文件
     * @param file 需要播放的文件
     */
	public void playMicFile(File file){
		if (file!=null && file.exists()) {
			Uri uri = Uri.fromFile(file);
			mMediaPlayer = MediaPlayer.create(mContext, uri);
			mMediaPlayer.start(); 
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					//TODO:finish 
					Log.i(TAG, "Finish");
				}
			});
		}
	}

    /**
     * 停止播放正在播放的文件
     */
	public void stopPlayer(){
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
	}
}
