package com.example.mealz.view.mealdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.databinding.ItemIngredientBinding;
import com.example.mealz.model.Ingredient;

import java.util.ArrayList;

import kotlin.io.path.LinkFollowing;

public class IngredientAdapter extends ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder> {


    protected IngredientAdapter() {
        super(new DiffUtil.ItemCallback<Ingredient>() {
            @Override
            public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
        });
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return IngredientViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        ItemIngredientBinding binding;

        public IngredientViewHolder(@NonNull ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static IngredientViewHolder create(ViewGroup parent) {
            ItemIngredientBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_ingredient,
                    parent,
                    false
            );

            return new IngredientViewHolder(binding);
        }

        public void bind(Ingredient ingredient) {
            Glide.with(binding.ingredientImageView.getContext()).load(ingredient.getImageUrl()).into(binding.ingredientImageView);
            binding.ingredientMeasureTextView.setText(ingredient.getMeasure());
            binding.ingredientNameTextView.setText(ingredient.getName());
        }
    }

}
