package com.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.main.adapters.TrivialWSAdapter;
import com.main.http.ColaPeticionesSingletone;
import com.main.modelo.Pregunta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    private TrivialWSAdapter adapter;
    private List<Pregunta> listaPreguntas;
    private Button btnJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnJugar=findViewById(R.id.btnJugar);
        cargarListaAdapter();
        cargarPreguntasWS();
        pruebaLanzarHilo();


        btnJugar.setOnClickListener(v->jugar());


    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarListaAdapter();
        cargarPreguntasWS();
        pruebaLanzarHilo();
    }

    private void cargarListaAdapter(){
        adapter=new TrivialWSAdapter(listaPreguntas);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView=findViewById(R.id.recyclerViewPreguntas);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    public void jugar(){
        int total=listaPreguntas.size();
        int acierto=0;
        int numeroPreguntasRespondidas=0;
        for (Pregunta pregunta:listaPreguntas){
            if (pregunta.getRespuestaJugador()!=null){
                numeroPreguntasRespondidas++;
                if (pregunta.getRespuestaJugador()==pregunta.isRespuesta()){
                    acierto++;
                }
            }

        }
            String msg="";
            if (numeroPreguntasRespondidas !=total){
                msg="Faltan respuestas. siga Jugando.";
            }else{
                msg=" ha acertado "+acierto+" de "+total;
            }

            //Mostrar Resultado
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("RESULTADO").setMessage(msg)
                    .setIcon((android.R.drawable.ic_dialog_info))
                    .setNeutralButton(android.R.string.ok,((dialog, which) -> {}));
            builder.create().show();
    }


    private void cargarPreguntasWS(){
        String url="http://10.0.2.2:8080/2001_Trivial_WS/webresources/preguntas";

        requestQueue= ColaPeticionesSingletone.getInstance(this).getRequestQueue();
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.i("Trivial WS",response.toString());
                    // recorrer para llenar el array list
                    listaPreguntas=new ArrayList<>();
                    for (int i=0; i<response.length();i++){
                        try {
                            JSONObject objPregunta=response.getJSONObject(i);
                            Pregunta p=new Pregunta(
                                    objPregunta.getInt("id"),
                                    objPregunta.getString("pregunta"),
                                    objPregunta.getBoolean("respuesta"),
                                    objPregunta.getString("explicacion"));
                            listaPreguntas.add(p);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //cargo lista adapter
                    cargarListaAdapter();
                },
                error -> {
                    Log.i("Trivial WS",error.getMessage());
                }
        );

        requestQueue.add(jsonArrayRequest);

    }
    //Menu y opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Enlace a mantenimiento
        if (item.getItemId()==R.id.opcionMto){
            Intent intent=new Intent (this, MantenimientoActivity.class);
            startActivityForResult(intent,1);
            return true;

            //Modo Noche
        }else if(item.getItemId()==R.id.nightMode){
            if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO){

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        }else{

            return super.onContextItemSelected(item);
        }
    }

    //Hilo
    private void pruebaLanzarHilo(){
        ExecutorService executor= Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("LANZA HILO","iniciado");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("LANZA HILO","fin");
                    }
                });
            }
        });
    }// fin prueba lanza hilo

}