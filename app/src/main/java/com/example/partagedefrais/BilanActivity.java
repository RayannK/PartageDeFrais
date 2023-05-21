package com.example.partagedefrais;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.List;

public class BilanActivity extends AppCompatActivity {

    private ListView bilanListe;

    private TextView depenseTotal;

    private TextView bilanPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilan);

        List<Depense> listeDepense = DepenseDao.getInstance(this).getAll();
        List<Utilisateur> listeUtilisateur = UtilisateurDao.getInstance(this).getAll();


        Double sommeDepense = 0.0;

        for (Depense depense: listeDepense) {
            sommeDepense +=  depense.getMontant();
        }

        depenseTotal = findViewById(R.id.bilan_final);

        bilanPart = findViewById(R.id.bilan_part);

        depenseTotal.setText("La somme total des dépense est de : "+sommeDepense);

        Double partUtilisateur = sommeDepense / listeUtilisateur.size();

        bilanPart.setText("Chaque utilisateur doit donc payer : "+partUtilisateur);


        String[] bilanUtilisateur = calculBilanUtilissateur(listeDepense, listeUtilisateur, partUtilisateur);


        bilanListe = findViewById(R.id.resultatListView_bilan);

        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_list_item_1, bilanUtilisateur);
        bilanListe.setAdapter(adaptateur);
    }

    public String[] calculBilanUtilissateur(List<Depense> depenses, List<Utilisateur> utilisateurs, double partUtilisateur) {
        String[] bilanUtilisateur = new String[utilisateurs.size()];


        for (int i =0; i < utilisateurs.size(); i++) {
            double depenseUtilisateur = 0.0;

            for (Depense depense : depenses) {
                if (utilisateurs.get(i).getId() == depense.getUtilisateur().getId())
                {
                    depenseUtilisateur += depense.getMontant();
                }
            }

            double delta = depenseUtilisateur - partUtilisateur;

            if (delta > 0)
            {
                bilanUtilisateur[i] = utilisateurs.get(i).getPrenom()+" à payer "+depenseUtilisateur+" euros de dépense."
                        + " Il doit être remboursé de : "+delta+" euros";
            }
            else
            {
                bilanUtilisateur[i] = utilisateurs.get(i).getPrenom()+" à payer "+depenseUtilisateur+" euros de dépense."
                        +"Il doit payer : "+(-delta)+" euros";
            }
        }

        return bilanUtilisateur;
    }

}
