package com.example.divye.atlas;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.pubnub.api.*;

/**
 * Created by Divye10 on 06-03-2016.
 */
public class Players extends AppCompatActivity {

    private static final String TAG_PASSWORD="password";
    private static final String TAG_SUCCESS="success";
    Pubnub pubnub;
    String password,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Intent in = getIntent();

        Button submit=(Button)findViewById(R.id.join);
        submit.setText("SUBMIT");

        final EditText use = (EditText)findViewById(R.id.user);
        final EditText ed=(EditText)findViewById(R.id.edit_pass);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=use.getText().toString();
                password=ed.getText().toString();

                Intent intent = new Intent(getApplicationContext(),WaitPlayers.class);
                intent.putExtra("password",password);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }

}
