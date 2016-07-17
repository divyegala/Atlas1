package com.example.divye.atlas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FirstPlayerGame extends Fragment {

    //SQLiteDatabase db;
    //DatabaseHandler db = new DatabaseHandler(this);
    //int allID = new int[100];
    static HashMap<Integer,String> allName = new HashMap<Integer,String>();
    public static HashMap<Integer,String> chanceDecider = new HashMap<>();
   public static HashMap<String,Integer> scoreAll= new HashMap<>();
    static Integer points=0;
    //ArrayList<String> a=new ArrayList<>();
    public static Integer yourChanceNo=0;
    JSONParser jParser = new JSONParser();
    Button go;
    Button pass;
    EditText in1;
    GetCity getcitymethod = null;
    private static String url = "http://192.168.0.107/android_connect/getname.php";
    Pubnub pubnub;
    String name,exit="exit_game";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG = "NewGame";
    private String password,list;
    static int chanceNo=1;
    static String content="";
    static Integer count = 0,kkk = 1;
    static int key=1,turn = 0;
    Integer person;
    static String username,receivedName,totalList,currentCity="",currentChance,passChance="pass",currentScore;
    static StringBuffer curCity = new StringBuffer("");
    View v;
    Activity a;
    int status;
    TextView score;
    int scr=0;
    public FirstPlayerGame() {
        // Required empty public constructor
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
         v =inflater.inflate(R.layout.activity_new_game, container, false);
         a=getActivity();
        go = (Button)v.findViewById(R.id.go);
        pass = (Button) v.findViewById(R.id.pass);
        score=(TextView) v.findViewById(R.id.text_score);
         Bundle extras=getArguments();
         int status=extras.getInt("status");
         person=extras.getInt("person");
        username=extras.getString("username");
        Toast.makeText(a,person.toString(),Toast.LENGTH_SHORT).show();
        if(person == 0 || status == 0) {
            go.setEnabled(true);
            pass.setEnabled(true);
            score.setText("Current User: "+username);
            tabbed.isTurn=true;
            yourChanceNo=1;
        }
        if(person == 2) {
            go.setEnabled(false);
            pass.setEnabled(false);
            score.setText("");
            tabbed.isTurn=false;
        }

        //a=extras.getStringArrayList("chances");
        //list=extras.getString("chance");
        totalList=extras.getString("totalList");
        password=extras.getString("password");
        username=extras.getString("username");

        pubnub = new Pubnub("pub-c-4aeb3dae-c48d-4f23-b3b2-e54d08bd88ce", "sub-c-fe640624-e5fa-11e5-aad5-02ee2ddab7fe");
        final TextView textView = (TextView)v.findViewById(R.id.textView4);
        textView.setText(currentCity);

        if(turn == 0 && person==0){
            char a[]=totalList.toCharArray();
            String n1="";
            for (int i = 0; i < a.length; i++) {
                while (a[i] != ',') {
                    n1 = n1 + a[i++];
                }
                chanceDecider.put(kkk,n1);
                kkk++;
                n1="";
                //turn++;
            }
            pubnub.publish(password, totalList, new Callback() {
            });
            try {
                Thread.sleep(1000);
            }
            catch (Exception e){

            }
            turn++;
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
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentCity="";
                                        currentChance="";
                                        currentScore="";
                                        int i=0;
                                            while (receivedName.charAt(i) != ',') {
                                                currentCity = currentCity + receivedName.charAt(i++);
                                            }
                                        i++;
                                        Toast.makeText(a,currentCity,Toast.LENGTH_SHORT).show();
                                        if(!currentCity.equals(passChance) && !currentCity.equals(exit)) {
                                           // Toast.makeText(getApplicationContext(),chanceDecider.get(chanceNo)+","+username,Toast.LENGTH_SHORT).show();
                                            textView.setText(currentCity);

                                            allName.put(count, currentCity);

                                            while(receivedName.charAt(i) != ','){
                                                currentChance = currentChance + receivedName.charAt(i++);
                                            }
                                            i++;
                                            while(i<receivedName.length()){
                                                currentScore = currentScore + receivedName.charAt(i++);
                                            }
                                            chanceNo = Integer.parseInt(currentChance);
                                            int scoreNow = Integer.parseInt(currentScore);
                                            int scoreUser = chanceNo-1;
                                            if(scoreUser==0){
                                                scoreUser = chanceDecider.size();
                                            }
                                            scoreAll.put(chanceDecider.get(scoreUser),scoreNow);
                                            count++;
                                            if (chanceDecider.get(chanceNo).equals(username)) {
                                                go.setEnabled(true);
                                                pass.setEnabled(true);
                                                score.setText("Current User: "+username);
                                                tabbed.isTurn=true;
                                                yourChanceNo=chanceNo;
                                            } else {
                                                go.setEnabled(false);
                                                pass.setEnabled(false);
                                                score.setText("");
                                                tabbed.isTurn=false;
                                            }
                                        }
                                        else if(currentCity.equals(exit)){
                                            while(i<receivedName.length()){
                                                currentChance = currentChance + receivedName.charAt(i++);
                                            }
                                            chanceNo = Integer.parseInt(currentChance);
                                            //chanceDecider.remove(chanceNo);
                                            if(yourChanceNo!=chanceNo){
                                                if(chanceNo<chanceDecider.size()){
                                                    for(int l=chanceNo;l<chanceDecider.size();l++){
                                                        chanceDecider.put(l,chanceDecider.get(l+1));
                                                        chanceDecider.remove(l+1);
                                                    }
                                                }
                                                else if(chanceNo==chanceDecider.size()){
                                                    chanceDecider.remove(chanceNo);
                                                }
                                                if(chanceNo>chanceDecider.size())
                                                    chanceNo=1;
                                                Toast.makeText(a,"Now:"+chanceDecider.get(chanceNo),Toast.LENGTH_SHORT).show();
                                                if (chanceDecider.get(chanceNo).equals(username)) {
                                                    go.setEnabled(true);
                                                    pass.setEnabled(true);
                                                    score.setText("Current User: "+username);
                                                    tabbed.isTurn=true;
                                                    //yourChanceNo=chanceNo;
                                                } else {
                                                    go.setEnabled(false);
                                                    pass.setEnabled(false);
                                                    score.setText("");
                                                    tabbed.isTurn=false;
                                                }
                                            }
                                        }
                                        else{
                                            //content=textView.getText().toString();
                                           //Toast.makeText(getApplicationContext(),"Else",Toast.LENGTH_SHORT).show();
                                            //if(!content.isEmpty() && content!=null)
                                                currentCity="";
                                                Toast.makeText(a,receivedName,Toast.LENGTH_SHORT).show();
                                                i=0;
                                                while (receivedName.charAt(i) != ',') {
                                                    i++;
                                                    // currentCity = currentCity + receivedName.charAt(i++);
                                                }
                                                i++;
                                                while (receivedName.charAt(i) != ',') {
                                                    currentCity = currentCity + receivedName.charAt(i++);
                                                }
                                                i++;
                                                while (receivedName.charAt(i) != ',') {
                                                    currentChance = currentChance + receivedName.charAt(i++);
                                                }
                                                i++;
                                                while(i < receivedName.length()) {

                                                    currentScore = currentScore + receivedName.charAt(i++);
                                                }
                                                    chanceNo = Integer.parseInt(currentChance);
                                                Toast.makeText(a,currentCity,Toast.LENGTH_SHORT).show();
                                                textView.setText(currentCity);

                                                allName.put(count, currentCity);
                                                chanceNo = Integer.parseInt(currentChance);
                                                int scoreNow = Integer.parseInt(currentScore);
                                                int scoreUser = chanceNo-1;
                                                if(scoreUser==0){
                                                    scoreUser = chanceDecider.size();
                                                }
                                                scoreAll.put(chanceDecider.get(scoreUser),scoreNow);

                                                count++;
                                                if (chanceDecider.get(chanceNo).equals(username)) {
                                                    pass.setEnabled(true);
                                                    go.setEnabled(true);
                                                    score.setText("Current User: "+username);
                                                    tabbed.isTurn=true;
                                                } else {
                                                    go.setEnabled(false);
                                                    pass.setEnabled(false);
                                                    score.setText("");
                                                    tabbed.isTurn=false;
                                                }

                                        }
                                    }
                                });

                            }
                            else  if(turn==0 && person==2) {
                                list = message.toString();

                                final char b[] = list.toCharArray();
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(a,"List: "+list,Toast.LENGTH_SHORT).show();
                                        String n1 = "";
                                        for (int i = 0; i < b.length; i++) {
                                            while (b[i] != ',') {
                                                n1 = n1 + b[i++];
                                            }
                                            chanceDecider.put(kkk,n1);
                                            //score.put(n1,0);
                                            if(n1.equals(username)){
                                                yourChanceNo=kkk;
                                            }
                                            kkk++;
                                            n1="";
                                        }
                                        turn++;
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

        in1 = (EditText)v. findViewById(R.id.input_city);

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
                points=-10;
                scoreAll.put(username,points);

                Toast.makeText(a,points.toString(),Toast.LENGTH_SHORT).show();

                if(content.isEmpty() || content==null){

                    pubnub.publish(password,"pass,"+chanceNo+","+points, new Callback() {
                    });
                }
                else {
                    pubnub.publish(password, "pass," + content + "," + chanceNo+","+points, new Callback() {
                   });
                }
                in1.setText("");

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCity = in1.getText().toString();
                name = inputCity.toLowerCase();
                int check1=0,check2=0;
                content=textView.getText().toString();
                if (name.length() < 3) {
                    Toast.makeText(a, "Incorrect input", Toast.LENGTH_SHORT).show();
                    in1.setText("");
                    check1=1;

                }
                if(!content.isEmpty() && name.charAt(0)!=content.charAt(content.length()-1)){
                    Toast.makeText(a, "Last letter should match first letter of received place.", Toast.LENGTH_SHORT).show();
                    in1.setText("");
                    check2=1;
                }
                if(check1!=1 && check2!=1) {
                    getcitymethod = new GetCity();
                    getcitymethod.execute();
                }
            }
        });
        return v;
    }


    class GetCity extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {

            a.runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    Integer success;
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
                        Toast.makeText(a,"success:"+success.toString(),Toast.LENGTH_SHORT).show();

                        //String responseCity = json.getString(TAG_NAME);
                        System.out.println("hello");
                        Log.e("Get User Details", json.toString());
                        System.out.println("hello");
                        //Log.isLoggable(TAG,success);
                        if (success == 1) {
                            System.out.println("hello");
                           // Toast.makeText(getApplicationContext(), "got the city", Toast.LENGTH_LONG);

                            //  JSONArray places = json.getJSONArray(TAG_NAME); // JSON Array
                            // System.out.println(response);

                            // get first product object from JSON Array
                            //    JSONObject cityName = places.getJSONObject(0);
                            // System.out.println(cityName.get(TAG_NAME));


                            // Log.v(TAG,cityName.getString(TAG_NAME));

                            // int c=db.getCityCount(name);
                            int j=0,check=0;
                           // System.out.println(count);
                            Iterator listIterator=allName.entrySet().iterator();
                            //System.out.println(count);
                            //System.out.println(allName.size());
                            while(listIterator.hasNext())
                            {
                                //System.out.println(count);
                                Map.Entry pair = (Map.Entry)listIterator.next();
                                if(pair.getValue().equals(name)){

                                    check= 1;
                                    break;
                                }
                            }
                            while(listIterator.hasNext())
                            {


                                //System.out.println(listIterator.next());
                            }
                            //Log.isLoggable(TAG, c);
                            //Cursor c=db.rawQuery("SELECT * FROM city WHERE name = '"+cityName.getString(TAG_NAME) +"'",null);
                            if(check==1){
                                Log.v(TAG,"checkin");
                                Toast.makeText(a,"Place already used",Toast.LENGTH_SHORT).show();
                                in1.setText("");
                                getcitymethod.cancel(true);
                                /*Intent in=getIntent();
                                in.putExtra("status",0);
                                in.putExtra("person",1);
                                overridePendingTransition(0, 0);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(in);*/
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
                                Toast.makeText(a, "Correct!", Toast.LENGTH_SHORT).show();
                                points=points+10;
                                scoreAll.put(username,points);
                                //score.setText("Score :"+scr);
                                chanceNo++;
                                if(chanceNo>chanceDecider.size()){
                                    chanceNo=1;
                                }
                               // go.setEnabled(false);
                               // pass.setEnabled(false);
                               in1.setText("");
                                pubnub.publish(password, name + "," + chanceNo+","+points, new Callback() {
                                });
                                getcitymethod.cancel(true);
                               if( getcitymethod.isCancelled());
                                   //Toast.makeText(a,"true",Toast.LENGTH_SHORT).show();
                               /*Intent in=getIntent();
                                in.putExtra("status",1);
                                in.putExtra("person",1);
                                overridePendingTransition(0, 0);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(in);*/

                            }
                            //Log.v(TAG, cityName.getString(TAG_NAME));
                            name="";
                        } else {
                            Toast.makeText(a, "Place not found", Toast.LENGTH_SHORT).show();
                            in1.setText("");
                            getcitymethod.cancel(true);
                            /*Intent in=getIntent();
                            in.putExtra("status",0);
                            in.putExtra("person",1);
                            overridePendingTransition(0, 0);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(in);*/
                        }
                    } catch (JSONException e) {
                    }
                }
            });
            return null;
        }
    }
}
