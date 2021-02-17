package com.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.main.adapters.MantenimientoWSAdapter;
import com.main.http.ColaPeticionesSingletone;
import com.main.modelo.Pregunta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MantenimientoActivity extends AppCompatActivity implements View.OnClickListener{
    private RequestQueue requestQueue;
    private MantenimientoWSAdapter adapter;
    public EditText editTextDescripcion;
    public EditText editTextMensaje;
    public CheckBox checkBoxNew;
    public Button btnGuardar;
    public Button btnCerrarGestion;
    private List<Pregunta> listaPreguntas;

    public RecyclerView recyclerView;

    public Pregunta preguntaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento);

        editTextDescripcion=findViewById(R.id.editTextDescripcionForm);
        editTextMensaje=findViewById(R.id.editTextMensajeForm);
        checkBoxNew=findViewById(R.id.checkBoxForm);
        btnGuardar=findViewById(R.id.btnGuardarForm);
        btnCerrarGestion=findViewById(R.id.btnCerrarGestion);

        cargarListaAdapter();
        cargarPreguntasWS();

        btnGuardar.setOnClickListener(this);
        btnCerrarGestion.setOnClickListener(this);
    }

    private void cargarListaAdapter(){
        adapter=new MantenimientoWSAdapter(listaPreguntas,this);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView=findViewById(R.id.listaModRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCerrarGestion:
                Intent intent= new Intent();
                intent.putExtra("mantenimiento", true);
                setResult(MainActivity.RESULT_OK,intent);
                finish();
                break;

        }
    }
}