package com.hackupc2017;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import static com.hackupc2017.NotificationService.REPLY_ACTION;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static String KEY_NOTIFICATION_ID = "key_noticiation_id";
    private static String KEY_MESSAGE_ID = "key_message_id";

    public static Intent getReplyMessageIntent(Context context, int notificationId, int messageId) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
        intent.putExtra(KEY_MESSAGE_ID, messageId);
        return intent;
    }
    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = NotificationService.getReplyMessage(intent);
            int messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);

            String Word_question = "";
            try {
                FileInputStream fileIn= context.openFileInput("Question.txt");
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
                FileInputStream fileIn= context.openFileInput(file_word_question);
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



            Toast.makeText(context, "Message: " + feedback,
                    Toast.LENGTH_LONG).show();

            // update notification
            int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1);
            //updateNotification(context, notifyId);
        }
    }/*

    private void updateNotification(Context context, int notifyId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true);

    }*/

}
