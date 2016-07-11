package com.example.divye.atlas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pubnub.api.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JoinGame extends AppCompatActivity {
    //JSONParser jParser = new JSONParser();
    Pubnub pubnub;
    int check=0;

    private static String url = "http://192.168.0.107/android_connect/joinsession.php";
    private static final String TAG_PASSWORD="password";
    private static final String TAG_SUCCESS="success";
    String password,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent in=getIntent();

        Button join=(Button)findViewById(R.id.join);
        final EditText pass=(EditText)findViewById(R.id.edit_pass);
        final EditText user=(EditText)findViewById(R.id.user);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=user.getText().toString();
                password=pass.getText().toString();
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
                                    check=1;
                                    System.out.println("After changing value"+check);

                                }

                                @Override
                                public void errorCallback(String channel, PubnubError error) {
                                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                            + " : " + error.toString());
                                }
                            }
                    );
                    pubnub.publish(password, username, new Callback() {
                    });
                } catch (PubnubException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(),tabbed.class);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                intent.putExtra("person",2);
                startActivity(intent);
               // System.out.println("hello");

            }
        });
    }

}
