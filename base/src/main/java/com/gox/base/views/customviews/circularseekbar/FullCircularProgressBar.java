package com.gox.base.views.customviews.circularseekbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class FullCircularProgressBar extends View {

    private float textHeight;
    private float progress = 0;
    private RectF rectCircle;
    private RectF rectFMain;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint drawTitle;
    private Paint drawSubTitle;
    private Paint dashLinePaint;
    private Paint drawtargetTex;
    private CircularProgressBarModel mCircularProgressBarModel;

    public FullCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(CircularProgressBarModel circularProgressBarModel) {

        try {
            rectCircle = new RectF();
            rectFMain = new RectF();
            mCircularProgressBarModel = circularProgressBarModel;

            // Init Background
            backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            backgroundPaint.setColor(mCircularProgressBarModel.getBackgroundColor());
            backgroundPaint.setStyle(Paint.Style.STROKE);
            backgroundPaint.setStrokeWidth(mCircularProgressBarModel.getBackgroundStrokeWidth());

            // Init Foreground
            foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            foregroundPaint.setColor(mCircularProgressBarModel.getColor());
            foregroundPaint.setStyle(Paint.Style.STROKE);
            foregroundPaint.setStrokeWidth(mCircularProgressBarModel.getStrokeWidth());
            foregroundPaint.setMaskFilter(new BlurMaskFilter(mCircularProgressBarModel.getBlur(), BlurMaskFilter.Blur.NORMAL));
            foregroundPaint.setStrokeCap(Paint.Cap.ROUND);

            //draw tite
            drawTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
            drawTitle.setTextSize(mCircularProgressBarModel.getTitleSize());
            drawTitle.setColor(mCircularProgressBarModel.getTitleColor());
            drawTitle.setTextAlign(Paint.Align.CENTER);

            //draw sub title
            drawSubTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
            drawSubTitle.setTextSize(mCircularProgressBarModel.getSubTitleSize());
            drawSubTitle.setColor(mCircularProgressBarModel.getSubTitleColor());
            drawSubTitle.setTextAlign(Paint.Align.CENTER);

            //draw dash line
            dashLinePaint = new Paint();
            dashLinePaint.setARGB(255, 0, 0, 0);
            dashLinePaint.setStyle(Paint.Style.STROKE);
            dashLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            //draw target text
            drawtargetTex = new Paint(Paint.ANTI_ALIAS_FLAG);
            drawtargetTex.setTextSize(mCircularProgressBarModel.getTargetSize());
            drawtargetTex.setColor(mCircularProgressBarModel.getTargetColor());
            drawtargetTex.setTextAlign(Paint.Align.CENTER);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCircularProgressBarModel != null) {

            float left = getWidth() / 4;
            float width = getWidth() / 2;
            float top = getWidth() / 4;
            float angle = 360 * progress / 100;

            rectFMain.set(0, 0, getWidth(), getWidth());
            rectFMain.set(rectCircle);
            rectCircle.set(left, top, left + width, top + width);

            //drawing background
            canvas.drawOval(rectCircle, backgroundPaint);

            //drawing progress bar
            canvas.drawArc(rectCircle, mCircularProgressBarModel.getStartAngle(), angle, false, foregroundPaint);

            //calculate title text size
            mCircularProgressBarModel.setTitleSize(mCircularProgressBarModel.calculateTitleTextSize(width,
                    drawTitle, mCircularProgressBarModel.getTitleText(), mCircularProgressBarModel.getStrokeWidth(),
                    mCircularProgressBarModel.getTitleSize()));

            //drawing title
            canvas.drawText(mCircularProgressBarModel.getTitleText(), getWidth() / 2, getHeight() / 2, drawTitle);

            //calculating title text height
            textHeight = mCircularProgressBarModel.getTextHeight(mCircularProgressBarModel.getTitleText(), drawTitle);

            //calculate subtitle text size
            mCircularProgressBarModel.setSubTitleSize(mCircularProgressBarModel.calculateSubTitleTextSize(width,
                    drawSubTitle, mCircularProgressBarModel.getSubTitleText(), mCircularProgressBarModel.getStrokeWidth(),
                    mCircularProgressBarModel.getSubTitleSize()));

            //draw subtitle with top margin of 10
            canvas.drawText(mCircularProgressBarModel.getSubTitleText(), getWidth() / 2, getWidth() / 2 + textHeight + 10, drawSubTitle);

            //calculate target text
            mCircularProgressBarModel.setTargetSize(mCircularProgressBarModel.calculateTitleTextSize(left, drawtargetTex,
                    mCircularProgressBarModel.getTargetText(), mCircularProgressBarModel.getStrokeWidth(), mCircularProgressBarModel.getTargetSize()));

            if (mCircularProgressBarModel.getProgress() == progress) {
                PointF tmp = mCircularProgressBarModel.getTargetPoint(rectCircle, width, angle);

            /*    canvas.drawPath(mCircularProgressBarModel.getTargetPath(tmp, progress, left), dashLinePaint);

                //set target text
                mCircularProgressBarModel.setTargetText("Target: " + ((int) progress + 4));
*/

                System.out.println("target size: " + mCircularProgressBarModel.getTargetSize());

                //get target text width or use left-50 to center text on line
                float targetTextWidth = mCircularProgressBarModel.getTextWidth(mCircularProgressBarModel.getTargetText(), drawtargetTex);
                float targetTextHeight = mCircularProgressBarModel.getTextHeight(mCircularProgressBarModel.getTargetText(), drawtargetTex);
/*
                canvas.drawText(mCircularProgressBarModel.getTargetText(), mCircularProgressBarModel.getTargetTextPosition(
                        tmp, progress, left - 50, targetTextHeight).x, mCircularProgressBarModel.getTargetTextPosition(
                        tmp, progress, left - 50, targetTextHeight).y, drawtargetTex);*/
            }
        }
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public void setProgressWithAnimation(float progress, int duration) {
        mCircularProgressBarModel.setProgress(progress);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
