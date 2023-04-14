package com.example.partagedefrais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.partagedefrais.adapter.DepenseAdapter;
import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Objet destiné à faciliter l'accès à la table des utilisateurs */
    private UtilisateurDao accesUtilisateur;

    /** Objet destiné à faciliter l'accès à la table des dépenses */
    private DepenseDao accesDepense;
    /**
     * Element permettant d'afficher la liste des photos
     */
    private RecyclerView depenseRecyclerView;
    private ArrayList<Depense> listeDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        depenseRecyclerView = findViewById(R.id.recycler_depense);

        initialiseDatas();

        /*
         * On crée un gestionnaire de layout linéaire, et on l'associe à la
         * liste de type RecyclerView
         */
        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(this);
        depenseRecyclerView.setLayoutManager(gestionnaireLineaire);

        /*
         * On crée un adaptateur personnalisé et permettant de gérer spécifiquement
         * l'affichage des instances de type PhotoParis en tant que item de la liste.
         * Cet adapatateur est associé au RecyclerView
         */
        DepenseAdapter adaptateur = new DepenseAdapter(listeDepense);
        depenseRecyclerView.setAdapter(adaptateur);


    }

    /**
     * Méthode pour initialiser la liste des photos et des textes
     */
    private void initialiseDatas() {
        accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();

        accesDepense = DepenseDao.getInstance(this);
        accesDepense.open();

        ArrayList<Utilisateur> users = accesUtilisateur.getAll();

        listeDepense = accesDepense.getAll();
    }

    @Override
    public void onDestroy() {
        accesUtilisateur.close();
        accesDepense.close();
        super.onDestroy();
    }
}