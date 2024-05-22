package com.softec.zonaazul.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.ActivityInicio;

public class FragmentLogin extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AlertDialog alertDialog;
    private boolean DIALOG_ACTIVO = false;
    private String user, pass;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View vista;

    public FragmentLogin() {
        // Required empty public constructor
    }

    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
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
        vista = inflater.inflate(R.layout.fragment_login, container, false);
        ((ActivityInicio) getActivity()).complementos.PANTALLA_CARGA_INICIO = false;
        usernameEditText = vista.findViewById(R.id.login_email);
        passwordEditText = vista.findViewById(R.id.login_password);
        vista.findViewById(R.id.login).setOnClickListener(v -> MetodoIniciarSesion());
        vista.findViewById(R.id.BOcontra).setOnClickListener(v -> DialogRecuperarContra());
        return vista;
    }

    public void DialogRecuperarContra() {
        if (!DIALOG_ACTIVO) {
            DIALOG_ACTIVO = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View VistaDialog;
            VistaDialog = inflater.inflate(R.layout.dialog_recuperar_contra, null);
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
                    ((ActivityInicio) getActivity()).MetodoRecuperarContra(cantidadPro.getText().toString().trim());
                    alertDialog.dismiss();
                    DIALOG_ACTIVO = false;
                }


            });

            BotonCancelar.setOnClickListener(view -> {
                alertDialog.dismiss();
                DIALOG_ACTIVO = false;
            });
        }
    }

    public void MetodoIniciarSesion() {
        user = usernameEditText.getText().toString();
        pass = passwordEditText.getText().toString();
        if (!user.trim().equals("") && !pass.trim().equals("")) {
            ((ActivityInicio) getActivity()).IniciarSesionComoUsuarioExistente(user.trim(), pass.trim());
        } else {
            ActivityInicio.complementos.showSnackBar(R.string.llene_todos_los_campos, R.drawable.ic_warning, R.color.colorprin);
        }

    }

    public boolean MetodoComprobarCamposLlenos() {
        try {
            return usernameEditText.getText().toString().trim().length() > 0 || passwordEditText.getText().toString().trim().length() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}