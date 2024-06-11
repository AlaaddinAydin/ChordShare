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

import com.example.chordshare.Adapters.EducationAdapter;
import com.example.chordshare.Adapters.MusicAdapter;
import com.example.chordshare.Education;
import com.example.chordshare.EducationDetail;
import com.example.chordshare.EducationDetailView;
import com.example.chordshare.Music;
import com.example.chordshare.MusicDetail;
import com.example.chordshare.R;



public class EducationMain extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private EducationAdapter adapter;

    static public EducationDetail educationDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        mRecyclerView = (RecyclerView) findViewById(R.id.education_activity_recyclerView);
        adapter = new EducationAdapter(Education.getData(this), this);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new  EducationMain.GridManagerDecoration());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EducationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Education education, int position) {
                educationDetail = new EducationDetail(education.getEducationName(), education.getEducationDescription(), education.getEducationLink());

                Intent detailIntent = new Intent(EducationMain.this, EducationDetailView.class);
                detailIntent.putExtra("position", position);
                detailIntent.putExtra("educationLink", education.getEducationLink());
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
        getMenuInflater().inflate(R.menu.education_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.education_menu_add_education) {
            // Intent geçiş
            Intent addEducationIntent = new Intent(this, AddEducation.class);
            finish();
            startActivity(addEducationIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}