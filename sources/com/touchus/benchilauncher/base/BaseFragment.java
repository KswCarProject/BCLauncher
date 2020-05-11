package com.touchus.benchilauncher.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    public abstract boolean onBack();

    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public void onDestroyView() {
        super.onDestroyView();
    }
}
