package de.hackathon.friendshipfinderandroid.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insertAll(User... users);

    @Query("UPDATE user SET username=:username, bio=:bio, profilePicture=:profilePicture WHERE username=:oldUsername")
    void updateUser(String username, String bio, byte[] profilePicture, String oldUsername);

    @Delete
    void delete(User user);
}
