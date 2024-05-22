package com.softec.zonaazul.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softec.zonaazul.R;
import com.softec.zonaazul.fragments.FragmentLogin;
import com.softec.zonaazul.utilities.Complementos;
import com.softec.zonaazul.utilities.Utilidades;

public class ActivityInicio extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Complementos complementos;
    LinearLayout.LayoutParams lparams;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Intent intent;
    private View vistaAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        if (VerificarLoginOffline()) {
            if (currentUser != null) {
                startActivity(intent);
                this.finish();
            }
        } else {
            setContentView(R.layout.activity_inicio);
            complementos = new Complementos(this);
            vistaAppBar = findViewById(R.id.app_bar);
            vistaAppBar.findViewById(R.id.Batras).setBackground(getDrawable(R.drawable.ic_atras));
            vistaAppBar.findViewById(R.id.Batras).setOnClickListener(view -> {
                onBackPressed();
            });
            MostrarFragments(new FragmentLogin(), Utilidades.FRAGMENT_LOGIN);
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


    public void MetodoRecuperarContra(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (!email.toLowerCase().trim().equals("")) {
            complementos.PantallaDeCarga(true);
            auth.sendPasswordResetEmail(email.toLowerCase().trim()).addOnCompleteListener(task -> {
                complementos.PantallaDeCarga(false);
                complementos.showSnackBar(R.string.correo_enviado, R.drawable.ic_ok, R.color.colorGreen);

            }).addOnFailureListener(e -> {
                complementos.PantallaDeCarga(false);
                complementos.showSnackBar(R.string.algo_salio_mal, R.drawable.ic_warning, R.color.colorRed);
            });
        } else {
            complementos.showSnackBar(R.string.ingrese_un_correo_valido, R.drawable.ic_warning, R.color.colorRed);
        }
    }

    public void IniciarSesionComoUsuarioExistente(String email, String password) {
        complementos.PantallaDeCarga(true);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d("TAG", "signInWithEmail:success");
                FirebaseUser user = auth.getCurrentUser();
                complementos.PantallaDeCarga(false);
                updateUI(user);
            } else {
                Log.w("TAG", "signInWithEmail:failure" + task.getException().getLocalizedMessage());
                if (task.getException().getLocalizedMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    MetodoMostrarAlerta(getString(R.string.correo_incorrecto));
                } else if (task.getException().getLocalizedMessage().equals("The password is invalid or the user does not have a password.")) {
                    MetodoMostrarAlerta(getString(R.string.contrasenias_invalida));
                } else {
                    MetodoMostrarAlerta(getString(R.string.algo_salio_mal));
                }
                complementos.PantallaDeCarga(false);
                updateUI(null);
            }
            // ...
        });
    }

    private void MetodoMostrarAlerta(String mensaje) {
        LinearLayout linearLayout = findViewById(R.id.linearReporte);
        TextView des = findViewById(R.id.tv_report), time = findViewById(R.id.tv_time);
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lparams);
        des.setText(mensaje);

        try {
            int t = 5000;
            final int[] i = {5};
            time.setText((t / 1000) + "s");
            new CountDownTimer(t, 1000) {
                public void onTick(final long millisUntilFinished) {
                    try {
                        i[0]--;
                        time.setText(i[0] + "s");
                    } catch (Exception e) {
                        Log.d("TAGerror", "onFinish: " + e.getMessage());
                    }
                }

                @Override
                public void onFinish() {
                    try {
                        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        linearLayout.setLayoutParams(lparams);
                    } catch (Exception e) {

                    }
                }
            }.start();
        } catch (Exception e) {

        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            complementos.PermisosApp();
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(this, ActivityClient.class);
            this.finishAndRemoveTask();
            this.startActivity(intent);
            this.finish();
        } catch (Exception e) {
            e.getMessage();
        }
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


}