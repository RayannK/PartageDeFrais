package com.example.partagedefrais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.partagedefrais.adapter.UtilisateurAdapter;
import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Objet destiné à faciliter l'accès à la table des utilisateurs */
    private UtilisateurDao accesUtilisateur;

    /** Objet destiné à faciliter l'accès à la table des dépenses */
    private DepenseDao accesDepense;
    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView utilisateurRecyclerView;

    private ArrayList<Depense> listeDepense;
    private ArrayList<Utilisateur> listeUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utilisateurRecyclerView = findViewById(R.id.recycler_utilisateur);

        initialiseDatas();

        /*
         * On crée un gestionnaire de layout linéaire, et on l'associe à la
         * liste de type RecyclerView
         */
        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        utilisateurRecyclerView.setLayoutManager(gestionnaireLineaire);

        /*
         * On crée un adaptateur personnalisé et permettant de gérer spécifiquement
         * l'affichage des instances de type PhotoParis en tant que item de la liste.
         * Cet adapatateur est associé au RecyclerView
         */
//        DepenseAdapter adaptateur = new DepenseAdapter(listeDepense);
        UtilisateurAdapter adaptateur = new UtilisateurAdapter(listeUtilisateur);
        utilisateurRecyclerView.setAdapter(adaptateur);

        registerForContextMenu(utilisateurRecyclerView);
    }

    /**
     * Méthode pour initialiser la liste des photos et des textes
     */
    private void initialiseDatas() {
        accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();

        accesDepense = DepenseDao.getInstance(this);
        accesDepense.open();

        listeUtilisateur = accesUtilisateur.getAll();

//        listeDepense = accesDepense.getAll();
    }

    @Override
    public void onDestroy() {
        accesUtilisateur.close();
        accesDepense.close();
        super.onDestroy();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo information =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.i("Partage","Menu context");
        switch (item.getItemId()) {
            case R.id.voirDetail:
                // TODO
                break;

            case R.id.modifierDepense:
                // TODO
                break;

            case R.id.annuler :
                break;
        }
        return (super.onContextItemSelected(item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // réaliser l'opération souhaitée par l'utilisateur
        switch (item.getItemId()) {
            case R.id.option_ajouter:
                saisirDepense();
                break;
            case R.id.option_rechercher:
                saisirRechercherMot();
                break;
            case R.id.option_reset:
                // TODO
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Cette méthode affiche une boîte de dialogue permettant
     * à l'utilisateur de saisir un nom d'aliment
     */
    private void saisirRechercherMot() {

        // on désérialise le layout qui est associé à la boîte de saisie d'un pays
        final View boiteSaisie = getLayoutInflater().inflate(R.layout.saisi_nom_recherche, null);

        /*
         * Création d'une boîte de dialogue pour saisir un aliment
         */
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titre_Recherche))
                .setView(boiteSaisie)
                .setPositiveButton(getResources().getString(R.string.bouton_positif),
                        new DialogInterface.OnClickListener() {

                            // méthode invoquée lorsque l'utilisateur validera la saisie
                            public void onClick(DialogInterface dialog,
                                                int leBouton) {
                                String alimentSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText nomAliment =
                                        boiteSaisie.findViewById(R.id.saisi_mot_recherche);
                                alimentSaisi = nomAliment.getText().toString() ;

                                // pour afficher le résultat de la recherche
                                //TODO appel méthode pour l'affichage du resultat
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.bouton_negatif), null)
                .show();
    }

    /**
     * Cette méthode affiche une boîte de dialogue permettant
     * à l'utilisateur de saisir une depense
     */
    private void saisirDepense() {

        // on désérialise le layout qui est associé à la boîte de saisie d'un pays
        final View boiteSaisie = getLayoutInflater().inflate(R.layout.ajout_depense, null);

        /*
         * Création d'une boîte de dialogue pour saisir un aliment
         */
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titre_Ajout))
                .setView(boiteSaisie)
                .setPositiveButton(getResources().getString(R.string.bouton_positif),
                        new DialogInterface.OnClickListener() {

                            // méthode invoquée lorsque l'utilisateur validera la saisie
                            public void onClick(DialogInterface dialog,
                                                int leBouton) {
                                String UtilisateurSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText nomUtilisateur =
                                        boiteSaisie.findViewById(R.id.saisi_nom_utilisateur);
                                UtilisateurSaisi = nomUtilisateur.getText().toString() ;

                                String NomDepenseSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText nomDepense =
                                        boiteSaisie.findViewById(R.id.saisi_nom_depense);
                                NomDepenseSaisi = nomUtilisateur.getText().toString() ;

                                String MontantDepenseSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText montantDepense =
                                        boiteSaisie.findViewById(R.id.saisi_montant_depense);
                                MontantDepenseSaisi = nomUtilisateur.getText().toString() ;

                                // pour afficher le résultat de la recherche
                                //TODO appel méthode pour l'ajout d'une depense
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.bouton_negatif), null)
                .show();
    }

}