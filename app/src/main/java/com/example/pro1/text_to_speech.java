package com.example.pro1;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Locale;

public class text_to_speech extends AppCompatActivity {

    private String text, translatedString, str1, str2, inputLanguage;
    private Boolean flag = false;
    private Button speak, translate;
    private EditText et;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        speak = findViewById(R.id.speak);
        et = findViewById(R.id.et);
        translate = findViewById(R.id.translate);
        final Spinner s1 = findViewById(R.id.spinner1);
        final Spinner s2 = findViewById(R.id.spinner2);
        text = et.getText().toString();

        //translatedString = text;

        final String[] outputLanguage = new String[1];
        final Locale[] loc = new Locale[1];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(text_to_speech.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s2.setAdapter(adapter);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    tts.setLanguage(new Locale("en_US"));
            }
        });

       /* tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    tts.setLanguage(new Locale("en_US"));
            }
        });
*/
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(et.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                str1 = String.valueOf(s1.getSelectedItem());
                str2 = String.valueOf(s2.getSelectedItem());

                if (str1.equalsIgnoreCase("English"))
                    inputLanguage = TranslateLanguage.ENGLISH;
                else if (str1.equalsIgnoreCase("French"))
                    inputLanguage = TranslateLanguage.FRENCH;
                else if (str1.equalsIgnoreCase("German"))
                    inputLanguage = TranslateLanguage.GERMAN;
                else if (str1.equalsIgnoreCase("Hindi"))
                    inputLanguage = TranslateLanguage.HINDI;
                else if (str1.equalsIgnoreCase("Italian"))
                    inputLanguage = TranslateLanguage.ITALIAN;
                else if (str1.equalsIgnoreCase("Japanese"))
                    inputLanguage = TranslateLanguage.JAPANESE;
                else if (str1.equalsIgnoreCase("Korean"))
                    inputLanguage = TranslateLanguage.KOREAN;

                if (str2.equalsIgnoreCase("English")) {
                    loc[0] = new Locale("en_US");
                    outputLanguage[0] = TranslateLanguage.ENGLISH;
                } else if (str2.equalsIgnoreCase("French")) {
                    loc[0] = new Locale("fr_FR");
                    outputLanguage[0] = TranslateLanguage.FRENCH;
                } else if (str2.equalsIgnoreCase("German")) {
                    loc[0] = new Locale("gsw_CH");
                    outputLanguage[0] = TranslateLanguage.GERMAN;
                } else if (str2.equalsIgnoreCase("Hindi")) {
                    loc[0] = new Locale("hi_IN");
                    outputLanguage[0] = TranslateLanguage.HINDI;
                } else if (str2.equalsIgnoreCase("Italian")) {
                    loc[0] = new Locale("it_IT");
                    outputLanguage[0] = TranslateLanguage.ITALIAN;
                } else if (str2.equalsIgnoreCase("Japanese")) {
                    loc[0] = new Locale("ja_JP");
                    outputLanguage[0] = TranslateLanguage.JAPANESE;
                } else if (str2.equalsIgnoreCase("Korean")) {
                    loc[0] = new Locale("ko_KP");
                    outputLanguage[0] = TranslateLanguage.KOREAN;
                }

                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS)
                            tts.setLanguage(loc[0]);
                    }
                });

                TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(inputLanguage).setTargetLanguage(outputLanguage[0]).build();
                final Translator translator = Translation.getClient(options);

                DownloadConditions conditions = new DownloadConditions.Builder().build();
                translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(text_to_speech.this, "Couldn't download the model", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                translator.translate(et.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        et.setText(s);
                        translatedString = s;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(text_to_speech.this, "Downloading Model. Please Wait...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}