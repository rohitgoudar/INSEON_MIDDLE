package com.appdeveloperrohitgmail.middle_layer;

/**
 * Created by Rohit Goudar on 06-07-2017.
 */

public class ResponseCheck1 {
    String response_entry_id,question_no,response_ques_id,response_text;
    public ResponseCheck1(String response_entry_id,String question_no, String response_ques_id, String response_text)
    {
        this.response_entry_id = response_entry_id;
        this.question_no = question_no;
        this.response_ques_id =response_ques_id;
        this.response_text = response_text;
    }
}
