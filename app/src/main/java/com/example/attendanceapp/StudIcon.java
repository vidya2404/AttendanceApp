package com.example.attendanceapp;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class StudIcon
{
    Button btn;
    int roll_no;
    LinearLayout lay;
    Context cntx;
    GridLayout g_lay;
    boolean present;
    TextView roll_no_text;
    StudIcon(Context ctx,GridLayout gl,int roll_no)
    {
        this.roll_no = roll_no;
        present = true;
        g_lay = gl;

        try {
//        Linearlayout Settings
            lay = new LinearLayout(ctx);
            lay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lay.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(lay.getLayoutParams());
            lp.setMargins(5, 5, 5, 5);
            lay.setLayoutParams(lp);
            g_lay.addView(lay);
//        Button (Image)
            btn = new Button(ctx);
            btn.setText("P");
//        btn.setText(""+roll_no);
            btn.setBackgroundColor(0xff00ff00);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (present) {
                        setAbsent();
                    } else {
                        setPresent();
                    }
                }
            });

            roll_no_text = new TextView(ctx);
            roll_no_text.setText("" + roll_no);
            roll_no_text.setGravity(1);
            roll_no_text.setTextColor(0xff000000);


//        Add layouts
            lay.addView(btn);
            lay.addView(roll_no_text);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public   void setPresent()
    {
//        btn.setText(""+roll_no);
        btn.setText("P");
        btn.setBackgroundColor(0xff00ff00);
        present = true;
    }
    public void setAbsent()
    {
//        btn.setText(""+roll_no);
        btn.setText("A");
        btn.setBackgroundColor(0xffff0000);
        present = false;
    }
    static void allPresent(StudIcon[] arr)
    {
        for(StudIcon s:arr)
        {
            s.setPresent();
        }
    }
    static void allAbsent(StudIcon[] arr)
    {
        for(StudIcon s:arr)
        {
            s.setAbsent();
        }
    }
    static ArrayList<Integer> getAbsentRollNo(StudIcon[] arr)
    {
        ArrayList<Integer> absent_rollno = new ArrayList<Integer>();
        for(StudIcon s:arr)
        {
            if(s.present==false)
            {
                absent_rollno.add(s.roll_no);
            }
        }

        return absent_rollno;

    }
    static void setState(StudIcon arr[],int roll)
    {
        for(StudIcon s:arr)
        {
            if(s.roll_no==roll)
            {
                s.setAbsent();
            }
        }
    }


}
