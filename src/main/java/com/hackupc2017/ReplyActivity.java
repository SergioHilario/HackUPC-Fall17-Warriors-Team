package com.hackupc2017;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import static com.hackupc2017.NotificationService.REPLY_ACTION;

public class ReplyActivity extends AppCompatActivity {

    private static final String KEY_MESSAGE_ID = "key_message_id";
    private static final String KEY_NOTIFY_ID = "key_notify_id";

    private int mMessageId;
    private int mNotifyId;

    private ImageButton mSendButton;
    private EditText mEditReply;

    public static Intent getReplyMessageIntent(Context context, int notifyId, int messageId) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(KEY_MESSAGE_ID, messageId);
        intent.putExtra(KEY_NOTIFY_ID, notifyId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();

        if (REPLY_ACTION.equals(intent.getAction())) {
            mMessageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);
            mNotifyId = intent.getIntExtra(KEY_NOTIFY_ID, 0);
        }

        mEditReply = (EditText) findViewById(R.id.edit_reply);
        mSendButton = (ImageButton) findViewById(R.id.button_send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(mNotifyId, mMessageId);
            }
        });
    }

    private void sendMessage(int notifyId,  int messageId) {
        // update notification
        updateNotification(notifyId);

        String message = mEditReply.getText().toString().trim();
        // handle send message



            String Word_question = "";
            try {
                FileInputStream fileIn= this.openFileInput("Question.txt");
                InputStreamReader InputRead= new InputStreamReader(fileIn);

                char[] inputBuffer= new char[MainActivity.READ_BLOCK_SIZE];

                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    //foo.add(readstring);
                    Word_question +=readstring;
                }
                InputRead.close();
                //Definition_question = s;

            } catch (Exception e) {
                e.printStackTrace();
            }


            String file_word_question = Word_question + ".txt";
            String s="";
            try {
                FileInputStream fileIn= this.openFileInput(file_word_question);
                InputStreamReader InputRead= new InputStreamReader(fileIn);

                char[] inputBuffer= new char[MainActivity.READ_BLOCK_SIZE];

                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    //foo.add(readstring);
                    s +=readstring;
                }
                InputRead.close();
                //Definition_question = s;

            } catch (Exception e) {
                e.printStackTrace();
            }


            String feedback="??";

            if(s.contentEquals(message)){feedback = "Congratulations! You nailed it" ;
            }
            else{feedback = "Wrong answer, keep trying in later replies...";}



            Toast.makeText(this, "Message: " + feedback,
                    Toast.LENGTH_LONG).show();

            // update notification
            //updateNotification(context, notifyId);

        finish();
    }

    private void updateNotification(int notifyId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(notifyId);
    }
}
