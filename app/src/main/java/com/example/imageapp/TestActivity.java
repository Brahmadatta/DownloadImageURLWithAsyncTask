package com.example.imageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        makefile();
    }

    private void makefile() {
        File folder = getExternalFilesDir(null);
        folder.mkdirs();

        File file = new File(folder,"demotest.txt");

        try {
//https://stackoverflow.com/questions/51942381/getting-exposed-beyond-app-through-intent-getdata-error-while-installing-the-d
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.append("Hello World!!!");
            fileOutputStream.close();
            openfile(file);
            moveFile(file,Environment.getExternalStorageDirectory());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/4178168/how-to-programmatically-move-copy-and-delete-files-and-directories-on-sd
    //https://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
    private void moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }

    private void movieFile(File file) {

            InputStream in = null;
            OutputStream out = null;
            try {

                //create output directory if it doesn't exist
                File dir = new File (Environment.getExternalStorageDirectory().getPath());
                if (!dir.exists())
                {
                    dir.mkdirs();
                }


                in = new FileInputStream(file.getPath() + file);
                out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + file);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                // delete the original file
                new File(file.getPath() + file).delete();


            }

            catch (FileNotFoundException fnfe1) {
                Log.e("tag", fnfe1.getMessage());
            }
            catch (Exception e) {
                Log.e("tag", e.getMessage());
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

//            if (file != null) {
//                //movieFile(file);
//                try {
//                    moveFile(file,getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText( this,"No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }

    }



}