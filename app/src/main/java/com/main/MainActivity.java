package com.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.main.adapters.TrivialWSAdapter;
import com.main.modelo.Pregunta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    private TrivialWSAdapter adapter;
    private List<Pregunta> listaPreguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listaPreguntas=new ArrayList<>();
//        listaPreguntas.add(new Pregunta(1,"pregunta 1",true,""));
//        listaPreguntas.add(new Pregunta(2,"pregunta 2",true,""));
//        listaPreguntas.add(new Pregunta(3,"pregunta 3",true,""));
//        listaPreguntas.add(new Pregunta(4,"pregunta 4",true,""));

        cargarListaAdapter();
        cargarPreguntasWS();

    }
    private void cargarListaAdapter(){
        adapter=new TrivialWSAdapter(listaPreguntas);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView=findViewById(R.id.recyclerViewPreguntas);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void cargarPreguntasWS(){
        String url="http://10.0.2.2:8080/2001_Trivial_WS/webresources/preguntas";

        requestQueue= Volley.newRequestQueue(this);
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
}