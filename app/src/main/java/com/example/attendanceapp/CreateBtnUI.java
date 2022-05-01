package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendanceapp.AttendData;

import java.util.ArrayList;

public class CreateBtnUI extends AppCompatActivity {
    TextView r_range;
    LinearLayout lay;
    GridLayout gridLayout;
    int i;
    StudIcon studIcon[];
    AttendData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_btn_ui);

        try {
            r_range = findViewById(R.id.rollno_range);
            lay = findViewById(R.id.studs_lay);
            gridLayout = findViewById(R.id.studs_lay_grid);


            String f = getIntent().getExtras().getString("roll_from").toString();
            String t = getIntent().getExtras().getString("roll_to").toString();
            r_range.setText("Roll NO:" + f + " -> " + t);


            int r_f = Integer.parseInt(f);
            int r_t = Integer.parseInt(t);
            int total = r_t - r_f + 1;
            int roll_no = r_f;
            studIcon = new StudIcon[total];
            for (i = 0; i < studIcon.length; i++) {
                studIcon[i] = new StudIcon(getApplicationContext(), gridLayout, roll_no);
                roll_no++;
            }

            String abs = "";

            if (!abs.isEmpty()) {
                setRollno(abs);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//
//        StudIcon s = new StudIcon(getApplicationContext(),gridLayout,1);
//        StudIcon s1 = new StudIcon(getApplicationContext(),gridLayout,2);
//        StudIcon s2 = new StudIcon(getApplicationContext(),gridLayout,3);
//        StudIcon s3 = new StudIcon(getApplicationContext(),gridLayout,4);



    }
    public  void setRollno(String a)
    {
        String s[] = a.split(",");
        for(int i=0;i<s.length;i++)
        {
            try
            {
                int no = Integer.parseInt(s[i]);
                StudIcon.setState(studIcon,no);
            }
            catch (Exception e)
            {

            }
        }

    }

    public void setAllPresent(View view)
    {
        StudIcon.allPresent(studIcon);
    }
    public void setAllAbsent(View view)
    {
        StudIcon.allAbsent(studIcon);
    }
    public void returnData(View view)
    {
//        EditText et = findViewById(R.id.absent_rollno);
        String s = "";
        ArrayList<Integer> a = StudIcon.getAbsentRollNo(studIcon);
        for(int i=0;i<a.size();i++)
        {
            if(i==(a.size()-1))
            {
                s=s+""+a.get(i);
                continue;
            }
            s=s+""+a.get(i)+",";
        }

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
//        et.setText(s);
        data = (AttendData) getIntent().getSerializableExtra("Data");
        Intent back = new Intent(getApplicationContext(),TakeAttendance.class);
        back.putExtra("absentRolls",s);
        back.putExtra("Data",data);
        startActivity(back);
        finish();
    }
}
