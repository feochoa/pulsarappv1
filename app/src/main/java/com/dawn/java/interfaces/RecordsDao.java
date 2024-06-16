package com.dawn.java.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dawn.java.entities.Records;

import java.util.List;

@Dao
public interface RecordsDao {

    // Seleccionar todos los registros
    @Query("SELECT * FROM " + Records.TABLE_NAME)
    List<Records> getAll();

    // Insertar un record
    @Insert
    void insert(Records record);

    // Actualizar un record
    @Update
    void update(Records record);

    // Eliminar un record
    @Query("DELETE FROM " + Records.TABLE_NAME + " WHERE " + Records.COLUMN_ID + " = :id")
    void delete(int id);

    //Eliminar todos los records
    @Query("DELETE FROM " + Records.TABLE_NAME)
    void deleteAll();

    // Seleccionar un registro por nro de guia
    @Query("SELECT * FROM " + Records.TABLE_NAME + " WHERE " + Records.COLUMN_GUIA + " = :gd" + " ORDER BY " + Records.COLUMN_ID + " DESC")
    List<Records> getByGD(String gd);

    @Query("SELECT * FROM " + Records.TABLE_NAME + " WHERE " + Records.COLUMN_GUIA + " = :gd" + " AND " + Records.COLUMN_SILO + " = :silo" + " ORDER BY " + Records.COLUMN_ID + " DESC" )
    List<Records> getByGDAndSilo(String gd, String silo);

    // Seleccionar un registro por silo y barcode
    @Query("SELECT * FROM " + Records.TABLE_NAME + " WHERE " + Records.COLUMN_SILO + " = :silo" + " AND " + Records.COLUMN_BARCODE + " = :code" )
    Records getBySiloAndCode(String silo, String code);

}