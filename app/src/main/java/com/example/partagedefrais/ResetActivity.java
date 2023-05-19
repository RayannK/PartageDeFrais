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
    private Button btnResetCreer;

    /**
     * Objet destiné à faciliter l'accès à la table des dépenses
     */
    private UtilisateurDao accesUtilisateur;

    private AlertDialog addUserDialog;

    private List<Utilisateur> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);


        accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();

        editNombreUtilisateur = findViewById(R.id.reset_saisi_nombre_utilisateur);
        btnResetCreer = findViewById(R.id.btn_reset_creer);
        btnResetCreer.setOnClickListener(this);


        // on désérialise le layout qui est associé à la boîte de saisie d'un pays
        final View boiteSaisie = getLayoutInflater().inflate(R.layout.ajout_utilisateur, null);

        addUserDialog = (new AlertDialog.Builder(this)
                .setView(boiteSaisie)
                .setPositiveButton(getResources().getString(R.string.bouton_positif),
                        (dialog, leBouton) -> {
                            EditText editNomUtilisateur = boiteSaisie.findViewById(R.id.reset_saisi_nom_utilisateur);

                            String nomUtilisateur = DataHelper.getString(editNomUtilisateur);

                            if (DataHelper.isStringEmptyOrNull(nomUtilisateur)) {
//                                       index--;
                                Toast.makeText(this.getApplicationContext(), "Aucun nom n'a été saisie", Toast.LENGTH_SHORT).show();
                            }

                            users.add(new Utilisateur(0, nomUtilisateur));
                        })
                .setNegativeButton(getResources().getString(R.string.bouton_negatif), null).create());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_creer:
                int nbUtilisateur = DataHelper.getInt(editNombreUtilisateur);
                if (nbUtilisateur <= 0) {
                    break;
                }

                users = addUser(nbUtilisateur);

                for (Utilisateur user : users) {
                    accesUtilisateur.insert(user);
                }

//                Intent myIntent = new Intent(ResetActivity.this, MainActivity.class);
//                ResetActivity.this.startActivity(myIntent);
//                finish();

                break;
        }
    }

    public synchronized List<Utilisateur> addUser(int number) {
        List<Utilisateur> users = new ArrayList<>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String titre = getResources().getString(R.string.titre_Ajout_utilisateur);

                // méthode invoquée lorsque l'utilisateur validera la saisie

                for (int index = 0; index < number; index++) {
                    addUserDialog.setTitle(titre + (users.size()+1));
                    addUserDialog.show();
                }
            }
        });
        return users;
    }

    @Override
    public void onDestroy() {
        accesUtilisateur.close();
        super.onDestroy();
    }
}
