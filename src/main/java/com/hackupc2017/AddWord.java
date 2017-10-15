package com.hackupc2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class AddWord extends AppCompatActivity {
    public static final String words_file = "index.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

    }



    public void SaveWord(View view) {
        EditText wordName = (EditText) findViewById(R.id.word_name);
        EditText wordDef = (EditText) findViewById(R.id.word_definition);
        String wordName_string = wordName.getText().toString();
        String wordDef_string = wordDef.getText().toString();

        try {
            FileOutputStream fileout=openFileOutput(wordName_string + ".txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(wordDef_string);
            outputWriter.close();

            //display file saved message
           //Toast.makeText(getBaseContext(), "Word saved successfully!", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FileOutputStream fileout=openFileOutput(words_file, MODE_APPEND);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(wordName_string + ","); // /n
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Word saved to INDEX.TXT successfully!", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent();
        intent.putExtra("wordname", wordName_string);
        intent.putExtra("worddef", wordDef_string);
        setResult(0, intent);
        finish();
    }


}
