package com.wify.smart.home.utils;

import com.google.firebase.FirebaseOptions;

public class FirebaseConfiguration {

    public static FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId(UtilityConstants.FIREBASE_APP_ID) // Required for Analytics.
            .setApiKey(UtilityConstants.FIREBASE_API_KEY) // Required for Auth.
            .setDatabaseUrl(UtilityConstants.FIREBASE_DATABASE_URL)// Get it from service account
            .build();

}
