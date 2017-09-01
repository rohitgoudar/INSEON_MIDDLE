package com.appdeveloperrohitgmail.middle_layer;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
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


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Boolean internet;
    Button save,submit;
    CheckBox checkBox[];
    int i,j,flag;
    int count_hFirebase, count_lFirebase, count_mFirebase,checked_count,response_count,response_count_shared,response_count_temp,response_No;
    int response_enrty_no;


    String high, low, medium,questionFirebase,question,questionNo="1",respoString;
    String[] checkhFirebase, checklFirebase, checkmFirebase;
    String[] response_entry_id = new String[200],response_id = new String[200],DateToStr = new String[200],response_text= new String[200],response_ques_id = new String[200],response_option = new String[200];
    String[] response_entry_id_shared = new String[200],response_id_shared = new String[200],DateToStr_shared = new String[200],response_text_shared= new String[200],response_option_shared = new String[200];
    StringBuilder resp;
    TextView q2;

    LinearLayout v2;
    SmileRating smileRating;
    SharedPreferences mySharedPref1,mySharedPref,mySharedRespo,mySharedRespo1,mySharedRespoEntry,mySharedRespoEntry1;

    DatabaseReference myRef,myrefResp,myrefRespEntry;
    FirebaseDatabase database,databaserespo;

    ResponseCheck responseCheck;
    ResponseCheck1 responseCheck1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v2=(LinearLayout)findViewById(R.id.v2);
        q2=(TextView)findViewById(R.id.q2);

        database = FirebaseDatabase.getInstance();
        databaserespo = FirebaseDatabase.getInstance();
        internet = haveNetworkConnection();
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



        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent j =new Intent(MainActivity.this,Read.class);
                startActivity(j);*/

                onRespponse();

                mySharedRespo = getSharedPreferences("response",Context.MODE_PRIVATE);
                SharedPreferences.Editor editorRespo = mySharedRespo.edit();
                response_count = mySharedRespo.getInt("response_count",1);
                response_count_temp = mySharedRespo.getInt("response_count_temp",1);

                if(response_count==0) {
                    response_count = 1;
                }
                else{
                    response_count +=1;
                }
                if(response_count_temp==0) {
                    response_count_temp = 1;
                }
                else{
                    response_count_temp +=1;
                }
                response_id[response_count]="resp"+response_count;
                response_option[response_count]=String.valueOf(flag);

                Date curDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss ");
                DateToStr[response_count] = format.format(curDate);
                System.out.println(DateToStr[response_count]);
                Log.d("date123",DateToStr[response_count]);
                Log.d("date123",response_id[response_count]);
                //Log.d("date123",response_id[10]);
                /*response_count=0;
                response_enrty_no=0;*/
                editorRespo.putString("response_option"+response_count,response_option[response_count]);
                editorRespo.putInt("response_count",response_count);
                editorRespo.putInt("response_count_temp",response_count_temp);
                editorRespo.putString("response_id"+response_count,response_id[response_count]);
                editorRespo.putString("time"+response_count,DateToStr[response_count]);
                editorRespo.apply();


                Log.d("date123","count"+response_count);
                Log.d("responseoption1",""+response_option[response_count]);
                /*responseCheck = new ResponseCheck(response_id,DateToStr);
                myrefResp = databaserespo.getReference("userid1/surveys/survey1/responses/");
                myrefResp.child("2").setValue(responseCheck);*/
            }
        });

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //submiting the first part of the response

                mySharedRespo1 = getSharedPreferences("response",Context.MODE_PRIVATE);
                mySharedRespoEntry1 = getSharedPreferences("responseEntry",Context.MODE_PRIVATE);

                myrefResp = databaserespo.getReference("userid1/surveys/survey1/responses/");
                response_count_shared=mySharedRespo1.getInt("response_count",1);
                for(j=1;j<response_count_shared;j++)
                {
                    response_option_shared[j] = mySharedRespoEntry1.getString("response_option"+j,"1");
                    response_id_shared[j] = mySharedRespo1.getString("response_id" + j, "");
                    DateToStr_shared[j] = mySharedRespo1.getString("time" + j, "");
                    responseCheck = new ResponseCheck(response_id_shared[j], DateToStr_shared[j]);

                    /*response_id_shared[response_count] = mySharedRespo1.getString("response_id" + response_count, "");
                    DateToStr_shared[response_count] = mySharedRespo1.getString("time" + response_count, "");
                    responseCheck = new ResponseCheck(response_id_shared[response_count], DateToStr_shared[response_count]);*/

                    myrefResp.child(response_id_shared[j]).child("q"+questionNo).setValue("response_input : "+response_option_shared[j]);  //enter the database for response child
                    myrefResp.child(response_id_shared[j]).child("Time").setValue( DateToStr_shared[j]);

                    Log.d("time456re", "" + j);
                    Log.d("time456id", response_id_shared[j]);
                    Log.d("time456date", DateToStr_shared[j]);

                }

                // submiting the second part of the response
                myrefRespEntry = databaserespo.getReference("userid1/surveys/survey1/responses_entry/");
                mySharedRespoEntry1 = getSharedPreferences("responseEntry",Context.MODE_PRIVATE);
                response_enrty_no = mySharedRespoEntry1.getInt("response_enrty_no",1);
                for(int k=1;k<=response_enrty_no;k++)
                {
                    response_entry_id[k] = mySharedRespoEntry1.getString("response_entry_id"+k,"");

                    response_ques_id[k] = mySharedRespoEntry1.getString("response_ques_id"+k,"");
                    response_text[k]= mySharedRespoEntry1.getString("response_text"+k,"");

                    responseCheck1 =new ResponseCheck1(response_entry_id[k],questionNo,response_ques_id[k],response_text[k]);
                    myrefRespEntry.child("response"+k).setValue(responseCheck1);
                }

            response_count_temp = 0;
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

            myRef = database.getReference("userid1/surveys/survey1");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("ds1", "Value is: " + dataSnapshot.toString());


                    mySharedPref = getSharedPreferences("myData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPref.edit();


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


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
                                            editor.putString("option_high",high);
                                            Log.d("ds2", "high is: ." + high + ".");
                                        }
                                        if (dataSnapshot111.getKey().equals("low")) {
                                            low = dataSnapshot111.getValue().toString();
                                            editor.putString("option_low",low);
                                        }
                                        if (dataSnapshot111.getKey().equals("medium")) {
                                            medium = dataSnapshot111.getValue().toString();
                                            editor.putString("option_medium",medium);
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
        mySharedRespoEntry = getSharedPreferences("responseEntry",Context.MODE_PRIVATE);
        SharedPreferences.Editor editorRespoEntry = mySharedRespoEntry.edit();
        response_enrty_no = mySharedRespoEntry.getInt("response_enrty_no",1);
        resp = new StringBuilder();
        int j=0;
        for(j=0;j<checked_count;j++)
        {
            if(checkBox[j].isChecked())
            {
                resp.append(j+1).append(",");
                response_text[j+1]= checkBox[j].getText().toString();
                response_ques_id[j+1] = ""+flag+"."+j;

                if(response_enrty_no == 0)
                {  response_enrty_no=1; }
                else{  response_enrty_no += 1;  }

                response_text[response_enrty_no]= checkBox[j].getText().toString();
                response_ques_id[response_enrty_no] = ""+flag+"."+(j+1);
                response_entry_id[response_enrty_no] = "resp_ent_"+response_enrty_no;
                response_count_temp +=1;
                //response_enrty_no =0;
                editorRespoEntry.putInt("response_enrty_no",response_enrty_no);
                editorRespoEntry.putString("response_ques_id"+response_enrty_no,response_ques_id[response_enrty_no]);
                editorRespoEntry.putString("response_entry_id"+response_enrty_no,response_entry_id[response_enrty_no]);
                editorRespoEntry.putString("response_text"+response_enrty_no,response_text[response_enrty_no]);
                editorRespoEntry.apply();
            }
        }
        respoString = resp.toString();
        response_option[response_count]=String.valueOf(flag);
        editorRespoEntry.putString("response_option"+response_count,response_option[response_count]);
        editorRespoEntry.apply();
        Log.d("responseoption",""+response_option[response_count]);
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