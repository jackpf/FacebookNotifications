package com.jackpf.facebooknotifications.Facebook;

import com.restfb.Facebook;

import java.awt.Image;

public class Notification
{
    public static class User
    {
        @Facebook
        public String id;

        @Facebook
        public String name;
    }

    public static class Application
    {
        @Facebook
        public String id;

        @Facebook
        public String name;

        @Facebook
        public String namespace;
    }

    @Facebook
    public String id;

    @Facebook
    public User from;

    @Facebook
    public User to;

    @Facebook("created_time")
    public String createdTime;

    @Facebook("updated_time")
    public String updatedTime;

    @Facebook
    public String title;

    @Facebook
    public String link;

    @Facebook
    public Application application;

    @Facebook
    public int unread;

    public Image image;

    public int position;

    // Object?
}