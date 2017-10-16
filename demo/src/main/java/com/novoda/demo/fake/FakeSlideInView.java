package com.novoda.demo.fake;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.novoda.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * This class doesn't contain anything that is transferable except the `animateOut()` method
 */
public class FakeSlideInView extends FrameLayout {

    private RecyclerView recyclerView;
    private Button button;
    private View moreThingsContainer;

    public FakeSlideInView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_peekable_more_things, this);

        View moreThingsTouchStealingOverlay = findViewById(R.id.more_things_touch_stealing_overlay);
        moreThingsTouchStealingOverlay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (moreThingsContainer.getTranslationX() != translationXForPeekMode()) {
                    return false;
                }
                callback.onPeekViewDismissed();
                showExpandedView();
                return true;
            }
        });

        moreThingsContainer = findViewById(R.id.more_things_container);
        button = (Button) findViewById(R.id.more_things_button);
        recyclerView = (RecyclerView) findViewById(R.id.more_things_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RowsAdapter(createMoreThings().rows));

        moreThingsContainer.setVisibility(INVISIBLE);
    }

    private MoreThings createMoreThings() {
        return new MoreThings(
                Arrays.asList(
                        new Row("Things next", Arrays.asList(new Item("Red", Color.RED), new Item("Red2", Color.RED), new Item("Red3", Color.RED))),
                        new Row("Blue things", Arrays.asList(new Item("Blue", Color.BLUE), new Item("Blue2", Color.BLUE), new Item("Blue3", Color.BLUE), new Item("Blue4", Color.BLUE), new Item("Blue5", Color.BLUE))),
                        new Row("Some green things", Arrays.asList(new Item("Green", Color.GREEN), new Item("Green2", Color.GREEN), new Item("Green3", Color.GREEN))),
                        new Row("Yellow things", Arrays.asList(new Item("Yellow", Color.YELLOW), new Item("Yellow2", Color.YELLOW), new Item("Yellow3", Color.YELLOW), new Item("Yellow4", Color.YELLOW), new Item("Yellow5", Color.YELLOW)))
                )
        );
    }

    public interface Callback {
        void onPeekViewDismissed();
    }

    public void animateOut(Callback callback) {
        this.callback = callback;

        moreThingsContainer.setTranslationX(getWidth());
        moreThingsContainer.setVisibility(VISIBLE);
        moreThingsContainer.animate().translationX(translationXForPeekMode())
                .setDuration(getResources().getInteger(R.integer.animation_duration_millis))
                .setInterpolator(new AccelerateDecelerateInterpolator());
        updateButtonWithExpandAction();
        setDismissPeekViewClickListener();
    }

    private void updateButton(String label, View.OnClickListener onClickListener) {
        button.setText(label);
        button.setOnClickListener(onClickListener);
    }

    private void updateButtonWithExpandAction() {
        updateButton("<", new OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpandedView();
            }
        });
    }

    private void showExpandedView() {
        moreThingsContainer.setVisibility(VISIBLE);
        moreThingsContainer.animate().translationX(0);
        updateButton("x", new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissExpandedView();
            }
        });
        removeDismissPeekViewClickListener();
    }

    @Nullable
    private Callback callback;

    private void setDismissPeekViewClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPeekView();
                if (callback != null) {
                    callback.onPeekViewDismissed();
                }
            }
        });
    }

    private void dismissExpandedView() {
        moreThingsContainer.animate().translationX(translationXForPeekMode());
        setDismissPeekViewClickListener();
        updateButtonWithExpandAction();
    }

    private int translationXForPeekMode() {
        int peekViewWidth = getResources().getDimensionPixelSize(R.dimen.peek_width);
        return getWidth() - peekViewWidth;
    }

    private void dismissPeekView() {
        int parentWidth = getWidth();
        moreThingsContainer.animate().translationX(parentWidth)
                .setDuration(getResources().getInteger(R.integer.animation_duration_millis));
        removeDismissPeekViewClickListener();
    }

    private void removeDismissPeekViewClickListener() {
        setOnClickListener(null);
        setClickable(false);
    }

    private static class RowsAdapter extends RecyclerView.Adapter {

        private final List<Row> rows;
        private Toast toast;

        RowsAdapter(List<Row> rows) {
            this.rows = rows;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row, parent, false);
            return new PlainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Row row = rows.get(position);
            ((RowView) holder.itemView).update(row, itemClickListener);
        }

        private final ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(Context context, Item item) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(context, "click " + item.title, Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        @Override
        public int getItemCount() {
            if (rows == null) {
                return 0;
            }
            return rows.size();
        }
    }

    private static class MoreThings {

        final List<Row> rows;

        MoreThings(List<Row> rows) {
            this.rows = rows;
        }
    }

}
