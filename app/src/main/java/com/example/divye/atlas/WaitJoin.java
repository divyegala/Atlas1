package com.example.divye.atlas;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class WaitJoin extends AppCompatActivity {

    Pubnub pubnub;
    String messages;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_join);

        Bundle extras = getIntent().getExtras();
        final String password = extras.getString("password");
        final String username = extras.getString("username");

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
                            String start="start",end="exit_wait";
                            if(message.equals(start)){
                                intent=new Intent(getApplicationContext(),tabbed.class);
                                intent.putExtra("username",username);
                                intent.putExtra("password",password);
                                //intent.putExtra("status",1);
                                intent.putExtra("person",2);
                                startActivity(intent);
                            }
                            else if(message.equals(end)){

                                Intent launchNextActivity;
                                launchNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(launchNextActivity);
                            }
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
    }
    @Override
    public void onBackPressed(){
        View layout=findViewById(R.id.parent);
        Snackbar.make(layout, "Wait For Turn To Exit", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }
}
