package com.softec.zonaazul.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.MainActivity;
import com.softec.zonaazul.utilities.Utilidades;

import java.util.HashMap;
import java.util.Map;

public class FragmentMiPerfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View vista;
    private AlertDialog alertDialog;
    private boolean DIALOG_ACTIVO = false;
    private TextView email, nombres;
    private Button cerrar_sesion, editarNombre;
    private String mParam1;
    private String mParam2;

    public FragmentMiPerfil() {
        // Required empty public constructor
    }

    public static FragmentMiPerfil newInstance(String param1, String param2) {
        FragmentMiPerfil fragment = new FragmentMiPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        nombres = vista.findViewById(R.id.Bnombres);
        editarNombre = vista.findViewById(R.id.BeditarNombres);
        editarNombre.setOnClickListener(v -> {
            MetodoEditarNombres();
        });
        email = vista.findViewById(R.id.Bemail);
        cerrar_sesion = vista.findViewById(R.id.Bcerrar_sesion);
        cerrar_sesion.setOnClickListener(v -> ((MainActivity) getActivity()).complementos.MetodoCerrarSesion());
        try {
            nombres.setText(MainActivity.usuario.getNombre());
            email.setText(MainActivity.usuario.getEmail());
        } catch (Exception e) {
            Log.d("TAGerror", "onCreateView: " + e.getMessage());
        }
        MetodoVistaConfiguraciones();

        return vista;
    }

    private void MetodoVistaConfiguraciones() {
        TextView tv_version = vista.findViewById(R.id.tv_version);
        tv_version.setText(getString(R.string.app_name) + " v" + MainActivity.complementos.getVersionName());

    }


    private void MetodoEditarNombres() {
        if (!DIALOG_ACTIVO) {
            DIALOG_ACTIVO = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View VistaDialog;
            VistaDialog = inflater.inflate(R.layout.dialog_cambiar_nombres, null);
            alert.setView(VistaDialog);
            alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            /////////////////////////////////////////////////
            final EditText cantidadPro = VistaDialog.findViewById(R.id.recuperar_email);
            Button BotonCancelar = VistaDialog.findViewById(R.id.BRecuperarContraCancelar);
            Button BotonRecuperar = VistaDialog.findViewById(R.id.BRecuperarContra);
            BotonRecuperar.setOnClickListener(view -> {
                if (!cantidadPro.getText().toString().trim().equals("")) {
                    if (!cantidadPro.getText().toString().trim().equals(nombres.getText().toString().trim())) {
                        MetodoCambiarNombre(cantidadPro.getText().toString().trim());
                        alertDialog.dismiss();
                        DIALOG_ACTIVO = false;
                    } else {
                        Snackbar.make(view, getString(R.string.nombre_de_usuario_igual), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, getString(R.string.campo_de_texto_vacio), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });

            BotonCancelar.setOnClickListener(view -> {
                alertDialog.dismiss();
                DIALOG_ACTIVO = false;
            });
        }
    }

    private void MetodoCambiarNombre(String Nusuario) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put(Utilidades.USUARIONOMBRE, Nusuario);
        ((MainActivity) getActivity()).firestore.collection(Utilidades.USUARIOS).document(MainActivity.usuario.getEmail()).update(usuario);
        // nombres.setText(Nusuario);
    }
}