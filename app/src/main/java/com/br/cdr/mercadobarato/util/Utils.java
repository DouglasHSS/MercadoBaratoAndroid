package com.br.cdr.mercadobarato.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

    public static boolean validatePermissions(Activity activity, Fragment fragment, int requestCode, String... permissions) {
        List<String> list = new ArrayList<>();

        if (fragment != null) {
            activity = fragment.getActivity();
        }

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
        newPermissions = list.toArray(newPermissions);
        ActivityCompat.requestPermissions(activity, newPermissions, 1);

        return false;
    }

}
