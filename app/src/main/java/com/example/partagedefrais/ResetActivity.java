/* ResetActivity.java                                             16 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.helper.DataHelper;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO commenter la classe
 *
 * @author rayann.karon
 */
public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editNombreUtilisateur;
    private EditText editNomUtilisateur;
    private TextView textNomUtilisateur;
    private Button btnResetCreer;
    private Button btnResetAjouter;
    private Button btnResetAnnuler;
    private LinearLayout viewAjout;
    private LinearLayout viewCreer;
    private int numUtilisateur;
    private int currentNumUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        numUtilisateur = 0;

        editNombreUtilisateur = findViewById(R.id.reset_saisie_nombre_utilisateur);
        editNomUtilisateur = findViewById(R.id.reset_saisie_nom_utilisateur);
        textNomUtilisateur = findViewById(R.id.reset_nom_utilisateur);
        btnResetCreer = findViewById(R.id.btn_reset_creer);
        btnResetAjouter = findViewById(R.id.btn_reset_ajouter);
        btnResetAnnuler = findViewById(R.id.btn_reset_annuler);

        viewAjout = findViewById(R.id.reset_zone_ajout);
        viewAjout.setVisibility(View.INVISIBLE);

        viewCreer = findViewById(R.id.reset_zone_creer);

        btnResetCreer.setOnClickListener(this);
        btnResetAjouter.setOnClickListener(this);
        btnResetAnnuler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_creer:
                int nbUtilisateur = DataHelper.getInt(editNombreUtilisateur);
                if (nbUtilisateur <= 0) {
                    break;
                }
                viewAjout.setVisibility(View.VISIBLE);
                viewCreer.setVisibility(View.INVISIBLE);
                numUtilisateur = nbUtilisateur;
                currentNumUtilisateur = 1;
                textNomUtilisateur.setText(getString(R.string.reset_user) + currentNumUtilisateur);
                break;
            case R.id.btn_reset_ajouter:
                String nomUser = DataHelper.getString(editNomUtilisateur);
                if (!DataHelper.isStringEmptyOrNull(nomUser)) {
                    UtilisateurDao accesUtilisateur = UtilisateurDao.getInstance(this);
                    accesUtilisateur.open();

                    accesUtilisateur.insert(new Utilisateur(0,nomUser));

                    accesUtilisateur.close();

                    currentNumUtilisateur++;
                    editNomUtilisateur.setText("");
                }
                if (currentNumUtilisateur > numUtilisateur) {
                    Intent myIntent = new Intent(ResetActivity.this, MainActivity.class);
                    ResetActivity.this.startActivity(myIntent);
                    finish();
                }
                textNomUtilisateur.setText(getString(R.string.reset_user) + currentNumUtilisateur);
                break;
            case R.id.btn_reset_annuler:
                viewAjout.setVisibility(View.INVISIBLE);
                viewCreer.setVisibility(View.VISIBLE);
                break;
        }
    }
}
