package com.coollime.coollime;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coollime.coollime.db.RankDAO;

import java.util.Random;

public class G1to50Activity extends Activity implements View.OnClickListener,Runnable {

    private Button btns[] = new Button[25];
    private StopWatch sw = null;
    private int num[] = new int[25];
    private TextView time   = null;
    private RankDAO dao       = null;
    private Dialog  dlg = null;
    private int index = 1;
    private boolean is_start = false;
    private TextView result = null;
    String strTime = "000.00";
    String playerName = null;
    static Vibrator vibe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g1to50);
        Intent intent = getIntent();
        playerName=intent.getStringExtra("Name");
        index = 1;
        is_start = false;

        time = (TextView) findViewById(R.id.time);
        time.setTextColor(Color.RED);

        sw = new StopWatch();
        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Init();
        InitValue();
    }

    public void Init(){

        for(int i=0; i< 25; i++){
            btns[i] = (Button)findViewById(R.id.btn1+i);
            btns[i].setVisibility(View.VISIBLE);
            btns[i].setText(i + 1 +"");
            btns[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            btns[i].setOnClickListener(this);
            btns[i].setTextSize(20);

        }

    }

    public void InitValue(){
        index = 1;
        is_start = false;
        time.setText(R.string.init_time);
    }

    public void initNumberArr(int nStartNum){
        for(int i=0; i< 25; i++){
            num[i] = i+ nStartNum;
        }
    }

    public void shakeNumber(){

        int x   = 0;
        int y   = 0;
        int tmp = 0;

        Random _ran = new Random();

        for(int i=0; i< 100; i++){
            x = _ran.nextInt(24);
            y = _ran.nextInt(24);

            if(x == y) continue;
            tmp = num[x];
            num[x] = num[y];
            num[y] = tmp;
        }
    }

    public void onClick(View v) {

        if(!is_start && (v.equals(btns[0])||v.equals(btns[1])||v.equals(btns[2])||v.equals(btns[3])||v.equals(btns[4])
                ||v.equals(btns[5]) ||v.equals(btns[6])||v.equals(btns[7])||v.equals(btns[8])||v.equals(btns[9])
                ||v.equals(btns[10])||v.equals(btns[11])||v.equals(btns[12])||v.equals(btns[13])||v.equals(btns[14])
                ||v.equals(btns[15]) ||v.equals(btns[16])||v.equals(btns[17])||v.equals(btns[18])||v.equals(btns[19])
                ||v.equals(btns[20])||v.equals(btns[21])||v.equals(btns[22])||v.equals(btns[23])||v.equals(btns[24]))){
            initNumberArr(1);
            shakeNumber();

            for(int i=0; i< 25; i++) {
                btns[i].setText(num[i]+"");
                btns[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                btns[i].setTextSize(20);
            }

            is_start = !is_start;

            initNumberArr(26);
            shakeNumber();

            new Thread(this).start();
            sw.start();
        }

        if(is_start){
            vibe.vibrate(25);
        }

        int x = Integer.parseInt(((Button)v).getText().toString());

        if(x == index){
            if(index >= 26)
            {
                ((Button)v).setVisibility(View.INVISIBLE);

            }
            else{
                ((Button)v).setText(num[25-index]+"");

            }

            index++;
        }

        if(index == 51)
        {
            is_start = false;
            sw.stop();
            onSucc();
        }

    }

    public void onSucc() {
        Toast.makeText(getApplicationContext(),playerName+" : "+strTime, Toast.LENGTH_LONG).show();

        ShowDialog();

    }

    public void run() {

        while(is_start)
        {
            handler.sendEmptyMessage(0);

            try{
                Thread.sleep(50);
            }catch(Exception ig){
                ig.printStackTrace();
            }

        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            strTime = String.format("%.02f",sw.getFormatF());

            int nLen = strTime.length();

            if(nLen != 6)
                while(6 - nLen++ > 0)
                    strTime = "0" + strTime;

            time.setText(strTime);
            super.handleMessage(msg);
        }
    };



    public void ShowDialog(){
        dlg = new Dialog(this);
        dlg.setContentView(R.layout.dialog);
        dlg.setTitle("Result");
        result = (TextView)dlg.findViewById(R.id.result);
        String r = strTime;
        result.setText(r);

        dlg.show();
        sw.reset();
        InitValue();

    }



}
