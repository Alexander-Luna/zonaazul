package com.softec.zonaazul.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;
import com.softec.zonaazul.R;
import com.softec.zonaazul.fragments.FragmentCliente;
import com.softec.zonaazul.utilities.Complementos;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

import java.util.ArrayList;

public class ActivityClient extends AppCompatActivity {
    public Complementos complementos;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private View vistaAppBar;
    public static FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VerificarLogin();

    }

    private void VerificarLogin() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (VerificarLoginOffline()) {
            if (currentUser != null) {
                Log.d("TAGerror", "VerificarLogin: ENTRAAA CLIENTE");
                startActivity(intent);
                this.finish();
            }
        } else {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_client);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            firestore = FirebaseFirestore.getInstance();
            zonasArrayList = new ArrayList<>();
            complementos = new Complementos(this);
            complementos.setDelayPantallaDeCarga(true);
            complementos.PantallaDeCarga(true);
            vistaAppBar = findViewById(R.id.app_bar);
            ((Button) vistaAppBar.findViewById(R.id.Bbuscar)).setBackgroundResource(R.drawable.ic_login);
            vistaAppBar.findViewById(R.id.Bbuscar).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ActivityInicio.class)));

            MostrarDatosZonas();
        }
    }

    public boolean VerificarLoginOffline() {
        boolean log = false;
        try {
            currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                log = true;
            }
        } catch (Exception e) {
            Log.d("TAGerror", "VerificarLoginOffline: " + e.getMessage());
            return false;
        }
        return log;
    }

    public void MostrarFragments(Fragment fragment, String TAG) {
        complementos.PermisosApp();
        try {
            MetodoActionBar(!TAG.equals(Utilidades.FRAGMENT_MAIN));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment, TAG).addToBackStack(null).commit();
        } catch (Exception e) {
            Log.d("TAGerror", "MostrarFragments: " + e.getMessage());
        }

    }

    public void MetodoActionBar(boolean ATRAS) {
        if (ATRAS) {
            vistaAppBar.findViewById(R.id.Batras).setBackground(getDrawable(R.drawable.ic_atras));
            vistaAppBar.findViewById(R.id.Batras).setOnClickListener(view -> {
                onBackPressed();
            });
        } else {
            vistaAppBar.findViewById(R.id.Batras).setBackground(getDrawable(R.drawable.ic_share));
            vistaAppBar.findViewById(R.id.Batras).setOnClickListener(view -> {
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MostrarFragments(new FragmentCliente(), Utilidades.FRAGMENT_CLIENTE);
    }

    public ArrayList<ObjetoZona> zonasArrayList;

    private void MostrarDatosZonas() {
        try {
            firestore.collection(Utilidades.ZONAS).addSnapshotListener(MetadataChanges.INCLUDE, (value, error) -> {
                if (error == null) {
                    assert value != null;
                    ObjetoZona objetoTransaccion;
                    for (final DocumentChange document : value.getDocumentChanges()) {
                        objetoTransaccion = new ObjetoZona();
                        objetoTransaccion.setId(document.getDocument().getId());
                        try {
                            objetoTransaccion.setCuadras(document.getDocument().getData().get(Utilidades.CUADRAS).toString());
                            objetoTransaccion.setEstado(document.getDocument().getData().get(Utilidades.ESTADO).toString());
                            objetoTransaccion.setInterseccion1(document.getDocument().getData().get(Utilidades.INTERSECCION1).toString());
                            objetoTransaccion.setInterseccion2(document.getDocument().getData().get(Utilidades.INTERSECCION2).toString());
                            objetoTransaccion.setNombre(document.getDocument().getData().get(Utilidades.NOMBRE).toString());
                            objetoTransaccion.setOrientacion(document.getDocument().getData().get(Utilidades.ORIENTACION).toString());
                            objetoTransaccion.setRecaudador(document.getDocument().getData().get(Utilidades.RECAUDADOR).toString());
                            objetoTransaccion.setCalle(document.getDocument().getData().get(Utilidades.CALLE).toString());

                        } catch (Exception e) {
                            Log.d("TAGerror", "MostrarDatosSupervisor: " + e.getMessage());
                        }
                        switch (document.getType()) {
                            case ADDED:
                                if (!zonasArrayList.contains(objetoTransaccion)) {
                                    zonasArrayList.add(objetoTransaccion);
                                }
                                break;
                            case MODIFIED:
                                if (zonasArrayList.contains(objetoTransaccion)) {
                                    zonasArrayList.set(zonasArrayList.indexOf(objetoTransaccion), objetoTransaccion);
                                }

                                break;
                            case REMOVED:
                                zonasArrayList.remove(objetoTransaccion);

                                break;
                        }

                    }
                } else {
                    return;
                }

                if (zonasArrayList.size() == value.size()) {
                    complementos.setDelayPantallaDeCarga(false);
                    complementos.PantallaDeCarga(false);
                    MostrarFragments(new FragmentCliente(), Utilidades.FRAGMENT_CLIENTE);
                }
            });
        } catch (final Exception e) {
            Log.d("TAGerror", "MostrarOperaciones: " + e.getMessage());
        }
    }
}