package com.gox.base.views.customviews.circularseekbar;

import android.graphics.Bitmap;
import android.graphics.Color;

public class CircularProgressBarModel extends CircularProgressDimensions {

    // progress properties
    private float progress = 0;
    private float strokeWidth = 0x7f080046;
    private float cuttingAngle = 0;
    private int blur = 1;
    private int color = Color.BLACK;
    private int backCircleColor = Color.BLACK;

    // background progress properties
    private float backgroundStrokeWidth = 0x7f080045;
    private int backgroundColor = Color.GRAY;

    //text properties
    private int titleSize = 70;
    private int subTitleSize = 45;
    private int timerSize = 70;
    private int targetSize = 10;
    private int titleColor = Color.GRAY;
    private int subTitleColor = Color.GRAY;
    private int timerColor = Color.GRAY;
    private int targetColor = Color.GRAY;
    private String titleText = "Title";
    private String subTitleText = "Subtitle";
    private String timerText = "48:25:17";
    private String targetText = "target";

    // Object used to draw
    private int startAngle = -90;
    private Bitmap backgroundImage;

    public CircularProgressBarModel() {}

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(int targetColor) {
        this.targetColor = targetColor;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public int getTimerSize() {
        return timerSize;
    }

    public void setTimerSize(int timerSize) {
        this.timerSize = timerSize;
    }

    public int getTimerColor() {
        return timerColor;
    }

    public void setTimerColor(int timerColor) {
        this.timerColor = timerColor;
    }

    public String getTimerText() {
        return timerText;
    }

    public void setTimerText(String timerText) {
        this.timerText = timerText;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getCuttingAngle() {
        return cuttingAngle;
    }

    public void setCuttingAngle(float cuttingAngle) {
        this.cuttingAngle = cuttingAngle;
    }

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBackCircleColor() {
        return backCircleColor;
    }

    public void setBackCircleColor(int backCircleColor) {
        this.backCircleColor = backCircleColor;
    }

    public float getBackgroundStrokeWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundStrokeWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getSubTitleSize() {
        return subTitleSize;
    }

    public void setSubTitleSize(int subTitleSize) {
        this.subTitleSize = subTitleSize;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getSubTitleColor() {
        return subTitleColor;
    }

    public void setSubTitleColor(int subTitleColor) {
        this.subTitleColor = subTitleColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubTitleText() {
        return subTitleText;
    }

    public void setSubTitleText(String subTitleText) {
        this.subTitleText = subTitleText;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public Bitmap getBackgroundImage(float width) {
        return Bitmap.createScaledBitmap(backgroundImage, (int)width/2, (int)width/2, true);
    }

    public void setBackgroundImage(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
