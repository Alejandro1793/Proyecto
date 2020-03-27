package com.frutosajniahperez.proyecto;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


public class Dialogo_aula {


    public interface ResultadoDialogoAula {
        void ResultadoDialogoAula(String idAula);
    }

    private ResultadoDialogoAula interfaz;

    public Dialogo_aula(Context context, ResultadoDialogoAula actividad){

        interfaz = actividad;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_aula);
        dialog.setCanceledOnTouchOutside(true);

        final EditText etIdAula = dialog.findViewById(R.id.etIdAula);
        etIdAula.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        TextView btnAceptarAula = dialog.findViewById(R.id.btnAceptarAula);

        btnAceptarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoDialogoAula(etIdAula.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
