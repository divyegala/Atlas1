package com.example.divye.atlas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pubnub.api.*;

import java.util.ArrayList;
import java.util.Iterator;

public class WaitPlayers extends AppCompatActivity {

    Pubnub pubnub;
    TextView textView;
    static Integer key = 1;
    String messages;
    static ArrayList<String> a = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_players);

        Bundle extras = getIntent().getExtras();
        final String password = extras.getString("password");
        textView = (TextView)findViewById(R.id.textView);
        final String username = extras.getString("username");
        Integer priority=0;
        a.add(priority,username);
        pubnub = new Pubnub("pub-c-4aeb3dae-c48d-4f23-b3b2-e54d08bd88ce", "sub-c-fe640624-e5fa-11e5-aad5-02ee2ddab7fe");

        try {
            pubnub.subscribe(password, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                            pubnub.publish("my_channel", "Hello from the PubNub Java SDK", new Callback() {
                            });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {

                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            messages = message.toString();
                            Log.v("WaitPlayers",messages);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    a.add(key, messages);
                                    key++;
                                    messages = "";
                                    textView.append("\n"+messages);
                                }
                            });

                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );

        } catch (PubnubException e) {
        }
        Button start_game = (Button)findViewById(R.id.start_game);
        start_game.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String r = "";
                Iterator itr=a.iterator();
                while(itr.hasNext()){
                    //Log.v("ArrayList",(String)itr);
                    r = r + (String)itr.next();
                    r = r + ",";
                    //  String s = r.toString();
                    //Log.v("NewGame",r);
                }
                Log.v("List of usernames",r);

                pubnub.publish(password, r, new Callback() {
                });
                Intent intent = new Intent(getApplicationContext(),FirstPlayerGame.class);
                intent.putExtra("totalList",r);
                intent.putExtra("username",username);
                //intent.putStringArrayListExtra("chances",a);
                intent.putExtra("password",password);
                startActivity(intent);

                    //System.out.println(a1[0].toString());
            }
        });
    }
}
