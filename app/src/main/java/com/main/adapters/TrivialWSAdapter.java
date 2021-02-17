package com.main.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.main.MainActivity;
import com.main.MantenimientoActivity;
import com.main.R;
import com.main.modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class TrivialWSAdapter extends RecyclerView.Adapter<TrivialWSAdapter.MiViewHolder> {

    private List<Pregunta> listaPreguntas;
    private MantenimientoActivity mtoActivity;
    private MainActivity mainActivity;

    public TrivialWSAdapter(List<Pregunta> preguntas) {

        //llama al web services para pedirle las preguntas

        if (preguntas==null){
            this.listaPreguntas=new ArrayList<>();
        }else{
            this.listaPreguntas=preguntas;
        }

    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        // la plantilla de cada item va a ser item_pregunta
        View view= inflater.inflate(R.layout.item_pregunta_juego, parent, false);
        TrivialWSAdapter.MiViewHolder miViewHolder=new TrivialWSAdapter.MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        final Pregunta pregunta=listaPreguntas.get(position);
        holder.textViewId.setText(""+pregunta.getId());
        holder.textViewPreg.setText(pregunta.getDescripcion());
        //Falta el onclick
        holder.btnPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setMessage(pregunta.mensaje);
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPreguntas.size();
    }

    public class MiViewHolder extends RecyclerView.ViewHolder{
        TextView textViewId;
        TextView textViewPreg;
        ImageButton btnPista;
        RadioButton radioButtonTrue;
        RadioButton radioButtonFalse;

        public MiViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId=itemView.findViewById(R.id.textViewId);
            textViewPreg=itemView.findViewById(R.id.textViewPregunta);
            btnPista=itemView.findViewById(R.id.btnPista);
            radioButtonTrue=itemView.findViewById(R.id.radioButtonTrue);
            radioButtonFalse=itemView.findViewById(R.id.radioButtonFalse);

        }
    }
}
