package com.example.instagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public final String KEY_DESCRIPTION = "description";
    public final String KEY_IMAGE = "image";
    public final String KEY_USER = "user";

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

}