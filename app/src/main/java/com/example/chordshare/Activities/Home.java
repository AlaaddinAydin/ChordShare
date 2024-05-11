package com.example.chordshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.chordshare.About;
import com.example.chordshare.Music;
import com.example.chordshare.Adapters.MusicAdapter;
import com.example.chordshare.MusicDetail;
import com.example.chordshare.R;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MusicAdapter adapter;

    static public MusicDetail musicDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mRecyclerView = (RecyclerView) findViewById(R.id.home_avtivity_recyclerView);
        adapter = new MusicAdapter(Music.getData(this), this);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new GridManagerDecoration());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Music music, int position) {
                musicDetail = new MusicDetail(music.getMusicName(),music.getMusicGroup(),music.getMusicLyrics(), music.getMusicLink(),music.getMusicImage(),music.getMusicChord());

                Intent detailIntent = new Intent(Home.this, Detail.class);
                detailIntent.putExtra("position", position);
                startActivity(detailIntent);
            }
        });
    }

    private class GridManagerDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = 20;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_menu_add_book) {
            // Intent geçiş
            Intent addMusicIntent = new Intent(this, AddActivity.class);
            finish();
            startActivity(addMusicIntent);
            return true;
        }

        if (item.getItemId() == R.id.menu)
        {
            Intent aboutUsIntent = new Intent(this, About.class);
            startActivity(aboutUsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}