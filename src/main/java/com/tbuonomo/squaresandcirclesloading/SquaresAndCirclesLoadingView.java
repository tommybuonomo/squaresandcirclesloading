package com.tbuonomo.squaresandcirclesloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by tommy on 02/07/16.
 */
public class SquaresAndCirclesLoadingView extends RelativeLayout {
    private enum InterpolatorValues {
        FAST_IN_FAST_OUT (new FastOutSlowInInterpolator()),
        ANTICIPATE_OVERSHOOT(new AnticipateOvershootInterpolator()),
        OVERSHOOT(new OvershootInterpolator()),
        ACCELERATE_DECELERATE(new AccelerateDecelerateInterpolator()),
        DECELERATE(new DecelerateInterpolator()),
        LINEAR_OUT_SLOW_IN(new LinearOutSlowInInterpolator());

        private Interpolator mInterpolator;

        InterpolatorValues(Interpolator interpolator) {
            this.mInterpolator = interpolator;
        }

        public Interpolator getInterpolator() {
            return mInterpolator;
        }

    }
    private static final int DEFAULT_DURATION = 800;

    private static final int DEFAULT_POINT_COLOR = Color.parseColor("#1255c6");
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private ImageView[] mCircles;

    private Interpolator mInterpolator;
    private AnimatorSet mCircleAnimator;
    private int mDuration;
    private int mSize;
    private int mCircleRadius;
    private ImageView mSquare;

    public SquaresAndCirclesLoadingView(Context context) {
        super(context);
        init(context, null);
    }

    public SquaresAndCirclesLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SquaresAndCirclesLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        removeViewsIfNeeded();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SquaresAndCirclesLoadingView);

            int circleColor = a.getColor(R.styleable.SquaresAndCirclesLoadingView_circlesColor, DEFAULT_POINT_COLOR);
            setUpCircles(context, circleColor);

            mDuration = a.getInt(R.styleable.SquaresAndCirclesLoadingView_animationDuration, DEFAULT_DURATION);
            int backgroundColor = a.getColor(R.styleable.SquaresAndCirclesLoadingView_backgroundColor, DEFAULT_BACKGROUND_COLOR);

            int interpolator = a.getInt(R.styleable.SquaresAndCirclesLoadingView_interpolator, 0);

            mInterpolator = InterpolatorValues.values()[interpolator].getInterpolator();

            setUpSquare(context, backgroundColor);
            a.recycle();
        } else {
            setUpCircleColors(DEFAULT_POINT_COLOR);
            mDuration = DEFAULT_DURATION;
        }
    }

    private void setUpCircles(Context context, int circleColor) {
        mCircles = new ImageView[4];

        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(new QuartCircle(circleColor, QuartCircle.Direction.values()[i]));
            mCircles[i] = imageView;
            addView(imageView);
        }
    }

    private void setUpSquare(Context context, int color) {
        mSquare = new ImageView(context);
        mSquare.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.square));
        addView(mSquare);
        GradientDrawable gradientDrawable = (GradientDrawable) mSquare.getDrawable();
        gradientDrawable.setColor(color);
        mSquare.setVisibility(INVISIBLE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize = Math.min(w, h);
        setUpCirclesSize();
        setCirclesAnglePosition(Math.PI / 4);
        setUpAnimators(mDuration);
        mCircleAnimator.start();
    }

    private void setUpAnimators(int duration) {
        ValueAnimator rotationStart = ValueAnimator.ofFloat(0, (float) Math.PI * 3 / 2);

        rotationStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setCirclesAnglePosition(value / 3 + Math.PI / 4);
                for (ImageView circle : mCircles) {
                    circle.setRotation((float) Math.toDegrees(value));
                }
            }
        });

        rotationStart.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSquare.setVisibility(INVISIBLE);
                for (ImageView circle : mCircles) {
                    ((QuartCircle) circle.getDrawable()).setFill(false);
                }
            }
        });

        ValueAnimator rotationEnd = ValueAnimator.ofFloat(0, -(float) Math.PI * 3 / 2);
        rotationEnd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setCirclesAnglePosition(value / 3 + Math.PI / 4);
                mSquare.setRotation((float) Math.toDegrees(value / 3));
                for (ImageView circle : mCircles) {
                    circle.setRotation((float) Math.toDegrees(value));
                }
            }
        });

        rotationEnd.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCircleAnimator.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSquare.setVisibility(VISIBLE);
                for (ImageView circle : mCircles) {
                    ((QuartCircle) circle.getDrawable()).setFill(true);
                }
            }
        });

        mCircleAnimator = new AnimatorSet();
        mCircleAnimator.playSequentially(rotationStart, rotationEnd);

        for (Animator animator : mCircleAnimator.getChildAnimations()) {
            animator.setDuration(duration);
            animator.setInterpolator(mInterpolator);
            animator.setStartDelay(500);
        }
    }

    private void setUpCirclesSize() {
        mCircleRadius = mSize / 5;
        for (ImageView circle : mCircles) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) circle.getLayoutParams();
            params.width = mCircleRadius * 2;
            params.height = mCircleRadius * 2;
            params.addRule(CENTER_IN_PARENT, TRUE);
            circle.setLayoutParams(params);
        }

        int translation = mSize / 2 - mCircleRadius;
        RelativeLayout.LayoutParams squareParams = (RelativeLayout.LayoutParams) mSquare.getLayoutParams();
        squareParams.width = (int) ((Math.cos(Math.PI / 4) * translation) * 2);
        squareParams.height = (int) ((Math.sin(Math.PI / 4) * translation) * 2);
        squareParams.addRule(CENTER_IN_PARENT, TRUE);
        mSquare.setLayoutParams(squareParams);
    }

    private void setCirclesAnglePosition(double startAngle) {
        int translation = mSize / 2 - mCircleRadius;
        for (int i = 0; i < 4; i++) {
            double angle = startAngle + i * Math.PI / 2;
            mCircles[i].setTranslationX((float) (Math.cos(angle) * translation));
            mCircles[i].setTranslationY(-(float) (Math.sin(angle) * translation));
        }
    }


    private void setUpCircleColors(int color) {
        for (ImageView circle : mCircles) {
            GradientDrawable gradientDrawable = (GradientDrawable) circle.getDrawable();
            gradientDrawable.setColor(color);
        }
    }

    private void removeViewsIfNeeded() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
    }

    //*********************************************************
    // Users Methods
    //*********************************************************

    public void setCirclesColor(int color) {
        setUpCircleColors(color);
    }

    //*********************************************************
    // Lifecycle
    //*********************************************************

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCircleAnimator != null) {
            mCircleAnimator.end();
            mCircleAnimator.removeAllListeners();
            for (Animator animator : mCircleAnimator.getChildAnimations()) {
                animator.end();
                animator.removeAllListeners();
            }
            mCircleAnimator = null;
        }
    }
}
