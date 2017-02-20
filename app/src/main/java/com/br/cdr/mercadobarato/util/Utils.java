package com.br.cdr.mercadobarato.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.br.cdr.mercadobarato.R;

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

}
