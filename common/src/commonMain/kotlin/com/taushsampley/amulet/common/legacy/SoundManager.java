package acggames.amulet;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager {
	private static SoundPool pool;
	
	public static int wonderSong;
	
	public static void load(Context c) {
		Log.v(AmuletActivity.D_OTHER, "Sound Manager static block");
		pool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		wonderSong = pool.load(c, R.raw.wonder_song, 1);
		Log.v(AmuletActivity.D_OTHER, "Sounds loaded");
	}
	
	public static void play(int id) {
		pool.play(id, 1, 1, 1, 0, 1);
	}
	
	public static void stop(int id) {
		pool.stop(id);
	}
	
	public static void release() {
		pool.release();
	}
}
