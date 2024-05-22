package com.softec.zonaazul.utilities.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softec.zonaazul.R;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

import java.util.ArrayList;

public class AdaptadorZonas extends RecyclerView.Adapter<AdaptadorZonas.ViewHolerDatos> implements View.OnClickListener {
    ArrayList<ObjetoZona> ObjetoEspacios;
    Activity activity;
    private View.OnClickListener listener;

    public AdaptadorZonas(ArrayList<ObjetoZona> ObjetoEspacios, Activity activity) {
        this.ObjetoEspacios = ObjetoEspacios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolerDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zonas, parent, false);
        view.setOnClickListener(this);
        lparamsO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        lparamsM = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparamsO1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lparamsM1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return new ViewHolerDatos(view);
    }

    LinearLayout.LayoutParams lparamsM, lparamsO, lparamsM1, lparamsO1;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolerDatos holder, int position) {
        ObjetoZona objetoOperacion = this.ObjetoEspacios.get(position);
        holder.zona.setText(objetoOperacion.getNombre());
        holder.calle.setText(objetoOperacion.getCalle());
        holder.inter1.setText(objetoOperacion.getInterseccion1());
        holder.inter2.setText(objetoOperacion.getInterseccion2());
        //    TextView text = holder.boton.findViewById(R.id.textoSlide);
      /*  holder.boton.setOnClickListener(v -> {
            if (objetoOperacion.getEstado().equals(Utilidades.ESTADO_DISPONIBLE) || objetoOperacion.getEstado().equals(Utilidades.ESTADO_OCUPADO)) {
                MetodoShetBottomPagadas(objetoOperacion);
            }
        });
    */
    }


    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        try {
            return ObjetoEspacios.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


    public class ViewHolerDatos extends RecyclerView.ViewHolder {
        TextView zona, inter1, inter2, calle;

        public ViewHolerDatos(@NonNull View itemView) {
            super(itemView);
            zona = itemView.findViewById(R.id.tv_zona);
            calle = itemView.findViewById(R.id.tv_calle);
            inter1 = itemView.findViewById(R.id.tv_inter1);
            inter2 = itemView.findViewById(R.id.tv_inter2);
        }
    }
}