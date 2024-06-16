package com.dawn.java.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
// import androidx.annotation.VisibleForTesting;

import com.dawn.java.entities.Config;
import com.dawn.java.entities.Records;
import com.dawn.java.entities.Silos;
import com.dawn.java.entities.Users;
import com.dawn.java.interfaces.ConfigDao;
import com.dawn.java.interfaces.RecordsDao;
import com.dawn.java.interfaces.SilosDao;
import com.dawn.java.interfaces.UsersDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Database(entities = {Config.class, Records.class, Silos.class, Users.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    @SuppressWarnings("WeakerAccess")
    public abstract ConfigDao configDao();
    public abstract RecordsDao recordsDao();
    public abstract SilosDao silosDao();
    public abstract UsersDao usersDao();

    private static volatile AppDatabase INSTANCE = null;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Consts.BD_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        // TODO: comentar esta linea si no se requiere autocompletar con datos de prueba
        new PopulateDbAsync(INSTANCE).execute();
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ConfigDao mConfigDao;
        private final SilosDao mSilosDao;
        private final UsersDao mUsersDao;

        PopulateDbAsync(AppDatabase db) {
            mConfigDao = db.configDao();
            mSilosDao = db.silosDao();
            mUsersDao = db.usersDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            mConfigDao.deleteAll();
            Config config = new Config(1, "http://192.168.1.86:8000/api/", "demo1", "demo1" );
            mConfigDao.insert(config);

            mUsersDao.deleteAll();
            Users user = new Users(1, "user1", Users.hashPassword("user1"), "centro01",1);
            mUsersDao.insert(user);
            user = new Users(2, "user2", Users.hashPassword("user2"), "centro02", 1);
            mUsersDao.insert(user);
            user = new Users(3, "user3", Users.hashPassword("user3"), "centro01",1);
            mUsersDao.insert(user);
            user = new Users(4, "user4", Users.hashPassword("user4"), "centro02", 1);
            mUsersDao.insert(user);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());
            mSilosDao.deleteAll();
            for (int i = 1; i <= 5; i++) {
                Silos silos = new Silos(i, "Silo A-0" + i, "centro01" , "-" + i, 1, currentDateAndTime, currentDateAndTime);
                mSilosDao.insert(silos);
            }
            for (int i = 6; i <= 9; i++) {
                Silos silos = new Silos(i, "Silo B-0" + i, "centro02" , "-" + i, 1, currentDateAndTime, currentDateAndTime);
                mSilosDao.insert(silos);
            }

            return null;
        }
    }

}
