package com.example.attendanceapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

public class Unit_Test extends AppCompatActivity {
    AutoCompleteTextView year_text,subject_text,exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit__test);


        year_text  = findViewById(R.id.year);
        subject_text  = findViewById(R.id.subject);
        exam  = findViewById(R.id.exam);

        CommonMethods commonMethods= new CommonMethods();
        year_text.setAdapter(commonMethods.getYearAdpt(getApplicationContext()));
        year_text.setTextColor(Color.BLACK);
        subject_text.setAdapter(commonMethods.getSubjectAdpt(getApplicationContext()));
        subject_text.setTextColor(Color.BLACK);
        exam.setAdapter(commonMethods.getExamTypeAdpt(getApplicationContext()));
        exam.setTextColor(Color.BLACK);


    }

    public  void showUtTable(View v)
    {
        // Add Validation
        // 1st Year -> 1st_Year
        String s = year_text.getText().toString();
        s = s.replace(" ","_");

        Intent i = new Intent(getApplicationContext(),UnitTestTable.class);
        i.putExtra("YEAR",s);
        i.putExtra("SUBJECT",subject_text.getText().toString());
        i.putExtra("EXAM_TYPE",exam.getText().toString());

        startActivity(i);

    }
//    public void setAutoList(AutoCompleteTextView t, String list[])
//    {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.select_dialog_item, list);
//        t.setThreshold(1);
//        t.setAdapter(adapter);
//        t.setTextColor(0xff000000);
//    }
}
