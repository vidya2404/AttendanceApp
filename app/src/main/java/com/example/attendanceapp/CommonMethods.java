package com.example.attendanceapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;

public class CommonMethods
{
    String[] yearList = {"1st Year", "2nd Year", "3rd Year"};
    String[] subList = {"ENG","BMS","BSC","ICT","EGE","WPC","EEE","AMS","BEL",
            "C","BCC","CPHM","WPD","OOP","DSC","CGR","DBMS","DTE","JPR","SEN",
            "DCN","MIC","GUI","EST","OSY","AJP","STE","CSS","ITR","CPP","PWP",
            "MAD","EDP","ETI","MGMT","WBP","CPE"};

    String[] monthList = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    String[] exam_types = {"UT_1","UT_2"};
    ArrayAdapter<String> yearAdpt, subAdpt, monthAdpt,examAdpt;

    public  int getStudentTotal(String year)
    {
        switch (year){
           case  "1st Year" : return 10;
            case "2nd Year" : return 10;
            case "3rd Year" : return 10;
            default:
                return 10;
        }
    }


    public ArrayAdapter<String> getYearAdpt(Context c)
    {
        yearAdpt = new ArrayAdapter(c, android.R.layout.select_dialog_item, yearList);
        return yearAdpt;
    }
    public ArrayAdapter<String> getExamTypeAdpt(Context c)
    {
        examAdpt = new ArrayAdapter(c, android.R.layout.select_dialog_item, exam_types);
        return examAdpt;
    }

    public ArrayAdapter<String> getSubjectAdpt(Context c)
    {
        subAdpt = new ArrayAdapter<>(c, android.R.layout.select_dialog_item, subList);
        return subAdpt;
    }

    public ArrayAdapter<String> getMonthAdpt(Context c)
    {
        monthAdpt = new ArrayAdapter<>(c, android.R.layout.select_dialog_item, monthList);
        return monthAdpt;
    }

    String getMonth(int num)
    {
        return monthList[num-1];
    }
//    public String[] getSubList(String year)
//    {
//        try {
//            db = SQLiteDatabase.openOrCreateDatabase("attendancedb", null);
//            String sql = "Select Subjects from allsubjects;";
////                    String sql = "Select * from " + tablenameBox.getText().toString();
//            Cursor c = db.rawQuery(sql, null);
//
//            int Column1 = c.getColumnIndex("Subjects");
//            // Check if our result was valid.
//
//            if (c != null && c.moveToFirst()) {
//                // Loop through all Results
//                do {
//                    subs = c.getString(Column1);
//                } while (c.moveToNext());
//                db.close();
//            }
//        }
//        catch (SQLException e)
//        {
//
//        }
//        return subs.split(",");
//    }
}
