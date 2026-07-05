package com.example.united.login2.Chats;

/**
 * Created by United on 9/26/2018.
 */

public class ChatObject {

    private String message;
    private Boolean currentUser;




    public ChatObject(String message, Boolean currentUser){

        this.message=message;
        this.currentUser=currentUser;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }


}
