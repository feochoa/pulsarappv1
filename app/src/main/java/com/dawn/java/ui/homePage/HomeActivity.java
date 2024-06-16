package com.dawn.java.ui.homePage;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dawn.java.R;
import com.dawn.java.database.AppDatabase;
import com.dawn.java.entities.Records;
import com.dawn.java.ui.MyApplication;
import com.dawn.java.ui.activity.AuthFuncActivity;
import com.dawn.java.ui.activity.VersionShowActivity;
import com.dawn.java.ui.activity.base.DawnActivity;
import com.dawn.java.ui.welcomePage.WelcomeActivity;
import com.dawn.java.ui.widget.TopToolbar;
import com.google.android.material.navigation.NavigationView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.content.Intent.ACTION_USER_PRESENT;

public class HomeActivity extends DawnActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean permissionsAreOk = false;

    private TopToolbar topBar;
    private Button btn_status, saveButton;
    private EditText barcode_input, nro_guia;
    private TextView tv_status;
    private Spinner siloSpinner;
    DrawerLayout drawer;
    NavigationView navigationView;
    private AtomicLong decodeTime = new AtomicLong(0);

    private ProgressDialog initProgressDialog = null;
    private boolean needToResetScanCallback = false;
    private PopupMenu popup = null;
    private AppDatabase db;
    private String siloSeleccionado = "";
    private String apertura = "";    // manual o automatica
    private AlertDialog dialog;
    private Boolean aperturaSilo = false;  // aperturaSilo Setea si se muestran ambas opciones de apertura false muestra solo una, true muestra ambas
    private ArrayAdapter<String> adapter;
    private RecordsAdapter adapterList;
    private RecyclerView barcodeList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_navi_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(ACTION_SCREEN_ON);
        filter.addAction(ACTION_SCREEN_OFF);
        filter.addAction(ACTION_USER_PRESENT);
        // registerReceiver(screenOnOffReceiver, filter);

        initView();
        permissionsAreOk = checkPermissions();
        if (permissionsAreOk) {
            initData();
        }
        
    }
    private void initData() {
        // 接下来的初始化操作，有可能会自动更新模组固件，需要保持屏幕常亮，避免整机休眠。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initView() {
        topBar = findViewById(R.id.topBar);  // barra superior de titulo, logout y ayuda
        topBar.setMainTitle(getResources().getString(R.string.app_name));
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
		navigationView.getMenu().findItem(R.id.nav_start_auth).setVisible(false);
        btn_status = findViewById(R.id.btn_status);     // boton de apertura de silo
        btn_status.setEnabled(false);
        tv_status = findViewById(R.id.tv_status);       // texto de estado de l silo : abierto o cerrado
        siloSpinner = findViewById(R.id.silo_spinner);  // spinner para seleccionar silo
        barcode_input = findViewById(R.id.barcode_input);   // edittext para ingreso de codigos de barra (o escaneo automatico utilizando el lector 2d)
        barcode_input.setEnabled(false);
        nro_guia = findViewById(R.id.nro_guia);     // editext para ingresar numero de guía
        nro_guia.setEnabled(false);
        saveButton = findViewById(R.id.save_button);    // boton para ingresar el codigo manual en caso que el qr sea ilegible
        saveButton.setEnabled(false);
        barcodeList = findViewById(R.id.barcode_list);  // recivlerview para listar los codigos de barra capturados

        // Inicialización de la base de datos
        db = AppDatabase.getDatabase(this);

        topBar.setMenuToolBarListener(new TopToolbar.MenuToolBarListener() {
            @Override
            public void onToolBarClickLeft(View v) {
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onToolBarClickRight(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });


        // Obtener location desde shared preferences
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String location = sharedPref.getString("location", "");

        // Initialize Spinner (dropdown)
        List<String> silos = db.silosDao().getByLocation(location);

        if (silos.isEmpty()) {
            silos.add(0, "No hay Silos para seleccionar");
        } else {
            silos.add(0, "Seleccione Silo");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, silos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        siloSpinner.setAdapter(adapter);
        siloSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSilo = (String) parent.getItemAtPosition(position);
                //String silo = selectedSilo.getName(); // desde aquí se puede obtener el id o el nombre u otra informacion del silo
                siloSeleccionado = selectedSilo;
                aperturaSilo = false;  // si es que se cambia de silo se setea a cerrado mientras no se abra
                tv_status.setText("CERRADO");
                updateSaveButton();
                if (siloSeleccionado.equals("Seleccione Silo")) {
                    btn_status.setEnabled(false);
                } else {
                    btn_status.setEnabled(true);
                    String guide = nro_guia.getText().toString().trim();
                    if ( !guide.equals("") ){   // actualizo el listado con el silo actual seleccionado
                        List<Records> latestData = db.recordsDao().getByGDAndSilo(guide, siloSeleccionado);
                        adapterList.updateData(latestData);
                    }
                }
                Toast.makeText(HomeActivity.this, "Silo seleccionado: " + selectedSilo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeActivity.this, "No hay silo seleccionado " , Toast.LENGTH_SHORT).show();
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpinner();
            }
        });

        nro_guia.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Este método se llama antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Este método se llama cuando el texto está cambiando
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Este método se llama después de que el texto ha cambiado
                        String data = s.toString().trim();
                        if(data.length() > 0){
                            barcode_input.setEnabled(true);
                            saveButton.setEnabled(true);
                        } else {
                            barcode_input.setEnabled(false);
                            saveButton.setEnabled(false);
                        }

                        List<Records> latestData = db.recordsDao().getByGDAndSilo(data, siloSeleccionado);
                        adapterList.updateData(latestData);
                    }
                };
                handler.post(runnable);
            }
        });

        barcode_input.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Este método se llama antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Este método se llama cuando el texto está cambiando
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Este método se llama después de que el texto ha cambiado
                        String input = s.toString();

                        if (input.endsWith("\n")) {
                            input = input.substring(0, input.length() - 1);  // Elimina el salto de línea del final
                            saveData();
                        }
                    }
                };
                handler.post(runnable);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = barcode_input.getText().toString();
                saveData();
            }
        });


        // Initialize ListView for displaying the scanned barcodes
        List<Records> initialData = db.recordsDao().getByGDAndSilo("","");
        adapterList = new RecordsAdapter(initialData);
        barcodeList.setAdapter(adapterList);
        barcodeList.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
    }


    private void showSpinner(){  // muestra spinner de silos
        if (siloSeleccionado.equals("")){
            Toast.makeText(HomeActivity.this, "Primero seleccione Silo" , Toast.LENGTH_SHORT).show();
            return;
        }

        // Opciones del menu
        List<String> menuList = new ArrayList<>() ;
        menuList.add("APERTURA AUTOMATICA");
        if (aperturaSilo){
            menuList.add("APERTURA MANUAL");
        }
        // Crea un AlertDialog con el Spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(view);


        ListView list = view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.my_spinner_item, menuList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Si el elemento es el segundo, cambia su color de fondo a naranja
                if (position == 1) {
                    view.setBackgroundColor(Color.parseColor("#FB8B23"));  // OTRO COLOR
                } else {
                    view.setBackgroundColor(Color.parseColor("#00BF9A"));  // Color original
                }
                return view;
            }
        };
        list.setAdapter(adapter);

        dialog = builder.create();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String optionSelected = menuList.get(position);
                if (!optionSelected.isEmpty()) {
                    // Encuentra el itinerario seleccionado
                    for (String option : menuList) {
                        if (option.equals("APERTURA AUTOMATICA")) {
                            apertura = "AUTOMATICA";
                            // btn_status.setText("APERTURA AUTOMATICA"); // se puede cambiar el texto del boton status si se considera necesario
                            // TODO: aqui se enviaria la peticion de apertura a api o endpoint externo para abrir el silo
                            // TODO: si es que hay monitoreo de estado de silo abierto/ccerrado, aqui se monitorearia el estado
                            tv_status.setText("ABIERTO");
                            dialog.dismiss();
                            break;
                        } else if (option.equals("APERTURA MANUAL")) {
                            tv_status.setText("ABIERTO");
                            apertura = "MANUAL";
                            // btn_status.setText("APERTURA MANUAL");  // se puede cambiar el texto del boton status si se considera necesario
                            // TODO: si es que hay monitoreo de estado de silo abierto/ccerrado, aqui se monitorearia el estado
                            dialog.dismiss();
                            break;

                        }
                    }
                    aperturaSilo = true; // si se hace clic una vez en una opcion de btn_status, ya quedará disponible la opción de apertura manual
                    updateSaveButton();
                } else {
                    // Muestra un mensaje de error si no se seleccionó nada
                    tv_status.setText("DESCONOCIDO");
                }
            }
        });
        dialog.show();
    }

    private void saveData(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat _dateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateCreated = _dateCreated.format(calendar.getTime());

        String codigo = barcode_input.getText().toString().trim();
        String guia = nro_guia.getText().toString().trim();

        if(codigo.isEmpty() || guia.isEmpty() || codigo == "" || guia == "") {
            try {
                ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen.startTone(ToneGenerator.TONE_CDMA_CONFIRM);   // sonido en caso de error
            } catch (Exception ex){
                Log.e("TONEGENERATOR","No se pudo reproducir beep");
            }
            Toast.makeText(HomeActivity.this, "Debe haber codigo y guía para registrar el ingreso", Toast.LENGTH_LONG).show();
            return;
        }
        Records data = db.recordsDao().getBySiloAndCode(siloSeleccionado, codigo);
        if(data != null){
            barcode_input.setText("");
            try {
                ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen.startTone(ToneGenerator.TONE_CDMA_CONFIRM);   // sonido en caso de error
            } catch (Exception ex){
                Log.e("TONEGENERATOR","No se pudo reproducir beep");
            }
            Toast.makeText(getApplicationContext(), "CODIGO YA SE HA INGRESADO EN SILO " + siloSeleccionado + " CON LA GUIA " + data.getGuia() , Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "CODIGO YA SE HA INGRESADO EN SILO " + siloSeleccionado + " CON LA GUIA " + data.getGuia() , Toast.LENGTH_LONG).show();
            return;
        }

        Records obj = new Records();
        obj.setBarcode(codigo);
        obj.setOpening_type(apertura);
        obj.setSilo(siloSeleccionado);
        obj.setGuia(guia);
        obj.setUid("0");
        obj.setCreatedAt(dateCreated);
        obj.setUpdatedAt(dateCreated);
        db.recordsDao().insert(obj);

        try {
            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);   // sonido en caso de exito

        } catch (Exception ex){
            Log.e("TONEGENERATOR","No se pudo reproducir beep");
        }
        Toast.makeText(getApplicationContext(), "registro ingresado " + codigo, Toast.LENGTH_SHORT).show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Records> latestData = db.recordsDao().getByGDAndSilo(guia, siloSeleccionado);
                adapterList.updateData(latestData);
            }
        });
        barcode_input.setText("");
        barcode_input.requestFocus();
        // sendRecordToApi();


    }

    private void updateSaveButton() {
        if (!siloSeleccionado.isEmpty() && (siloSeleccionado != "Seleccione Silo") && aperturaSilo) {
            nro_guia.setEnabled(true);
            if(nro_guia.getText().toString().equals("")){
                saveButton.setEnabled(false);
                barcode_input.setEnabled(false);
            } else {
                saveButton.setEnabled(true);
                barcode_input.setEnabled(true);
            }

        } else {
            saveButton.setEnabled(false);
            barcode_input.setEnabled(false);
            nro_guia.setEnabled(false);
        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsAreOk = checkPermissions();
                    if (permissionsAreOk) {
                        initData();
                    }
                } else {
                    finish();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (needToResetScanCallback) {
            // setScanCallback();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        needToResetScanCallback = true;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

		// unregisterReceiver(screenOnOffReceiver);

        // ServiceTools.getInstance().deInit();
    }


    static boolean BtnFlg = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "down code: " + keyCode);
        if (keyCode == 242 || keyCode == 241 || keyCode == 134 || keyCode == 135 || keyCode == 289) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (!BtnFlg) {
                        BtnFlg = true;
                        Log.d(MyApplication.TAG, "App button key down");
                        decodeTime.set(System.currentTimeMillis());
                        // ServiceTools.getInstance().startScan();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    BtnFlg = false;
                    break;

                default:
                    break;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d(TAG, " up code: " + keyCode);
//        if (keyCode == 242 || keyCode == 241) {
        if (keyCode == 242 || keyCode == 241 || keyCode == 134 || keyCode == 135 || keyCode == 289) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (BtnFlg) {
                        BtnFlg = false;
                        // ServiceTools.getInstance().stopScan();
                    }
                    break;

                default:
                    break;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, " onNavigationItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.nav_version_show:
                startActivity(new Intent(HomeActivity.this, VersionShowActivity.class));
                break;
            case R.id.nav_start_auth:
                startActivity(new Intent(HomeActivity.this, AuthFuncActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
