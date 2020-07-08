package com.example.instagram.models;

import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject implements Parcelable {

    public final String TAG = "PostModel";

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_TIME = "createdAt";
    public static final String KEY_LIKE = "likedBy";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImg() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImg(ParseFile img) {
        put(KEY_IMAGE, img);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getTime() {
        Date date = getCreatedAt();
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(date);
    }

    public String getRelativeTime() {
        Date date = getCreatedAt();
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public int getNumLike() throws ParseException {
        ParseRelation<ParseUser> likedUsers = getRelation(KEY_LIKE);
        return likedUsers.getQuery().count();
    }

    public boolean userLiked(ParseUser user) throws ParseException {
        ParseRelation<ParseUser> likedUsers = getRelation(KEY_LIKE);
        int count = likedUsers.getQuery().whereContains("objectId", user.getObjectId()).count();
        Log.i(TAG, String.valueOf(count));
        return count > 0;
    }

    public void attemptToLike(ParseUser user) throws ParseException {
        ParseRelation<ParseUser> likedUsers = getRelation(KEY_LIKE);
        if (userLiked(user)) {
            likedUsers.remove(user);
            Log.i(TAG, "Unliked!");
        } else {
            likedUsers.add(user);
            Log.i(TAG, "Liked!");
        }
        save();
    }
}
