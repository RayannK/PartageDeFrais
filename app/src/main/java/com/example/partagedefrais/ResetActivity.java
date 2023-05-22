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
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private EditText editNomUtilisateur;
    private TextView textNomUtilisateur;
    private Button btnResetCreer;
    private Button btnResetAjouter;
    private Button btnResetAnnuler;
    private LinearLayout viewAjout;
    private LinearLayout viewCreer;
    private int numUtilisateur;
    private boolean editState;

    private List<Utilisateur> addingUtilisateur;

    private final String CLE_numUtilisateur = "numUtilisateur";
    private final String CLE_editNombreUtilisateur = "editNombreUtilisateur";
    private final String CLE_editNomUtilisateur = "editNomUtilisateur";
    private final String CLE_addingUtilisateur = "addingUtilisateur";
    private final String CLE_editState = "editState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        numUtilisateur = 0;
        addingUtilisateur = new ArrayList<>();

        editNombreUtilisateur = findViewById(R.id.reset_saisie_nombre_utilisateur);
        editNomUtilisateur = findViewById(R.id.reset_saisie_nom_utilisateur);
        textNomUtilisateur = findViewById(R.id.reset_nom_utilisateur);
        btnResetCreer = findViewById(R.id.btn_reset_creer);
        btnResetAjouter = findViewById(R.id.btn_reset_ajouter);
        btnResetAnnuler = findViewById(R.id.btn_reset_annuler);

        viewAjout = findViewById(R.id.reset_zone_ajout);
        viewAjout.setVisibility(View.INVISIBLE);

        viewCreer = findViewById(R.id.reset_zone_creer);

        btnResetCreer.setOnClickListener(this);
        btnResetAjouter.setOnClickListener(this);
        btnResetAnnuler.setOnClickListener(this);

        if (savedInstanceState != null) {
            numUtilisateur = savedInstanceState.getInt(CLE_numUtilisateur);
            editNombreUtilisateur.setText(savedInstanceState.getString(CLE_editNombreUtilisateur));
            editNomUtilisateur.setText(savedInstanceState.getString(CLE_editNomUtilisateur));
            addingUtilisateur= savedInstanceState.getParcelableArrayList(CLE_addingUtilisateur);
            switchMode(savedInstanceState.getBoolean(CLE_editState));
        }

        textNomUtilisateur.setText(getString(R.string.reset_user) + (addingUtilisateur.size()+1));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_creer:
                numUtilisateur = DataHelper.getInt(editNombreUtilisateur);
                if (numUtilisateur <= 0) {
                    break;
                }
                switchMode(true);
                textNomUtilisateur.setText(getString(R.string.reset_user) + (addingUtilisateur.size()+1));
                break;
            case R.id.btn_reset_ajouter:
                String nomUser = DataHelper.getString(editNomUtilisateur);
                if (!DataHelper.isStringEmptyOrNull(nomUser)) {
                    addingUtilisateur.add(new Utilisateur(0,nomUser));

                    editNomUtilisateur.setText("");
                }
                if (addingUtilisateur.size() >= numUtilisateur) {
                    UtilisateurDao accesUtilisateur = UtilisateurDao.getInstance(this);
                    accesUtilisateur.open();

                    for (Utilisateur utilisateur :addingUtilisateur ) {
                        accesUtilisateur.insert(utilisateur);
                    }

                    accesUtilisateur.close();
                    Intent myIntent = new Intent(ResetActivity.this, MainActivity.class);
                    ResetActivity.this.startActivity(myIntent);
                    finish();
                }
                textNomUtilisateur.setText(getString(R.string.reset_user) + (addingUtilisateur.size()+1));
                break;
            case R.id.btn_reset_annuler:
                addingUtilisateur.clear();
                switchMode(false);
                break;
        }
    }

    /**
     * Change le mode entre saisie du nombre ou saisie du nom
     * @param newState true si en mode saisie du nom sinon false
     */
    private void switchMode(Boolean newState) {
        editState = newState;
        if (editState) {
            viewAjout.setVisibility(View.VISIBLE);
            viewCreer.setVisibility(View.INVISIBLE);
        } else {
            viewAjout.setVisibility(View.INVISIBLE);
            viewCreer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle etat) {
        etat.putInt(CLE_numUtilisateur, numUtilisateur);
        etat.putString(CLE_editNombreUtilisateur, DataHelper.getString(editNombreUtilisateur));
        etat.putString(CLE_editNomUtilisateur, DataHelper.getString(editNomUtilisateur));
        etat.putParcelableArrayList(CLE_addingUtilisateur, new ArrayList<Utilisateur>(addingUtilisateur));
        etat.putBoolean(CLE_editState, editState);
        super.onSaveInstanceState(etat);
    }
}
