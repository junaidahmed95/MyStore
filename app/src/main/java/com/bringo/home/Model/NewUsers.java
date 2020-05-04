package com.bringo.home.Model;

public class NewUsers {

    public String last;
    public int unread;
    public long time;
    String name,image;

    public NewUsers(String name, String image,long time) {
        this.name = name;
        this.time= time;
        this.image = image;
    }

    public NewUsers() {
    }

    public NewUsers(String last, int unread, long time, String name, String image) {
        this.last = last;
        this.unread = unread;
        this.time = time;
        this.name = name;
        this.image = image;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}