package com.example.reshad.mymp3player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    MediaPlayer mediaPlayer;
    public static final int NOTIFICATION_ID = 1543;
    ArrayList<Song> song_list;
    private final IBinder binder = new MusicBinder();
    private int list_pos;
    private NotificationManager notifManager;
    private Notification.Builder builder;
    public MyService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Intent intent = new Intent(this, MP3Player.class);   // Notification intent
        PendingIntent notifIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder = new Notification.Builder(this);
        builder.setContentIntent(notifIntent).setContentTitle("MP3 Player")
                .setSmallIcon(R.drawable.play_icon)
            .setWhen(System.currentTimeMillis());

//.setContent(getComplexNotificationView())

        String ns = Context.NOTIFICATION_SERVICE;
        notifManager = (NotificationManager) getSystemService(ns);
        list_pos = 0;
        mediaPlayer = new MediaPlayer();
        initPlayer();
    }
    private RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                MP3Player.PACKAGE_NAME,
                R.layout.activity_custom_notification

        );
        return notificationView;
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
            notifManager.cancel(NOTIFICATION_ID);
        }
    }
    public void play_pause()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.start();
        }
    }
    public void play(int id)
    {
        list_pos = id;
       final Song song = song_list.get(id);
        if(song == null) return;

        try
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop(); // stop the current song
            }
            builder.setTicker(song.getName()).setContentText(song.getName());
            mediaPlayer.reset(); // reset the resource of player
            //mediaPlayer.setDataSource(this, Uri.parse(song.getPath())); // set the song to play
            String path = song.getPath();
            mediaPlayer.setDataSource(this, Uri.parse(path)); // set the song to play
            mediaPlayer.prepareAsync(); // play!


            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notifManager.notify(NOTIFICATION_ID, notification);
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

        if(list_pos == (song_list.size() - 1))
        {
            list_pos = 0;
            play(list_pos++);
        }
        else
        {
            play(list_pos++);

        }
    }

    public int listSize(){return song_list.size();}
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

    public Song getPlayingSong() {
        return song_list.get(list_pos);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void playPrev() {
        if(list_pos == 0)
        {
            list_pos = song_list.size() - 1;
            play(list_pos);
        }
        else
        {
            play(--list_pos);
        }
    }
    public void playNext() {
        if(list_pos == (song_list.size() - 1))
        {
            list_pos = 0;
            play(list_pos);
        }
        else
        {
            play(++list_pos);

        }
    }

    public class MusicBinder extends Binder {
         MyService getService()
        {
            return MyService.this;
        }
    }

}
