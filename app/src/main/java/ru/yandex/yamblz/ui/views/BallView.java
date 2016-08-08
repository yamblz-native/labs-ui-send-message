package ru.yandex.yamblz.ui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.yamblz.R;

/**
 * Created by vorona on 07.08.16.
 */

public class BallView extends LinearLayout {

    @BindView(R.id.ball_img)
    ImageView ball;
    @BindView(R.id.my_button)
    ButtonView button;

    private Paint background;
    private boolean waiting = false;
    private boolean success = false;

    public BallView(Context context) {
        super(context);
        init(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initColor(context, attrs);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initColor(context, attrs);
    }

    private void initColor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BallView, 0, 0);
        String str = a.getString(R.styleable.BallView_ball_color);
        background.setColor(Color.parseColor(str));
        a.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.loading_ball, this);
        ButterKnife.bind(this, view);
        background = new Paint();
    }

    private Bitmap drawCircle() {
        Bitmap tempBitmap = Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawColor(Color.TRANSPARENT);
        tempCanvas.drawCircle(getHeight()/2, getHeight()/2, getHeight() / 2, background);
        return tempBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.w("view", "open" + getId());
        ball.setImageDrawable(new BitmapDrawable(getResources(), drawCircle()));
        ObjectAnimator.ofFloat(ball, "alpha", 0f).setDuration(0).start();
    }

    public void animateWaiting() {
        waiting = true;
        ObjectAnimator.ofFloat(ball, "alpha", 1f).setDuration(0).start();

        ObjectAnimator anim = ObjectAnimator.ofFloat(ball, "translationY", 0, 300, -300, 300);
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), null));

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(button.closeButton(), anim);
        set.start();

//        while (waiting) {
//            ObjectAnimator.ofFloat(ball, "translationY", 0, 300, -300, 300);
//            anim.setDuration(1000);
//            anim.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), null));
//        }
//        }
    }

    void finishWaitingWithSuccess() {
        waiting = false;
        success = true;
    }
}
