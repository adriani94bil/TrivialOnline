package com.main.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.main.MantenimientoActivity;
import com.main.R;
import com.main.modelo.Pregunta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MantenimientoWSAdapter extends RecyclerView.Adapter<MantenimientoWSAdapter.MiViewHolder> {
    private RequestQueue requestQueue;
    private List<Pregunta> listaPreguntas;
    private Pregunta pregunta;
    private MantenimientoActivity mtoActivity;
    private String url="http://10.0.2.2:8080/2001_Trivial_WS/webresources/preguntas";

    private int posicionSel = 0;

    public MantenimientoWSAdapter(List<Pregunta> listaPreguntas, MantenimientoActivity mtoActivity) {
        if (listaPreguntas==null){
            this.listaPreguntas=new ArrayList<>();
        }else{
            this.listaPreguntas=listaPreguntas;
        }
        this.mtoActivity = mtoActivity;
        requestQueue=Volley.newRequestQueue(mtoActivity);
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        // la plantilla de cada item va a ser item_pregunta
        View view= inflater.inflate(R.layout.item_mantenimiento_preguntas, parent, false);
        MantenimientoWSAdapter.MiViewHolder miViewHolder=new MantenimientoWSAdapter.MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        final Pregunta pregunta=listaPreguntas.get(position);
        holder.textViewDescMod.setText(""+pregunta.getDescripcion());
        holder.textViewPistaMod.setText(pregunta.getMensaje());
        holder.checkBoxMod.setChecked(pregunta.isRespuesta());
        holder.btnBorrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePregunta(pregunta.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pregunta preguntaSel=listaPreguntas.get(position);
                mtoActivity.editTextDescripcion.setText(preguntaSel.getDescripcion());
                mtoActivity.checkBoxNew.setChecked(preguntaSel.isRespuesta());
                mtoActivity.editTextMensaje.setText(preguntaSel.getMensaje());
                mtoActivity.preguntaSeleccionada=preguntaSel;
                posicionSel=position;

            }
        });
    }

    @Override
    public int getItemCount() {

        return listaPreguntas.size();
    }
    public void createPregunta(Pregunta pregunta){
        HashMap map=new HashMap();
        map.put("id",pregunta.getId());
        map.put("pregunta",pregunta.getDescripcion());
        map.put("explicacion",pregunta.getMensaje());
        map.put("respuesta",pregunta.isRespuesta());
        JSONObject datosPeticion = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                url,
                datosPeticion,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSON CREATE","Ha sido creado");
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON CREATE", error.getMessage());
                        error.printStackTrace();
                    }
                }

        );
        requestQueue.add(jsonObjectRequest);
    }
    public void updatePregunta(int id, String descripcion, String mensaje, boolean respuesta){
        HashMap map=new HashMap();
        map.put("id",id);
        map.put("pregunta",descripcion);
        map.put("explicacion",mensaje);
        map.put("respuesta",respuesta);
        JSONObject datosPeticion = new JSONObject(map);
        String urlUpdate=url+"/"+id;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.PUT,
                urlUpdate,
                datosPeticion,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSON MODIFICAR","Ha sido modificado");
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON MODIFICAR", error.getMessage());
                        error.printStackTrace();
                    }
                }

        );
        requestQueue.add(jsonObjectRequest);

    }
    public void deletePregunta(int id){
        HashMap map=new HashMap();
        map.put("id",id);
        String urlDelete=url+"/"+id;
        JSONObject datosPeticion = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE,
                        urlDelete,
                        null,
                        new Response.Listener<JSONObject>() { //se ejecuta

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() { //Se ejecuta caundo hay error, ha caido la url,.....

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("JSON",error.getMessage());
                    }
                });
        // se añade la peticion a la cola de peticiones
        requestQueue.add(jsonObjectRequest);
    }


    public class MiViewHolder extends  RecyclerView.ViewHolder{
        TextView textViewDescMod;
        TextView textViewPistaMod;
        ImageButton btnBorrrar;
        CheckBox checkBoxMod;
        public MiViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewDescMod=itemView.findViewById(R.id.textViewDescrMod);
            textViewPistaMod=itemView.findViewById(R.id.textViewPistaMod);
            btnBorrrar=itemView.findViewById(R.id.btnBorrar);
            checkBoxMod=itemView.findViewById(R.id.checkBoxMod);
        }
    }
}
