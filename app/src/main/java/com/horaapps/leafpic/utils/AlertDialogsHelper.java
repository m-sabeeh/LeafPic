package com.horaapps.leafpic.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.horaapps.leafpic.Base.Media;
import com.horaapps.leafpic.R;
import com.horaapps.leafpic.Views.ThemedActivity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dnld on 19/05/16.
 */
public class AlertDialogsHelper {

    public static void setCursorDrawableColor( EditText editText, int color) {
        try {
            Field fCursorDrawableRes =
                    TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {  }
    }

    public static  AlertDialog getInsertTextDialog(final ThemedActivity activity, AlertDialog
            .Builder dialogBuilder , EditText editText, String title) {
        View renameDialogLayout = activity.getLayoutInflater().inflate(R.layout.insert_text_dialog, null);
        TextView textViewTitle = (TextView) renameDialogLayout.findViewById(R.id.rename_title);
        CardView cardViewRename = (CardView) renameDialogLayout.findViewById(R.id.rename_card);

        cardViewRename.setBackgroundColor(activity.getCardBackgroundColor());
        textViewTitle.setBackgroundColor(activity.getPrimaryColor());
        textViewTitle.setText(title);
        setCursorDrawableColor(editText, activity.getTextColor());
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine(true);
        editText.getBackground().mutate().setColorFilter(activity.getTextColor(), PorterDuff.Mode.SRC_IN);
        editText.setTextColor(activity.getTextColor());
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, null);
        } catch (Exception ignored) { }
        ((RelativeLayout) renameDialogLayout.findViewById(R.id.container_edit_text)).addView(editText);

        dialogBuilder.setView(renameDialogLayout);
        return dialogBuilder.create();
    }

    public static AlertDialog getDetailsDialog(final ThemedActivity activity, AlertDialog.Builder detailsDialogBuilder, Media f) {
        /****** BEAUTIFUL DIALOG ****/
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.photo_detail_dialog, null);
        TextView Size = (TextView) dialogLayout.findViewById(R.id.photo_size);
        TextView Type = (TextView) dialogLayout.findViewById(R.id.photo_type);
        TextView Resolution = (TextView) dialogLayout.findViewById(R.id.photo_resolution);
        TextView Data = (TextView) dialogLayout.findViewById(R.id.photo_date);
        TextView DateTaken = (TextView) dialogLayout.findViewById(R.id.date_taken);
        TextView Path = (TextView) dialogLayout.findViewById(R.id.photo_path);
        TextView txtTitle = (TextView) dialogLayout.findViewById(R.id.details_title);
        TextView txtSize = (TextView) dialogLayout.findViewById(R.id.label_size);
        TextView txtType = (TextView) dialogLayout.findViewById(R.id.label_type);
        TextView txtResolution = (TextView) dialogLayout.findViewById(R.id.label_resolution);
        TextView txtData = (TextView) dialogLayout.findViewById(R.id.label_date);
        TextView txtDateTaken = (TextView) dialogLayout.findViewById(R.id.label_date_taken);
        TextView txtPath = (TextView) dialogLayout.findViewById(R.id.label_path);
        TextView txtDevice = (TextView) dialogLayout.findViewById(R.id.label_device);
        TextView Device = (TextView) dialogLayout.findViewById(R.id.photo_device);
        TextView txtEXIF = (TextView) dialogLayout.findViewById(R.id.label_exif);
        TextView EXIF = (TextView) dialogLayout.findViewById(R.id.photo_exif);
        TextView txtLocation = (TextView) dialogLayout.findViewById(R.id.label_location);
        TextView Location = (TextView) dialogLayout.findViewById(R.id.photo_location);
        ImageView imgMap = (ImageView) dialogLayout.findViewById(R.id.photo_map);

        txtTitle.setBackgroundColor(activity.getPrimaryColor());

        Size.setText(f.getHumanReadableSize());
        Resolution.setText(f.getResolution());
        Data.setText(SimpleDateFormat.getDateTimeInstance().format(new Date(f.getDateModified())));
        Type.setText(f.getMIME());
        Path.setText(f.getPath());

        txtData.setTextColor(activity.getTextColor());
        txtPath.setTextColor(activity.getTextColor());
        txtResolution.setTextColor(activity.getTextColor());
        txtType.setTextColor(activity.getTextColor());
        txtSize.setTextColor(activity.getTextColor());
        txtDevice.setTextColor(activity.getTextColor());
        txtEXIF.setTextColor(activity.getTextColor());
        txtLocation.setTextColor(activity.getTextColor());
        txtDateTaken.setTextColor(activity.getTextColor());

        Data.setTextColor(activity.getSubTextColor());
        DateTaken.setTextColor(activity.getSubTextColor());
        Path.setTextColor(activity.getSubTextColor());
        Resolution.setTextColor(activity.getSubTextColor());
        Type.setTextColor(activity.getSubTextColor());
        Size.setTextColor(activity.getSubTextColor());
        Device.setTextColor(activity.getSubTextColor());
        EXIF.setTextColor(activity.getSubTextColor());
        Location.setTextColor(activity.getSubTextColor());

        try {
            ExifInterface exif = new ExifInterface(f.getPath());
            if (exif.getAttribute(ExifInterface.TAG_MAKE) != null) {
                Device.setText(String.format("%s %s",
                        exif.getAttribute(ExifInterface.TAG_MAKE),
                        exif.getAttribute(ExifInterface.TAG_MODEL)));

                EXIF.setText(String.format("f/%s ISO-%s %ss",
                        exif.getAttribute(ExifInterface.TAG_APERTURE),
                        exif.getAttribute(ExifInterface.TAG_ISO),
                        exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)));

                final float[] output= new float[2];
                if(exif.getLatLong(output)) {

                    String url = "http://maps.google.com/maps/api/staticmap?center=" + output[0] + "," + output[1] + "&zoom=15&size="+400+"x"+400+"&scale=2&sensor=false&&markers=color:red%7Clabel:C%7C"+output[0]+","+output[1];
                    //url = "https://api.mapbox.com/v4/mapbox.dark/-76.9,38.9,5/1000x1000.png";
                    Glide.with(activity.getApplicationContext())
                            .load(url)
                            .asBitmap()
                            .centerCrop()
                            .animate(R.anim.fade_in)
                            .into(imgMap);
                    imgMap.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=zoom", output[0], output[1]);
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                        }
                    });

                    Location.setText(String.format(Locale.getDefault(),"%f, %f",
                            output[0], output[1]));
                    dialogLayout.findViewById(R.id.ll_location).setVisibility(View.VISIBLE);
                    dialogLayout.findViewById(R.id.ll_map).setVisibility(View.VISIBLE);

                }
                dialogLayout.findViewById(R.id.ll_exif).setVisibility(View.VISIBLE);
            }
            long dateTake;
            if ((dateTake = f.getDateEXIF())!=-1) {
                DateTaken.setText(SimpleDateFormat.getDateTimeInstance().format(new Date(dateTake)));
                dialogLayout.findViewById(R.id.ll_date_taken).setVisibility(View.VISIBLE);
            }
        }
        catch (IOException e){ e.printStackTrace(); }

        CardView cv = (CardView) dialogLayout.findViewById(R.id.photo_details_card);
        cv.setCardBackgroundColor(activity.getCardBackgroundColor());
        detailsDialogBuilder.setView(dialogLayout);

        return detailsDialogBuilder.create();

    }
}
