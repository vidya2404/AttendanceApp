package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {
  TableLayout tl;
  String year, subject, mode, date;
  TableRow tr;
  TextView rollText, statusText, mobileText, nameText;
  int start_roll, end_roll;

  String[]  data;
  String[][] contacts;
  DataBaseOp db;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    tl = findViewById(R.id.table);
    db = new DataBaseOp(getApplicationContext());
    try
    {
      year = getIntent().getStringExtra("year");
      subject = getIntent().getStringExtra("subject");
      mode = getIntent().getStringExtra("mode");
      date = getIntent().getStringExtra("date");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    try {
      data = db.getDateData(year, subject, mode, date);
//          contacts = db.getMobileNo(year, 0, 0);
//          showContacts();
      makeReport();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), "No attendance taken on this day", Toast.LENGTH_SHORT).show();
    }
  }
  public void makeReport()
  {
    // data = [AbsentRolls, StartRoll, EndRoll]
    start_roll = Integer.parseInt(data[1]);
    end_roll = Integer.parseInt(data[2]);
    String names[][] = db.getStudentNames(year, start_roll, end_roll); //For student names
    int k = 0;
    for (int i = start_roll; i <= end_roll; i++)
    {
      tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      rollText = new TextView(getApplicationContext());
      rollText.setGravity(Gravity.CENTER);
      rollText.setText(""+i);
      rollText.setTextSize(20);
      tr.addView(rollText);

      nameText = new TextView(getApplicationContext());
      nameText.setGravity(Gravity.CENTER);
      nameText.setText(names[k][1]);
      nameText.setTextSize(20);
      tr.addView(nameText);

      statusText = new TextView(getApplicationContext());
      statusText.setGravity(Gravity.CENTER);
      if(checkAbsent(""+i, data[0]))
      {
        statusText.setText("A");
        statusText.setBackgroundResource(R.drawable.absent_cell);
      }
      else
      {
        statusText.setText("P");
        statusText.setBackgroundResource(R.drawable.present_cell);
      }
      statusText.setTextSize(20);
      tr.addView(statusText);

      tl.addView(tr);
      k++;
    }

  }

  boolean checkAbsent(String roll, String absentrolls)
  {
    String[] rolls = absentrolls.split(",");
    for(int i=0; i<rolls.length;i++)
    {
      if(roll.equals(rolls[i]))
        return true;
    }
    return false;
  }

  void showContacts()
  {

    for (int i = 0; i < contacts.length; i++)
    {
      tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      rollText = new TextView(getApplicationContext());
      rollText.setGravity(Gravity.CENTER);
      rollText.setText(""+contacts[i][0]);
      rollText.setTextSize(20);
      tr.addView(rollText);

      mobileText = new TextView(getApplicationContext());
      mobileText.setGravity(Gravity.CENTER);
      mobileText.setTextSize(20);
      mobileText.setText(""+contacts[i][1]);
      tr.addView(mobileText);

      tl.addView(tr);
    }
  }
}
