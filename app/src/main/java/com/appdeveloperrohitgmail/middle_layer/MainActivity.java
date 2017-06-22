package com.appdeveloperrohitgmail.middle_layer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;

public class MainActivity extends AppCompatActivity {

    TextView q2;
    String high, low, medium,questionFirebase,question;
    String[] checkhFirebase, checklFirebase, checkmFirebase;
    Button next;
    Boolean internet;
    CheckBox checkBox[];
    int count_hFirebase, count_lFirebase, count_mFirebase,i,flag,checked_count;
    LinearLayout v2;
    SharedPreferences mySharedPref1,mySharedPref;
    SmileRating smileRating;
    DatabaseReference myRef;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v2=(LinearLayout)findViewById(R.id.v2);
        q2=(TextView)findViewById(R.id.q2);


        internet=haveNetworkConnection();
        smileRating=(SmileRating)findViewById(R.id.smiley_rating2);

        mySharedPref1 = getSharedPreferences("myData", Context.MODE_PRIVATE);

        question=mySharedPref1.getString("q1","false");

        if(internet)
        {
            storeFromFirebase();
            retriveFromShared();
            smilyRate();
        }
        else
        {
            retriveFromShared();
            smilyRate();
        }

        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent j =new Intent(MainActivity.this,Read.class);
                startActivity(j);*/
                onRespponse();
            }
        });
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public void storeFromFirebase()
    {

        if (internet)
        {Log.d("internet",""+internet);
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("userid1/surveys/survey1");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("ds1", "Value is: " + dataSnapshot.toString());


                    mySharedPref = getSharedPreferences("myData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPref.edit();


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        // q2.setText(dataSnapshot1.getValue().toString());
                        if (dataSnapshot1.getKey().equals("questions")) {
                            for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                if (dataSnapshot11.getKey().equals("q1")) {
                                    for (DataSnapshot dataSnapshot111 : dataSnapshot11.getChildren()) {
                                        if (dataSnapshot111.getKey().equals("question")) {
                                            questionFirebase = dataSnapshot111.getValue().toString();
                                            //q2.setText(questionFirebase);
                                            editor.putString("q1", questionFirebase);
                                        }
                                        if (dataSnapshot111.getKey().equals("high")) {
                                            high = (String) dataSnapshot111.getValue();
                                            Log.d("ds2", "high is: ." + high + ".");
                                        }
                                        if (dataSnapshot111.getKey().equals("low")) {
                                            low = dataSnapshot111.getValue().toString();
                                        }
                                        if (dataSnapshot111.getKey().equals("medium")) {
                                            medium = dataSnapshot111.getValue().toString();
                                        }
                                    }
                                }
                            }
                        }
                    }


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (dataSnapshot1.getKey().equals("options")) {
                            for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                Log.d("ds1", "datasnapshot 1 : " + high);
                                if (dataSnapshot11.getKey().equals(high)) {
                                    count_hFirebase = (int) dataSnapshot11.getChildrenCount();
                                    checkhFirebase = new String[count_hFirebase];
                                    editor.putInt("count_h",count_hFirebase);
                                    Log.d("count12345 inside",""+ count_hFirebase);
                                    i = 0;
                                    for (DataSnapshot dataSnapshot111 : dataSnapshot11.getChildren()) {
                                        checkhFirebase[i++] = dataSnapshot111.getValue().toString();

                                    }
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < count_hFirebase; i++) {
                                        sb.append(checkhFirebase[i]).append(",");
                                    }
                                    editor.putString("checkhFirebase", sb.toString());
                                }

                                if (dataSnapshot11.getKey().equals(low)) {
                                    count_lFirebase = (int) dataSnapshot11.getChildrenCount();
                                    checklFirebase = new String[count_lFirebase];
                                    i = 0;
                                    editor.putInt("count_l",count_lFirebase);
                                    for (DataSnapshot dataSnapshot111 : dataSnapshot11.getChildren()) {
                                        checklFirebase[i++] = dataSnapshot111.getValue().toString();
                                    }
                                    StringBuilder sb1 = new StringBuilder();
                                    for (int i = 0; i < count_lFirebase; i++) {
                                        sb1.append(checklFirebase[i]).append(",");
                                    }
                                    editor.putString("checklFirebase", sb1.toString());
                                }

                                if (dataSnapshot11.getKey().equals(medium)) {
                                    count_mFirebase = (int) dataSnapshot11.getChildrenCount();
                                    checkmFirebase = new String[count_mFirebase];
                                    i = 0;
                                    editor.putInt("count_m",count_mFirebase);
                                    for (DataSnapshot dataSnapshot111 : dataSnapshot11.getChildren()) {
                                        checkmFirebase[i++] = dataSnapshot111.getValue().toString();

                                    }
                                    StringBuilder sb2 = new StringBuilder();
                                    for (int i = 0; i < count_mFirebase; i++) {
                                        sb2.append(checkmFirebase[i]).append(",");
                                    }
                                    editor.putString("checkmFirebase", sb2.toString());
                                }
                            }
                        }
                    }
                    editor.apply();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("ds1", "Failed to read value.");

                }
            });
        }
    }

    public void retriveFromShared()
    {
        q2.setText(question);
        count_hFirebase = mySharedPref1.getInt("count_h",1);
        Log.d("count12345",""+ count_hFirebase);
        count_lFirebase = mySharedPref1.getInt("count_l",1);
        count_mFirebase = mySharedPref1.getInt("count_m",1);
        String check_h11=mySharedPref1.getString("checkhFirebase","");
        checkhFirebase = check_h11.split(",");
        String check_h12=mySharedPref1.getString("checklFirebase","");
        checklFirebase = check_h12.split(",");
        Log.d("count12345",""+ checklFirebase[0]);
        String check_h13=mySharedPref1.getString("checkmFirebase","");
        checkmFirebase = check_h13.split(",");
        Log.d("count12345",""+ checkmFirebase[1]);
    }
    public void smilyRate()
    {
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley) {
                int i=0;
                switch (smiley)
                {
                    case SmileRating.TERRIBLE:
                        flag=1;
                    case SmileRating.BAD:
                        v2.removeAllViews();
                        checkBox = new CheckBox[count_lFirebase];
                        for(String value: checklFirebase){
                            checkBox[i] = new CheckBox(MainActivity.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                            flag=2;

                            Log.d("count12345",""+ count_hFirebase);
                            Log.d("low check",""+ i);
                        }
                        break;
                    case SmileRating.GOOD:
                        flag=3;
                    case SmileRating.OKAY:
                        v2.removeAllViews();
                        checkBox = new CheckBox[count_mFirebase];
                        for(String value: checkmFirebase){
                            checkBox[i] = new CheckBox(MainActivity.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                            flag=4;
                            Log.d("medium check",""+ i);
                        }
                        break;
                    case SmileRating.GREAT:
                        v2.removeAllViews();
                        checkBox = new CheckBox[count_hFirebase];
                        for(String value: checkhFirebase){
                            checkBox[i] = new CheckBox(MainActivity.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                            flag=5;
                            Log.d("high check",""+ i);
                        }
                        break;
                }
            }
        });
    }
    public void onRespponse(){
        switch (flag)
        {
            case 1:
                checked_count = count_lFirebase;
                checkResponse(checked_count);
                break;
            case 2:
                checked_count = count_lFirebase;
                checkResponse(checked_count);
                break;
            case 3:
                checked_count = count_mFirebase;
                checkResponse(checked_count);
                break;
            case 4:
                checked_count = count_mFirebase;
                checkResponse(checked_count);
                break;
            case 5:
                checked_count = count_hFirebase;
                checkResponse(checked_count);
                break;
        }
    }
    public void checkResponse(int checked_count)
    {
        int j=0;
        for(j=0;j<checked_count;j++)
        {
            if(checkBox[j].isChecked())
            {
                Log.d("checkbox",""+j);
            }
        }
    }


}


/*if(dataSnapshot1.getKey().equals("high")){
                        countHigh =(int) dataSnapshot1.getChildrenCount();
                        high=new String[countHigh];
                        int i=0;
                        for(DataSnapshot snapshot:dataSnapshot1.getChildren()){
                            high[i++]=snapshot.getValue().toString();
                        }
                    }
                    if(dataSnapshot1.getKey().equals("low")){
                        countLow = (int) dataSnapshot1.getChildrenCount();
                        low=new String[countLow];
                        int i=0;
                        for(DataSnapshot snapshot:dataSnapshot1.getChildren()){
                            low[i++]=snapshot.getValue().toString();
                        }
                    }*/

// Log.d("DS","KEY: "+dataSnapshot1.getKey()+" \nvalue"+dataSnapshot1.getValue());

// Log.i("HIGH VALUES: ",high[1]);
// Log.i("LOW VALUES : ",low.toString());