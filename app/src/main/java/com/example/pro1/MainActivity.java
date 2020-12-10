package com.example.pro1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView img_text, text_speech, speech_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_text = findViewById(R.id.img_text);
        text_speech = findViewById(R.id.text_speech);
        speech_text = findViewById(R.id.speech_text);


        img_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, img_to_text.class);
                startActivity(intent);
            }
        });

        text_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, text_to_speech.class);
                startActivity(intent);
            }
        });

        speech_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, speech_to_text.class);
                startActivity(intent);
            }
        });


    }
}