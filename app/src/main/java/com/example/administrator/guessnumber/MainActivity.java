package com.example.administrator.guessnumber;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView log,mesg;
    private EditText input;
    private Button guess;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.log);
        input = findViewById(R.id.input);
        guess = findViewById(R.id.guess);
        mesg = findViewById(R.id.mesg);
        //Android button只能有一個listener 所以用setXXX
        guess.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doGuess();
            }
        });

        mesg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesg.setVisibility(View.GONE);
            }
        });

        initGame();
    }

    private void initGame(){
        answer = createAnswer(3);
        answer = "123";
        input.setText("");
        log.setText("");
    }

    private void doGuess(){
        String strInput = input.getText().toString();
        //log.setText(strInput);

        input.setText("");

        String result = checkAB(answer,strInput);

        log.append(strInput+":"+result+"\n");

        //Log.v("maggie",createAnswer(4));

        if (result.equals("3A0B")) {
            showDialog();
        }
    }

    private void showDialog(){
        //mesg.setVisibility(View.VISIBLE);

        //利用AlertDialog.Builder
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WINNER");
        builder.setMessage("恭喜猜對了!!");

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });
        alert = builder.create();
        alert.show();
    }

    public void end(View view) {
        finish();
    }


    //onClickListener 匿名物件 也可以寫一個類別 並new出來
    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    static String createAnswer(int d) {
        //隨機亂數＝撲克發牌v1
        int[] poker = new int[10];
        for( int i = 0 ; i<poker.length ; i++ ) {
            poker[i] = i ;
        }
        for( int i = poker.length ; i>0 ; i-- ) {
            int rand = (int)(Math.random() * i);	// rand = 0~9 (index)
            //交換 => last:i-1
            int temp = poker[rand];
            poker[rand] = poker[i-1];
            poker[i-1] = temp;
        }

        //字串相加
        String ret = "";
        for (int i=0; i<d ; i++ ) {
            ret += poker[i];
        }

        return ret;
    }

    static String checkAB(String a,String b) {
        int A = 0, B= 0 ;

        //String 的長度是方法所以加()
        //charAt , indexOf
        for ( int i=0; i<a.length() ; i++ ) {
            if(b.charAt(i) == a.charAt(i)) {   //if b的第i字元　是否與a的i第字元　相同
                A++;
            }else if(a.indexOf(b.charAt(i)) != -1) {	// b的第i字元　是否存在於a中
                B++;
            }
        }
        return A+"A"+B+"B";
    }

}
