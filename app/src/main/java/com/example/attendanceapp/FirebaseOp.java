package com.example.attendanceapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseOp {

  FirebaseDatabase database;
  DatabaseReference reference;

  FirebaseOp()
  {
    database = FirebaseDatabase.getInstance();
    reference = database.getReference("/");
  }


  void saveData(String date, String year, String subject, String mode, String absentRolls)
  {
    // later take date

    reference = database.getReference("/"+year+"/"+subject+"/"+date);

    Map<String, Object> data2 = new HashMap<>();
    data2.put("PR_AbsentRolls", "C");
    data2.put("TH_AbsentRolls", "C");
    Map<String, Object> data = new HashMap<>();
    data.put(year, data2);
    String id = reference.push().getKey();
    reference.child(id).updateChildren(data2);


//    reference.setValue("absentRolls", absentRolls);
  }
}


class AttendanceData
{
  String subject, mode, absentRolls;
  AttendanceData(String subject, String mode, String absentRolls)
  {
    this.subject = subject;
    this.mode = mode;
    this.absentRolls = absentRolls;
  }
}
