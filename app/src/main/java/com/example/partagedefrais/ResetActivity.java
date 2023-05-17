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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.helper.DataHelper;
import com.example.partagedefrais.model.Utilisateur;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO commenter la classe
 * @author rayann.karon
 */
public class ResetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editNombreUtilisateur;
    private Button btnResetCreer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        UtilisateurDao accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();
        DepenseDao accesDepense = DepenseDao.getInstance(this);
        accesDepense.open();
        int nbuser= accesUtilisateur.getAll().size();
        accesUtilisateur.close();
        accesDepense.close();
        if (nbuser != 0 ){
            Intent myIntent = new Intent(ResetActivity.this, MainActivity.class);
            ResetActivity.this.startActivity(myIntent);
            finish();
        }
        
        editNombreUtilisateur = findViewById(R.id.reset_saisi_nombre_utilisateur);
        btnResetCreer = findViewById(R.id.btn_reset_creer);
        btnResetCreer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_creer:
                int nbUtilisateur = DataHelper.getInt(editNombreUtilisateur);
                if (nbUtilisateur <= 0) {
                    break;
                }

                // on désérialise le layout qui est associé à la boîte de saisie d'un pays
                final View boiteSaisie = getLayoutInflater().inflate(R.layout.ajout_utilisateur, null);

                String titre = getResources().getString(R.string.titre_Ajout_utilisateur);

                /*
                 * Création d'une boîte de dialogue pour saisir un aliment
                 */

                // méthode invoquée lorsque l'utilisateur validera la saisie
                AlertDialog addUser = (new AlertDialog.Builder(this)
                        .setView(boiteSaisie)
                        .setPositiveButton(getResources().getString(R.string.bouton_positif),
                           (dialog, leBouton) -> {
                               EditText editNomUtilisateur = boiteSaisie.findViewById(R.id.reset_saisi_nom_utilisateur);
                               UtilisateurDao accesUtilisateur = UtilisateurDao.getInstance(this.getApplicationContext());
                               accesUtilisateur.open();

                               String nomUtilisateur = DataHelper.getString(editNomUtilisateur);

                                   if (DataHelper.isStringEmptyOrNull(nomUtilisateur)) {
//                                       index--;
                                       Toast.makeText(this.getApplicationContext(),"Aucun nom n'a été saisie", Toast.LENGTH_SHORT).show();
                                   }

                                   Utilisateur newUser = new Utilisateur(0,nomUtilisateur);

                                   accesUtilisateur.insert(newUser);

                               accesUtilisateur.close();
                        })
                        .setNegativeButton(getResources().getString(R.string.bouton_negatif), null).create());

                for (int index = 0; index < nbUtilisateur; index++) {
                    addUser.setTitle(titre+index);
                    addUser.show();
                }
                break;
        }
    }
}
