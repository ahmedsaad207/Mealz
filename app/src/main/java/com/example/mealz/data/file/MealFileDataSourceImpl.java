package com.example.mealz.data.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class MealFileDataSourceImpl implements MealFileDataSource {

    private static MealFileDataSourceImpl instance = null;
    private final Context context;

    private MealFileDataSourceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public static MealFileDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealFileDataSourceImpl(context);
        }
        return instance;
    }

    public void downloadMealImage(Meal meal) {

        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "meals");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = meal.getUrlImage().substring(meal.getUrlImage().lastIndexOf("/") + 1);
        File file = new File(dir, fileName);

        if (file.exists()) {
            return;
        }

        Glide.
                with(context)
                .asBitmap()
                .load(meal.getUrlImage())
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

    public Completable downloadIngredientImages(List<Ingredient> ingredients) {
        return Completable.create(emitter -> {
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
        });
    }

    @Override
    public String getIngredientFilePath(String imageUrl, String folder) {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder);
        if (!dir.exists()) {
            return imageUrl;
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        File file = new File(dir, fileName);
        return file.getAbsolutePath();
    }
}

