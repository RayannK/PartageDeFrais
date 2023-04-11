package com.example.partagedefrais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

public class MainActivity extends AppCompatActivity {

    /** Objet destiné à faciliter l'accès à la table des utilisateurs */
    private UtilisateurDao accesUtilisateur;

    /** Objet destiné à faciliter l'accès à la table des dépenses */
    private DepenseDao accesDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accesUtilisateur = UtilisateurDao.getInstance(this);
        accesUtilisateur.open();

        accesUtilisateur.insert(new Utilisateur(0,"jonny"));
        accesUtilisateur.insert(new Utilisateur(0,"albert"));

        accesDepense = DepenseDao.getInstance(this);
        accesDepense.open();

        Utilisateur jonny = accesUtilisateur.get("jonny");
        accesDepense.insert(new Depense(0, "bar", 10.0),jonny.getId());
    }

    @Override
    public void onDestroy() {
        accesUtilisateur.close();
        accesDepense.close();
        super.onDestroy();
    }
}