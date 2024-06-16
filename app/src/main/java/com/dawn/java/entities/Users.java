package com.dawn.java.entities;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.dawn.java.interfaces.DataRow;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Entity(tableName = Users.TABLE_NAME)
public class Users implements DataRow {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_STATUS = "status";  // indicará si está acivo = 1 o no = 0

    @PrimaryKey(autoGenerate = true)    // eliminar (autoGenerate = true) si es necesario usar id de bd externa
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(index = true, name = COLUMN_USERNAME)
    private String username;

    @ColumnInfo(name = COLUMN_PASSWORD)
    private String password;

    @ColumnInfo(name = COLUMN_LOCATION)
    private String location;

    @ColumnInfo(name = COLUMN_STATUS)
    private int status;


    // getters & setters
    @Override
    public String getDataRow() {
        return this.username + " " + this.location + " " + this.status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }


    @Ignore
    public Users() { }

    public Users( int id, String username, String password, String location, int status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.location = location;
        this.status = status;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Users fromApi(JSONObject jsonObject) throws JSONException {
        Users data = new Users();

        data.setId(jsonObject.getInt("id"));
        data.setUsername(jsonObject.getString("username"));
        data.setPassword(jsonObject.getString("password"));
        data.setLocation(jsonObject.getString("location"));
        boolean status = jsonObject.getBoolean("status");
        data.setStatus(status ? 1 : 0);

        return data;
    }


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }

    public static Users fromContentValues(ContentValues values){
        final Users data = new Users();
        if (values.containsKey(COLUMN_ID)) data.setId(values.getAsInteger(COLUMN_ID));
        if (values.containsKey(COLUMN_USERNAME)) data.setUsername(values.getAsString(COLUMN_USERNAME));
        if (values.containsKey(COLUMN_PASSWORD)) data.setPassword(values.getAsString(COLUMN_PASSWORD));
        if (values.containsKey(COLUMN_LOCATION)) data.setLocation(values.getAsString(COLUMN_LOCATION));
        if (values.containsKey(COLUMN_STATUS)) data.setStatus(values.getAsInteger(COLUMN_STATUS));
        return data;
    }
}