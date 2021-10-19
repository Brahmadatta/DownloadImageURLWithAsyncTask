package com.example.imageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        makefile();
    }

    private void makefile() {
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        folder.mkdirs();

        File file = new File(folder,"demotest.txt");

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.append("Hello World!!!");
            fileOutputStream.close();
            openfile(file);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openfile(File file) {

        try {
            //file = new File(Environment.getExternalStorageDirectory() + "/" + "default.pdf");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile( this,"com.example.imageapp.provider", file);
                intent.setDataAndType(contentUri, "text/html");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "text/html");
            }
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText( this,"No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }

    }



}