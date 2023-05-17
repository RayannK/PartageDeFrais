/* RechercheActivity.java                                  17/05/2023 mai 2023
 * IUT Rodez, No copyright
 */

package com.example.partagedefrais;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.model.Depense;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activité dans laquelle sont affiché les résultat de la recherche
 * @author clement.bonaz
 */
public class RechercheActivity extends AppCompatActivity {


    private ListView resultatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        // on récupère l'intention qui a lancé cette activité
        Intent intentionRecu = getIntent();

        // on récupère l'extra envoyant grace à l'intention
        long[] listeDepense = intentionRecu.getLongArrayExtra(MainActivity.CLE_RECHERCHE);

        String[] depenses = new String[listeDepense.length];

        // récupération des dépenses correspondant au id du tableau listeDepense
        for (int i =0; i < listeDepense.length; i++)
        {
            depenses[i] = DepenseDao.getInstance(this).getById(listeDepense[i]).toString();
        }

        resultatList = findViewById(R.id.resultatListView_depense);

        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_list_item_1, depenses);
        resultatList.setAdapter(adaptateur);
    }
}
