package com.example.mealz.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mealz.R;
import com.example.mealz.model.Meal;

public class MealAdapter extends ListAdapter<Meal, MealAdapter.MealViewHolder> {
    OnMealItemClickListener onMealItemClickListener;

    public MealAdapter(OnMealItemClickListener onMealItemClickListener) {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return false;
            }
        });
        this.onMealItemClickListener = onMealItemClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MealViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal currentMeal = getItem(position);
        holder.bind(currentMeal, onMealItemClickListener);
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView;
        ImageView mealImageView;

        public MealViewHolder(@NonNull View view) {
            super(view);
            mealNameTextView = view.findViewById(R.id.mealNameTextView);
            mealImageView = view.findViewById(R.id.mealImageView);
        }

        public static MealViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
            return new MealViewHolder(view);
        }

        public void bind(Meal meal, OnMealItemClickListener onMealItemClickListener) {
            mealNameTextView.setText(meal.getName());
            Glide.with(mealImageView.getContext())
                    .load(meal.getUrlImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(mealImageView);
            itemView.setOnClickListener(v -> onMealItemClickListener.onclick(meal));
        }
    }
}

