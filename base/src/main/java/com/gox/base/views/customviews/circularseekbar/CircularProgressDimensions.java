package com.gox.base.views.customviews.circularseekbar;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class CircularProgressDimensions {

    public float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public float getTextWidth(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    public int calculateTitleTextSize(float mWidth, Paint paint, String text, float stroke, int titleSize) {
        float textWith = getTextWidth(text, paint);
        float textSize = titleSize;
        float width = mWidth - (stroke * 4);
        if (textWith > width) {
            textSize = textSize - 2;
        }
        paint.setTextSize(textSize);

        return (int)textSize;
    }

    public int calculateSubTitleTextSize(float mWidth, Paint paint, String text, float stroke, int subTitleSize) {
        float textWith = getTextWidth(text, paint);
        float textHeight = getTextHeight(text, paint);
        float textSize = subTitleSize;
        float width = (float) Math.sqrt((mWidth * mWidth) - (textHeight * textHeight)) - ((stroke * 4) + 10);
        System.out.println("text width: " + width);
        if (textWith > width) {
            textSize = textSize - 2;
        }
        paint.setTextSize(textSize);

        return (int) textSize;
    }

    public PointF getTargetPoint(RectF rectF, float width, float angle) {
        return new PointF(
                (float) (rectF.left + rectF.width() / 2 + width / 2 * Math.cos((angle + 4 - 90) * Math.PI / 180)),
                (float) (rectF.top + rectF.height() / 2 + width / 2 * Math.sin((angle + 4 - 90) * Math.PI / 180))
        );
    }

    public Path getTargetPath(PointF pointF, float progress, float size) {
        Path path = new Path();
        path.moveTo(pointF.x, pointF.y);

        if (progress >= 0 && progress < 25) {
            path.lineTo(pointF.x + 50, pointF.y - 50);
            path.lineTo(pointF.x + size, pointF.y - 50);

        } else if (progress >= 25 && progress < 50) {
            path.lineTo(pointF.x + 50, pointF.y + 50);
            path.lineTo(pointF.x + size, pointF.y + 50);

        } else if (progress >= 50 && progress < 75) {
            path.lineTo(pointF.x - 50, pointF.y + 50);
            path.lineTo(pointF.x - size, pointF.y + 50);

        } else if (progress >= 75 && progress <= 100) {
            path.lineTo(pointF.x - 50, pointF.y - 50);
            path.lineTo(pointF.x - size, pointF.y - 50);
        }
        return path;
    }

    public PointF getTargetTextPosition(PointF pointF, float progress, float textWidth, float textHeight) {

        if (progress >= 0 && progress < 25) {
            pointF.x += 60 + textWidth / 2;
            pointF.y -= textHeight;

        } else if (progress >= 25 && progress < 50) {
            pointF.x += 60 + textWidth / 2;
            pointF.y += textHeight / 2;

        } else if (progress >= 50 && progress < 75) {
            pointF.x -= 50 + textWidth / 2;
            pointF.y += textHeight / 2;

        } else if (progress >= 75 && progress <= 100) {
            pointF.x -= 50 + textWidth / 2;
            pointF.y -= textHeight;
        }
        return pointF;
    }
}
