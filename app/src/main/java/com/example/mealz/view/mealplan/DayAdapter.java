package com.example.mealz.view.mealplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealz.R;

import java.util.List;

public class DayAdapter extends ListAdapter<Integer, DayAdapter.DayViewHolder> {
    OnDayItemClickListener onDayItemClickListener;

    public DayAdapter(OnDayItemClickListener onDayItemClickListener, List<Integer> days) {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
                return false;
            }
        });
        submitList(days);
        this.onDayItemClickListener = onDayItemClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DayViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Integer currentDay = getItem(position);
        holder.bind(currentDay, onDayItemClickListener);
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;

        public DayViewHolder(@NonNull View view) {
            super(view);
            dayTextView = view.findViewById(R.id.dayTextView);
        }

        public static DayViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
            return new DayViewHolder(view);
        }

        public void bind(Integer day, OnDayItemClickListener onDayItemClickListener) {
            dayTextView.setText(day+"");
            itemView.setOnClickListener(v -> onDayItemClickListener.displayMeals(day));
        }
    }
}


