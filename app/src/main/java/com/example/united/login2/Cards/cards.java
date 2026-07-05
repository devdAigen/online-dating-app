package com.example.united.login2.Cards;

/**
 * Created by United on 8/21/2018.
 */

public class cards {
    private String userId;
    private  String name;
    private  String profilurl;
    private String age;
    private String sex;

    public cards(String userId, String name,String profilurl,String age,String sex){//String profilurl
        this.userId=userId;
        this.name=name;
        this.profilurl=profilurl;
        this.age=age;
        this.sex=sex;

    }
    public void setName(String name){
        this.name=name;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserID(String userId){
        this.userId=userId;
    }
    public String getName(){
        return name;
    }


    public String getSex(){
        return sex;
    }



    public void setAge(String age){
        this.age=age;
    }
    public String getAge(){
        return age;
    }


    public void setUserId(String userId){
        this.userId=userId;
    }

//
       public String getProfilurl(){
        return profilurl;
    }

   public void setProfilurl(String profilurl){
       this.profilurl=profilurl;
   }



}
