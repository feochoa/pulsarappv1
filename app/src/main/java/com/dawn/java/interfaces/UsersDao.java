package com.dawn.java.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.dawn.java.entities.Users;

import java.util.List;

@Dao
public interface UsersDao {

    // Seleccionar todos los usuarios
    @Query("SELECT * FROM " + Users.TABLE_NAME)
    List<Users> getAll();

    // Insertar un usuario
    @Insert
    void insert(Users user);

    // Actualizar un usuario
    @Update
    void update(Users user);

    // Eliminar un usuario
    @Query("DELETE FROM " + Users.TABLE_NAME + " WHERE " + Users.COLUMN_ID + " = :id")
    void delete(int id);

    //Eliminar todos los usuarios
    @Query("DELETE FROM " + Users.TABLE_NAME)
    void deleteAll();

    // Seleccionar un usuario por id
    @Query("SELECT * FROM " + Users.TABLE_NAME + " WHERE " + Users.COLUMN_USERNAME + " = :user")
    Users getByUserName(String user);


}