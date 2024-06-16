package com.dawn.java.ui.welcomePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dawn.java.R;
import com.dawn.java.database.AppDatabase;
import com.dawn.java.entities.Users;
import com.dawn.java.ui.homePage.HomeActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                performLogin(username, password, v.getContext());
            }
        });
    }

    private void performLogin(String username, String password, Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: eliminar estas lineas y realizar el proceso de login segun corresponda. Mpdificar Modelo User si es necesario
                    AppDatabase db = AppDatabase.getDatabase(WelcomeActivity.this);
                    Users user = db.usersDao().getByUserName(username);

                    if (user != null ) {
                        String hashedPwd = Users.hashPassword(password);

                        if (hashedPwd.equals(user.getPassword()) ){
                            // Guarda el usuario y ubicacion (centro) en las SharedPreferences
                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("username", username);
                            editor.putString("location", user.getLocation());
                            editor.commit();
                            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                            startActivity(intent);
                            return;
                        }else{
                            Log.i("LOGIN", "pwd incorrecta");
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(context, "USUARIO O CONTRASEÑA INCORRECTA" , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Log.i("LOGIN", "usr incorrecto");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(context, "USUARIO O CONTRASEÑA INCORRECTA" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    // TODO: este ejemplo utiliza jwt
                    URL url = new URL("http://192.168.1.86:8000/api/auth/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    String body = "username=" + username + "&password=" + password;
                    OutputStream os = conn.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WelcomeActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                
                                // Optionally, if you don't want users to go back to the WelcomeActivity when pressing back button, else, use return
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WelcomeActivity.this, "Login con error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
