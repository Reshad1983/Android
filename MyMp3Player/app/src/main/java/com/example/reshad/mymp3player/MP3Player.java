package com.example.reshad.mymp3player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple MP3 player skeleton for 2DV606 Assignment 2.
 *
 * Created by Oleksandr Shpak in 2013.
 * Ported to Android Studio by Kostiantyn Kucher in 2015.
 * Last modified by Kostiantyn Kucher on 10/09/2015.
 */
public class MP3Player extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "This activity Mp3 player";
    // This is an oversimplified approach which you should improve
    // Currently, if you exit/re-enter activity, a new instance of player is created
    // and you can't, e.g., stop the playback for the previous instance,
    // and if you click a song, you will hear another audio stream started
    public static MyService player_service;
    private Intent intent;
    Button play_btn, next_btn, prev_btn;
    TextView artist_name;
    public static String PACKAGE_NAME;
    ArrayList<Song> songs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3_layout);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        artist_name = (TextView)findViewById(R.id.artist_id);

        play_btn = (Button)findViewById(R.id.paus_btn_id);
        prev_btn = (Button)findViewById(R.id.pre_btn_id);
        next_btn = (Button)findViewById(R.id.next_btn_id);
        play_btn.setOnClickListener(this);
        prev_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        songs = songList();
        ListView view = (ListView)findViewById(R.id.listview_id);
        view.setAdapter(new PlayListAdapter(this, songs));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {

                playPickedSong(pos);
            }
        });

    }
    private void playPickedSong(int id) {

        play_btn.setBackgroundResource(android.R.drawable.ic_media_pause);
        player_service.play(id);
        artist_name.setText(player_service.getPlayingSong().getName());
    }

    @Override
    public void onClick(View view) {
       switch(view.getId())
       {
           case R.id.paus_btn_id:
               if(player_service.listSize() > 0)
               {
                   if(player_service.isPlaying())
                   {
                       play_btn.setBackgroundResource(android.R.drawable.ic_media_play);
                        player_service.play_pause();
                        artist_name.setText(player_service.getPlayingSong().getName());
                   }
                   else
                   {
                        play_btn.setBackgroundResource(android.R.drawable.ic_media_pause);
                       player_service.play_pause();
                        artist_name.setText(player_service.getPlayingSong().getName());
                   }

               }
               break;
           case R.id.next_btn_id:
               play_btn.setBackgroundResource(android.R.drawable.ic_media_pause);
               player_service.playNext();
               artist_name.setText(player_service.getPlayingSong().getName());
               break;
           case R.id.pre_btn_id:
               play_btn.setBackgroundResource(android.R.drawable.ic_media_pause);
               player_service.playPrev();
               artist_name.setText(player_service.getPlayingSong().getName());
               break;

       }
    }



    private class PlayListAdapter extends ArrayAdapter<Song>
    {
        public PlayListAdapter(Context context, ArrayList<Song> objects)
        {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View row, ViewGroup parent)
        {
            Song data = getItem(position);

            row = getLayoutInflater().inflate(R.layout.layout_row, parent, false);

            TextView name = (TextView) row.findViewById(R.id.label);
            name.setTextColor(Color.WHITE);
            name.setText(data.getName()+"\n" +
                    data.getArtist());
            row.setTag(data);

            return row;
        }
    }

    /**
     * Checks the state of media storage. True if mounted;
     * @return
     */
    private boolean isStorageAvailable()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Reads song list from media storage.
     * @return
     */
    private ArrayList<Song> songList()
    {
        ArrayList<Song> songs = new ArrayList<Song>();

        if(!isStorageAvailable()) // Check for media storage
        {
            Toast.makeText(this, R.string.nosd, Toast.LENGTH_SHORT).show();
            return songs;
        }

        Cursor music = getContentResolver().query( // using content resolver to read music from media storage
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.IS_MUSIC + " > 0 ",
                null, null
        );

        if (music.getCount() > 0)
        {
            music.moveToFirst();
            Song prev = null;
            do
            {
                Song song = new Song(music.getString(0), music.getString(1), music.getString(2), music.getString(3));

                if (prev != null) // play the songs in a playlist, if possible
                    prev.setNext(song);

                prev = song;
                songs.add(song);
            }
            while(music.moveToNext());

            prev.setNext(songs.get(0)); // play in loop
        }
        music.close();

        return songs;
    }


    private void stop()
    {
        player_service.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mp3_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_player:
                stop();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private ServiceConnection playerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MusicBinder binder= (MyService.MusicBinder)service;
            player_service = binder.getService();
            player_service.SetSongList(songs);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            player_service = null;

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(intent == null)
        {
            intent = new Intent(this, MyService.class);
            bindService(intent, playerConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(MP3Player.this, MainActivity.class));
            moveTaskToBack(true);
            return  true;
        }
        return super.onKeyUp(keyCode, event);
    }
@Override
    public void onDestroy() {
       try
        {
            player_service.stop();
        }
        catch (NullPointerException e)
        {
            System.out.print("Here");
        }
        stopService(intent);
        player_service = null;
        super.onDestroy();
    }
}
