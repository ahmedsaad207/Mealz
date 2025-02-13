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
import com.example.mealz.model.NetworkMeal;

public class MealAdapter extends ListAdapter<NetworkMeal, MealAdapter.MealViewHolder> {
    OnMealItemClickListener onMealItemClickListener;

    public MealAdapter(OnMealItemClickListener onMealItemClickListener) {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull NetworkMeal oldItem, @NonNull NetworkMeal newItem) {
                return oldItem.getMealName().equals(newItem.getMealName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull NetworkMeal oldItem, @NonNull NetworkMeal newItem) {
                return oldItem.getMealName().equals(newItem.getMealName());
            }
        });
        this.onMealItemClickListener = onMealItemClickListener;
    }

    public MealAdapter() {
        this(null);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MealViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        NetworkMeal currentNetworkMeal = getItem(position);
        holder.bind(currentNetworkMeal, onMealItemClickListener);
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

        public void bind(NetworkMeal networkMeal, OnMealItemClickListener onMealItemClickListener) {
            mealNameTextView.setText(networkMeal.getMealName());
            Glide.with(mealImageView.getContext())
                    .load(networkMeal.getMealImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(mealImageView);
            itemView.setOnClickListener(v -> onMealItemClickListener.onclick(networkMeal.getMealId()));
        }
    }
}

