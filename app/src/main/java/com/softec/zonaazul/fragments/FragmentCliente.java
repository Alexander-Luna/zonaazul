package com.softec.zonaazul.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.ActivityClient;
import com.softec.zonaazul.utilities.Complementos;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.adapters.AdaptadorEspaciosCliente;
import com.softec.zonaazul.utilities.objects.ObjetoEspacio;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCliente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View vista;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText TIbuscar;
    ArrayList<ObjetoEspacio> espacioArrayList;
    RecyclerView recyclerView;

    public FragmentCliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCliente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCliente newInstance(String param1, String param2) {
        FragmentCliente fragment = new FragmentCliente();
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
        vista = inflater.inflate(R.layout.fragment_cliente, container, false);
        ConstruirRecycler();
        CargarDatos();
        TIbuscar = vista.findViewById(R.id.TIbuscar);
        MetodoBuscar();

        return vista;
    }

    private void ConstruirRecycler() {
        espacioArrayListTemp = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        final AdaptadorEspaciosCliente cargarSeries = new AdaptadorEspaciosCliente(espacioArrayListTemp, getActivity());

        cargarSeries.setOnclickListener(view -> {
            ObjetoEspacio transaccion = espacioArrayListTemp.get(recyclerView.getChildAdapterPosition(view));
              /*  FragmentVerPartitura verPartitura = new FragmentVerPartitura();
                verPartitura.setPartitura(transaccion);
                ((MainActivity) activity).MostrarFragments(verPartitura, Utilidades.FRAGMENT_VER_PARTITURA);
                ((MainActivity) activity).BbarIzq.setBackground(activity.getResources().getDrawable(R.drawable.ic_atras));
               */

        });
        recyclerView.setAdapter(cargarSeries);
    }


    private void CargarDatos() {
        espacioArrayList = new ArrayList<>();
        for (ObjetoZona zona : ((ActivityClient) getActivity()).zonasArrayList) {
            ActivityClient.firestore.collection(Utilidades.ZONAS).document(zona.getId()).collection(Utilidades.ESPACIOS).orderBy(Utilidades.NUMERO, Query.Direction.ASCENDING).addSnapshotListener(MetadataChanges.INCLUDE, (value, error) -> {
                if (error == null) {
                    assert value != null;
                    ObjetoEspacio objetoTransaccion;
                    for (final DocumentChange document : value.getDocumentChanges()) {
                        objetoTransaccion = new ObjetoEspacio();
                        objetoTransaccion.setId(document.getDocument().getId());
                        objetoTransaccion.setRecaudador(zona.getRecaudador());
                        objetoTransaccion.setZona(zona.getId() + " " + zona.getNombre());
                        objetoTransaccion.setNombreCalle("Zona: " + zona.getId() + " Calle: " + zona.getCalle() + " Cuadras: " + document.getDocument().getData().get(Utilidades.CUADRA).toString());
                        objetoTransaccion.setEstado(document.getDocument().getData().get(Utilidades.ESTADO).toString());
                        objetoTransaccion.setHora_ven(document.getDocument().getData().get(Utilidades.HORA_VEN).toString());
                        objetoTransaccion.setHora_in(document.getDocument().getData().get(Utilidades.HORA_IN).toString());
                        objetoTransaccion.setNombreEspacio("Espacio " + document.getDocument().getData().get(Utilidades.NUMERO).toString());
                        objetoTransaccion.setPlaca(document.getDocument().getData().get(Utilidades.PLACA).toString());
                        switch (document.getType()) {
                            case ADDED:
                                if (!espacioArrayList.contains(objetoTransaccion)) {
                                    espacioArrayList.add(objetoTransaccion);
                                }
                                try {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                                break;
                            case MODIFIED:
                                if (espacioArrayList.contains(objetoTransaccion)) {
                                    espacioArrayList.set(espacioArrayList.indexOf(objetoTransaccion), objetoTransaccion);
                                }
                                try {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                                break;
                            case REMOVED:
                                espacioArrayList.remove(objetoTransaccion);
                                try {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                                break;
                        }

                    }
                } else {
                    return;
                }

            });
        }
    }

    ArrayList<ObjetoEspacio> espacioArrayListTemp;

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility"})
    public void MetodoBuscar() {

        try {

            TIbuscar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    espacioArrayListTemp.clear();
                    if (TIbuscar.getText() != null && TIbuscar.getText().toString().length() > 0) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        for (ObjetoEspacio producto : espacioArrayList) {
                            if (Complementos.getRemoverTildes(producto.getPlaca()).toLowerCase(Locale.getDefault()).contains(Complementos.getRemoverTildes(TIbuscar.getText().toString().toLowerCase(Locale.getDefault()).trim()))) {
                                if (!espacioArrayListTemp.contains(producto)) {
                                    espacioArrayListTemp.add(producto);
                                    try {
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                    } catch (Exception e) {
                                        Log.d("TAGerror", "afterTextChanged: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    } else {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        espacioArrayListTemp.clear();
                    }
                }
            });
        } catch (Exception e) {
        }

    }
    /*
RecyclerView recyclerView;
    @SuppressLint({"SetJavaScriptEnabled", "NotifyDataSetChanged"})
    private void ConstruirRecycler() {
        //intent = new Intent(getContext(), ActivityVerPartitura.class);
        recyclerView = vista.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final AdaptadorTransacciones adaptadorTransacciones = new AdaptadorTransacciones(((MainActivity) getActivity()).partituraTodoArrayList, getActivity(), true);
        adaptadorTransacciones.setOnclickListener(v -> {
            ObjetoPartitura transaccion = ((MainActivity) getActivity()).partituraTodoArrayList.get(recyclerView.getChildAdapterPosition(v));
            if (Click) {
                Click = false;
                //((MainActivity) getActivity()).MostrarOcultar(true);
                verPartitura = new FragmentVerPartitura();
                verPartitura.setPartitura(transaccion);
                ((MainActivity) getActivity()).MostrarFragments(verPartitura, Utilidades.FRAGMENT_VER_PARTITURA);
                ((MainActivity) getActivity()).BbarIzq.setBackground(getResources().getDrawable(R.drawable.ic_atras));
                try {
                    new CountDownTimer(2000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Click = true;
                        }
                    }.start();
                } catch (Exception e) {
                    Log.d("TAGerror", "ClickActivity: " + e.getMessage());
                }
            }
        });
        adaptadorTransacciones.setOnLongclickListener(v -> {
            ObjetoPartitura partitura1 = ((MainActivity) getActivity()).partituraTodoArrayList.get(recyclerView.getChildAdapterPosition(v));
            MetodoBottomSheetDialog(partitura1);
            return false;
        });

        recyclerView.setAdapter(adaptadorTransacciones);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

    }*/
}