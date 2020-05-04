package com.myapp.maddy.mybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Maddy on 5/3/2020.
 */

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2){
            if(data.getStringExtra("result").equals("yes")){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else if(data.getStringExtra("result").equals("no")){
                Intent intent = new Intent(this,FirstPage.class);
                startActivity(intent);
                finish();
            }else if(data.getStringExtra("result").equals("back")){
                finish();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,FingerprintAuth.class);
        startActivityForResult(intent,2);
    }
}
