package com.appoets.basemodule.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.appoets.basemodule.BuildConfig;
import com.appoets.basemodule.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Objects;

import androidx.annotation.DrawableRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.PermissionRequest;

public final class ViewUtils {

    private ViewUtils() {

    }


    @MainThread
    public static void showToast(@NonNull Context context, @StringRes int messageResId, boolean isSuccess) {
        if (isSuccess)
            Toasty.success(context, messageResId, Toast.LENGTH_SHORT).show();
        else
            Toasty.error(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    @MainThread
    public static void showNormalToast(@NonNull Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @MainThread
    public static void showAlert(@NonNull Context context, @StringRes int messageResId) {
        new AlertDialog.Builder(context)
                .setTitle(BuildConfig.APP_NAME)
                .setMessage(messageResId)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @MainThread
    public static void showRationaleAlert(@NonNull Context context, @StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(context)
                .setTitle(BuildConfig.APP_NAME)
                .setMessage(messageResId)
                .setPositiveButton(context.getString(R.string.allow), (dialog, which) -> {
                    request.proceed();
                })
                .setNegativeButton(context.getString(R.string.deny), (dialog, which) -> {
                    request.cancel();
                })
                .show();
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(@NonNull Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, Objects.requireNonNull(vectorDrawable).getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
