package de.hackathon.friendshipfinderandroid.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @NonNull
    @PrimaryKey
    public String username;

    @ColumnInfo(name = "bio")
    public String bio;

    @ColumnInfo(name = "profilepicture")
    public byte[] profilePicture;

}
