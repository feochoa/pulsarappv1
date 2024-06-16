package com.dawn.java.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.dawn.java.entities.Silos;

import java.util.List;

@Dao
public interface SilosDao {

    // Seleccionar todos los Silos
    @Query("SELECT * FROM " + Silos.TABLE_NAME)
    List<Silos> getAll();

    @Query("SELECT " + Silos.COLUMN_NAME + " FROM " + Silos.TABLE_NAME)
    List<String> getAllNames();

    // Insertar un silo
    @Insert
    void insert(Silos silo);

    // Actualizar un silo
    @Update
    void update(Silos silo);

    // Eliminar un silo
    @Query("DELETE FROM " + Silos.TABLE_NAME + " WHERE " + Silos.COLUMN_ID + " = :id")
    void delete(int id);

    //Eliminar todos los silos
    @Query("DELETE FROM " + Silos.TABLE_NAME)
    void deleteAll();

    // Seleccionar un silo por id
    @Query("SELECT " + Silos.COLUMN_LOCATION + " || ' ' || " + Silos.COLUMN_NAME +
            " FROM " + Silos.TABLE_NAME + " WHERE " + Silos.COLUMN_LOCATION + " = :location")
    List<String> getByLocation(String location);


    @Query("SELECT " + Silos.COLUMN_LOCATION + " || ' ' || " + Silos.COLUMN_NAME +
            " FROM " + Silos.TABLE_NAME + " WHERE " + Silos.COLUMN_STATUS + " = 1")
    List<String> getSpinner();

}