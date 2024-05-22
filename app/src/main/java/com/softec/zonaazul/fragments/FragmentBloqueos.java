package com.softec.zonaazul.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.MainActivity;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.adapters.AdaptadorEspacios;
import com.softec.zonaazul.utilities.objects.ObjetoEspacio;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBloqueos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBloqueos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View vista;
    private String mParam2;

    public FragmentBloqueos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBloqueos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBloqueos newInstance(String param1, String param2) {
        FragmentBloqueos fragment = new FragmentBloqueos();
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
    private int CON_DISPONIBLES = 0;
    private TextView tot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_bloqueos, container, false);

        tot = vista.findViewById(R.id.textTotal);
        recyclerView = vista.findViewById(R.id.RecyclerView);
        ConstruirRecycler();
        MostrarDatosBloqueos();
        return vista;
    }

    private void MostrarDatosBloqueos() {
        for (ObjetoZona zona : ((MainActivity) getActivity()).zonasArrayList) {
            MainActivity.firestore.collection(Utilidades.ZONAS).document(zona.getId()).collection(Utilidades.ESPACIOS).orderBy(Utilidades.NUMERO, Query.Direction.ASCENDING).addSnapshotListener(MetadataChanges.INCLUDE, (value, error) -> {
                if (error == null) {
                    assert value != null;
                    ObjetoEspacio objetoTransaccion;
                    for (final DocumentChange document : value.getDocumentChanges()) {
                        objetoTransaccion = new ObjetoEspacio();
                        objetoTransaccion.setId(document.getDocument().getId());
                        objetoTransaccion.setRecaudador(zona.getRecaudador());
                        objetoTransaccion.setZonaId(zona.getId());
                        objetoTransaccion.setZona(zona.getNombre());
                        objetoTransaccion.setNombreCalle("Zona: " + zona.getId() + " Calle: " + zona.getCalle() + " Cuadras: " + document.getDocument().getData().get(Utilidades.CUADRA).toString());
                        objetoTransaccion.setEstado(document.getDocument().getData().get(Utilidades.ESTADO).toString());
                        objetoTransaccion.setHora_ven(document.getDocument().getData().get(Utilidades.HORA_VEN).toString());
                        objetoTransaccion.setHora_in(document.getDocument().getData().get(Utilidades.HORA_IN).toString());
                        objetoTransaccion.setNombreEspacio("Espacio " + document.getDocument().getData().get(Utilidades.NUMERO).toString());
                        objetoTransaccion.setPlaca(document.getDocument().getData().get(Utilidades.PLACA).toString());
                        switch (document.getType()) {
                            case ADDED:
                                if (!espacioArrayList.contains(objetoTransaccion) && objetoTransaccion.getEstado().equals(Utilidades.ESTADO_BLOQUEADO)) {
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
                                if (!objetoTransaccion.getEstado().equals(Utilidades.ESTADO_BLOQUEADO)) {
                                    espacioArrayList.remove(objetoTransaccion);
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
                    MetodoOrdenarDatos(espacioArrayList);
                    MetodoEspaciosBloqueados();
                } else {
                    return;
                }
            });
        }
    }
    private void MetodoOrdenarDatos(ArrayList<ObjetoEspacio> espacioArrayList) {
        Collections.sort(espacioArrayList, (o1, o2) -> {
            int numero1 = Integer.parseInt(o1.getNombreEspacio().split(" ")[1]);
            int numero2 = Integer.parseInt(o2.getNombreEspacio().split(" ")[1]);
            return numero1 - numero2;
        });
    }
    private void MetodoEspaciosBloqueados() {
        CON_DISPONIBLES = 0;
        for (ObjetoEspacio espacio : espacioArrayList) {
            if (espacio.getEstado().equals(Utilidades.ESTADO_BLOQUEADO)) {
                CON_DISPONIBLES++;
            }
        }
        tot.setText("Espacios Bloqueados: " + CON_DISPONIBLES);
    }

    ArrayList<ObjetoEspacio> espacioArrayList;

    @SuppressLint("SetTextI18n")
    private void ConstruirRecycler() {
        espacioArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdaptadorEspacios adaptadorTransacciones = new AdaptadorEspacios(espacioArrayList, getActivity());
        adaptadorTransacciones.setOnclickListener(v -> {
            if (MainActivity.usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
                ObjetoEspacio transaccion = espacioArrayList.get(recyclerView.getChildAdapterPosition(v));
                MetodoShetBottomSupervisorEspacio(transaccion);
            }
        });

        recyclerView.setAdapter(adaptadorTransacciones);
    }

    private void MetodoShetBottomSupervisorEspacio(ObjetoEspacio transaccion) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomShet);

        final View bottomShetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_supervisor_espacios, vista.findViewById(R.id.linear1));
        final TextView recaudador = bottomShetView.findViewById(R.id.tv_recaudador), zona = bottomShetView.findViewById(R.id.tv_zona), calle = bottomShetView.findViewById(R.id.tv_calle);
        RadioButton Rbpro = bottomShetView.findViewById(R.id.RBprohibido), Rbblock = bottomShetView.findViewById(R.id.RBbloqueado), Rbdes = bottomShetView.findViewById(R.id.RBdesbloqueado);
        recaudador.setText(transaccion.getRecaudador());
        zona.setText(transaccion.getZona());
        calle.setText(transaccion.getNombreCalle());
        if (transaccion.getEstado().equals(Utilidades.ESTADO_BLOQUEADO)) {
            Rbblock.setChecked(true);
        } else if (transaccion.getEstado().equals(Utilidades.ESTADO_PROHIBIDO)) {
            Rbpro.setChecked(true);
        } else if (transaccion.getEstado().equals(Utilidades.ESTADO_DISPONIBLE) || transaccion.getEstado().equals(Utilidades.ESTADO_OCUPADO)) {
            Rbdes.setChecked(true);
        }
        bottomShetView.findViewById(R.id.Bsalir).setOnClickListener(v -> {
            Map<String, Object> data = new HashMap<>();
            String estado = "";
            if (Rbdes.isChecked() && !Rbpro.isChecked()) {
                estado = Utilidades.ESTADO_DISPONIBLE;
            } else if (Rbblock.isChecked()) {
                estado = Utilidades.ESTADO_BLOQUEADO;
            } else if (Rbpro.isChecked()) {
                estado = Utilidades.ESTADO_PROHIBIDO;
            }
            data.put(Utilidades.ESTADO, estado);
            Log.d("TAGzona", "MetodoShetBottomSupervisorEspacio: " + transaccion.getZona());
            MainActivity.firestore.collection(Utilidades.ZONAS).document(transaccion.getZonaId()).collection(Utilidades.ESPACIOS).document(transaccion.getId()).update(data).addOnSuccessListener(unused -> {
                MainActivity.complementos.showSnackBar("OK", R.drawable.ic_ok, R.color.colorprin);
            });
            MainActivity.complementos.MetodoEnvioLogs("Bloqueo de Vehiculos Estado: " + estado);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomShetView);
        bottomSheetDialog.show();
        MainActivity.complementos.setupFullHeight(bottomSheetDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}