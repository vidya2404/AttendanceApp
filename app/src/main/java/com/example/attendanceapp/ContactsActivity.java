package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {
  TableLayout tableLayout;
  AutoCompleteTextView years;
  EditText toRoll, fromRoll;
  Button save, go;
  String year;
  String data[][];
  DataBaseOp db;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts);
    tableLayout = findViewById(R.id.tableid);
    years  = findViewById(R.id.year);
    save = findViewById(R.id.btn);
    toRoll = findViewById(R.id.to_roll);
    fromRoll = findViewById(R.id.from_roll);
    go = findViewById(R.id.go);

    db = new DataBaseOp(getApplicationContext());
    CommonMethods commonMethods= new CommonMethods();
    years.setAdapter(commonMethods.getYearAdpt(getApplicationContext()));
    years.setTextColor(Color.BLACK);

    go.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        makeTable();
      }
    });

    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveContacts();
      }
    });
  }

  private void saveContacts() {
    data = new String[tableLayout.getChildCount() - 1][3];
    for(int i=1;i<tableLayout.getChildCount();i++)
    {
      TableRow tr = (TableRow) tableLayout.getChildAt(i);
      TextView roll_no = (TextView) tr.getChildAt(0);
      EditText name = (EditText) tr.getChildAt(1);
      EditText mobile = (EditText) tr.getChildAt(2);
      data[i-1][0] = roll_no.getText().toString();
      data[i-1][1] = name.getText().toString();
      data[i-1][2] = mobile.getText().toString();

    }
    try {
      year = getYear();
      db.saveContact(year, data);
      Toast.makeText(getApplicationContext(),"Contacts Saved",Toast.LENGTH_SHORT).show();
    }
    catch (Exception e)
    {
      Log.d("UnitTestTable", "save: "+e);
      Toast.makeText(getApplicationContext(),"Error while saving",Toast.LENGTH_SHORT).show();
    }
  }

  private void makeTable() {
    int start = Integer.parseInt(fromRoll.getText().toString());
    int end = Integer.parseInt(toRoll.getText().toString());
    for(int i=start; i<=end; i++)
    {
      TableRow tr = new TableRow(getApplicationContext());
      tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
      TextView roll = new TextView(getApplicationContext());
      roll.setWidth(20);
      roll.setText(""+i);
      roll.setTextSize(25);
      roll.setGravity(1);

      EditText name = new EditText(getApplicationContext());
//      name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
//      name.setMaxWidth(190);
//      name.setMaxLines(1);
      EditText mobile = new EditText(getApplicationContext());
//      mobile.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
      mobile.setInputType(2);
//      mobile.setMaxWidth(150);

      tr.addView(roll);
      tr.addView(name);
      tr.addView(mobile);
      tableLayout.addView(tr);
    }


  }
  public String getYear()
  {
    if(years.getText().toString().equals("1st Year"))
    {
      year = "first_year_contacts";
    }
    if(years.getText().toString().equals("2nd Year"))
    {
      year = "second_year_contacts";
    }
    if(years.getText().toString().equals("3rd Year"))
    {
      year = "third_year_contacts";
    }
    return year;
  }

}
