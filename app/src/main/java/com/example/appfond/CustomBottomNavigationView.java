package com.example.appfond;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomBottomNavigationView extends BottomNavigationView {

    public CustomBottomNavigationView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        MenuItem item = getMenu().findItem(R.id.bottom_action_fix);
        if (item != null) {
            Drawable icon = item.getIcon();
            if (icon != null) {
                int iconSize = getResources().getDimensionPixelSize(R.dimen.custom_icon_size);
                icon.setBounds(0, 0, iconSize, iconSize);
                item.setIcon(icon);
            }
        }
    }
}

