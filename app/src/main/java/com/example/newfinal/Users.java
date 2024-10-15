package com.example.newfinal;


public class Users {

    private String name;
    private String id;
    private String password;

    public Users(){}
    public Users(String name,String id){
        this.id=id;
        this.name=name;
    }
    public String getname(){
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getid(){
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
