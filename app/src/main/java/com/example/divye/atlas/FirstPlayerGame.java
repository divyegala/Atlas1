package com.example.divye.atlas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class FirstPlayerGame extends AppCompatActivity {

    //SQLiteDatabase db;
    //DatabaseHandler db = new DatabaseHandler(this);
    //int allID = new int[100];
    static HashMap<Integer,String> allName = new HashMap<Integer,String>();
    static HashMap<Integer,String> chanceDecider = new HashMap<>();
    static HashMap<String,Integer> score= new HashMap<>();
    static Integer points=0;
    //ArrayList<String> a=new ArrayList<>();
    JSONParser jParser = new JSONParser();
    Button go;
    Button pass;

    private static String url = "http://192.168.0.101/android_connect/getname.php";
    Pubnub pubnub;
    String name;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG = "NewGame";
    private String password,list;
    static int chanceNo=1;
    static String content;
    static Integer count = 0,kkk = 1;
    static int key=1,turn = 0;
    static String username,receivedName,totalList,currentUser,currentChance,passChance="pass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        go = (Button) findViewById(R.id.go);
        pass = (Button) findViewById(R.id.pass);
        Bundle extras=getIntent().getExtras();
        int status=extras.getInt("status");
        if(turn == 0 || status == 0 )
            go.setEnabled(true);
        else if(status == 1)
            go.setEnabled(false);

        //a=extras.getStringArrayList("chances");
        //list=extras.getString("chance");
        totalList=extras.getString("totalList");
        password=extras.getString("password");
        username=extras.getString("username");

        pubnub = new Pubnub("pub-c-4aeb3dae-c48d-4f23-b3b2-e54d08bd88ce", "sub-c-fe640624-e5fa-11e5-aad5-02ee2ddab7fe");
        final TextView textView = (TextView)findViewById(R.id.textView4);
        char a[]=totalList.toCharArray();
        if(turn == 0){
            String n1="";
            for (int i = 0; i < a.length; i++) {
                while (a[i] != ',') {
                    n1 = n1 + a[i++];
                }
                chanceDecider.put(kkk,n1);
                kkk++;
                n1="";
                turn++;
            }
        }
        Iterator listIterator = chanceDecider.entrySet().iterator();
        while (listIterator.hasNext()){
            System.out.println(listIterator.next());
        }
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

                            if(turn!=0){

                                receivedName=message.toString();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentUser="";
                                        currentChance="";
                                        for (int i = 0; i < receivedName.length(); i++) {
                                            while (receivedName.charAt(i) != ',') {
                                                currentUser = currentUser + receivedName.charAt(i++);
                                            }
                                            i++;
                                            currentChance = currentChance + receivedName.charAt(i++);
                                            chanceNo = Integer.parseInt(currentChance);
                                        }
                                        Toast.makeText(getApplicationContext(),currentUser,Toast.LENGTH_SHORT).show();
                                        if(!currentUser.equals(passChance)) {
                                            Toast.makeText(getApplicationContext(),chanceDecider.get(chanceNo)+","+username,Toast.LENGTH_SHORT).show();
                                            textView.setText(currentUser);
                                            allName.put(count, currentUser);
                                            count++;
                                            if (chanceDecider.get(chanceNo).equals(username)) {
                                                go.setEnabled(true);
                                            } else
                                                go.setEnabled(false);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Else",Toast.LENGTH_SHORT).show();
                                            if(!content.isEmpty() && content!=null) {
                                                Toast.makeText(getApplicationContext(),"Else If",Toast.LENGTH_SHORT).show();
                                                for (int i = 0; i < receivedName.length(); i++) {
                                                    while (receivedName.charAt(i) != ',') {
                                                        // currentUser = currentUser + receivedName.charAt(i++);
                                                    }
                                                    i++;
                                                    while (receivedName.charAt(i) != ',') {
                                                        currentUser = currentUser + receivedName.charAt(i++);
                                                    }
                                                    i++;
                                                    currentChance = currentChance + receivedName.charAt(i++);
                                                    chanceNo = Integer.parseInt(currentChance);
                                                }
                                                textView.setText(currentUser);
                                                allName.put(count, currentUser);
                                                count++;
                                                if (chanceDecider.get(chanceNo).equals(username)) {
                                                    go.setEnabled(true);
                                                } else
                                                    go.setEnabled(false);
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"HERE",Toast.LENGTH_SHORT).show();
                                                allName.put(count, currentUser);
                                                count++;
                                                if (chanceDecider.get(chanceNo).equals(username)) {
                                                    go.setEnabled(true);
                                                } else
                                                    go.setEnabled(false);
                                            }

                                        }
                                    }
                                });

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
       // char allUsers[]=new char[100];


        //pubnub = new Pubnub("pub-c-4aeb3dae-c48d-4f23-b3b2-e54d08bd88ce", "sub-c-fe640624-e5fa-11e5-aad5-02ee2ddab7fe");
        //db=openOrCreateDatabase("places", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS city (id INT,name VARCHAR);");

        final EditText in1 = (EditText) findViewById(R.id.input_city);

        System.out.println(chanceDecider.get(chanceNo));
        System.out.println(username);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content=textView.getText().toString();
                chanceNo++;
                if(chanceNo>chanceDecider.size()){
                    chanceNo=1;
                }
                points--;
                score.put(username,points);
                if(content.isEmpty() || content==null){

                    pubnub.publish(password,"pass,"+chanceNo, new Callback() {
                    });
                }
                else {
                    pubnub.publish(password, "pass," + currentUser + "," + chanceNo, new Callback() {
                    });
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCity = in1.getText().toString();
                name = inputCity.toLowerCase();
                if (name.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
                    Intent in = getIntent();
                    in.putExtra("status",0);


                    overridePendingTransition(0, 0);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(in);
                } else {
                    GetCity getcitymethod = new GetCity();
                    getcitymethod.execute();
                }
            }
        });
    }

    class GetCity extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    // String response;
                    try {
                        // Building Parameters
                        //List<NameValuePair> params = new ArrayList<NameValuePair>();
                        // params.add(new BasicNameValuePair(TAG_NAME, name));
                        HashMap<String,String> params = new HashMap<String, String>();
                        params.put(TAG_NAME,name);
                        Log.e(TAG, name);
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request

                        JSONObject json = jParser.makeHttpRequest(
                                url, "GET", params);
                        Log.e("Get User Details", json.toString());
                        success = json.getInt(TAG_SUCCESS);
                        //JSONObject jsonObject=new JSONObject(json.toString());


                        // response = json.getString(TAG_NAME);
                        System.out.println("hello");
                        Log.e("Get User Details", json.toString());
                        System.out.println("hello");
                        //Log.isLoggable(TAG,success);
                        if (success == 1) {
                            System.out.println("hello");
                            Toast.makeText(getApplicationContext(), "got the city", Toast.LENGTH_LONG);

                            //  JSONArray places = json.getJSONArray(TAG_NAME); // JSON Array
                            // System.out.println(response);

                            // get first product object from JSON Array
                            //    JSONObject cityName = places.getJSONObject(0);
                            // System.out.println(cityName.get(TAG_NAME));


                            // Log.v(TAG,cityName.getString(TAG_NAME));

                            // int c=db.getCityCount(name);
                            int j=0,check=0;
                            System.out.println(count);
                            Iterator listIterator=allName.entrySet().iterator();
                            System.out.println(count);
                            System.out.println(allName.size());
                            while(listIterator.hasNext())
                            {
                                System.out.println(count);
                                Map.Entry pair = (Map.Entry)listIterator.next();
                                if(pair.getValue().equals(name)){

                                    check= 1;
                                    break;
                                }
                            }
                            while(listIterator.hasNext())
                            {


                                System.out.println(listIterator.next());
                            }
                            //Log.isLoggable(TAG, c);
                            //Cursor c=db.rawQuery("SELECT * FROM city WHERE name = '"+cityName.getString(TAG_NAME) +"'",null);
                            if(check==1){
                                Log.v(TAG,"checkin");
                                Toast.makeText(getApplicationContext(),"Place already used",Toast.LENGTH_SHORT).show();
                                Intent in=getIntent();
                                in.putExtra("status",0);
                                overridePendingTransition(0, 0);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(in);
                            }
                            else {
                                Log.v(TAG, "in");
                                allName.put(count, name);
                                System.out.println(name);
                                System.out.println(allName.values());
                               /* while(listIterator.hasNext())
                                {

                                    System.out.println(listIterator.next());
                                }*/
                                count++;
                                //db.addCity(c1);
                                //db.execSQL("INSERT INTO city VALUES(count,'" + cityName.getString(TAG_NAME) + "')");
                                Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                                chanceNo++;
                                if(chanceNo>chanceDecider.size()){
                                    chanceNo=1;
                                }
                                pubnub.publish(password, name + "," + chanceNo, new Callback() {
                                });

                                Intent in=getIntent();
                                in.putExtra("status",1);
                                overridePendingTransition(0, 0);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(in);
                                //go.setEnabled(false);
                            }
                            //Log.v(TAG, cityName.getString(TAG_NAME));
                            name="";
                        } else {
                            Toast.makeText(getApplicationContext(), "Place not found", Toast.LENGTH_SHORT).show();
                            Intent in=getIntent();
                            in.putExtra("status",0);
                            overridePendingTransition(0, 0);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(in);
                        }
                    } catch (JSONException e) {
                    }
                }
            });
            return null;
        }
    }
}
