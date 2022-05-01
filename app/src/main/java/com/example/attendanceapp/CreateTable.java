package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static java.lang.Integer.parseInt;

public class CreateTable extends AppCompatActivity {
    Button insert, display, update, delete;
    EditText idBox, subjectBox, modeBox, absentrollsBox, dateBox, tablenameBox, yearBox;
    int id;
    TextView data;
    String subject, mode, absent_roll, tablename, date, year;
    SQLiteDatabase db;
    DatePickerDialog dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        insert = findViewById(R.id.insert);
        display = findViewById(R.id.display);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        idBox = findViewById(R.id.id);
        subjectBox = findViewById(R.id.sub);
//        modeBox = findViewById(R.id.mode);
        absentrollsBox = findViewById(R.id.absent);
        dateBox = findViewById(R.id.date);
        tablenameBox = findViewById(R.id.tablename);
        data = findViewById(R.id.data);
        yearBox = findViewById(R.id.year);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    db = openOrCreateDatabase("database", MODE_PRIVATE, null);
//                    id = parseInt(idBox.getText().toString());
//                    subject = subjectBox.getText().toString();
//                    mode = modeBox.getText().toString();
//                    absent_roll = absentrollsBox.getText().toString();
//                    tablename = tablenameBox.getText().toString();
                    subject = subjectBox.getText().toString();
                    year = yearBox.getText().toString();

//                    String query = "drop table allsubjects";
//                    String query = "insert into allsubjects(Year, Subjects) values('"+year+"','"+subject+"');";
                    String query = "CREATE TABLE IF NOT EXISTS second_year(Subject text, Mode text, AbsentRolls text, Date text, EndRoll INT);";
//                    String query = "delete from first_year;";
//                    String query = "ALTER TABLE third_year ADD EndRoll INT";
                    db.execSQL(query);
                    Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                    db.close();
                }
                catch (SQLException e)
                {
                    Toast.makeText(getApplicationContext(), "Data Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        dateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp = new DatePickerDialog(CreateTable.this);
                dp.show();
                dp.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month+1) + "/" + year;
                        dateBox.setText(date);
                    }
                });

            }
        });
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db = openOrCreateDatabase("attendancedb", MODE_PRIVATE, null);
                    String query = "Select * from first_year where Date='"+date+"'"+";";
//                    String sql = "Select * from " + tablenameBox.getText().toString();
                    Cursor c = db.rawQuery(query, null);

//                    int Column1 = c.getColumnIndex("Year");
                    int Column2 = c.getColumnIndex("AbsentRolls");
                    // Check if our result was valid.
                    Vector details = new Vector(3);

                    if (c != null && c.moveToFirst()) {
                        // Loop through all Results
                        do {
                            subject = c.getString(Column2);
                            details.add("\n"+subject);
                        } while (c.moveToNext());
                        db.close();
                        data.setText(details.toString());
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
