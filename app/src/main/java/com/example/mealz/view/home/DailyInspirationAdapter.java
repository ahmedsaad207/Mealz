package com.example.mealz.view.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.databinding.ItemDailyInspirationBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.view.OnMealItemClickListener;

public class DailyInspirationAdapter extends ListAdapter<Meal, DailyInspirationAdapter.DailyInspirationViewHolder> {

    OnMealItemClickListener onMealItemClickListener;

    public DailyInspirationAdapter(OnMealItemClickListener onMealItemClickListener) {
        super(new DiffUtil.ItemCallback<Meal>() {
            @Override
            public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return oldItem.getMealId() == newItem.getMealId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return oldItem.getMealId() == newItem.getMealId();
            }
        });
        this.onMealItemClickListener = onMealItemClickListener;
    }

    @NonNull
    @Override
    public DailyInspirationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DailyInspirationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyInspirationViewHolder holder, int position) {
        Meal currentMeal = getItem(position);
        holder.bind(currentMeal, onMealItemClickListener);
    }

    static class DailyInspirationViewHolder extends RecyclerView.ViewHolder {
//        TextView mealName;

        ItemDailyInspirationBinding binding;

        public DailyInspirationViewHolder(@NonNull ItemDailyInspirationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static DailyInspirationViewHolder create(ViewGroup parent) {
            return new DailyInspirationViewHolder(DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_daily_inspiration, parent, false
            ));
        }

        public void bind(Meal meal, OnMealItemClickListener onMealItemClickListener) {
            Glide
                    .with(binding.imageViewDailyInspiration.getContext())
                    .load(meal.getMealImage())
                    .into(binding.imageViewDailyInspiration);
            itemView.setOnClickListener(v -> onMealItemClickListener.onclick(meal.getMealId()));
        }
    }

}