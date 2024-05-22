package com.softec.zonaazul.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.MainActivity;
import com.softec.zonaazul.utilities.Utilidades;

public class FragmentMain extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View vista;
    private String mParam1;
    private String mParam2;
    private View ajustes, perfil, supervisor;

    public FragmentMain() {

    }

    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
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

    TextView zona, calle, inter1, inter2, calle1, inter11, inter22;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_main, container, false);
        ajustes = vista.findViewById(R.id.Bop1);
        perfil = vista.findViewById(R.id.Bop2);
        supervisor = vista.findViewById(R.id.Bop3);
        MetodoBotones(ajustes);
        MetodoBotones(perfil);
        MetodoBotones(supervisor);
        zona = vista.findViewById(R.id.tv_zona);
        calle = vista.findViewById(R.id.tv_calle);
        inter1 = vista.findViewById(R.id.tv_inter1);
        inter2 = vista.findViewById(R.id.tv_inter2);
        calle1 = vista.findViewById(R.id.tv_calle1);
        inter11 = vista.findViewById(R.id.tv_inter11);
        inter22 = vista.findViewById(R.id.tv_inter22);
        zona.setText(MainActivity.objetoZona.getNombre());
        calle.setText(MainActivity.objetoZona.getCalle());
        inter1.setText(MainActivity.objetoZona.getInterseccion1());
        inter2.setText(MainActivity.objetoZona.getInterseccion2());
        calle1.setText(MainActivity.objetoZona.getCalle());
        inter11.setText(MainActivity.objetoZona.getInterseccion1());
        inter22.setText(MainActivity.objetoZona.getInterseccion2());
        try {
            RelativeLayout relativeLayout = vista.findViewById(R.id.croquis);
            if (MainActivity.objetoZona.getOrientacion().equals(Utilidades.ORIENTACION_HORIZONTAL)) {
                relativeLayout.setRotation(-90);
            }
        } catch (Exception e) {
            Log.d("TAGerror", "onCreateView: " + e.getMessage());
        }
        if (MainActivity.usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
            LinearLayout linearLayout = vista.findViewById(R.id.lRecaudador);
            linearLayout.setLayoutParams(lparamsO);
        }
        return vista;
    }

    ViewGroup.LayoutParams lparamsO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
    ViewGroup.LayoutParams lparamsM = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    private void MetodoBotones(View boton) {
        ImageView logo = boton.findViewById(R.id.item_logo);
        TextView nombre = boton.findViewById(R.id.textoSlide);
        switch (boton.getId()) {
            case R.id.Bop1:
                nombre.setText(getString(R.string.mi_perfil));
                logo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_perfil));
                boton.setOnClickListener(view -> {
                    ((MainActivity) getActivity()).MostrarFragments(new FragmentMiPerfil(), Utilidades.FRAGMENT_MI_PERFIL);
                });
                break;
            case R.id.Bop2:
                if (MainActivity.usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
                    boton.setVisibility(View.VISIBLE);
                    nombre.setText(getString(R.string.bloqueos));
                    logo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bloqueo));
                    boton.setOnClickListener(view -> {
                        ((MainActivity) getActivity()).MostrarFragments(new FragmentBloqueos(), Utilidades.FRAGMENT_BLOQUEOS);
                    });
                } else {
                    boton.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.Bop3:
                if (MainActivity.usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
                    nombre.setText(getString(R.string.zonas));
                    logo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_zona));
                    boton.setOnClickListener(view -> {
                        ((MainActivity) getActivity()).MostrarFragments(new FragmentZonas(), Utilidades.FRAGMENT_ZONAS);
                    });
                } else {
                    nombre.setText(getString(R.string.espacios));
                    logo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_espacios));
                    boton.setOnClickListener(view -> {
                        ((MainActivity) getActivity()).MostrarFragments(new FragmentEspacios(), Utilidades.FRAGMENT_ESPACIOS);
                    });
                }
                break;
        }
    }

}