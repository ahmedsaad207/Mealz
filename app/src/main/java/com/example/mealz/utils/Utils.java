package com.example.mealz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Utils {
    private Utils() {
    }

    public static String getVideoIframe(String youtubeUrl) {
        if (youtubeUrl != null && youtubeUrl.contains("v=")) {
            String videoId = youtubeUrl.substring(youtubeUrl.indexOf("v=") + 2);
            return "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        }
        return null;
    }

    public static int getDrawableResourceForCountry(String countryName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(
                countryName.toLowerCase(),
                "drawable",
                context.getPackageName());
    }
}
