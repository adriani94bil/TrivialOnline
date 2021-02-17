package com.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.MantenimientoActivity;
import com.main.R;
import com.main.modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class MantenimientoWSAdapter extends RecyclerView.Adapter<MantenimientoWSAdapter.MiViewHolder> {
    private List<Pregunta> listaPreguntas;
    private Pregunta pregunta;
    private MantenimientoActivity mtoActivity;

    public MantenimientoWSAdapter(List<Pregunta> listaPreguntas, MantenimientoActivity mtoActivity) {
        if (listaPreguntas==null){
            this.listaPreguntas=new ArrayList<>();
        }else{
            this.listaPreguntas=listaPreguntas;
        }
        this.mtoActivity = mtoActivity;
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
    }

    @Override
    public int getItemCount() {

        return listaPreguntas.size();
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
