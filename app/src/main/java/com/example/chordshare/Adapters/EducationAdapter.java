package com.example.chordshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chordshare.Education;
import com.example.chordshare.R;

import java.util.ArrayList;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationHolder> {

    private ArrayList<Education> educationList;
    private Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnItemClickListener listener;

    public EducationAdapter(ArrayList<Education> educationList, Context context) {
        this.educationList = educationList;
        this.context = context;
    }

    @NonNull
    @Override
    public EducationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.education_item, parent, false);
        return new EducationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationHolder holder, int position) {
        Education education = educationList.get(position);
        holder.setData(education);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(educationList.get(clickedPosition), clickedPosition);
                }
            }
        });

        if (position == selectedPosition) {
            // Seçili öğenin arka planını veya başka bir özelliğini değiştirme
        } else {
            // Diğer öğelerin arka planını veya başka bir özelliğini değiştirme
        }
    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

    class EducationHolder extends RecyclerView.ViewHolder {

        TextView txtEducationName, txtEducationDescription;

        public EducationHolder(@NonNull View itemView) {
            super(itemView);

            txtEducationName = itemView.findViewById(R.id.education_item_textViewEducationName);
            txtEducationDescription = itemView.findViewById(R.id.education_item_textViewEducationDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(educationList.get(position), position);
                    }
                }
            });
        }

        public void setData(Education education) {
            this.txtEducationName.setText(education.getEducationName());
            this.txtEducationDescription.setText(education.getEducationDescription());
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < educationList.size()) {
            educationList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, educationList.size());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Education education, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
