package com.softec.zonaazul.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softec.zonaazul.R;
import com.softec.zonaazul.activities.ActivityClient;
import com.softec.zonaazul.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Complementos {
    private final Activity activity;
    public boolean PANTALLA_CARGA_INICIO = true;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private boolean DELAY_PANTALLA_DE_CARGA = false, TIMER_ACTIVO = false;

    public Complementos(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getHora() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String sumarHoras(String hora, int horasASumar) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formatter.parse(hora);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, horasASumar);
            Date nuevaHora = calendar.getTime();
            return formatter.format(nuevaHora);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFecha() {
        try {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            return DateFormat.format("dd/MM/yyyy", c).toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRemoverTildes(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }

    public void MetodoEnvioLogs(String evento) {
        Map<String, Object> data = new HashMap<>();
        data.put(Utilidades.FECHA, getFecha() + " - " + getHora());
        data.put(Utilidades.EVENTO, evento+"/"+MainActivity.usuario.getEmail());
        MainActivity.firestore.collection(Utilidades.LOGS).add(data).addOnSuccessListener(unused -> {
        });
    }


    public void PantallaDeCarga(boolean inicio) {
        MetodoOcultarTeclado();
        if (inicio) {
            builder = new AlertDialog.Builder(activity, R.style.tamanio_dialog);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView;
            if (PANTALLA_CARGA_INICIO) {
                dialogView = inflater.inflate(R.layout.progres_dialog_inicio, null);
                PANTALLA_CARGA_INICIO = false;
            } else {
                dialogView = inflater.inflate(R.layout.progres_dialog, null);
            }
            builder.setView(dialogView);
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        } else {
            if (dialog != null && dialog.isShowing()) {
                if (DELAY_PANTALLA_DE_CARGA) {
                    DELAY_PANTALLA_DE_CARGA = false;
                    final int[] TIEMPO = {0};
                    if (!TIMER_ACTIVO) {
                        TIEMPO[0] = 1000;
                        TIMER_ACTIVO = true;
                        try {
                            new CountDownTimer(TIEMPO[0], 1000) {
                                public void onTick(final long millisUntilFinished) {
                                    TIMER_ACTIVO = true;
                                }

                                @Override
                                public void onFinish() {
                                    try {
                                        TIEMPO[0] = 0;
                                        TIMER_ACTIVO = false;
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        Log.d("TAGerror", "onFinish: " + e.getMessage());
                                    }
                                }
                            }.start();
                        } catch (Exception e) {
                            TIEMPO[0] = 0;
                            TIMER_ACTIVO = false;
                            dialog.dismiss();
                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }
    }

    public void setDelayPantallaDeCarga(boolean DELAY_PANTALLA_DE_CARGA) {
        this.DELAY_PANTALLA_DE_CARGA = DELAY_PANTALLA_DE_CARGA;
    }

    public void MetodoOcultarTeclado() {
        View focus = activity.getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focus != null) {
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    public void PermisosApp() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET,}, 1000);
        }
    }

    public void MetodoCerrarSesion() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.cerrar_sesion));
        builder.setMessage(activity.getString(R.string.seguro_que_desea_cerrar_sesion));
        builder.setPositiveButton(activity.getString(R.string.si), (dialogInterface, i) -> {
            if (((MainActivity) activity).auth.getCurrentUser() != null) {
                ((MainActivity) activity).auth.signOut();
                if (((MainActivity) activity).auth.getCurrentUser() == null) {
                    dialogInterface.dismiss();
                    MetodoCerrarActividad();
                } else {
                    showSnackBar(R.string.error_al_cerrar_sesion, R.drawable.ic_warning, R.color.colorRed);
                }
            }

        });
        builder.setNegativeButton(activity.getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setCancelable(false).create().show();
    }

    private void MetodoCerrarActividad() {
        try {
            Intent intent = new Intent(activity, ActivityClient.class);
            ((MainActivity) activity).firestore = FirebaseFirestore.getInstance();
            activity.finishAndRemoveTask();
            activity.startActivity(intent);
            activity.finish();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String getVersionName() {
        try {
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void showSnackBar(int ID_STRING, int ICO_ID, int COLOR_BACKGROUND) {
        try {
            TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), activity.getString(ID_STRING), TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbar.setIconLeft(ICO_ID, 50);
            snackbarView.setBackgroundColor(activity.getResources().getColor(COLOR_BACKGROUND));
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            snackbar.show();
        } catch (Exception e) {
            Log.d("TAGerror", "showSnackBar: " + e.getMessage());
        }
    }

    public void showSnackBar(String ID_STRING, int ICO_ID, int COLOR_BACKGROUND) {
        try {
            TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), ID_STRING, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbar.setIconLeft(ICO_ID, 50);
            snackbarView.setBackgroundColor(activity.getResources().getColor(COLOR_BACKGROUND));
            //snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            snackbar.show();
        } catch (Exception e) {
            Log.d("TAGerror", "showSnackBar: " + e.getMessage());
        }
    }

    ////BOTTOM SHET VIEW
    public void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        CardView bottomSheet = bottomSheetDialog.findViewById(R.id.card_busqueda);
        //BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;

        }
        bottomSheet.setLayoutParams(layoutParams);
        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
