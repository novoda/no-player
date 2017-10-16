package com.novoda.demo.fake;

import android.support.annotation.ColorInt;

class Item {

    final String title;
    final int color;

    Item(String title, @ColorInt int color) {
        this.title = title;
        this.color = color;
    }
}
