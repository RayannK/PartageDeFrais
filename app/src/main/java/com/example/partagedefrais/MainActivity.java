package com.example.partagedefrais;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.adapter.DepenseAdapter;
import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.helper.DataHelper;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * Objet destiné à faciliter l'accès à la table des utilisateurs
     */
    private UtilisateurDao accesUtilisateur;

    /**
     * Clé pour l'ajout à l'intent de la liste de recherche
     */
    public final static String CLE_RECHERCHE
            = "com.example.partagedefrais.RECHERCHE";

    /**
     * Objet destiné à faciliter l'accès à la table des dépenses
     */
    private DepenseDao accesDepense;
    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView utilisateurRecyclerView;

    /**
     * adptateur contenant la liste des dépenses
     */
    private DepenseAdapter adaptateur;

    /**
     * liste de toutes les dépenses
     */
    private ArrayList<Depense> listeDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utilisateurRecyclerView = findViewById(R.id.recycler_utilisateur);

        initialiseDatas();
        /* change sur la vue pour initialiser les utilisateur
         * ferme cette vue (afin de ne pas pouvoir y retourner dessus)
         */
        if (accesUtilisateur.getAll().size() <= 0) {
            Intent myIntent = new Intent(MainActivity.this,
                                         ResetActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }

        /*
         * On crée un gestionnaire de layout linéaire, et on l'associe à la
         * liste de type RecyclerView
         */
        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(
                this);
        utilisateurRecyclerView.setLayoutManager(gestionnaireLineaire);

        /*
         * On crée un adaptateur personnalisé et permettant de gérer spécifiquement
         * l'affichage des instances de type PhotoParis en tant que item de la liste.
         * Cet adapatateur est associé au RecyclerView
         */
        adaptateur = new DepenseAdapter(listeDepense);
        utilisateurRecyclerView.setAdapter(adaptateur);
    }

    /**
     * Méthode pour initialiser la liste des photos et des textes
     */
    private void initialiseDatas() {
        // récupère les DAO et les ouvres
        accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();

        accesDepense = DepenseDao.getInstance(this);
        accesDepense.open();

        listeDepense = accesDepense.getAll();
    }

    @Override
    public void onDestroy() {
        // ferme les DAO avant la fermeture de l'application
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
                AfficherBilan();

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
        final View boiteSaisie = getLayoutInflater().inflate(
                R.layout.saisi_nom_recherche, null);

        /*
         * Création d'une boîte de dialogue pour saisir un aliment
         */
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titre_Recherche))
                .setView(boiteSaisie)
                .setPositiveButton(
                        getResources().getString(R.string.bouton_positif),
                        new DialogInterface.OnClickListener() {

                            // méthode invoquée lorsque l'utilisateur validera la saisie
                            public void onClick(DialogInterface dialog,
                                                int leBouton) {
                                String motSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText motRecherche =
                                        boiteSaisie.findViewById(
                                                R.id.saisi_mot_recherche);
                                motSaisi = DataHelper.getString(motRecherche);
                                // pour afficher le résultat de la recherche
                                rechercheDepense(motSaisi);
                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.bouton_negatif), null)
                .show();
    }

    /**
     * Cette méthode affiche une boîte de dialogue permettant
     * à l'utilisateur de saisir une depense
     */
    private void saisirDepense() {

        // on désérialise le layout qui est associé à la boîte de saisie d'un pays
        final View boiteSaisie = getLayoutInflater().inflate(
                R.layout.ajout_depense, null);
        Spinner spinner = (Spinner) boiteSaisie.findViewById(
                R.id.search_user_spinner);
        ArrayList<String> users = new ArrayList<>();
        for (Utilisateur user : accesUtilisateur.getAll()) {
            users.add(user.getPrenom());
        }
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_spinner_item, users);
        adaptateur.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptateur);

        /*
         * Création d'une boîte de dialogue pour saisir un aliment
         */
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titre_Ajout))
                .setView(boiteSaisie)
                .setPositiveButton(
                        getResources().getString(R.string.bouton_positif),
                        new DialogInterface.OnClickListener() {

                            // méthode invoquée lorsque l'utilisateur validera la saisie
                            public void onClick(DialogInterface dialog,
                                                int leBouton) {
                                // on récupère le nom de l'utilisateur sélectionner
                                String UtilisateurSaisi
                                        = spinner.getSelectedItem().toString();

                                String NomDepenseSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText nomDepense =
                                        boiteSaisie.findViewById(
                                                R.id.saisi_nom_depense);
                                NomDepenseSaisi = DataHelper.getString(
                                        nomDepense);

                                String MontantDepenseSaisi;
                                // on récupère un accès sur les zones de saisies de la boîte
                                EditText montantDepense =
                                        boiteSaisie.findViewById(
                                                R.id.saisi_montant_depense);
                                MontantDepenseSaisi = DataHelper.getString(
                                        montantDepense);

                                // pour afficher le résultat de la recherche
                                ajoutDepense(UtilisateurSaisi, NomDepenseSaisi,
                                             MontantDepenseSaisi);
                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.bouton_negatif), null)
                .show();
    }

    /**
     * Vide la base de donnée et force la transition sur la vue
     * d'initialisation des utilisateurs
     */
    public void resetApplication() {
        accesDepense.deleteAll();

        Intent myIntent = new Intent(MainActivity.this, ResetActivity.class);
        MainActivity.this.startActivity(myIntent);
        finish();
    }

    /**
     * Controle des informations passer en parametre puis ajout de la dépense pour un utilisateur
     * @param prenomUtilisateur
     * @param nomDepense
     * @param montantDepense
     */
    public void ajoutDepense(String prenomUtilisateur, String nomDepense,
                             String montantDepense) {
        if (prenomUtilisateur.equals("") || nomDepense.equals("") ||
            montantDepense.equals("")) {
            Toast.makeText(this, "Les informations saisies ne sont pas valide",
                           Toast.LENGTH_SHORT).show();
        } else {
            Utilisateur utilisateur = accesUtilisateur.get(prenomUtilisateur);

            Depense depense = new Depense(0, utilisateur, nomDepense,
                                          Double.parseDouble(montantDepense));

            accesDepense.insert(depense, utilisateur.getId());

            adaptateur.SetList(accesDepense.getAll());
            Toast.makeText(this, "Nouvelle dépense enregistré",
                           Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Controle du paramettre puis recherche des dépenses qui contient le mot recherché dans leur libéllé
     * @param motRechercher
     */
    public void rechercheDepense(String motRechercher) {
        if (DataHelper.isStringEmptyOrNull(motRechercher)) {
            Toast.makeText(this, "Le mot saisi n'est pas valide",
                           Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Depense> listDepense = accesDepense.getAll();

            ArrayList<Long> listeIdDepenseMot = new ArrayList<>();

            for (Depense depense : listDepense) {
                if (depense.getNom().toLowerCase()
                           .contains(motRechercher.toLowerCase())) {
                    listeIdDepenseMot.add(depense.getId());
                }
            }

            if (!listeIdDepenseMot.isEmpty()) {
                AfficherResultat(listeIdDepenseMot);
            } else {
                Toast.makeText(this,
                               "Aucune dépense ne contient le mot recherché",
                               Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Création d'une nouvelle activité pour afficher le résultat de la recherche
     * @param listeDepense
     */
    public void AfficherResultat(ArrayList<Long> listeDepense) {
        Intent intention =
                new Intent(MainActivity.this, RechercheActivity.class);

        long[] tabId = new long[listeDepense.size()];

        for (int i = 0; i < listeDepense.size(); i++) {
            tabId[i] = listeDepense.get(i);
        }

        intention.putExtra(CLE_RECHERCHE, tabId);

        MainActivity.this.startActivity(intention);
    }

    /**
     * Création d'une nouvelle activité pour afficher le bilan des dépenses
     */
    public void AfficherBilan() {
        Intent intention =
                new Intent(MainActivity.this, BilanActivity.class);

        MainActivity.this.startActivity(intention);
    }

}