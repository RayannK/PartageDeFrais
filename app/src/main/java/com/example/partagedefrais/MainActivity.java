package com.example.partagedefrais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

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
                // TODO
                break;
            case R.id.option_rechercher:
                // TODO
                break;
            case R.id.option_reset:
                // TODO
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}