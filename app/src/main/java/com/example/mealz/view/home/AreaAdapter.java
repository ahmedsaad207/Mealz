package com.example.mealz.view.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealz.R;
import com.example.mealz.databinding.ItemAreaBinding;
import com.example.mealz.model.Area;

public class AreaAdapter extends ListAdapter<Area, AreaAdapter.AreaViewHolder> {
    OnItemClickListener onItemClickListener;

    public AreaAdapter(OnItemClickListener onItemClickListener) {
        super(new DiffUtil.ItemCallback<>() {

            @Override
            public boolean areItemsTheSame(@NonNull Area oldItem, @NonNull Area newItem) {
                return oldItem.getAreaName().equals(newItem.getAreaName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Area oldItem, @NonNull Area newItem) {
                return oldItem.getAreaName().equals(newItem.getAreaName());
            }
        });
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("TAG", "onCreateViewHolder: ");
        return AreaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area currentArea = getItem(position);
        holder.bind(currentArea, onItemClickListener);
    }

    static class AreaViewHolder extends RecyclerView.ViewHolder {

        ItemAreaBinding binding;

        public AreaViewHolder(@NonNull ItemAreaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static AreaViewHolder create(ViewGroup parent) {
            return new AreaViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_area, parent, false));
        }

        public void bind(Area area, OnItemClickListener onItemClickListener) {
            binding.areaTextview.setText(area.getAreaName());
            binding.areaImageview.setImageResource(area.getImageResourceId());
            itemView.setOnClickListener(v -> onItemClickListener.onclick(area.getAreaName()));
        }
    }
}