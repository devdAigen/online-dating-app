package com.example.united.login2.anoynimous;

public class AnoObject {
    private String userId;



    public AnoObject(String userId){//String profilurl
        this.userId=userId;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserID(String userId){
        this.userId=userId;
    }
}