package com.knomatic.weather.providers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

import com.knomatic.weather.R;

import static com.knomatic.weather.utils.Constants.REQUEST_PERMISSION_CODE;

/**
 * Created stephany.berrio on 28/02/17.
 */

public class PermissionProvider {

    private String rationalTitle = null;
    private String rationalMessage = null;
    private String rationalTxtBtn = null;
    private static PermissionProvider instance = null;


    private PermissionProvider() {
    }

    public static PermissionProvider getInstance() {

        if (instance == null) {
            instance = new PermissionProvider();
        }
        return instance;

    }

    /**
     * Method to get the rational title dialog
     *
     * @return String
     */
    public String getRationalTitle() {
        return rationalTitle;
    }

    /**
     * Method to set the rational title dialog
     *
     * @param rationalTitle
     */
    public void setRationalTitle(String rationalTitle) {
        this.rationalTitle = rationalTitle;
    }

    /**
     * Method to get rational message dialog
     *
     * @return String
     */
    public String getRationalMessage() {
        return rationalMessage;
    }

    /**
     * Method to set rational message dialog
     *
     * @param rationalMessage
     */
    public void setRationalMessage(String rationalMessage) {
        this.rationalMessage = rationalMessage;
    }

    /**
     * Method to get rational button text dialog
     *
     * @return String
     */
    public String getRationalTxtBtn() {
        return rationalTxtBtn;
    }

    /**
     * Method to set rational button text dialog
     *
     * @param rationalTxtBtn
     */
    public void setRationalTxtBtn(String rationalTxtBtn) {
        this.rationalTxtBtn = rationalTxtBtn;
    }

    /**
     * Method to check if the application has requested permission
     * returns true if the have and false if not
     * @param context current context
     * @param permission permission to request
     * @return boolean
     */
    public boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Method to displays an alert, in which he performed the request of
     * the desired user permission
     *
     * @param activity   current activity
     * @param permission permission to request
     */
    private void showRequestPermissionDialog(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                REQUEST_PERMISSION_CODE);
    }

    /**
     * This method verifies whether to display an explanation of the reason
     * why is this asking specific permission if sample not be necessary
     * alert requesting permission
     *
     * @param activity   the current activity
     * @param permission the permission to requests
     */
    public void askForPermissions(final Activity activity, final String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.PermissionDialog))
                    .setTitle(rationalTitle)
                    .setMessage(rationalMessage)
                    .setCancelable(false)
                    .setPositiveButton(rationalTxtBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showRequestPermissionDialog(activity, permission);
                        }
                    });

            Dialog dialog = builder.create();
            dialog.show();


        } else {
            showRequestPermissionDialog(activity, permission);
        }
    }
}
