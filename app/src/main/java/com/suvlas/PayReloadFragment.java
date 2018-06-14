package com.suvlas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by hp on 3/7/2017.
 */

public class PayReloadFragment extends Fragment implements  View.OnClickListener {

    RelativeLayout rel_procced;
    View rootView;
    Toolbar toolbar;
    ImageView img_back;
    static final int DATE_DIALOG_ID = 1;
    private int mYear = 2013;
    private int mMonth = 5;
    private int mDay = 30;
    EditText expirationdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pay_reload, container, false);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
        return rootView;
    }
    private void findviewID() {
        rel_procced=(RelativeLayout)rootView.findViewById(R.id.rel_procced);
        img_back=(ImageView)rootView.findViewById(R.id.img_back);
        expirationdate=(EditText)rootView.findViewById(R.id.expirationdate);

    }
    private void set_listeners() {
       rel_procced.setOnClickListener(this);
        img_back.setOnClickListener(this);
//        expirationdate.setOnClickListener(new View.OnClickListener() {
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onClick(View arg0) {
//                showDialog(DATE_DIALOG_ID);
//            }
//        });
//        final Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
    }
//    DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//
//            mYear = year;
//            mMonth = monthOfYear;
//            mDay = dayOfMonth;
//            updateDate();
//        }
//    };
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//            /*
//             * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth,
//             * mDay);
//             */
//                DatePickerDialog datePickerDialog = this.customDatePicker();
//                return datePickerDialog;
//        }
//        return null;
//    }
//
//    @SuppressWarnings("deprecation")
//    protected void updateDate() {
//        int localMonth = (mMonth + 1);
//        String monthString = localMonth < 10 ? "0" + localMonth : Integer
//                .toString(localMonth);
//        String localYear = Integer.toString(mYear).substring(2);
//        expirationdate.setText(new StringBuilder()
//                // Month is 0 based so add 1
//                .append(monthString).append("/").append(localYear).append(" "));
//        showDialog(DATE_DIALOG_ID);
//    }
//
//    private DatePickerDialog customDatePicker() {
//        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner,
//                mYear, mMonth, mDay);
//        try {
//            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
//            for (Field datePickerDialogField : datePickerDialogFields) {
//                if (datePickerDialogField.getName().equals("mDatePicker")) {
//                    datePickerDialogField.setAccessible(true);
//                    DatePicker datePicker = (DatePicker) datePickerDialogField
//                            .get(dpd);
//                    Field datePickerFields[] = datePickerDialogField.getType()
//                            .getDeclaredFields();
//                    for (Field datePickerField : datePickerFields) {
//                        if ("mDayPicker".equals(datePickerField.getName())
//                                || "mDaySpinner".equals(datePickerField
//                                .getName())) {
//                            datePickerField.setAccessible(true);
//                            Object dayPicker = new Object();
//                            dayPicker = datePickerField.get(datePicker);
//                            ((View) dayPicker).setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//        }
//        return dpd;
//    }


    private void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_procced:
                Intent intent = new Intent(getActivity(), TransactionSucessActivity.class);
                startActivity(intent);

                break;
            case R.id.img_back:
                Fragment fragment = new PayManageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.payreload_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
//            case R.id.expirationdate:
//                createDialogWithoutDateField().show();
//                break;
        }
    }
}