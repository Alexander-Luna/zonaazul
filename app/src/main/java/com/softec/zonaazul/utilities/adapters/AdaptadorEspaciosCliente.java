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
import com.softec.zonaazul.utilities.objects.ObjetoEspacio;

import java.util.ArrayList;

public class AdaptadorEspaciosCliente extends RecyclerView.Adapter<AdaptadorEspaciosCliente.ViewHolerDatos> implements View.OnClickListener, View.OnLongClickListener {
    ArrayList<ObjetoEspacio> ObjetoEspacios;
    Activity activity;
    int AD_TYPE = 0;
    int CONTENT_TYPE = 1;
    private View.OnClickListener listener;
    private View.OnLongClickListener listenerLong;
    private boolean PAGADAS = false;

    public AdaptadorEspaciosCliente(ArrayList<ObjetoEspacio> ObjetoEspacios, Activity activity) {
        this.ObjetoEspacios = ObjetoEspacios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolerDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehiculos_item, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        lparamsO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        lparamsM = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparamsO1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lparamsM1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        return new ViewHolerDatos(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 6 == 3) return AD_TYPE;
        return CONTENT_TYPE;
    }

    LinearLayout.LayoutParams lparamsM, lparamsO, lparamsM1, lparamsO1;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolerDatos holder, int position) {


        ObjetoEspacio objetoOperacion = this.ObjetoEspacios.get(position);
        holder.placa.setText(objetoOperacion.getPlaca());
        holder.hora.setText(objetoOperacion.getHora_in() + " hasta " + objetoOperacion.getHora_ven());
        holder.estado.setText(objetoOperacion.getEstado());
        holder.zona.setText(objetoOperacion.getZona());
    }


    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongclickListener(View.OnLongClickListener listenerLong) {
        this.listenerLong = listenerLong;
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

    @Override
    public boolean onLongClick(View view) {
        if (listenerLong != null) {
            listenerLong.onLongClick(view);
        }
        return false;
    }

    public void setPagadas(boolean pagadas) {
        PAGADAS = pagadas;
    }

    public class ViewHolerDatos extends RecyclerView.ViewHolder {
        TextView placa, zona, estado, hora;

        public ViewHolerDatos(@NonNull View itemView) {
            super(itemView);
            placa = itemView.findViewById(R.id.item_placa);
            hora = itemView.findViewById(R.id.item_hour);
            zona = itemView.findViewById(R.id.item_zona);
            estado = itemView.findViewById(R.id.item_estado);

        }
    }
}