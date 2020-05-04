package com.myapp.maddy.mybook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
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
 * Created by Maddy on 5/2/2020.
 */

public class ViewPage extends AppCompatActivity {

    LinearLayout date,content,linearLayout;
    Button button1,viewButton,editButton;
    Calendar calendar;
    LinearLayout.LayoutParams p;
    static String contentText;
    LinkedList linkedList;

    public void updateLinkedList(View view){
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
                    t.setText(a1.task.time + " - ");
                    content.addView(t);
                    EditText edit = new EditText(getApplicationContext());
                    edit.setId(R.id.EditTextContent + a1.task.id);
                    edit.setText(a1.task.data);
                    content.addView(edit);

                    a1 = a1.next;
                }
                linearLayout.addView(content);
            }else{
                Toast.makeText(getApplicationContext(),"Time already present in the list!!",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Please select time!!",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViewById(R.id.Addtime).setVisibility(View.GONE);

        date = (LinearLayout) findViewById(R.id.datePicker);

        linearLayout = (LinearLayout) findViewById(R.id.contents1);
        linearLayout.setPadding(20,50,20,10);
        content = new LinearLayout(getApplicationContext());
        content.setId(R.id.LinearLayoutContent);
        content.setOrientation(LinearLayout.VERTICAL);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(p);


        button1 = new Button(this);
        button1.setBackgroundColor(0xFFFFFF);
        button1.setBackground(getResources().getDrawable(R.drawable.ic_date_range_black_24dp));

        final TextView textView1 = new TextView(this);
        textView1.setId(R.id.datePickerID);
        textView1.setText("");
        date.addView(button1);
        date.addView(textView1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int month = (calendar.get(Calendar.MONTH));
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(ViewPage.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((TextView)findViewById(R.id.datePickerID)).setText(year+"/"+(month+1)+"/"+day);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        contentText = "";
        viewButton = new Button(this);
        viewButton.setText("View Contents");
        date.addView(viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBookHelperClass myBookHelperClass = new MyBookHelperClass(getApplicationContext());
                SQLiteDatabase sql = myBookHelperClass.getReadableDatabase();
                Cursor c = sql.query("MYBOOK",new String[]{"content"},"date = ?",new String[]{textView1.getText().toString()},null,null,null);
                c.moveToFirst();
                if(c.getCount()>0){
                    findViewById(R.id.Addtime).setVisibility(View.GONE);
                    content.setVisibility(View.GONE);

                    content = new LinearLayout(getApplicationContext());
                    content.setPadding(10,40,10,30);
                    content.setId(R.id.LinearLayoutContent);
                    content.setOrientation(LinearLayout.VERTICAL);
                    p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    content.setLayoutParams(p);

                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFF);
                    border.setStroke(3, 0xFF000000);
                    content.setBackground(border);
                    content.setBackgroundColor(Color.parseColor("#ccd100"));


                    TextView textView2 = new TextView(getApplicationContext());
                    textView2.setText("Content on "+textView1.getText().toString());
                    textView2.setLayoutParams(p);
                    textView2.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    content.addView(textView2);
                    textView2.setTypeface(textView2.getTypeface(),Typeface.BOLD);
                    textView2.setPaddingRelative(50,10,0,50);

                    contentText = c.getString(0).toString();

                    String[] contents = contentText.split("\t\n");
                    for(int i=0;i<contents.length;i++){
                        try {

                            LinearLayout linearLayoutContentByTime1 = new LinearLayout(getApplicationContext());
                            linearLayoutContentByTime1.setLayoutParams(p);
                            linearLayoutContentByTime1.setPadding(20,10,20,10);
                            linearLayoutContentByTime1.setOrientation(LinearLayout.VERTICAL);

                            LinearLayout linearLayoutContentByTime = new LinearLayout(getApplicationContext());
                            linearLayoutContentByTime.setLayoutParams(p);
                            linearLayoutContentByTime.setPadding(25,25,25,25);
                            linearLayoutContentByTime.setOrientation(LinearLayout.VERTICAL);
                            linearLayoutContentByTime.setBackgroundColor(Color.parseColor("#dde1ea"));

                            TextView textView = new TextView(getApplicationContext());
                            String a = contents[i].substring(0, 4);
                            int id = Integer.parseInt(a);
                            textView.setId(id);
                            textView.setText("" + ((Integer.parseInt(a.substring(0, 2))) - 10) + ":" + ((Integer.parseInt(a.substring(2, 4))) - 10) +
                                    " - ");
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            textView.setBackgroundColor(0xCCD1BB);
                            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

                            TextView textViewContent = new TextView(getApplicationContext());
                            textViewContent.setId(R.id.TextViewContent + id);
                            textViewContent.setBackgroundColor(0xCCD1BB);
                            textViewContent.setText(contents[i].substring(7));
                            textViewContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            textViewContent.setTypeface(textView2.getTypeface(),Typeface.BOLD_ITALIC);

                            linearLayoutContentByTime.addView(textView);
                            linearLayoutContentByTime.addView(textViewContent);
                            linearLayoutContentByTime1.addView(linearLayoutContentByTime);
                            content.addView(linearLayoutContentByTime1);
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(),"Empty!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    linearLayout.addView(content);

                }else{
                    findViewById(R.id.Addtime).setVisibility(View.GONE);
                    content.setVisibility(View.GONE);

                    contentText = "";

                    Toast.makeText(getApplicationContext(),"No Content",Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton = new Button(this);
        editButton.setText("Edit Contents");
        date.addView(editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contentText.equals("") ) {
                    LinearLayout l12 = (LinearLayout) findViewById(R.id.Addtime);
                    l12.removeAllViews();

                    findViewById(R.id.Addtime).setVisibility(View.VISIBLE);

                    content.setVisibility(View.GONE);

                    content = new LinearLayout(getApplicationContext());
                    content.setId(R.id.LinearLayoutContent);
                    content.setOrientation(LinearLayout.VERTICAL);
                    p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    content.setLayoutParams(p);

                    linkedList = new LinkedList();

                    LinearLayout l = (LinearLayout) findViewById(R.id.Addtime);
                    Button updateButton = new Button(getApplicationContext());
                    updateButton.setBackgroundColor(0xFFFFFF);
                    updateButton.setBackground(getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
                    TextView timeTextView = new TextView(getApplicationContext());
                    timeTextView.setId(R.id.timePickerID);
                    timeTextView.setText("");
                    l.addView(updateButton);
                    l.addView(timeTextView);
                    Button newAddTimeButton = new Button(getApplicationContext());
                    newAddTimeButton.setText("Add New Time");
                    final Button updateContents = new Button(getApplicationContext());
                    updateContents.setText("Update contents");
                    l.addView(newAddTimeButton);
                    l.addView(updateContents);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(ViewPage.this, new TimePickerDialog.OnTimeSetListener() {
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
                    newAddTimeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateLinkedList(view);
                        }
                    });

                    updateContents.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Node a1 = linkedList.head;
                            Node b = linkedList.head;
                            String text1 = "";
                            if(a1!=null) {

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

                                MyBookHelperClass myBookHelperClass = new MyBookHelperClass(getApplicationContext());
                                SQLiteDatabase sql = myBookHelperClass.getReadableDatabase();

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
                                    MyBookHelperClass myBookHelperClass1 = new MyBookHelperClass(getApplicationContext());
                                    SQLiteDatabase sql1 = myBookHelperClass1.getWritableDatabase();

                                    long row = sql1.delete("MYBOOK","date = ?",new String[]{textView1.getText().toString()});
                                    if (row > 0) {
                                        Toast.makeText(getApplicationContext(), "Successfully deleted!!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ViewPage.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unable to update", Toast.LENGTH_SHORT).show();
                                    }
                                }else {

                                    MyBookHelperClass myBookHelperClass1 = new MyBookHelperClass(getApplicationContext());
                                    SQLiteDatabase sql1 = myBookHelperClass1.getWritableDatabase();

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("content", text1);

                                    long row = sql1.update("MYBOOK", contentValues, "date = ?", new String[]{textView1.getText().toString()});
                                    if (row > 0) {
                                        Toast.makeText(getApplicationContext(), "Successfully updated the contents", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ViewPage.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unable to update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No content added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    String[] contents = contentText.split("\t\n");

                    for(int i=0;i<contents.length;i++) {

                        String a = contents[i].substring(0,4);
                        int id = Integer.parseInt(a);

                        TaskByTime task = new TaskByTime();
                        task.setId(id);
                        task.setTime(((Integer.parseInt(a.substring(0,2)))-10)+":"+((Integer.parseInt(a.substring(2,4)))-10));
                        task.setData(contents[i].split(" - ")[1]);

                        linkedList.insertDataAscending(task);

                        TextView textView = new TextView(getApplicationContext());
                        String a1 = contents[i].substring(0,4);
                        int id1 = Integer.parseInt(a1);
                        textView.setId(id1);
                        textView.setText(""+((Integer.parseInt(a1.substring(0,2)))-10)+":"+((Integer.parseInt(a1.substring(2,4)))-10)+ " - ");

                        EditText editText = new EditText(getApplicationContext());
                        editText.setId(R.id.EditTextContent + id1);
                        editText.setText(contents[i].split(" - ")[1]);

                        content.addView(textView);
                        content.addView(editText);
                    }
                    linearLayout.addView(content);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Content to Edit",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
