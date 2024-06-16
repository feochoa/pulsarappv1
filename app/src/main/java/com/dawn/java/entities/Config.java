package com.dawn.java.entities;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.dawn.java.interfaces.DataRow;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = Config.TABLE_NAME)
public class Config implements DataRow {
    public static final String TABLE_NAME = "config";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SERVIDOR = "servidor";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_SERVIDOR)
    private String servidor;

    @ColumnInfo(name = COLUMN_USERNAME)
    private String username;

    @ColumnInfo(name = COLUMN_PASSWORD)
    private String password;


    // Getters and setters...
    @Override
    public String getDataRow() {
        return this.id + ". api:" + this.servidor + " | usr:" + this.username + " | pwd:" + this.password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getServidor() { return servidor; }
    public void setServidor(String servidor) { this.servidor = servidor; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    

    @Ignore
    public Config() { }

    public Config(int id, String servidor, String username, String password) {
        this.id = id;
        this.servidor = servidor;
        this.username = username;
        this.password = password;
    }

}