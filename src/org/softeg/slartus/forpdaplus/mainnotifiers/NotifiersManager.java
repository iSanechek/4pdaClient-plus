package org.softeg.slartus.forpdaplus.mainnotifiers;/*
 * Created by slinkin on 10.07.2014.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.softeg.slartus.forpdaplus.common.Log;

import java.util.ArrayList;

public class NotifiersManager implements DialogInterface.OnDismissListener {
    private Activity activity;
    private ArrayList<DialogInterface> dialogs = new ArrayList<>();

    public NotifiersManager(Activity activity) {

        this.activity = activity;
    }

    public void addNotifyDialog(AlertDialog alertDialog) {
        try {
            alertDialog.setOnDismissListener(this);
            dialogs.add(alertDialog);
            if (dialogs.size() == 1)
                alertDialog.show();
        } catch (Throwable ex) {
            Log.e(activity, ex);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        try {
            if (dialogs.contains(dialogInterface))
                dialogs.remove(dialogInterface);
            if (dialogs.size() > 0)
                ((AlertDialog) dialogs.get(0)).show();
        } catch (Throwable ex) {
            Log.e(activity, ex);
        }
    }
}
