package com.touchus.benchilauncher.utils;

import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;

public class DialogUtil {
    public static void setDialogLocation(Dialog dialog, int x, int y) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.gravity = 17;
        layoutParams.x = x;
        layoutParams.y = y;
        dialogWindow.setAttributes(layoutParams);
    }
}
