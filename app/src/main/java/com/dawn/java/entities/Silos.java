package com.dawn.java.entities;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.dawn.java.interfaces.DataRow;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = Silos.TABLE_NAME)
public class Silos implements DataRow {
    public static final String TABLE_NAME = "silos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    @PrimaryKey(autoGenerate = true)    // eliminar (autoGenerate = true) si es necesario usar id de bd externa
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(index = true, name = COLUMN_LOCATION)
    private String location;

    @ColumnInfo(name = COLUMN_COMMENTS)
    private String comments;

    @ColumnInfo(name = COLUMN_STATUS)
    private int status;

    @ColumnInfo(name = COLUMN_CREATED_AT)
    private String createdAt;

    @ColumnInfo(name = COLUMN_UPDATED_AT)
    private String updatedAt;

    // Getters and setters...
    @Override
    public String getDataRow() {
        return this.location + " " + this.name + " " + this.comments + " " + this.status + " " + this.updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Ignore
    public Silos() { }


    public Silos(int id, String name, String location, String comments, int status, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.comments = comments;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString(){
        return "Silos{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", comments='" + comments + '\'' +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public static Silos fromContentValues(ContentValues values){
        final Silos silos = new Silos();
        if (values.containsKey(COLUMN_ID)) {
            silos.setId(values.getAsInteger(COLUMN_ID));
        }
        if (values.containsKey(COLUMN_NAME)) {
            silos.setName(values.getAsString(COLUMN_NAME));
        }
        if (values.containsKey(COLUMN_LOCATION)) {
            silos.setLocation(values.getAsString(COLUMN_LOCATION));
        }
        if (values.containsKey(COLUMN_COMMENTS)) {
            silos.setComments(values.getAsString(COLUMN_COMMENTS));
        }
        if (values.containsKey(COLUMN_STATUS)) {
            silos.setStatus(values.getAsInteger(COLUMN_STATUS));
        }
        if (values.containsKey(COLUMN_CREATED_AT)) {
            silos.setCreatedAt(values.getAsString(COLUMN_CREATED_AT));
        }
        if (values.containsKey(COLUMN_UPDATED_AT)) {
            silos.setUpdatedAt(values.getAsString(COLUMN_UPDATED_AT));
        }
        return silos;
    }

    public static Silos fromApi(JSONObject jsonObject) throws JSONException {
        Silos silo = new Silos();

        silo.setId(jsonObject.getInt("id"));
        silo.setName(jsonObject.getString("name"));
        silo.setLocation(jsonObject.getString("location"));
        silo.setComments(jsonObject.getString("comments"));
        boolean status = jsonObject.getBoolean("status");
        silo.setStatus(status ? 1 : 0);
        silo.setCreatedAt(jsonObject.getString("created_at"));
        silo.setUpdatedAt(jsonObject.getString("updated_at"));

        return silo;
    }
}
