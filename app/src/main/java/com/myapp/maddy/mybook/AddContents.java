package com.myapp.maddy.mybook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Maddy on 5/1/2020.
 */

public class AddContents extends AppCompatActivity {

    Button button, AddButton,view;
    public LinearLayout.LayoutParams p;
    Button button1, Submit;
    Calendar calendar;
    LinkedList linkedList;
    AddContents a;
    LinearLayout content;
    LinearLayout s;

    public void updateContents(LinkedList list){

        Node a = list.head;
        while(a.next!=null){
            a = a.next;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        s = (LinearLayout) findViewById(R.id.contents1);
        content = new LinearLayout(getApplicationContext());
        content.setId(R.id.LinearLayoutContent);
        content.setOrientation(LinearLayout.VERTICAL);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(p);

        linkedList = new LinkedList();

        LinearLayout l1 = (LinearLayout) findViewById(R.id.datePicker);
        button1 = new Button(this);
        button1.setBackgroundColor(0xFFFFFF);
        button1.setBackground(getResources().getDrawable(R.drawable.ic_date_range_black_24dp));
        calendar = Calendar.getInstance();
        int month = (calendar.get(Calendar.MONTH));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        final TextView textView1 = new TextView(this);
        textView1.setId(R.id.datePickerID);
        textView1.setText(year+"/"+(month+1)+"/"+day);
        l1.addView(button1);
        l1.addView(textView1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int month = (calendar.get(Calendar.MONTH));
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);


                DatePickerDialog dialog = new DatePickerDialog(AddContents.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((TextView)findViewById(R.id.datePickerID)).setText(year+"/"+(month+1)+"/"+day);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        Submit = new Button(this);
        Submit.setText("Submit");
        l1.addView(Submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Node a1 = linkedList.head;
                String text1 = "";
                if(a1!=null) {



                    MyBookHelperClass myBookHelperClass = new MyBookHelperClass(getApplicationContext());
                    SQLiteDatabase sql = myBookHelperClass.getReadableDatabase();

                    Cursor cursor = sql.query("MYBOOK", new String[]{"date"}, "date = ?", new String[]{textView1.getText().toString()}, null, null, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(getApplicationContext(), "Already content present for the date.", Toast.LENGTH_SHORT).show();
                    } else{

                        LinkedList old = linkedList;
                        linkedList = new LinkedList();
                        Node newList = old.head;
                        while (newList != null) {
                            TaskByTime task = new TaskByTime();
                            task.id = newList.task.id;
                            task.time = newList.task.time;
                            task.data = ((EditText) findViewById(R.id.EditTextContent + task.id)).getText().toString();
                            linkedList.insertDataAscending(task);
                            newList = newList.next;
                        }

                        Node a = linkedList.head;
                        while (a.next != null) {
                            if(!a.task.data.equals("")) {
                                text1 += a.task.id + " - " + a.task.data + "\t\n";
                            }
                            a = a.next;
                        }
                        if(!a.task.data.equals("")) {
                            text1 += a.task.id + " - " + a.task.data;
                        }

                        if(text1.equals("")){
                            Toast.makeText(getApplicationContext(), "No contents to save", Toast.LENGTH_SHORT).show();
                        }else {
                            MyBookHelperClass myBookHelperClass1 = new MyBookHelperClass(getApplicationContext());
                            SQLiteDatabase sql1 = myBookHelperClass1.getWritableDatabase();

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("date", textView1.getText().toString());
                            contentValues.put("content", text1);

                            long row = sql1.insert("MYBOOK", null, contentValues);
                            if (row > 0) {
                                Toast.makeText(getApplicationContext(), "Successfully added to DB!!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to add to db", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "No content added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout l = (LinearLayout) findViewById(R.id.Addtime);
        button = new Button(this);
        button.setBackgroundColor(0xFFFFFF);
        button.setBackground(getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
        TextView textView = new TextView(this);
        textView.setId(R.id.timePickerID);
        textView.setText("");
        l.addView(button);
        l.addView(textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddContents.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        int hrs = hourOfDay;
                        int mins = minutes;

                        ((TextView)findViewById(R.id.timePickerID)).setText(hrs+":"+mins);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        AddButton = new Button(this);
        AddButton.setText("Add new Entry");
        l.addView(AddButton);
        a =new AddContents();
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((TextView)findViewById(R.id.timePickerID)).getText().equals("")){

                    String text = (String) ((TextView)findViewById(R.id.timePickerID)).getText();
                    int id = Integer.parseInt((10+Integer.parseInt(text.split(":")[0]))+""+(10+Integer.parseInt(text.split(":")[1])));

                    Node checkDuplicateTime = linkedList.head;
                    boolean status = true;
                    while(checkDuplicateTime!=null && status == true){
                        if(checkDuplicateTime.task.id == id){
                            status = false;
                        }
                        checkDuplicateTime = checkDuplicateTime.next;
                    }

                    if(status) {
                        LinkedList old = linkedList;
                        linkedList = new LinkedList();
                        Node newList = old.head;
                        while (newList != null) {
                            TaskByTime task = new TaskByTime();
                            task.id = newList.task.id;
                            task.time = newList.task.time;
                            task.data = ((EditText) findViewById(R.id.EditTextContent + task.id)).getText().toString();
                            linkedList.insertDataAscending(task);
                            newList = newList.next;
                        }


                        TaskByTime task = new TaskByTime();
                        task.setId(id);
                        task.setTime(text);
                        task.setData("");

                        linkedList.insertDataAscending(task);
                        ((TextView) findViewById(R.id.timePickerID)).setText("");

                        content.setVisibility(View.GONE);

                        content = new LinearLayout(getApplicationContext());
                        content.setId(R.id.LinearLayoutContent);
                        content.setOrientation(LinearLayout.VERTICAL);
                        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        content.setLayoutParams(p);

                        Node a1 = linkedList.head;
                        while (a1 != null) {
                            TextView t = new TextView(getApplicationContext());
                            t.setText(a1.task.getTime() + " - ");
                            content.addView(t);
                            EditText edit = new EditText(getApplicationContext());
                            edit.setId(R.id.EditTextContent + a1.task.id);
                            edit.setText(a1.task.getData());
                            content.addView(edit);

                            a1 = a1.next;
                        }
                        s.addView(content);
                    }else{
                        Toast.makeText(getApplicationContext(),"Time already present in the list!!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please select time!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        view = new Button(this);
        view.setText("View");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = linkedList.print();
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
