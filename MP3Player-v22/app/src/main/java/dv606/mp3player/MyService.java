package dv606.mp3player;

import android.app.Service;
import android.content.Intent;
import android.drm.DrmManagerClient;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    MediaPlayer mediaPlayer;
    ArrayList<Song> song_list;
    private final IBinder binder = new MusicBinder();
    private int list_pos;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list_pos = 0;
        mediaPlayer = new MediaPlayer();
        initPlayer();
    }
    public void SetSongList(ArrayList<Song> songs)
    {
        song_list = songs;
    }
    private void initPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }
    public void stop()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
    }
    public void play(int id)
    {
        list_pos = id;
       final Song song = song_list.get(id);
        if(song == null) return;

        try
        {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop(); // stop the current song

            mediaPlayer.reset(); // reset the resource of player
            //mediaPlayer.setDataSource(this, Uri.parse(song.getPath())); // set the song to play
            String path = song.getPath();
            mediaPlayer.setDataSource(this, Uri.parse(path)); // set the song to play
            mediaPlayer.prepareAsync(); // play!
        }
        catch(Exception e)
        {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        list_pos++;
        play(list_pos);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try
        {
            mediaPlayer.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public class MusicBinder extends Binder {
         MyService getService()
        {
            return MyService.this;
        }
    }

}
