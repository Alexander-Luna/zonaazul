package com.softec.zonaazul.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.softec.zonaazul.R;
import com.softec.zonaazul.fragments.FragmentMain;
import com.softec.zonaazul.utilities.Complementos;
import com.softec.zonaazul.utilities.Utilidades;
import com.softec.zonaazul.utilities.objects.ObjetoPersona;
import com.softec.zonaazul.utilities.objects.ObjetoZona;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean ENTRA = false;
    @SuppressLint("StaticFieldLeak")
    public static Complementos complementos;
    public static FirebaseFirestore firestore;
    public static ObjetoPersona usuario;
    public FirebaseAuth auth;
    private View vistaAppBar;
    public static ObjetoZona objetoZona;
    public ArrayList<ObjetoZona> zonasArrayList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("TAGerror", "onCreate: Main ");
        objetoZona = new ObjetoZona();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usuario = new ObjetoPersona();
        complementos = new Complementos(this);
        complementos.setDelayPantallaDeCarga(true);
        complementos.PantallaDeCarga(true);

        if (auth.getCurrentUser() == null) {
            this.finish();
        }

        MetodoDatosPerfil();
        complementos.PermisosApp();
        vistaAppBar = findViewById(R.id.app_bar);

    }


    private void MostrarDatosRecaudador() {
        try {
            objetoZona = new ObjetoZona();
            firestore.collection(Utilidades.ZONAS).document(MainActivity.usuario.getZona()).addSnapshotListener((value, error) -> {
                if (error == null) {
                    try {
                        objetoZona.setCalle(value.getString(Utilidades.CALLE));
                        objetoZona.setCuadras(value.getString(Utilidades.CUADRAS));
                        objetoZona.setEstado(value.getString(Utilidades.ESTADO));
                        objetoZona.setInterseccion1(value.getString(Utilidades.INTERSECCION1));
                        objetoZona.setInterseccion2(value.getString(Utilidades.INTERSECCION2));
                        objetoZona.setOrientacion(value.getString(Utilidades.ORIENTACION));
                        objetoZona.setNombre(value.getString(Utilidades.NOMBRE));
                        objetoZona.setRecaudador(value.getString(Utilidades.RECAUDADOR));
                        ENTRA = true;
                    } catch (Exception e) {
                        Log.d("TAGerror", "MetodoDatosPerfil1: " + e.getMessage());
                    }

                } else {
                    return;
                }
                if (ENTRA) {
                    complementos.PantallaDeCarga(false);
                    complementos.PANTALLA_CARGA_INICIO = false;
                    MostrarFragments(new FragmentMain(), Utilidades.FRAGMENT_MAIN);
                    ENTRA = false;
                }
            });
        } catch (Exception e) {
            Log.d("TAGerror", "MetodoDatosPerfil3: " + e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        try {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            FragmentManager manager = getSupportFragmentManager();
            FragmentManager.BackStackEntry stackEntry;
            MetodoActionBar(manager.getBackStackEntryCount() > 2);
            switch (fragment.getTag()) {
                case Utilidades.FRAGMENT_MI_PERFIL:
                case Utilidades.FRAGMENT_INVENTARIO:
                case Utilidades.FRAGMENT_ZONAS:
                case Utilidades.FRAGMENT_BLOQUEOS:
                case Utilidades.FRAGMENT_ESPACIOS:
                    stackEntry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
                    manager.popBackStack(stackEntry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    break;
                case Utilidades.FRAGMENT_MAIN:
                    MetodoFinalizarApp();
                    stackEntry = manager.getBackStackEntryAt(0);
                    manager.popBackStack(stackEntry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("TAGerror", "onBackPressed: " + e.getMessage());
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
        vistaAppBar.findViewById(R.id.Bbuscar).setVisibility(View.INVISIBLE);
    }

    private void MetodoFinalizarApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.salir));
        builder.setMessage(getString(R.string.desea_salir_de_la_aplicacion));
        builder.setPositiveButton(getString(R.string.si), (dialogInterface, i) -> {
            this.finishAndRemoveTask();
            this.finish();
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
            MostrarFragments(new FragmentMain(), Utilidades.FRAGMENT_MAIN);
            dialogInterface.dismiss();
        });
        builder.setCancelable(false).create().show();
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

    @SuppressLint("SetTextI18n")
    private void MetodoDatosPerfil() {
        try {
            firestore.collection(Utilidades.USUARIOS).document(auth.getCurrentUser().getEmail()).addSnapshotListener((value, error) -> {
                if (error == null) {
                    try {
                        usuario.setNombre(value.getString(Utilidades.USUARIONOMBRE));
                        usuario.setEmail(value.getString(Utilidades.EMAIL));

                        usuario.setRol(value.getString(Utilidades.ROL));
                        if (usuario.getRol().equals(Utilidades.ROL_RECAUDADOR)) {
                            usuario.setZona(value.getString(Utilidades.NOMBRE_ZONA));
                            if (usuario.getZona().equals("")) {
                                Intent intent = new Intent(getApplicationContext(), ActivityClient.class);
                                if (auth.getCurrentUser() != null) {
                                    auth.signOut();
                                }
                                Toast.makeText(this, "No cuenta con una zona asignada", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                this.finish();
                            } else {

                                MostrarDatosRecaudador();
                            }
                        } else if (usuario.getRol().equals(Utilidades.ROL_SUPERVISOR)) {
                            MostrarDatosSupervisor();
                        }
                    } catch (Exception e) {
                        Log.d("TAGerror", "MetodoDatosPerfil1: " + e.getMessage());
                    }

                } else {
                    return;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAGerror", "MetodoDatosPerfil3: " + e.getMessage());
        }
    }


    private void MostrarDatosSupervisor() {
        try {
            if (zonasArrayList == null) {
                zonasArrayList = new ArrayList<>();
            } else {
                zonasArrayList.clear();
            }
            MainActivity.firestore.collection(Utilidades.ZONAS).orderBy(Utilidades.NOMBRE, Query.Direction.ASCENDING).addSnapshotListener(MetadataChanges.INCLUDE, (value, error) -> {
                if (error == null) {
                    assert value != null;
                    ObjetoZona objetoTransaccion;
                    for (final DocumentChange document : value.getDocumentChanges()) {
                        objetoTransaccion = new ObjetoZona();
                        try {
                            objetoTransaccion.setId(document.getDocument().getId());
                            // Log.d("TAGid", "MostrarDatosSupervisor: "+document.getDocument());
                            objetoTransaccion.setCalle(document.getDocument().getData().get(Utilidades.CALLE).toString());
                            objetoTransaccion.setCuadras(document.getDocument().getData().get(Utilidades.CUADRAS).toString());
                            objetoTransaccion.setEstado(document.getDocument().getData().get(Utilidades.ESTADO).toString());
                            objetoTransaccion.setInterseccion1(document.getDocument().getData().get(Utilidades.INTERSECCION1).toString());
                            objetoTransaccion.setInterseccion2(document.getDocument().getData().get(Utilidades.INTERSECCION2).toString());
                            objetoTransaccion.setNombre(document.getDocument().getData().get(Utilidades.NOMBRE).toString());
                            objetoTransaccion.setOrientacion(document.getDocument().getData().get(Utilidades.ORIENTACION).toString());
                            objetoTransaccion.setRecaudador(document.getDocument().getData().get(Utilidades.RECAUDADOR).toString());
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
                    complementos.PantallaDeCarga(false);
                    complementos.PANTALLA_CARGA_INICIO = false;
                    MostrarFragments(new FragmentMain(), Utilidades.FRAGMENT_MAIN);

                }
            });
        } catch (final Exception e) {
            Log.d("TAGerror", "MostrarOperaciones: " + e.getMessage());
        }
    }
}