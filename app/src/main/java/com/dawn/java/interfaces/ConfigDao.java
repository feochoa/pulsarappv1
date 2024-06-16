package com.dawn.java.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dawn.java.entities.Config;

import java.util.List;

@Dao
public interface ConfigDao {

    // Seleccionar todas las configuraciones
    @Query("SELECT * FROM " + Config.TABLE_NAME)
    List<Config> getAll();

    // Insertar una configuración
    @Insert
    void insert(Config config);

    // Actualizar una configuración
    @Update
    void update(Config config);

    // Eliminar una configuración
    @Query("DELETE FROM " + Config.TABLE_NAME + " WHERE " + Config.COLUMN_ID + " = :id")
    void delete(int id);

    //Eliminar todas las configuraciones
    @Query("DELETE FROM " + Config.TABLE_NAME)
    void deleteAll();


    // Seleccionar una configuración por id
    @Query("SELECT * FROM " + Config.TABLE_NAME + " WHERE " + Config.COLUMN_ID + " = :id")
    Config getById(int id);
}