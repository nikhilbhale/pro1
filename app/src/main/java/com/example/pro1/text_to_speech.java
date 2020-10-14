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

    String text, translatedString, str1, str2;
    private Button etb, cb;
    private EditText et;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);


        etb = findViewById(R.id.etb);
        et = findViewById(R.id.et);
        cb = findViewById(R.id.cb);
        final Spinner s1 = findViewById(R.id.spinner1);
        final Spinner s2 = findViewById(R.id.spinner2);
        text = et.getText().toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(text_to_speech.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s2.setAdapter(adapter);

       /* str1 = s1.getSelectedItem().toString();
        str2 = s1.getSelectedItem().toString();
*/
        str1 = String.valueOf(s1.getSelectedItem());
        str2 = String.valueOf(s2.getSelectedItem());

        final String outputLanguage;
        final Locale loc;

        switch (str2) {
            case "English":
                loc = new Locale("en_US");
                outputLanguage = TranslateLanguage.ENGLISH;
                break;

            case "French":
                loc = new Locale("fr_FR");
                outputLanguage = TranslateLanguage.FRENCH;
                break;

            case "German":
                loc = new Locale("gsw_CH");
                outputLanguage = TranslateLanguage.GERMAN;
                break;

            case "Hindi":
                loc = new Locale("hi_IN");
                outputLanguage = TranslateLanguage.HINDI;
                break;

            case "Italian":
                loc = new Locale("it_IT");
                outputLanguage = TranslateLanguage.ITALIAN;
                break;

            case "Japanese":
                loc = new Locale("ja_JP");
                outputLanguage = TranslateLanguage.JAPANESE;
                break;

            case "Korean":
                loc = new Locale("ko_KP");
                outputLanguage = TranslateLanguage.KOREAN;
                break;

            default:
                loc = new Locale("en_US");
                outputLanguage = TranslateLanguage.ENGLISH;
                break;
        }

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    tts.setLanguage(loc);
                }
            }
        });


        etb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tts.speak(translatedString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputLanguage;
                switch (str1) {
                    case "English":
                        inputLanguage = TranslateLanguage.ENGLISH;
                        break;

                    case "French":
                        inputLanguage = TranslateLanguage.FRENCH;
                        break;

                    case "German":
                        inputLanguage = TranslateLanguage.GERMAN;
                        break;

                    case "Hindi":
                        inputLanguage = TranslateLanguage.HINDI;
                        break;

                    case "Italian":
                        inputLanguage = TranslateLanguage.ITALIAN;
                        break;

                    case "Japanese":
                        inputLanguage = TranslateLanguage.JAPANESE;
                        break;

                    case "Korean":
                        inputLanguage = TranslateLanguage.KOREAN;
                        break;

                    default:
                        inputLanguage = TranslateLanguage.ENGLISH;
                        break;
                }

                TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(inputLanguage).setTargetLanguage(outputLanguage).build();
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
                        Toast.makeText(text_to_speech.this, "downloading model", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}