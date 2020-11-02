package com.example.pro1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class speech_to_text extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView inputLang;
    private Spinner spinner;
    private ImageView imageView;
    private EditText et;
    private String selectedLang = "en_US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        inputLang = findViewById(R.id.inputLang);
        spinner = findViewById(R.id.spinner);
        imageView = findViewById(R.id.imageView);
        et = findViewById(R.id.et);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(speech_to_text.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedLang = "en_US";
                        break;
                    case 1:
                        selectedLang = "fr_FR";
                        break;
                    case 2:
                        selectedLang = "gsw_CH";
                        break;
                    case 3:
                        selectedLang = "hi_IN";
                        break;
                    case 4:
                        selectedLang = "it_IT";
                        break;
                    case 5:
                        selectedLang = "ja_JP";
                        break;
                    case 6:
                        selectedLang = "ko_KP";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLang);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et.setText(result.get(0));
                }
            }
            break;
        }
    }
}