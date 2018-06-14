package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


public class Comman_Dialog {

    private Context context;

    public Comman_Dialog(Context context) {
        this.context = context;
    }

    public void Show_alert(String Message) {
        new MaterialDialog.Builder(context)
                .title(Message)
                .positiveText("Ok")
                .cancelable(false)
                .positiveColor(Color.parseColor("#E43889"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void Show_register(final Activity activity, String Message) {
        new MaterialDialog.Builder(context)
                .title(Message)
                .positiveText("Ok")
                .cancelable(false)
                .positiveColor(Color.parseColor("#E43889"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void Show_trip_edi(final Activity activity, String Message) {
        new MaterialDialog.Builder(context)
                .title(Message)
                .positiveText("Ok")
                .cancelable(false)
                .positiveColor(Color.parseColor("#E43889"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.cancel();
                        Intent intent = new Intent();
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                })
                .show();
    }

    public void Finish_dialog(final Activity activity, String Message) {
        new MaterialDialog.Builder(context)
                .title(Message)
                .positiveText("Ok")
                .cancelable(false)
                .positiveColor(Color.parseColor("#E43889"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.cancel();
                        activity.finish();
                    }
                })
                .show();
    }

}
