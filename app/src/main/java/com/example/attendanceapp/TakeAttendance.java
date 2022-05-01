package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakeAttendance extends AppCompatActivity {

  String tablename, mode, subject, year, cr_date;
  Button showUI, submit;
  EditText roll_from, roll_to;
  TextView date, rollText, nameText;
  String absentRolls;
  AutoCompleteTextView subjects, years;
  DatePickerDialog dp;
  RadioGroup rg;
  int selID, endroll, startroll;
  RadioButton rb;
  AttendData data;
  CommonMethods cm = new CommonMethods();
  DataBaseOp db;
  TableLayout tableLayout;
  TableRow tr;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_take_attendance);
    try {
      showUI = findViewById(R.id.btn_ui);
      roll_from = findViewById(R.id.from_roll);
      roll_to = findViewById(R.id.to_roll);
      tableLayout = findViewById(R.id.table);
      submit = findViewById(R.id.submit);
      subjects = findViewById(R.id.subject);
      years = findViewById(R.id.year);
      date = findViewById(R.id.date);
      rg = findViewById(R.id.radiogrp);
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
      Date d = new Date();
      cr_date = formatter.format(d);
      date.setText("Date -> " + cr_date);
      years.setAdapter(cm.getYearAdpt(getApplicationContext()));
      years.setThreshold(1);

      db = new DataBaseOp(getApplicationContext());


      subjects.setAdapter(cm.getSubjectAdpt(getApplicationContext()));
      subjects.setThreshold(1);

      data = new AttendData();
      data.subj = subjects.getText().toString();
      data.year = years.getText().toString();
      data.rollFrom = roll_from.getText().toString();
      data.rollTo = roll_to.getText().toString();
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    try {
      absentRolls = getIntent().getExtras().getString("absentRolls");
      data = (AttendData) getIntent().getSerializableExtra("Data");
      subjects.setText(data.subj);
      years.setText(data.year);
      roll_from.setText(data.rollFrom);
      roll_to.setText(data.rollTo);
      showAbsentStudentDetails();
    } catch (Exception e) {
      e.printStackTrace();
      subjects.setText(data.subj);
      years.setText(data.year);
      roll_from.setText(data.rollFrom);
      roll_to.setText(data.rollTo);
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }




//        getSubjectList();
    showUI.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          int start = Integer.parseInt(roll_from.getText().toString());
          int end = Integer.parseInt(roll_to.getText().toString());

          if (start > end) {
            Toast.makeText(getApplicationContext(), "Roll_No. from cannot be Greater", Toast.LENGTH_SHORT).show();
          } else {
            data.subj = subjects.getText().toString();
            data.year = years.getText().toString();
            data.rollFrom = roll_from.getText().toString();
            data.rollTo = roll_to.getText().toString();

            Intent i = new Intent(getApplicationContext(), CreateBtnUI.class);
            i.putExtra("roll_from", roll_from.getText().toString());
            i.putExtra("roll_to", roll_to.getText().toString());
            i.putExtra("Data", data);
            i.putExtra("absent_rolls", absentRolls);
            startActivity(i);
            finish();
          }
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(getApplicationContext(), "Plz input Roll No correctly", Toast.LENGTH_SHORT).show();
        }
      }
    });

    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          getAllDetails();
          db = new DataBaseOp(getApplicationContext());
//                db.chooseTable(year);
          db.insertData(year, subject, mode, absentRolls, cr_date, startroll, endroll);
//          fb.saveData(cr_date, year, subject, mode, absentRolls);
          Toast.makeText(getApplicationContext(), "Attendance Recorded", Toast.LENGTH_SHORT).show();
//          sendSMSToAbsentStudents();
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(getApplicationContext(), "Enter data correctly...", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  void showAbsentStudentDetails() {
    String[] rolls = absentRolls.split(",");
    String names[] = new String[rolls.length];
    year = getYear();
    for(int i=0; i<rolls.length; i++)
    {
      names[i] = db.getNameByRoll(year, rolls[i]);
    }

    for(int i=0; i<rolls.length; i++)
    {
      tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

      rollText = new TextView(getApplicationContext());
      rollText.setPadding(5,5,5,5);
      rollText.setGravity(Gravity.CENTER);
      rollText.setText(rolls[i]);
      rollText.setTextSize(20);
      tr.addView(rollText);

      nameText = new TextView(getApplicationContext());
      nameText.setPadding(5,5,5,5);
      nameText.setGravity(Gravity.CENTER);
      nameText.setText(names[i]);
      nameText.setTextSize(20);
      tr.addView(nameText);


      tableLayout.addView(tr);
    }
  }


  public void getAllDetails() {
    subject = subjects.getText().toString();
    year = getYear();
    selID = rg.getCheckedRadioButtonId();
    rb = findViewById(selID);
    mode = rb.getText().toString();

    startroll = Integer.parseInt(roll_from.getText().toString());
    endroll = Integer.parseInt(roll_to.getText().toString());
  }

  public String getYear() {
    if (years.getText().toString().equals("1st Year")) {
      year = "first_year";
    }
    if (years.getText().toString().equals("2nd Year")) {
      year = "second_year";
    }
    if (years.getText().toString().equals("3rd Year")) {
      year = "third_year";
    }
    return year;
  }


  boolean checkAbsent(String roll, String absentrolls) {
    String[] rolls = absentRolls.split(",");
    for (int i = 0; i < rolls.length; i++) {
      if (roll.equals(rolls[i]))
        return true;
    }
    return false;
  }
}
class AttendData implements Serializable
{
  public String subj, year,rollFrom, rollTo;
}