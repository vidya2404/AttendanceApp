package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class TableReport extends AppCompatActivity {
    TableLayout tl;
    String tablename, subject, mode, month_no;
    TableRow tr;
    TextView rollText, nameText, totalText, presentText,
            absentText, percnetText, monthName;

    String data[][];
    DataBaseOp db;
    CommonMethods cm;
    Vector detained = new Vector(5, 5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_report);
        tl = findViewById(R.id.table);
        tl.setGravity(View.TEXT_ALIGNMENT_CENTER);
        db = new DataBaseOp(getApplicationContext());
        monthName = findViewById(R.id.monthName);
        totalText = findViewById(R.id.total);
        cm = new CommonMethods();
        try {
            tablename = getIntent().getStringExtra("year");
            subject = getIntent().getStringExtra("subject");
            mode = getIntent().getStringExtra("mode");
            month_no = getIntent().getExtras().get("month_no").toString();

            data = db.fetchMonthData(tablename, subject, mode, month_no);
            //data = [AbsentRolls, Date, StartRoll, EndRoll]
//        Toast.makeText(getApplicationContext(), ""+data[1][0], Toast.LENGTH_LONG).show();

            totalText.setText("Total : " + data.length);
            monthName.setText("Month : " + cm.getMonth(Integer.parseInt(month_no)));
            makeReport();
//            tr = new TableRow(getApplicationContext());
//            tr.setGravity(View.TEXT_ALIGNMENT_CENTER);
//            tr.setBackgroundColor(Color.YELLOW);
//
//            textView = new TextView(getApplicationContext());
//            textView.setTextSize(20);
//            textView.setText("Roll Nos below 75% Attendance : \n"+detained.toString());
//
//            tr.addView(textView);
//            tl.addView(tr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No attendance taken on this day...", Toast.LENGTH_SHORT).show();
        }

    }
    public void makeReport()
    {
        Integer total = new Integer(data.length),
        present = new Integer(0);

        int absent_time;
        int startroll = Integer.parseInt(data[0][2]);
        int endroll = Integer.parseInt(data[0][3]);

        String names[][] = db.getStudentNames(tablename, startroll, endroll); //For student names
        int k = 0;
        for (int i=startroll; i<=endroll; i++)
        {
            tr = new TableRow(getApplicationContext());
            tr.setBackgroundColor(Color.YELLOW);

            rollText = new TextView(getApplicationContext());
            rollText.setGravity(Gravity.CENTER);
            rollText.setPadding(3,3,3,3);
            rollText.setText(""+i);
            rollText.setTextSize(18);

            tr.addView(rollText);

            nameText = new TextView(getApplicationContext());
            nameText.setText(names[k][1]);
            nameText.setMaxLines(2);
            nameText.setPadding(3,3,3,3);
            nameText.setTextSize(18);
            tr.addView(nameText);

            presentText = new TextView(getApplicationContext());
            presentText.setGravity(Gravity.CENTER);
            presentText.setPadding(3,3,3,3);
            presentText.setTextSize(20);
            absent_time = 0;
            for(int j=0;j<data.length;j++)
            {
                if(checkAbsent(""+i,data[j][0]))
                    absent_time++;
            }
            present = total - absent_time;
            presentText.setText(""+present);
            tr.addView(presentText);

            absentText = new TextView(getApplicationContext());
            absentText.setGravity(Gravity.CENTER);
            absentText.setPadding(3,3,3,3);
            absentText.setTextSize(18);
            absentText.setText(""+absent_time);
            tr.addView(absentText);

            percnetText = new TextView(getApplicationContext());
            percnetText.setGravity(Gravity.CENTER);
            percnetText.setPadding(3,3,3,3);
            percnetText.setTextSize(18);
            float percent = Math.round((present.floatValue()/total)*100);
            percnetText.setText(""+percent);
            tr.addView(percnetText);

            if(percent < 75)
                detained.add(new String(""+i));

            tl.addView(tr);
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
}
