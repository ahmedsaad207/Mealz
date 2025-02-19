package com.example.mealz.view;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mealz.R;
import com.example.mealz.model.Meal;
import com.example.mealz.model.SearchItem;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.ItemType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MealAdapter<T> extends ListAdapter<T, MealAdapter.MealViewHolder> {
    OnMealItemClickListener onMealItemClickListener;

    public MealAdapter(OnMealItemClickListener onMealItemClickListener) {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                if (oldItem instanceof SearchItem) {
                    return ((SearchItem) oldItem).getName().equals(((SearchItem) newItem).getName());
                }
                return ((Meal) oldItem).getName().equals(((Meal) newItem).getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                if (oldItem instanceof SearchItem) {
                    return ((SearchItem) oldItem).getName().equals(((SearchItem) newItem).getName());
                }
                return ((Meal) oldItem).getName().equals(((Meal) newItem).getName());
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
        T currenT = getItem(position);

        if (currenT instanceof SearchItem) {
            holder.bindSearchItem((SearchItem) currenT, onMealItemClickListener);
        }
        else {
            holder.bind((Meal) currenT, onMealItemClickListener);
        }

    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView;
        ImageView mealImageView;
        ConstraintLayout constraintLayout;
        FloatingActionButton btnRemove;

        public MealViewHolder(@NonNull View view) {
            super(view);
            mealNameTextView = view.findViewById(R.id.mealNameTextView);
            mealImageView = view.findViewById(R.id.mealImageView);
            constraintLayout = view.findViewById(R.id.constraintLayout_item);
            btnRemove = view.findViewById(R.id.btnRemoveFav);
        }

        public static MealViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
            return new MealViewHolder(view);
        }

        public void bind(Meal meal, OnMealItemClickListener onMealItemClickListener) {
            mealNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(), R.drawable.bg_stroke_rounded));
            mealNameTextView.setText(meal.getName());
            itemView.setOnClickListener(v -> onMealItemClickListener.navigateToMealDetails(meal));


            Glide.with(mealImageView.getContext())
                    .load(meal.getUrlImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(mealImageView);

            if (meal.getDate() == Constants.TYPE_FAVORITE) {
                btnRemove.setImageResource(R.drawable.ic_fav_added);
                btnRemove.setVisibility(View.VISIBLE);
                btnRemove.setOnClickListener(v -> onMealItemClickListener.removeMealFromFavorites(meal));
            } else if (meal.getDate() != Constants.TYPE_DEFAULT) {
                btnRemove.setImageResource(android.R.drawable.ic_delete);
                btnRemove.setColorFilter(btnRemove.getContext().getResources().getColor(R.color.brownish_gray), android.graphics.PorterDuff.Mode.SRC_IN);
                btnRemove.setVisibility(View.VISIBLE);
                btnRemove.setOnClickListener(v -> onMealItemClickListener.removeMealFromFavorites(meal));
            }
        }

        public void bindSearchItem(SearchItem item, OnMealItemClickListener onMealItemClickListener) {
            mealNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(), R.drawable.bg_stroke_sharp));
            if (item.getType() == Constants.ITEM_AREA) {
                Glide.with(mealImageView.getContext())
                        .load(item.getResId())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(mealImageView);
            } else {
                Glide.with(mealImageView.getContext())
                        .load(item.getImageUrl())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(mealImageView);
            }
            mealNameTextView.setText(item.getName());
            itemView.setOnClickListener(v -> onMealItemClickListener.navigateToMealsList(item.getName(), item.getType()));
        }
    }
}

