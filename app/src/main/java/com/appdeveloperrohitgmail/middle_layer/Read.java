package com.appdeveloperrohitgmail.middle_layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hsalf.smilerating.SmileRating;


/**
 * Created by Rohit Goudar on 13-06-2017.
 */

public class Read extends AppCompatActivity {

    TextView q2;
    LinearLayout v2;
    String[] checkh,checkl,checkm;
    CheckBox checkBox[];

    SharedPreferences mySharedPref1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        q2=(TextView)findViewById(R.id.q2);
        v2=(LinearLayout)findViewById(R.id.v2);


        mySharedPref1 = getSharedPreferences("myData", Context.MODE_PRIVATE);

        String question=mySharedPref1.getString("q1","");
        q2.setText(question);
        String check_h11=mySharedPref1.getString("checkhFirebase","");
        checkh = check_h11.split(",");
        String check_h12=mySharedPref1.getString("checklFirebase","");
        checkl = check_h12.split(",");
        Log.d("count12345",""+checkl[0]);
        String check_h13=mySharedPref1.getString("checkmFirebase","");
        checkm = check_h13.split(",");
                

        /*mySharedPref1.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        });
*/


        final SmileRating smileRating=(SmileRating)findViewById(R.id.smiley_rating2);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley) {
                int i=0;
                switch (smiley)
                {
                    case SmileRating.TERRIBLE:
                    case SmileRating.BAD:
                        v2.removeAllViews();

                        checkBox = new CheckBox[checkl.length];
                        for(String value:checkl){
                            checkBox[i] = new CheckBox(Read.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                        }
                        break;
                    case SmileRating.GOOD:
                    case SmileRating.OKAY:
                        v2.removeAllViews();
                        checkBox = new CheckBox[checkm.length];
                        for(String value:checkm){
                            checkBox[i] = new CheckBox(Read.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                        }
                        break;
                    case SmileRating.GREAT:
                        v2.removeAllViews();
                        checkBox = new CheckBox[checkh.length];
                        for(String value:checkh){
                            checkBox[i] = new CheckBox(Read.this);
                            checkBox[i].setText(value);
                            checkBox[i].setId(i);
                            v2.addView(checkBox[i]);
                            i++;
                        }
                        break;
                }
            }
        });
    }
}




/*String fileName = "/sdcard/;
    FileInputStream fis = openFileInput(fileName);

    InputStreamReader isr = new InputStreamReader(fis);
    BufferedReader streamReader = new BufferedReader(isr);

    StringBuilder responseStrBuilder = new StringBuilder();

    public String inputStr;
while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
    JSONObject jsonobj = new JSONObject(responseStrBuilder.toString());
streamReader.close();
isr.close();
fis.close();*/