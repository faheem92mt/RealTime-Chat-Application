package com.faheem92mt.findfriends;

public class FindFriendModel {

    private String userName;
    private String photoName;
    private String userID;
    private boolean requestSent;

    public FindFriendModel(String userName, String photoName, String userID, boolean requestSent) {
        this.userName = userName;
        this.photoName = photoName;
        this.userID = userID;
        this.requestSent = requestSent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }

}
