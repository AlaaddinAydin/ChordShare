package com.example.chordshare;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Music {
    private String musicName , musicGroup , musicLyrics;

    private Bitmap musicImage, musicChord;


    public Music(){

    }


    public Music(String musicName, String musicGroup, String musicLyrics, Bitmap musicImage, Bitmap musicChord) {
        this.musicName = musicName;
        this.musicGroup = musicGroup;
        this.musicLyrics = musicLyrics;
        this.musicImage = musicImage;
        this.musicChord = musicChord;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicGroup() {
        return musicGroup;
    }

    public void setMusicGroup(String musicGroup) {
        this.musicGroup = musicGroup;
    }

    public String getMusicLyrics() {
        return musicLyrics;
    }

    public void setMusicLyrics(String musicLyrics) {
        this.musicLyrics = musicLyrics;
    }

    public Bitmap getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(Bitmap musicImage) {
        this.musicImage = musicImage;
    }

    public Bitmap getMusicChord() {
        return musicChord;
    }

    public void setMusicChord(Bitmap musicChord) {
        this.musicChord = musicChord;
    }

    static public ArrayList<Music> getData(Context context)
    {
        ArrayList<Music> musicList = new ArrayList<>();

        ArrayList<String> musicNameList = new ArrayList<>();
        ArrayList<String> musicGroupList = new ArrayList<>();
        ArrayList<String> musicLyricsList = new ArrayList<>();


        ArrayList<Bitmap> musicImageList = new ArrayList<>();
        ArrayList<Bitmap> musicChordList = new ArrayList<>();



        try{
            SQLiteDatabase database = context.openOrCreateDatabase("Musics",Context.MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM musics" , null);

            int musicNameIndex = cursor.getColumnIndex("musicName");
            int musicGroupIndex = cursor.getColumnIndex("musicGroup");
            int musicLyricsIndex = cursor.getColumnIndex("musicLyrics");
            int musicImageIndex = cursor.getColumnIndex("musicImage");
            int musicChordIndex = cursor.getColumnIndex("musicChord");

            while (cursor.moveToNext()){

                musicNameList.add(cursor.getString(musicNameIndex));
                musicGroupList.add(cursor.getString(musicGroupIndex));
                musicLyricsList.add(cursor.getString(musicLyricsIndex));

                byte[] blobMusicImage = cursor.getBlob(musicImageIndex);
                Bitmap bitmapMusicImage = BitmapFactory.decodeByteArray(blobMusicImage , 0 , blobMusicImage.length);

                byte[] blobMusicChord = cursor.getBlob(musicChordIndex);
                Bitmap bitmapMusicChord = BitmapFactory.decodeByteArray(blobMusicChord , 0 , blobMusicChord.length);


                musicImageList.add(bitmapMusicImage);
                musicChordList.add(bitmapMusicChord);
            }

            cursor.close();


            for(int i = 0 ; i < musicNameList.size(); i++){

                Music music = new Music();

                music.setMusicName(musicNameList.get(i));
                music.setMusicGroup(musicGroupList.get(i));
                music.setMusicLyrics(musicLyricsList.get(i));

                music.setMusicImage(musicImageList.get(i));
                music.setMusicChord(musicChordList.get(i));


                musicList.add(music);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return musicList;

    }
}
