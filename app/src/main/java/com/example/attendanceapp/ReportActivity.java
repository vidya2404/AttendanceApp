package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Vector;

public class ReportActivity extends AppCompatActivity {
    String year, subj, mode;
    AutoCompleteTextView yearBox, subBox, monthBox;
    Button fetch;
    RadioGroup rg;
    RadioButton rb;
    CommonMethods cm;
    int selID;
    SQLiteDatabase db;
    int month_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        yearBox = findViewById(R.id.year);
        subBox = findViewById(R.id.subject);
        monthBox = findViewById(R.id.month);
        fetch = findViewById(R.id.fetch);
        rg = findViewById(R.id.radiogrp);
        cm = new CommonMethods();


        try {
            yearBox.setAdapter(cm.getYearAdpt(getApplicationContext()));
            yearBox.setThreshold(1);
            subBox.setAdapter(cm.getSubjectAdpt(getApplicationContext()));
            subBox.setThreshold(1);

            monthBox.setAdapter(cm.getMonthAdpt(getApplicationContext()));
            monthBox.setThreshold(1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAllDetails();
//                    Toast.makeText(getApplicationContext(), absent_rolls, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), TableReport.class);
                    i.putExtra("year", year);
                    i.putExtra("subject", subj);
                    i.putExtra("mode", mode);
                    i.putExtra("month_no", month_no);
                    startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Enter data correctly...", Toast.LENGTH_SHORT).show();
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
        month_no = getMonthNum();
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

    int getMonthNum()
    {
        int m = 0;
        String[] months = cm.monthList;
        for(int i=0;i<months.length;i++)
        {
            if(monthBox.getText().toString().equals(months[i]))
            {
                m=i+1;
                return m;
            }
        }
        return m;
    }
}
