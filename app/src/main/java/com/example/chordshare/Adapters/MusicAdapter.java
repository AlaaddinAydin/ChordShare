package com.example.chordshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chordshare.Music;
import com.example.chordshare.R;

import java.util.ArrayList;

public class MusicAdapter  extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {

    private ArrayList<Music> musicList;

    private Context context;

    private OnItemClickListener listener;

    public MusicAdapter(ArrayList<Music> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.music_item, parent , false);

        return new MusicHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        Music music = musicList.get(position);
        holder.setData(music);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class MusicHolder extends RecyclerView.ViewHolder{

        TextView txtMusicName , txtMusicGroup , txtMusicLyrics;
        ImageView imgMusicImage;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);

            txtMusicName = (TextView) itemView.findViewById(R.id.music_item_textViewMusicName);
            txtMusicGroup = (TextView) itemView.findViewById(R.id.music_item_textViewMusicGroup);
            txtMusicLyrics = (TextView) itemView.findViewById(R.id.music_item_textViewMusicLyrics);

            imgMusicImage = (ImageView) itemView.findViewById(R.id.music_item_imageViewMusicImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(musicList.get(position));
                    }
                }
            });
        }

        public void setData(Music music){

            this.txtMusicName.setText(music.getMusicName());
            this.txtMusicGroup.setText(music.getMusicGroup());
            this.txtMusicLyrics.setText(music.getMusicLyrics());

            this.imgMusicImage.setImageBitmap(music.getMusicImage());

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Music music);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
