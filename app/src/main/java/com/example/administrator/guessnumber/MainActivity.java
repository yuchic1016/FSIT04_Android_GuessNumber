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
    private EditText input;
    private Button guess , setting;
    private TextView log, mesg;
    private String answer;
    private int guesscount;
    private int guessdigit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //log - id卻不是抓log 不會提醒錯 但會runtime error
        log = findViewById(R.id.log);
        input = findViewById(R.id.input);
        guess = findViewById(R.id.guess);
        mesg = findViewById(R.id.mesg);
        setting = findViewById(R.id.setting);

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

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSetting();
            }
        });

        initGame();
        guessdigit = 4;
    }

    private void initGame(){
        answer = createAnswer(guessdigit);
        input.setText("");
        log.setText("");
        guesscount = 1;
        //answer = "123";
        Log.v("answerIs", answer);
    }

    private void doSetting(){
        showSettingDialog();
    }

    private void doGuess(){
        String strInput = input.getText().toString();
        //log.setText(strInput);

        if(strInput.length() != guessdigit){
            Log.v("worng guess digit",""+strInput.length());
            showWrongInputDialog();
        }else{
            input.setText("");

            String result = checkAB(answer,strInput);

            log.append("猜第"+guesscount+"次:"+strInput+":"+result+"\n");

            //Log.v("maggie",createAnswer(4));
            guesscount++;

            if (guesscount > 10) {
                showLossDialog();
            }

            if (result.equals(guessdigit+"A0B")) {
                showWinDialog();
            }
        }



    }
    private void showLossDialog(){
        AlertDialog alert = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LOSER..");
        builder.setMessage("答案是:" + answer );

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
    private void showWinDialog(){
        //mesg.setVisibility(View.VISIBLE);

        //設計對話框 (不在版面設計) (沒有設計UI - 使用google原生的UI ,依手機不同長相不同)
        //利用AlertDialog的內部類別Builder 做出來 (活在顯示在this的Context)
        //類似js. alert
        AlertDialog alert = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WINNER");
        builder.setMessage("恭喜猜對了!!"+" 答案是:" + answer);

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });

        //AlertDialog alert 用builder create出來
        alert = builder.create();
        alert.show();
    }

    private void showSettingDialog(){
        AlertDialog alert = null;
        final CharSequence[] array = {"3","4","5"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("設定猜數字位數:");

        builder.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("chooseDigit", ""+array[which]);
                guessdigit = Integer.parseInt(array[which].toString());

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });

        //AlertDialog alert 用builder create出來
        alert = builder.create();
        alert.show();

    }

    private void showWrongInputDialog(){
        AlertDialog alert = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Wrong Number..");
        builder.setMessage("請輸入正確位數:" + guessdigit );

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input.setText("");
            }
        });

        alert = builder.create();
        alert.show();
    }


    //onClickListener匿名物件 (另法:也可以寫一個類別 並new出來 )
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

    public void reset(View view) {
        initGame();
    }

    public void end(View view) {
        finish();
    }

}
