package com.example.mealz.data.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mealz.model.Ingredient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class MealFileDataSource {

    private final Context context;

    private static MealFileDataSource instance = null;

    public static MealFileDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealFileDataSource(context);
        }
        return instance;
    }

    private MealFileDataSource(Context context) {
        this.context = context;
    }

    public void downloadMealImage(String mealImageUrl) {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "meals");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = mealImageUrl.substring(mealImageUrl.lastIndexOf("/") + 1);
        File file = new File(dir, fileName);

        if (file.exists()) {
            Toast.makeText(context, "already saved", Toast.LENGTH_SHORT).show();
            return;
        }


        Glide.
                with(context)
                .asBitmap()
                .load(mealImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void downloadIngredientImages(List<Ingredient> ingredients) {
        Log.d("TAG", "downloadIngredientImages: ");
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ingredients");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (Ingredient ingredient : ingredients) {

            String fileName = ingredient.getImageUrl().substring(ingredient.getImageUrl().lastIndexOf("/") + 1);
            File file = new File(dir, fileName);

            if (file.exists()) {
                continue;
            }

            Glide.
                    with(context)
                    .asBitmap()
                    .load(ingredient.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }


    }
}

