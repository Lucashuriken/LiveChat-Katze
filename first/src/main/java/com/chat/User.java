package com.chat;

public class User {
    private int id;
    private String username;
    private String status;
    private String email;
    private String img;

    public User(int id, String username, String email, String status, String img){
    this.id = id;
    this.username = username;
    this.status = status;
    this.email = email;
    this.img = img;
}
    //Getters

    public int getId(){return id;}
    public String getUsername(){return username;}
    public String getEmail() {return email;}
    public String getStatus() { return status; }
    public String getImg() {return img;}
    
          public void setStatus(String status){
            this.status = status;
        } 
}
