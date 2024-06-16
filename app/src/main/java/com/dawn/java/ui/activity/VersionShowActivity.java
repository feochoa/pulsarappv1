package com.dawn.java.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dawn.java.R;
import com.dawn.java.ui.activity.base.DawnActivity;
import com.dawn.java.ui.widget.TopToolbar;

public class VersionShowActivity extends DawnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TopToolbar topBar = findViewById(R.id.topBar);
        topBar.setMenuToolBarListener(new TopToolbar.MenuToolBarListener() {
            @Override
            public void onToolBarClickLeft(View v) {
                finish();
            }

            @Override
            public void onToolBarClickRight(View v) {

            }
        });

        TextView tv_help1 = findViewById(R.id.tv_help1);
        TextView tv_help2 = findViewById(R.id.tv_help2);
        TextView tv_help3 = findViewById(R.id.tv_help3);
        TextView tv_help4 = findViewById(R.id.tv_help4);

        tv_help1.setText("Para Ingresar alimento, primero debe seleccionar el Silo donde se almacenrá.");
        tv_help2.setText("Una vez seleccionado el silo, se habilita la opción de apertura de silo. Acá tiene la opción de apertura automática, y si no funciona la apertura automatica, se habilita la opción de apertura manual en este mismo botón.");
        tv_help3.setText("Una vez abierto el silo, se habilita la opción de ingreso de número de documento (guía). Al digitar la guía, se mostrará el listado de alimento ya ingresado a silos previamente.");
        tv_help4.setText("Con los datos anteriores registrados, puede comenzar a escanear los códigos de sacos de alimento.");

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_show;
    }
}
