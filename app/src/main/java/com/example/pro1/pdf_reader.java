package com.example.pro1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class pdf_reader extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 2;
    private static final String LOCAL_STORAGE = "/storage/self/primary/";
    private static final String COLON = ":";
    final String utteranceId = "id";
    File destinationFile;
    private FloatingActionButton fab;
    private TextView textView;
    private Uri uri;
    private TextToSpeech tts;
    private MediaPlayer mediaPlayer;
    private Button play, pause;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        fab = findViewById(R.id.fab);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        /*play.setEnabled(false);
        pause.setEnabled(false);*/

        play.setEnabled(false);
        //pause.setVisibility(View.INVISIBLE);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(pdf_reader.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    showFileChooser();
                else
                    ActivityCompat.requestPermissions(pdf_reader.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 45);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(fileName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(pdf_reader.this, "Sorry! Couldn't play", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null)
                    mediaPlayer.stop();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.stop();

        if (tts != null)
            tts.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null)
            mediaPlayer.release();

        if (tts != null)
            tts.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 45 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showFileChooser();
        else
            Toast.makeText(pdf_reader.this, "Please Provide Permission!", Toast.LENGTH_LONG).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            pdfToSpeech();
        }
    }

    private void pdfToSpeech() {
        String path;

        if (uri.getPath().contains("primary"))
            path = LOCAL_STORAGE + uri.getPath().split(COLON)[1];
        else
            path = uri.getPath();

        try {
            String str = "";
            PdfReader pdfReader = new PdfReader(path);
            int n = pdfReader.getNumberOfPages();

            for (int i = 0; i < n; i++)
                str += PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim();
            pdfReader.close();

            textView.setText(str);

            /*HashMap<String, String> myHashRenderer = new HashMap();
            myHashRenderer.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, str);
            String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("MainActivity", "exStoragePath : " + exStoragePath);
            File appTmpPath = new File(exStoragePath + "/sounds/");
            boolean isDirectoryCreated = appTmpPath.mkdirs();
            Log.d("MainActivity", "directory " + appTmpPath + " is created : " + isDirectoryCreated);
            String tempFilename = "tmpaudio.mp3";
            fileName = appTmpPath.getAbsolutePath() + File.separator + tempFilename;
            Log.d("MainActivity", "tempDestFile : " + fileName);*/

            /*fileName = Environment.getExternalStorageDirectory() + "/sound.wav";
            file = new File(fileName);*/
            /*if (file.exists())
                file.delete();*/

            destinationFile = new File(getCacheDir(), utteranceId + ".mp3");
            fileName = destinationFile.getAbsolutePath() + File.separator;

            //if (tts.synthesizeToFile(str, myHashRenderer, fileName) == TextToSpeech.SUCCESS) {
            if (tts.synthesizeToFile(str, null, destinationFile, utteranceId) == TextToSpeech.SUCCESS) {
                //Toast.makeText(MainActivity.this, "Sound File Created", Toast.LENGTH_LONG).show();
                /*play.setEnabled(true);
                pause.setEnabled(true);*/
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Toast.makeText(pdf_reader.this, "Creating Sound File. Please wait!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        //pause.setVisibility(View.VISIBLE);
                        play.setEnabled(true);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Toast.makeText(pdf_reader.this, "Couldn't create sound File", Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(pdf_reader.this, "Sound File not Created!", Toast.LENGTH_LONG).show();
            //tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}