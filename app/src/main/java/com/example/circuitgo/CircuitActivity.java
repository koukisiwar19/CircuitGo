package com.example.circuitgo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CircuitActivity extends AppCompatActivity {
    Cursor cur;
    SQLiteDatabase db;
    LinearLayout layNaviguer, layRecherche;
    EditText _txtRecherche,  _txtdepart, _txtarr, _txtpri, _txtdure;
    ImageButton _btnRecherche;
    Button _btnAdd, _btnUpdate, _btnDelete;
    Button _btsuivant, _btprecedent;
    Button   _btnCancel,_btnSave;


    int op = 0;
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit);
        layNaviguer = (LinearLayout) findViewById(R.id.layNaviguer);
        layRecherche = (LinearLayout) findViewById(R.id.layRecherche);

        _txtRecherche = (EditText) findViewById(R.id.txtRecherche);
        _txtdepart = (EditText) findViewById(R.id.txtdepart);
        _txtarr = (EditText) findViewById(R.id.txtarr);
        _txtpri = (EditText) findViewById(R.id.txtpri);
        _txtdure = (EditText) findViewById(R.id.txtdure);


        _btnAdd = (Button) findViewById(R.id.btnAdd);
        _btnUpdate = (Button) findViewById(R.id.btnUpdate);
        _btnDelete = (Button) findViewById(R.id.btnDelete);

        _btnCancel = (Button) findViewById(R.id.btnCancel);
        _btnSave = (Button) findViewById(R.id.btnSave);

        _btsuivant = (Button) findViewById(R.id.btsuivant);
        _btprecedent = (Button) findViewById(R.id.btprecedent);
        _btnRecherche = (ImageButton) findViewById(R.id.btnRecherche);

        db = openOrCreateDatabase("bdcircuits", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS  circuits (idcircuit integer primary key autoincrement, villedepart VARCHAR, villarriver VARCHAR, prix REAl, duree integer);");
        layNaviguer.setVisibility(View.INVISIBLE);
        _btnSave.setVisibility(View.INVISIBLE);
        _btnCancel.setVisibility(View.INVISIBLE);


        _btnRecherche.setOnClickListener(view -> {
            cur = db.rawQuery("select * from circuits where villedepart like ?", new String[]{"%" + _txtRecherche.getText().toString() + "%"});
            try {
                cur.moveToFirst();
                _txtdepart.setText(cur.getString(1));
                _txtarr.setText(cur.getString(2));
                _txtpri.setText(cur.getString(3));
                _txtdure.setText(cur.getString(4));
                if (cur.getCount() == 1) {
                    layNaviguer.setVisibility(View.INVISIBLE);
                } else {
                    layNaviguer.setVisibility(View.VISIBLE);
                    _btsuivant.setEnabled(false);
                    _btprecedent.setEnabled(false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "aucun résultat.", Toast.LENGTH_SHORT).show();
                _txtdepart.setText("");
                _txtarr.setText("");
                _txtpri.setText("");
                _txtdure.setText("");
                layNaviguer.setVisibility(View.INVISIBLE);
            }
        });
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 1;

                _txtdepart.setText("");
                _txtarr.setText("");
                _txtpri.setText("");
                _txtdure.setText("");
                _btnSave.setVisibility(View.VISIBLE);
                _btnCancel.setVisibility(View.VISIBLE);
                _btnUpdate.setVisibility(View.INVISIBLE);
                _btnDelete.setVisibility(View.INVISIBLE);
                _btnAdd.setEnabled(false);


                layNaviguer.setVisibility(View.INVISIBLE);
                layRecherche.setVisibility(View.INVISIBLE);
            }
        });
        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tester si les champs ne sont pas vides
                try {
                    x = cur.getString(0);
                    op = 2;

                    _btnSave.setVisibility(View.VISIBLE);
                    _btnCancel.setVisibility(View.VISIBLE);
                    _btnDelete.setVisibility(View.INVISIBLE);
                    _btnUpdate.setEnabled(false);
                    _btnAdd.setVisibility(View.INVISIBLE);

                    layNaviguer.setVisibility(View.INVISIBLE);
                    layRecherche.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Sélectionnez un compte puis appyuer sur le bouton de modification", Toast.LENGTH_SHORT).show();
                }

            }
        });
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (op == 1){
                    // insertion
                    db.execSQL("insert into circuits ( villedepart , villarriver, prix, duree ) values (?,?,?,?,?);", new String[] { _txtdepart.getText().toString(),_txtarr.getText().toString(),_txtpri.getText().toString(),_txtdure.getText().toString()});
                } else if (op == 2) {
                    // Mise à jour
                    db.execSQL("update circuits set  villedepart=?, villarriver=?, prix=?,duree=? where idcircuit=?;", new String[] { _txtdepart.getText().toString(),_txtarr.getText().toString(),_txtpri.getText().toString(),_txtdure.getText().toString(),x});
                }


                _btnSave.setVisibility(View.INVISIBLE);
                _btnCancel.setVisibility(View.INVISIBLE);
                _btnUpdate.setVisibility(View.VISIBLE);
                _btnDelete.setVisibility(View.VISIBLE);

                _btnAdd.setVisibility(View.VISIBLE);
                _btnAdd.setEnabled(true);
                _btnUpdate.setEnabled(true);
                _btnRecherche.performClick();
                layRecherche.setVisibility(View.VISIBLE);
            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 0;
                _btnSave.setVisibility(View.INVISIBLE);
                _btnCancel.setVisibility(View.INVISIBLE);
                _btnUpdate.setVisibility(View.VISIBLE);
                _btnDelete.setVisibility(View.VISIBLE);

                _btnAdd.setVisibility(View.VISIBLE);
                _btnAdd.setEnabled(true);
                _btnUpdate.setEnabled(true);

                layRecherche.setVisibility(View.VISIBLE);
            }
        });
        _btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    x = cur.getString(0);
                    AlertDialog dial = MesOptions();
                    dial.show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Sélectionner un compte puis appyuer sur le bouton de suppresssion", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        _btprecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur.moveToPrevious();
                    _txtdepart.setText(cur.getString(1));
                    _txtarr.setText(cur.getString(2));
                    _txtpri.setText(cur.getString(3));
                    _txtdure.setText(cur.getString(4));
                    _btsuivant.setEnabled(true);
                    if (cur.isFirst()){
                        _btprecedent.setEnabled(false);
                    }


                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        });
        _btsuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur.moveToNext();
                    _txtdepart.setText(cur.getString(1));
                    _txtarr.setText(cur.getString(2));
                    _txtpri.setText(cur.getString(3));
                    _txtdure.setText(cur.getString(4));
                    _btprecedent.setEnabled(true);
                    if (cur.isLast()){
                        _btsuivant.setEnabled(false);
                    }


                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        });

    }

    private AlertDialog MesOptions() {
        AlertDialog MiDia = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Est ce que vous voulez supprimer ce compte?")
                .setIcon(R.drawable.validate)
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.execSQL("delete from comptes where id=?;", new String[]{cur.getString(0)});
                        _txtdepart.setText("");
                        _txtarr.setText("");
                        _txtpri.setText("");
                        _txtdure.setText("");
                        layNaviguer.setVisibility(View.INVISIBLE);
                        cur.close();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        return MiDia;

    }
}