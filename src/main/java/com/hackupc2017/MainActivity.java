package com.hackupc2017;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String ARG_REPLY = "arg_reply";
    private Button mButtonNotif;

    public ArrayList<String> foo = new ArrayList<String>();
    public ArrayAdapter<String> adapter;
    static final int READ_BLOCK_SIZE = 100;
    public static final String dictionary_file = "index.txt";

    /**
     * Gets an intent to reply message
     * @param context
     * @param reply
     * @return
     */
    public static Intent getReplyMessageIntent(Context context, String reply) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ARG_REPLY, reply);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = new Intent(this , NotificationService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);


        Calendar calendar = Calendar.getInstance();



        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*60*1000/2 , pendingIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            FileInputStream fileIn=openFileInput(dictionary_file);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                //foo.add(readstring);
                s +=readstring;
            }
            InputRead.close();

            //List<String> elephantList = Arrays.asList(s.split(","));
            //ArrayList<String> str_list = Arrays.asList(s.split(","));
            String[] str_list = s.split(",");
            int n = str_list.length;
            for(int i=0; i<= n-1; i++){
                foo.add(str_list[i]);
            }

            //textView.setText(s);


        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foo);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            TextView textView = (TextView) findViewById(R.id.textView3);

            String actual_textview = (String) parent.getItemAtPosition(position); //v.getText().toString();
            String file_name = actual_textview + ".txt";


            try {
                FileInputStream fileIn=openFileInput(file_name);
                InputStreamReader InputRead= new InputStreamReader(fileIn);

                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
                textView.setText(s);


            } catch (Exception e) {
                e.printStackTrace();
            }
            //textView.setText(s);
        }
    };

    public void NewWord(View view) {
        Intent intent = new Intent(this, AddWord.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.add(data.getStringExtra("wordname"));

    }


}
