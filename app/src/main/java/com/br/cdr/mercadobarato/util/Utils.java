package com.br.cdr.mercadobarato.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.br.cdr.mercadobarato.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clebr on 18/02/2017.
 */

public class Utils {


    public static void openFragment(Class fragmentClass, FragmentManager fm) {
//        HomeActivity fr = new HomeActivity();
        Fragment fragment = null;
//        Class fragmentClass = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

//        FragmentManager fm = getFragmentManager();
    }

    public static boolean validatePermissions(Activity activity, int requestCode, String... permissions) {
        List<String> list = new ArrayList<>();

        for (String permission : permissions) {

            boolean ok = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!ok) {
                list.add(permission);
            }
        }

        if (list.isEmpty()) {
            return true;
        }

        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);
        ActivityCompat.requestPermissions(activity, newPermissions, 1);

        return false;
    }


    public static void alertAndFinish(final Activity activity) {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.finish();
                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    public static void setPreferredDistance(int preferredDistance, Activity activity) {
        SharedPreferences sharedPref = activity
                .getSharedPreferences(activity.getString(R.string.app_preferences),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.preferred_distance), preferredDistance);
        editor.commit();
    }

    public static int getPreferredDistance(Activity activity) {
        SharedPreferences sharedPref = activity
                .getSharedPreferences(activity.getString(R.string.app_preferences),
                        Context.MODE_PRIVATE);
        int preferredDistance = sharedPref.getInt(activity.getString(R.string.preferred_distance), 1000) / 1000;
        return preferredDistance;
    }

    public static double distanceBetween(Location l1, Location l2) {

        double lat1 = l1.getLatitude();
        double lon1 = l1.getLongitude();
        double lat2 = l2.getLatitude();
        double lon2 = l2.getLongitude();
        double R = 6371; // km
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        lat1 = lat1 * Math.PI / 180;
        lat2 = lat2 * Math.PI / 180;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c * 1000;
        return d;
    }


}
