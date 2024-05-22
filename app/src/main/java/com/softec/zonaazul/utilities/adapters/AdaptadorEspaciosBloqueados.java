package com.softec.zonaazul.utilities.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.MainActivity;
import com.softec.zonaazul.utilities.Complementos;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.objects.ObjetoEspacio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorEspaciosBloqueados extends RecyclerView.Adapter<AdaptadorEspaciosBloqueados.ViewHolerDatos> implements View.OnClickListener{
    ArrayList<ObjetoEspacio> ObjetoEspacios;
    Activity activity;
    private View.OnClickListener listener;

    public AdaptadorEspaciosBloqueados(ArrayList<ObjetoEspacio> ObjetoEspacios, Activity activity) {
        this.ObjetoEspacios = ObjetoEspacios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolerDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_espacios, parent, false);
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


        ObjetoEspacio objetoOperacion = this.ObjetoEspacios.get(position);
        holder.nombreCalle.setText(objetoOperacion.getNombreCalle());
        holder.hora_in.setText(objetoOperacion.getHora_in());
        holder.hora_ven.setText(objetoOperacion.getHora_ven());
        holder.estado.setText(objetoOperacion.getEstado());
        holder.placa.setText(objetoOperacion.getPlaca());
        holder.nombreEspacio.setText(objetoOperacion.getNombreEspacio());
        TextView text = holder.boton.findViewById(R.id.textoSlide);
        holder.boton.setOnClickListener(v -> {
            if (objetoOperacion.getEstado().equals(Utilidades.ESTADO_DISPONIBLE) || objetoOperacion.getEstado().equals(Utilidades.ESTADO_OCUPADO)) {
                MetodoShetBottomPagadas(objetoOperacion);
            }
        });
        switch (objetoOperacion.getEstado()) {
            case Utilidades.ESTADO_DISPONIBLE:
                holder.nombreCalle.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.nombreEspacio.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_in.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_ven.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_ven.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.placa.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.boton.setVisibility(View.VISIBLE);
                holder.cliente.setLayoutParams(lparamsO);
                text.setText("Reservar");
                holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorBlue));
                break;
            case Utilidades.ESTADO_OCUPADO:
                text.setText("Liberar");
                holder.cliente.setLayoutParams(lparamsM);
                holder.boton.setVisibility(View.VISIBLE);
                holder.nombreCalle.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.nombreEspacio.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_in.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_ven.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.hora_ven.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.placa.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorDesactivado));
                break;
            case Utilidades.ESTADO_PROHIBIDO:
                holder.cliente.setLayoutParams(lparamsO);
                holder.boton.setVisibility(View.INVISIBLE);
                holder.nombreCalle.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.nombreEspacio.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.hora_in.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.hora_ven.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.placa.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.estado.setTextColor(activity.getResources().getColor(R.color.colorTexto));
                holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorYellow));
                break;
            case Utilidades.ESTADO_BLOQUEADO:
                holder.cliente.setLayoutParams(lparamsM);
                holder.boton.setVisibility(View.INVISIBLE);
                text.setText("Liberar");
                holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
                break;
            default:
                holder.cliente.setLayoutParams(lparamsO);
                holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.colorBlue));
                break;
        }

    }

    private void MetodoShetBottomPagadas(ObjetoEspacio transaccion) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomShet);

        final View bottomShetView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_espacios, activity.findViewById(R.id.linear1));
        final LinearLayout LDispo = bottomShetView.findViewById(R.id.lDispo), lOcu = bottomShetView.findViewById(R.id.lOcu);
        final TextInputEditText placa = bottomShetView.findViewById(R.id.placa), nhoras = bottomShetView.findViewById(R.id.nhoras);
        if (transaccion.getEstado().equals(Utilidades.ESTADO_DISPONIBLE)) {
            lOcu.setLayoutParams(lparamsO1);
            LDispo.setLayoutParams(lparamsM1);
        } else {
            lOcu.setLayoutParams(lparamsM1);
            LDispo.setLayoutParams(lparamsO1);
        }
        bottomShetView.findViewById(R.id.Bliberar).setOnClickListener(v -> {
            Map<String, Object> data = new HashMap<>();
            data.put(Utilidades.ESTADO, Utilidades.ESTADO_DISPONIBLE);
            data.put(Utilidades.HORA_IN, "");
            data.put(Utilidades.HORA_VEN, "");
            data.put(Utilidades.PLACA, "");
            MainActivity.firestore.collection(Utilidades.ZONAS).document(MainActivity.usuario.getZona()).collection(Utilidades.ESPACIOS).document(transaccion.getId()).update(data).addOnSuccessListener(unused -> {
                MainActivity.complementos.showSnackBar("OK", R.drawable.ic_ok, R.color.colorprin);
            });
            MainActivity.complementos.MetodoEnvioLogs("Alquiler de espacios: " + Utilidades.ESTADO_DISPONIBLE);

        });
        bottomShetView.findViewById(R.id.BRealizarTransaccion).setOnClickListener(v1 -> {
//GUARDAR
            if (transaccion.getEstado().equals(Utilidades.ESTADO_DISPONIBLE)) {
                if (placa.getText().length() > 4 && nhoras.getText().length() > 0) {
                    Map<String, Object> data = new HashMap<>();
                    data.put(Utilidades.ESTADO, Utilidades.ESTADO_OCUPADO);
                    String hora = Complementos.getHora();
                    data.put(Utilidades.HORA_IN, hora);
                    data.put(Utilidades.HORA_VEN, Complementos.sumarHoras(hora, Integer.parseInt(nhoras.getText().toString())));
                    data.put(Utilidades.PLACA, placa.getText().toString());
                    MainActivity.firestore.collection(Utilidades.ZONAS).document(MainActivity.usuario.getZona()).collection(Utilidades.ESPACIOS).document(transaccion.getId()).update(data).addOnSuccessListener(unused -> {
                        MainActivity.complementos.showSnackBar("OK", R.drawable.ic_ok, R.color.colorprin);
                    });

                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(activity, "LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomSheetDialog.setContentView(bottomShetView);
        bottomSheetDialog.show();
        MainActivity.complementos.setupFullHeight(bottomSheetDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
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
        TextView nombreCalle, nombreEspacio, placa, estado, hora_in, hora_ven;
        LinearLayout cliente;
        View boton;
        LinearLayout cardView;

        public ViewHolerDatos(@NonNull View itemView) {
            super(itemView);
            nombreCalle = itemView.findViewById(R.id.item_calle);
            nombreEspacio = itemView.findViewById(R.id.item_nombre_esp);
            hora_in = itemView.findViewById(R.id.item_hora_in);
            hora_ven = itemView.findViewById(R.id.item_hora_ven);
            placa = itemView.findViewById(R.id.item_placa);
            estado = itemView.findViewById(R.id.item_estado);
            cliente = itemView.findViewById(R.id.item_hora);
            boton = itemView.findViewById(R.id.Badd);
            cardView = itemView.findViewById(R.id.item_card);
        }
    }
}