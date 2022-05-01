package com.example.attendanceapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UnitTestTable extends AppCompatActivity {


  TableLayout table;
  String subject,exam_type,year;
  String data[][];
  DataBaseOp db;
  ContentValues values;
  Button sendSMS;
  String smsData[][], attendData[][];
  float percentArr[];
  String mode, month;
  int total_stud=0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_unit_test_table);

    sendSMS = findViewById(R.id.send);


    // Getting Data of previous Intent
    subject = getIntent().getExtras().get("SUBJECT").toString();
    year = getIntent().getExtras().get("YEAR").toString();
    exam_type = getIntent().getExtras().get("EXAM_TYPE").toString();
    CommonMethods commonMethods = new CommonMethods();
    total_stud = commonMethods.getStudentTotal(year);

    // Set Title
    TextView t = findViewById(R.id.title);
    t.setText(subject + " : " + exam_type);


    table = findViewById(R.id.table);
    db = new DataBaseOp(this);

    // Fetching the Unit Test Table Data  if doesn't exist creates new Table
    values = new ContentValues();
    values.put("YEAR", year);
    values.put("SUBJECT", subject);
    values.put("EXAM_TYPE", exam_type);
    data = db.getUnitTestTable(values, total_stud);

    if (data == null) {
      Toast.makeText(this, "No Unit Test Records", Toast.LENGTH_SHORT).show();
    } else
      initTable();

    sendSMS.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try
        {
          smsData = db.getSMSData();
          getAttendanceDetails();
//                Toast.makeText(getApplicationContext(), ""+mobileNo[1], Toast.LENGTH_SHORT).show();
          sendSMSToStudent();

        }
        catch (Exception e)
        {
          Toast.makeText(getApplicationContext(), "You are not taken attendance current month."+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

      }
    });

  }
  public void save(View view)
  {
    // Get Data from table
    data = new String[table.getChildCount()-1][2];
    try {
      for(int i=1;i<table.getChildCount();i++)
      {
        TableRow tr = (TableRow) table.getChildAt(i);
        TextView roll_no = (TextView) tr.getChildAt(0);
        EditText marks = (EditText) tr.getChildAt(1);
        data[i-1][0] = roll_no.getText().toString();
        data[i-1][1] = marks.getText().toString();

      }

      db.updateUnitTest(values, data);
      Toast.makeText(getApplicationContext(),"Records Saved",Toast.LENGTH_SHORT).show();
    }
    catch (Exception e)
    {
      Log.d("UnitTestTable", "save: "+e);
      Toast.makeText(getApplicationContext(),"Error while saving",Toast.LENGTH_SHORT).show();
    }
  }
  void initTable() {
    try {
      int c = 0;
      for (String[] row : data) {
        TableRow tableRow = new TableRow(getApplicationContext());
        TextView roll_no = new TextView(getApplicationContext());
        roll_no.setTextColor(Color.BLACK);
        roll_no.setText(row[0]);
        roll_no.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        EditText marks = new EditText(getApplicationContext());
        marks.setText(row[1]);

        marks.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                marks.setInputType(2);

        TextView status = new TextView(getApplicationContext());
        status.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        status.setTextColor(Color.BLACK);

        if(row[1].equals("_") || row[1].equals(""))
          status.setText("");
        else {
          c = 1;
          if (Integer.parseInt(row[1]) < 10)
            status.setText("Fail");
          else
            status.setText("Pass");
        }
        tableRow.addView(roll_no);
        tableRow.addView(marks);
        tableRow.addView(status);
        table.addView(tableRow);
      }
//        TableRow tr = new TableRow(getApplicationContext());
//        TextView tv = new TextView(getApplicationContext());
//        float font_size = 20.0f;
//        // HEADERS  -> [  RollNO/Dates | 1,2,3,4..... | Total[#]  ]
//        tv.setText("RollNo");
//        tv.setTextSize(font_size);
//        tv.setBackgroundResource(R.drawable.table_header);
//        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        tr.addView(tv);
//        tv = new TextView(getApplicationContext());
//        tv.setText("Marks");
//        tv.setTextSize(font_size);
//        tv.setBackgroundResource(R.drawable.table_header);
//        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        tr.addView(tv);
//        table.addView(tr);
//        // Dates columns
//        for(String[] row:data)
//        {
//            tr  = new TableRow(getApplicationContext());
//            for(String cell:row)
//            {
//                tv = new TextView(this);
//                tv.setText(cell);
//                tv.setTextSize(font_size);
//                tv.setBackgroundResource(R.drawable.table_header);
//                tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                tr.addView(tv);
//            }
//            table.addView(tr);
      if(c == 0)
        Toast.makeText(getApplicationContext(), "Unit test is not taken", Toast.LENGTH_SHORT).show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Test is not taken", Toast.LENGTH_SHORT).show();
    }

  }

  void sendSMSToStudent()
  {
    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
    if(permissionCheck == PackageManager.PERMISSION_GRANTED)
    {
      SendSMS();
    }
    else
    {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
    }
  }
  public void SendSMS()
  {
    //SmsData = [EXAM_TYPE, SUBJECT, MARKS, MOBILENO]
//        String msg = "For UT You are Failed. You scored ";
    SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("9325503341", null, msg, null, null);
    Calendar cal = Calendar.getInstance();
    String str, month;
    month = new SimpleDateFormat("MMM").format(cal.getTime());
    for(int i=0; i<smsData.length; i++)
    {
      str = smsData[i][3];
      if(str != null)
        smsManager.sendTextMessage(str, null, "For "+smsData[i][0]+" Of Subject" +
                " "+smsData[i][1]+". In that you Scored "+smsData[i][2]+" Out of 30. \nAnd till now your " + month +
                " month attendance is "+percentArr[i] +"%.\n -From Smart Attendance " +
                "System", null, null);
    }
    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode){
      case 0:
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
//                    SendSMS();
        }
        else
        {
          Toast.makeText(this, "User Not Given Permission To Send SMS", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  public void getAttendanceDetails()
  {
    year = getYear();
    mode = "Theory";
    month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);

    if(month.length() < 2)
      month = "0" + month;

    data = db.fetchMonthData(year, subject, mode, month);
    Integer total = new Integer(data.length),
            present = new Integer(0);

    int absent_time;
    int startroll = Integer.parseInt(data[0][2]);
    int endroll = Integer.parseInt(data[0][3]);
    percentArr = new float[(endroll-startroll)+1];
    int k=0;
    for (int i=startroll; i<=endroll; i++)
    {
      absent_time = 0;
      for(int j=0;j<data.length;j++)
      {
        if(checkAbsent(""+i,data[j][0]))
          absent_time++;
      }
      present = total - absent_time;
      float percent = Math.round((present.floatValue()/total)*100);
      percentArr[k] = percent;
      k++;
    }
  }

  boolean checkAbsent(String roll, String arr)
  {
    String[] rolls = arr.split(",");
    for(int i=0; i<rolls.length;i++)
    {
      if(roll.equals(rolls[i]))
        return true;
    }
    return false;
  }

  public String getYear()
  {
    if(year.equals("1st_Year"))
    {
      year = "first_year";
    }
    if(year.equals("2nd_Year"))
    {
      year = "second_year";
    }
    if(year.equals("3rd_Year"))
    {
      year = "third_year";
    }
    return year;
  }

}


