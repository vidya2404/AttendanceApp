package com.example.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseOp extends SQLiteOpenHelper
{
  SQLiteDatabase db;
  static String dbname = "attendancedb";
  //    static String tablename;
  String query;
  final String TAG = "DatabaseOp";
  String smsRoll[][];
  String result[];
  public DataBaseOp(Context ctx)
  {
    super(ctx, dbname, null, 1);
    getWritableDatabase();
  }

  public void insertData(String tablename, String subject, String mode, String absentRolls, String cr_date, int startroll, int endroll) {
    try
    {
      SQLiteDatabase dbs = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("Subject", subject);
      values.put("Mode", mode);
      values.put("AbsentRolls", absentRolls);
      values.put("Date", cr_date);
      values.put("StartRoll", startroll);
      values.put("EndRoll", endroll);

      dbs.insert(tablename, null, values);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onCreate(SQLiteDatabase dbs)
  // CreTe Creafe Query.
  {
    try {
      query ="CREATE TABLE IF NOT EXISTS first_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
      dbs.execSQL(query);
      query = "CREATE TABLE IF NOT EXISTS second_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
      dbs.execSQL(query);
      query = "CREATE TABLE IF NOT EXISTS third_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
      dbs.execSQL(query);
      query = "CREATE TABLE IF NOT EXISTS first_year_contacts(Roll text, Name text, Mobile text);";
      dbs.execSQL(query);
      query = "CREATE TABLE IF NOT EXISTS second_year_contacts(Roll text, Name text, Mobile text);";
      dbs.execSQL(query);
      query = "CREATE TABLE IF NOT EXISTS third_year_contacts(Roll text, Name text, Mobile text);";
      dbs.execSQL(query);

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  String[][] fetchMonthData(String table_name, String subject,String mode,String month_no)
  {
    // For Date Format  2 -> 02
    if(month_no.length()==1)
      month_no = "0"+month_no;
    // Array List to store result
    String result[][];

    // Execute Query
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "Select AbsentRolls, Date, StartRoll, EndRoll from "+table_name+" where " +
            "Subject='"+subject+"' AND "+
            "Mode='"+mode+"' AND "+
            "Date like '__-"+month_no+"-____' ORDER BY Date ;";

    Cursor cursor = db.rawQuery(query,null);
    try {
      if (cursor.getCount() > 0) {
        result = new String[cursor.getCount()][4];
        int index=0;
        cursor.moveToFirst();
        do {
          result[index][0] = cursor.getString(0);
          result[index][1] = cursor.getString(1).substring(0,1);
          result[index][2] = cursor.getString(2);
          result[index][3] = cursor.getString(3);
          index++;
        }
        while (cursor.moveToNext());
        return result;
      } else {
        int a;
      }
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    cursor.close();
    return  null;
  }

  String[] getDateData(String table, String subj, String mode, String date)
  {

    String result[];

    // Execute Query
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "Select AbsentRolls, StartRoll, EndRoll from "+table+" where " +
            "Subject='"+subj+"' AND "+
            "Mode='"+mode+"' AND "+
            "Date='"+date+"'";

    Cursor cursor = db.rawQuery(query,null);
    try {
      if (cursor.getCount() > 0) {
        result = new String[3];
        int index=0;
        cursor.moveToFirst();
        do {
          result[0] = cursor.getString(0);
          result[1] = cursor.getString(1);
          result[2] = cursor.getString(2);
          index++;
        }
        while (cursor.moveToNext());

        return result;
      } else {

      }
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    cursor.close();
    return  null;
  }


  String[][] getStudentNames(String tablename, int startRoll, int endRoll)
  {
    String data[][];

    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT Roll, Name FROM "+ tablename +"_contacts WHERE Roll BETWEEN "+ startRoll +" AND "+ endRoll +";";

    Cursor cursor = db.rawQuery(query,null);
    try {
      if (cursor.getCount() > 0) {
        data = new String[cursor.getCount()][2];
        int index = 0;
        cursor.moveToFirst();
        do {
          data[index][0] = cursor.getString(0);
          data[index][1] = cursor.getString(1);
          index++;
        }
        while (cursor.moveToNext());
        return data;
      }
        else {
        Log.d(TAG,"ERROR");
      }
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    cursor.close();
    return null;

  }

  String getNameByRoll(String year, String roll)
  {
    String name="";
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "Select Name from "+year+"_contacts where " +
            "Roll='"+roll+"';";

    Cursor cursor = db.rawQuery(query,null);

      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        name = cursor.getString(0);
    }

    return name;
  }
  public static boolean isTableExists(SQLiteDatabase db,String table_name)
  {
    try {
      Cursor cursor = db.rawQuery("Select name from sqlite_master where type='table' and name='" + table_name + "';", null);
      if (cursor.getCount() > 0) {
        return true;
      }
      cursor.close();
      return false;
    }
    catch (Exception e)
    {
      return false;
    }

  }

  String[][] getUnitTestTable(ContentValues values,int total_stud)
  {
    String table_name = ""; // Format [Exam_TYPE]+[Subject]+[Year] eg. UT_1_Python_3rd_Year
    table_name = table_name + values.get("EXAM_TYPE").toString()+"_";
    table_name = table_name + values.get("SUBJECT").toString()+"_";
    table_name = table_name + values.get("YEAR").toString();
    String data[][] = new String[total_stud][3];
    if(isTableExists(this.getReadableDatabase(),table_name))
    {
      // Fetch all Data
      int i=0;
      String year;
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor =  db.rawQuery("Select * FROM " +table_name,null);
      try {
        if (cursor.getCount() > 0) {
          int index = 0;
          smsRoll = new String[cursor.getCount()][4];
          //[EXAM_TYPE, SUBJECT, MARKS, MOBILENO]
          cursor.moveToFirst();
          do {
            data[index][0] = cursor.getString(0); // Roll No
            data[index][1] = cursor.getString(1); // Marks
//                        if(Integer.parseInt(data[index][1]) < 10)
//                        {
            year = getYear(table_name);
            smsRoll[index][0] = values.get("EXAM_TYPE").toString();//getMobileNo(year, data[index][0]);
            smsRoll[index][1] = values.get("SUBJECT").toString();
            smsRoll[index][2] = cursor.getString(1);
            smsRoll[index][3] = getMobileNo(year, data[index][0]);
//                        }
            index++;
            if(total_stud==index)
              break;
          }
          while (cursor.moveToNext());
          cursor.close();
        }
      }catch (Exception e) {
        cursor.close();
        return null;
      }
    }
    else
    {
      // Create new table
      SQLiteDatabase db = this.getWritableDatabase();
      db.execSQL("Create table "+table_name+"(ROLL_NO TEXT,MARKS TEXT)");
      Log.d(TAG, "getUnitTestTable:Table Created "+table_name);
      for(int i=1;i<=total_stud;i++)
      {
        ContentValues value = new ContentValues();
        value.put("ROLL_NO",""+i);
        value.put("MARKS","_");
        data[i-1][0]=""+i;
        data[i-1][1]="";
        db.insert(table_name,null,value);
      }

    }
    return  data;
  }
  String getYear(String table)
  {
    if(table.contains("1st_Year"))
      return "first_year_contacts";
    if(table.contains("2nd_Year"))
      return "second_year_contacts";
    if(table.contains("3rd_Year"))
      return "third_year_contacts";
    else
      return null;
  }
  public  void updateUnitTest(ContentValues values,String[][] data)
  {
    String table_name = ""; // Format [Exam_TYPE]+[Subject]+[Year] eg. UT_1_Python_3rd_Year
    table_name = table_name + values.get("EXAM_TYPE").toString()+"_";
    table_name = table_name + values.get("SUBJECT").toString()+"_";
    table_name = table_name + values.get("YEAR").toString();

    // Update the table:
    db = getWritableDatabase();
    for(int i=0;i<data.length;i++)
    {
      ContentValues value = new ContentValues();
      value.put("ROLL_NO",data[i][0]);
      value.put("MARKS",data[i][1]);
      db.update(table_name,value,"ROLL_NO like \""+data[i][0]+"\"",null);
    }
  }

  public void saveContact(String tablename, String[][] data) {

    db = getWritableDatabase();
    String count = "SELECT count(*) FROM " + tablename + ";";
    Cursor mcursor = db.rawQuery(count, null);
    mcursor.moveToFirst();
    int icount = mcursor.getInt(0);
    if (icount > 0) {
      db.execSQL("DELETE FROM "+tablename);
      db.execSQL("VACUUM");
    }
    for (int i = 0; i < data.length; i++) {
      ContentValues value = new ContentValues();
      value.put("Roll", data[i][0]);
      value.put("Name", data[i][1]);
      value.put("Mobile", data[i][2]);
      db.insert(tablename, null, value);
    }
  }

  String getMobileNo(String tablename, String roll)
  {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "Select Mobile from "+tablename+" where " +
            "Roll='"+roll+"'";
    String result;
    Cursor cursor = db.rawQuery(query,null);
    try {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        result = cursor.getString(0);

        return result;
      } else {
        Log.d(TAG,"ERROR");
      }
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    cursor.close();
    return null;
  }
  //    public void chooseTable(String year)
//    {
//        if(year.equals("1st Year"))
//            this.tablename = "first_year";
//        if(year.equals("2nd Year"))
//            this.tablename = "second_year";
//        if(year.equals("3rd Year"))
//            this.tablename = "third_year";
//    }
  String[][] getSMSData()
  {
    return smsRoll;
  }

  String[][] getMobileNo(String year, int startRoll, int endRoll)
  {
//        year = getYear(year);
    String table_name = year + "_contacts"; // Format [Exam_TYPE]+[Subject]+[Year] eg. UT_1_Python_3rd_Year
    String sql="";

    if(startRoll == 0 && endRoll == 0)
      sql = "SELECT * FROM "+table_name;
    else
      sql = "SELECT * FROM "+table_name+" WHERE ROLL >= "+startRoll+" AND ROLL <= "+endRoll;

    String data[][];
    if(isTableExists(this.getReadableDatabase(),table_name))
    {
      // Fetch all Data
      int i=0;
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor =  db.rawQuery(sql,null);
      try {
        data = new String[cursor.getCount()][2];
        if (cursor.getCount() > 0) {
          int index = 0;

          //[EXAM_TYPE, SUBJECT, MARKS, MOBILENO]
          cursor.moveToFirst();
          do {
            data[index][0] = cursor.getString(0); // Roll No
            data[index][1] = cursor.getString(1); // Mobile No

            index++;

          }
          while (cursor.moveToNext());
          cursor.close();
        }
      }catch (Exception e) {
        cursor.close();
        return null;
      }
      return  data;
    }

    return null;
  }





}
