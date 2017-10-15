package com.hackupc2017;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class NotificationService extends IntentService {
    public static String REPLY_ACTION = "com.hackupc2017.REPLY_ACTION";
    private static String KEY_REPLY = "key_reply_message";

    private int mNotificationId;
    private int mMessageId;
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            showNotification();
        }
    }

    private void showNotification() {
        mNotificationId = 1;
        mMessageId = 123; // dummy message id, ideally would come with the push notification

        // 1. Build label
        String replyLabel = getString(R.string.notif_action_reply);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
                .setLabel(replyLabel)
                .build();

        // 2. Build action
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_notif_action_reply, replyLabel, getReplyPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        // 3. Build notification
        //
        ////Aquí ponemos que coja una Word del archivo index.txt
        //MainActivity main_act = new MainActivity();
        //ArrayList<String> foo = main_act.foo;

        ArrayList<String> foo = new ArrayList<String>();
        int randomIndex = 0; //default value we set it to 0, a notification will ask for the
        //first word in our library
        try {

            FileInputStream fileIn=openFileInput(MainActivity.dictionary_file);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[MainActivity.READ_BLOCK_SIZE];
            String s="";
            int charRead;
            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            String[] str_list = s.split(",");
            int n = str_list.length;
            for(int i=0; i<= n-1; i++){
                foo.add(str_list[i]);
            }

            //We choose a random number between 0 and n-1 (to choose a word of all the ones
            // we have in our library
            Random randomGenerator = new Random();
            randomIndex = randomGenerator.nextInt(n);
            //textView.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String Word_question = foo.get(randomIndex);





        //new NotificationCompat.Builder(this).setContentTitle(Word_question);

        try {
            FileOutputStream fileout=openFileOutput("Question.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(Word_question);
            outputWriter.close();

            //display file saved message
            //Toast.makeText(getBaseContext(), "Word saved successfully!", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notif_smile)
                .setContentTitle(Word_question)
                .setContentText(getString(R.string.notif_content))
                .setShowWhen(true)
                .addAction(replyAction);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    private PendingIntent getReplyPendingIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of this codelab, will be doing a broadcast receiver
            intent = NotificationBroadcastReceiver.getReplyMessageIntent(this, mNotificationId, mMessageId);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity
            intent = ReplyActivity.getReplyMessageIntent(this, mNotificationId, mMessageId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }
}
