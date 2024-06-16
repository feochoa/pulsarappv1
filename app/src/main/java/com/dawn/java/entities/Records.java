package com.dawn.java.entities;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.dawn.java.interfaces.DataRow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = Records.TABLE_NAME)
public class Records implements DataRow {
    public static final String TABLE_NAME = "records";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_OPENING_TYPE = "opening_type";  // si no es necesario se elimina o no se utiliza
    public static final String COLUMN_SILO = "silo";
    public static final String COLUMN_GUIA = "guia";    // numero de la guia o documento de recepcion
    public static final String COLUMN_UID = "uid";  // indicará si se ha enviado el registro a la api  0 = pendiente
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(index = true, name = COLUMN_BARCODE)
    private String barcode;

    @ColumnInfo(name = COLUMN_OPENING_TYPE)
    private String opening_type;

    @ColumnInfo(name = COLUMN_SILO)
    private String silo;

    @ColumnInfo(name = COLUMN_GUIA)
    private String guia;

    @ColumnInfo(name = COLUMN_UID)
    private String uid;

    @ColumnInfo(name = COLUMN_CREATED_AT)
    private String createdAt;

    @ColumnInfo(name = COLUMN_UPDATED_AT)
    private String updatedAt;

    // getters & setters
    @Override
    public String getDataRow() {
        return this.barcode + " " + this.opening_type + " " + this.silo + " " + this.guia + " " + this.uid + " " + this.createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOpening_type() {
        return opening_type;
    }

    public void setOpening_type(String opening_type) {
        this.opening_type = opening_type;
    }

    public String getSilo() { return silo; }

    public void setSilo(String silo) {  this.silo = silo;  }

    public String getGuia() {  return guia;  }

    public void setGuia(String guia) {  this.guia = guia;  }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Ignore
    public Records() { }

    public Records( int id, String barcode, String opening_type, String silo, String guia, String uid, String createdAt, String updatedAt) {
        this.id = id;
        this.barcode = barcode;
        this.opening_type = opening_type;
        this.silo = silo;
        this.guia = guia;
        this.uid = uid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Records fromApi(JSONObject jsonObject) throws JSONException {
        Records data = new Records();

        data.setId(jsonObject.getInt("id"));
        data.setBarcode(jsonObject.getString("barcode"));
        data.setOpening_type(jsonObject.getString("opening_type"));
        data.setSilo(jsonObject.getString("silo"));
        data.setGuia(jsonObject.getString("guia"));
        data.setUid(jsonObject.getString("uid"));
        data.setCreatedAt(jsonObject.getString("created_at"));
        data.setUpdatedAt(jsonObject.getString("updated_at"));

        return data;
    }


    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = "";
        try {
            Date date = format.parse(createdAt);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

            String day = dayFormat.format(date);
            String month = monthFormat.format(date);
            String year = yearFormat.format(date);
            String hour = hourFormat.format(date);
            String minute = minuteFormat.format(date);

            fecha = day + "-" + month + "-" + year + " " + hour + ":" + minute;
        } catch (Exception e) {
            e.printStackTrace();
            if (createdAt.length() > 3){
                fecha = createdAt.substring(0, createdAt.length() - 3);
            } else {
                fecha = createdAt;
            }
        }
        return silo + " " +  barcode + " " + '\'' + fecha;
    }

    public static Records fromContentValues(ContentValues values){
        final Records records = new Records();
        if (values.containsKey(COLUMN_ID)) records.setId(values.getAsInteger(COLUMN_ID));
        if (values.containsKey(COLUMN_BARCODE)) records.setBarcode(values.getAsString(COLUMN_BARCODE));
        if (values.containsKey(COLUMN_OPENING_TYPE)) records.setOpening_type(values.getAsString(COLUMN_OPENING_TYPE));
        if (values.containsKey(COLUMN_SILO)) records.setSilo(values.getAsString(COLUMN_SILO));
        if (values.containsKey(COLUMN_GUIA)) records.setGuia(values.getAsString(COLUMN_GUIA));
        if (values.containsKey(COLUMN_UID)) records.setUid(values.getAsString(COLUMN_UID));
        if (values.containsKey(COLUMN_CREATED_AT)) records.setCreatedAt(values.getAsString(COLUMN_CREATED_AT));
        if (values.containsKey(COLUMN_UPDATED_AT)) records.setUpdatedAt(values.getAsString(COLUMN_UPDATED_AT));
        return records;
    }
}