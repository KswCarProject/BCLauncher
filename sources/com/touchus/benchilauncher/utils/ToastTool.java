package com.touchus.benchilauncher.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.touchus.benchilauncher.R;

public class ToastTool {
    public static void showLongToast(Context cont, String msg) {
        try {
            Toast toast = Toast.makeText(cont, msg, 1);
            toast.setGravity(17, 0, 30);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void showShortToast(Context cont, String msg) {
        try {
            Toast toast = Toast.makeText(cont, msg, 0);
            toast.setGravity(17, 0, 30);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void showBigShortToast(Context cont, String str) {
        View view = LayoutInflater.from(cont).inflate(R.layout.toast_view, (ViewGroup) Toast.makeText(cont, "", 0).getView().findViewById(R.id.toastlayout));
        ((TextView) view.findViewById(R.id.txt_context)).setText(str);
        Toast toast = new Toast(cont);
        toast.setGravity(17, 200, 10);
        toast.setDuration(0);
        toast.setView(view);
        toast.show();
    }

    public static void showBigShortToast(Context cont, int resId) {
        View view = LayoutInflater.from(cont).inflate(R.layout.toast_view, (ViewGroup) Toast.makeText(cont, "", 0).getView().findViewById(R.id.toastlayout));
        ((TextView) view.findViewById(R.id.txt_context)).setText(cont.getString(resId));
        Toast toast = new Toast(cont);
        toast.setGravity(17, 200, 10);
        toast.setDuration(0);
        toast.setView(view);
        toast.show();
    }
}
