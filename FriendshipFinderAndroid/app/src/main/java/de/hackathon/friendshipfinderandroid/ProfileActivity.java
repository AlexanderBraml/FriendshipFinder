package de.hackathon.friendshipfinderandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import de.hackathon.friendshipfinderandroid.model.AppDatabase;
import de.hackathon.friendshipfinderandroid.model.User;

public class ProfileActivity extends AppCompatActivity {

    AppDatabase db;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "FriendshipFinder").build();

        Thread t = new Thread(() -> {
            List<User> users = db.userDao().getAll();
            Log.i("Profile", users.toString());
            if (users.isEmpty()) {
                user = new User();
            } else {
                user = users.get(0);
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ImageView imageView = findViewById(R.id.profilePicture);
        imageView.setOnClickListener(v -> choosePicture());
        setPicture(imageView, user.profilePicture);

        ((EditText) findViewById(R.id.inputUsername)).setText(user.username);
        ((EditText) findViewById(R.id.inputBio)).setText(user.bio);

        Button button = findViewById(R.id.uploadFile);
        button.setOnClickListener(v -> choosePicture());

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> save());
    }

    public void save() {
        user.bio = ((EditText) findViewById(R.id.inputBio)).getText().toString();
        String oldUsername = user.username;
        user.username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();
        new Thread(() -> {
            if (oldUsername == null) {
                Log.i("Profile", "INSERT");
                db.userDao().insertAll(user);
            } else {
                Log.i("Profile", "UPDATE");
                db.userDao().updateUser(user.username, user.bio, user.profilePicture, oldUsername);
            }
        }).start();
    }

    public void setPicture(ImageView imageView, byte[] picture) {
        setPicture(imageView, new ByteArrayInputStream(picture));
    }

    public void setPicture(ImageView imageView, InputStream inputStreamPicture) {
        try {
            File file = new File(getCacheDir(), "picture");
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStreamPicture.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();

                byte[] bytes = new byte[(int) file.length()];

                try(FileInputStream fis = new FileInputStream(file)){
                    fis.read(bytes);
                }

                user.profilePicture = bytes;

                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            try {
                setPicture(findViewById(R.id.profilePicture), getContentResolver().openInputStream(data.getData()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}