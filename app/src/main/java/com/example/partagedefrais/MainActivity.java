package com.example.partagedefrais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.partagedefrais.adapter.UtilisateurAdapter;
import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Objet destiné à faciliter l'accès à la table des utilisateurs */
    private UtilisateurDao accesUtilisateur;

    public final static String CLE_RECHERCHE = "com.example.partagedefrais.RECHERCHE";

    /** Objet destiné à faciliter l'accès à la table des dépenses */
    private DepenseDao accesDepense;
    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView utilisateurRecyclerView;

    private UtilisateurAdapter adaptateur;

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
        adaptateur = new UtilisateurAdapter(listeUtilisateur);
        utilisateurRecyclerView.setAdapter(adaptateur);
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

    }

    @Override
    public void onDestroy() {
        accesUtilisateur.close();
        accesDepense.close();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Appel de d'une fonction de traitement en fonction de l'item séléctionné par l'utilisateur
     * @param item
     * @return
     */
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
            case R.id.calcul_final:

                break;
            case R.id.option_reset:
                resetApplication();
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
                                String motSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText motRecherche =
                                        boiteSaisie.findViewById(R.id.saisi_mot_recherche);
                                motSaisi = motRecherche.getText().toString() ;

                                // pour afficher le résultat de la recherche
                                rechercheDepense(motSaisi);
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
                                NomDepenseSaisi = nomDepense.getText().toString() ;

                                String MontantDepenseSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText montantDepense =
                                        boiteSaisie.findViewById(R.id.saisi_montant_depense);
                                MontantDepenseSaisi = montantDepense.getText().toString() ;

                                // pour afficher le résultat de la recherche
                                ajoutDepense(UtilisateurSaisi,NomDepenseSaisi,MontantDepenseSaisi);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.bouton_negatif), null)
                .show();
    }

    public void resetApplication()
    {
        ArrayList<Depense> listDepense = DepenseDao.getInstance(this).getAll();

        for (Depense depense: listDepense) {
            DepenseDao.getInstance(this).delete(depense.getId());
        }

        ArrayList<Utilisateur> listUtilisateur = UtilisateurDao.getInstance(this).getAll();

        for (Utilisateur utilisateur: listUtilisateur) {
            UtilisateurDao.getInstance(this).delete(utilisateur.getId());
        }
    }

    /**
     * Controle des informations passer en parametre puis ajout de la dépense pour un utilisateur
     * @param prenomUtilisateur
     * @param nomDepense
     * @param montantDepense
     */
    public void ajoutDepense(String prenomUtilisateur, String nomDepense, String montantDepense)
    {
        if (prenomUtilisateur.equals("") || nomDepense.equals("") || montantDepense.equals(""))
        {
            Toast.makeText(this, "Les informations saisies ne sont pas valide", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Utilisateur utilisateur = UtilisateurDao.getInstance(this).get(prenomUtilisateur);

            Depense depense = new Depense(0, nomDepense, Double.parseDouble(montantDepense));

            DepenseDao.getInstance(this).insert(depense, utilisateur.getId());

            adaptateur.SetList(UtilisateurDao.getInstance(this).getAll());
            Toast.makeText(this, "Nouvelle dépense enregistré", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Controle du paramettre puis recherche des dépenses qui contient le mot recherché dans leur libéllé
     * @param motRechercher
     */
    public void rechercheDepense(String motRechercher)
    {
        if (motRechercher.equals(""))
        {
            Toast.makeText(this, "Le mot saisi n'est pas valide", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ArrayList<Depense> listDepense =  DepenseDao.getInstance(this).getAll();

            ArrayList<Long> listeIdDepenseMot = new ArrayList<>();

            for (Depense depense: listDepense) {
                if (depense.getNom().contains(motRechercher))
                {
                    listeIdDepenseMot.add(depense.getId());
                }
            }

            if (!listeIdDepenseMot.isEmpty())
            {
                AfficherResultat(listeIdDepenseMot);
            }

            Toast.makeText(this, "Aucune dépense ne contient le mot recherché", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Création d'une nouvelle activité pour afficher le résultat de la recherche
     * @param listeDepense
     */
    public void AfficherResultat(ArrayList<Long> listeDepense)
    {
        Intent intention =
                new Intent(MainActivity.this, RechercheActivity.class);

        long[] tabId = new long[listeDepense.size()];

        for (int i =0; i < listeDepense.size(); i++)
        {
            tabId[i] = listeDepense.get(i);
        }

        intention.putExtra(CLE_RECHERCHE,tabId);

        MainActivity.this.startActivity(intention);
    }

}