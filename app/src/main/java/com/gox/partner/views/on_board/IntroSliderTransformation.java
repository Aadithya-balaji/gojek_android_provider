package com.gox.partner.views.on_board;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.gox.partner.R;

public class IntroSliderTransformation implements ViewPager.PageTransformer {
    public void transformPage(@NonNull View page, float position) {
        TextView tvDesc = page.findViewById(R.id.description);
        TextView tvTitle = page.findViewById(R.id.title);
        ImageView ivIntroImg = page.findViewById(R.id.img_pager_item);
        if (position < (float) -1) page.setAlpha(0.0F);
        else if (position <= (float) 1) {
            ivIntroImg.setTranslationX(-position * 0.5F * (float) page.getWidth());
            tvTitle.setTranslationX(-position * 1.0F * (float) page.getWidth());
            tvDesc.setTranslationX(-position * 2.0F * (float) page.getWidth());
        } else page.setAlpha(0.0F);
    }
}