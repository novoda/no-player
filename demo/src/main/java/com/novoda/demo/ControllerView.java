package com.novoda.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.novoda.notils.exception.DeveloperError;

public class ControllerView extends LinearLayout {

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw new DeveloperError("This layout only supports horizontal orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_player_controls, this, true);
    }
}
