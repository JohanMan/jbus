package com.johan.jbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bus.INSTANCE.register(this);
        Bus.INSTANCE.post("test2", 25);
        input = (EditText) findViewById(R.id.input);
        post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus.INSTANCE.post("test1", input.getText().toString());
            }
        });
    }

    @Subscribe("test1")
    public void test1(String name) {
        Log.e(getClass().getName(), "name : " + name);
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
    }

    @Subscribe("test2")
    public void test2(int age) {
        Log.e(getClass().getName(), "age : " + age);
        Toast.makeText(this, String.valueOf(age), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        Bus.INSTANCE.unregister(this);
        super.onDestroy();
    }

}
