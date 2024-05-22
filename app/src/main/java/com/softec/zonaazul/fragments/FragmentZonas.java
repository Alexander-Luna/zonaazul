package com.softec.zonaazul.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.MainActivity;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.adapters.AdaptadorZonas;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentZonas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentZonas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View vista;

    public FragmentZonas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentZonas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentZonas newInstance(String param1, String param2) {
        FragmentZonas fragment = new FragmentZonas();
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

    private RecyclerView recyclerView;
    private TextView tot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_zonas, container, false);
        tot = vista.findViewById(R.id.textTotal);
        recyclerView = vista.findViewById(R.id.RecyclerView);
        ConstruirRecycler();
        MetodoNZonas();
        return vista;
    }

    @SuppressLint("SetTextI18n")
    private void ConstruirRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdaptadorZonas adaptadorTransacciones = new AdaptadorZonas(((MainActivity) getActivity()).zonasArrayList, getActivity());
        adaptadorTransacciones.setOnclickListener(v -> {
            if (MainActivity.usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
                ObjetoZona transaccion = ((MainActivity) getActivity()).zonasArrayList.get(recyclerView.getChildAdapterPosition(v));
                ((MainActivity) getActivity()).MostrarFragments(new FragmentEspacios(transaccion), Utilidades.FRAGMENT_ESPACIOS);
            }
        });

        recyclerView.setAdapter(adaptadorTransacciones);
    }


    private void MetodoNZonas() {
        tot.setText("Zonas Actuales: " + ((MainActivity) getActivity()).zonasArrayList.size());
    }

}