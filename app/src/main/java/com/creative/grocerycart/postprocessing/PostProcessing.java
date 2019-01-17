package com.creative.grocerycart.postprocessing;

import android.os.Handler;

import com.creative.grocerycart.adapter.GroceryAdapter;
import com.creative.grocerycart.utils.Constants;

public class PostProcessing {

    public static void handleDataPositions(GroceryAdapter groceryAdapter) {
        new Handler().postDelayed(() -> {
            groceryAdapter.notifyDataSetChanged();
        }, Constants.DELAY_TIME_IN_MILLIS);
    }
}
