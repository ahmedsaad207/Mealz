package com.example.mealz.view.home;

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
import com.example.mealz.model.Category;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {
    OnItemClickListener onItemClickListener;

    public CategoryAdapter() {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return oldItem.getIdCategory() == newItem.getIdCategory();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return oldItem.getIdCategory() == newItem.getIdCategory();
            }
        });
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CategoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentCategory = getItem(position);
        holder.bind(currentCategory, onItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, categoryDescription;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }

        public static CategoryViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        public void bind(Category category, OnItemClickListener onItemClickListener) {
            categoryName.setText(category.getCategoryName());
            categoryDescription.setText(category.getCategoryDescription());
            Glide.with(categoryImage.getContext())
                    .load(category.categoryImage)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(categoryImage);
            itemView.setOnClickListener(v -> onItemClickListener.onclick(category.getCategoryName()));
        }
    }
}