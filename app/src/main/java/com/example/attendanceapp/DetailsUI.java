package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class DetailsUI extends AppCompatActivity {

    String year, subj, mode, date;
    AutoCompleteTextView yearBox, subBox;
    Button fetch, dateBox;
    RadioGroup rg;
    RadioButton rb;
    CommonMethods cm;
    int selID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_u_i);
        yearBox = findViewById(R.id.year);
        subBox = findViewById(R.id.subject);
        fetch = findViewById(R.id.fetch);
        rg = findViewById(R.id.radiogrp);
        dateBox = findViewById(R.id.date);
        cm = new CommonMethods();

        try {
            yearBox.setAdapter(cm.getYearAdpt(getApplicationContext()));
            yearBox.setThreshold(1);
            subBox.setAdapter(cm.getSubjectAdpt(getApplicationContext()));
            subBox.setThreshold(1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        dateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsUI.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month += 1;
                            // Coverting to String for forming date format [2 -> 02]
                            String mon, day;
                            mon = String.valueOf(month);
                            day = String.valueOf(dayOfMonth);
                            if (mon.length() == 1) {
                                mon = "0" + mon;
                            }
                            if (day.length() == 1) {
                                day = "0" + day;
                            }
                            // Setting date to button
                            date = day + "-" + mon + "-" + year;
                            dateBox.setText(date);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAllDetails();
//                    Toast.makeText(getApplicationContext(), absent_rolls, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                    i.putExtra("year", year);
                    i.putExtra("subject", subj);
                    i.putExtra("mode", mode);
                    i.putExtra("date", date);
                    startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getAllDetails() {
        year = getYear();
        subj = subBox.getText().toString();
        selID = rg.getCheckedRadioButtonId();
        rb = findViewById(selID);
        mode = rb.getText().toString();

    }
    public String getYear()
    {
        if(yearBox.getText().toString().equals("1st Year"))
        {
            year = "first_year";
        }
        if(yearBox.getText().toString().equals("2nd Year"))
        {
            year = "second_year";
        }
        if(yearBox.getText().toString().equals("3rd Year"))
        {
            year = "third_year";
        }
        return year;
    }

}
