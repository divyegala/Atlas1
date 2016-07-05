package com.example.divye.atlas;

/**
 * Created by Divye10 on 06-03-2016.
 */
public class City {
    int _id;
    String _name;

    public City(int id, String name){
        this._id = id;
        this._name = name;
    }
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }
}
