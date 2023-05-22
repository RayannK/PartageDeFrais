/* ResetActivity.java                                             16 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partagedefrais.dao.UtilisateurDao;
import com.example.partagedefrais.helper.DataHelper;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rayann.karon
 */
public class ResetActivity extends AppCompatActivity
        implements View.OnClickListener {

    /**
     * edit text contenant le nombre d'utilisateur à créer
     */
    private EditText editNombreUtilisateur;

    /**
     * edit text contenant le nom de l'utilisateur a créer
     */
    private EditText editNomUtilisateur;

    /**
     * nom de l'utilisateur a créer
     */
    private TextView textNomUtilisateur;

    /**
     * boutton de validation du nombre d'utilisateur
     */
    private Button btnResetCreer;

    /**
     * boutton validant l'ajout d'un utilisateur
     */
    private Button btnResetAjouter;

    /**
     * boutton annulant l'ajout de tout les utilisateurs
     */
    private Button btnResetAnnuler;

    /**
     * vue contenant l'ajout d'un utilisateur
     */
    private LinearLayout viewAjout;

    /**
     * vue contenant la sélection du nombre d'utilisateur
     */
    private LinearLayout viewCreer;

    /**
     * nombre d'utilisateur a ajouter
     */
    private int numUtilisateur;

    /**
     * etat de l'application (saisie du nombre d'utilisateur ou saisie de l'utilisateur)
     */
    private boolean editState;

    /**
     * liste temporaire contenant tout les utilisateur a créer
     */
    private List<Utilisateur> addingUtilisateur;

    /**
     * clé de sauvegarde du nombre d'utilisateur
     */
    private final String CLE_numUtilisateur = "numUtilisateur";

    /**
     * clé de sauvegarde du contenu de l'edit text du nombre
     */
    private final String CLE_editNombreUtilisateur = "editNombreUtilisateur";

    /**
     * clé de sauvegarde du contenu de l'edit text du nom
     */
    private final String CLE_editNomUtilisateur = "editNomUtilisateur";

    /**
     * clé de sauvegarde de la liste des utilisateur a créer
     */
    private final String CLE_addingUtilisateur = "addingUtilisateur";

    /**
     * clé de sauvegarde de l'état de l'ajout
     */
    private final String CLE_editState = "editState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        numUtilisateur = 0;
        addingUtilisateur = new ArrayList<>();

        editNombreUtilisateur = findViewById(
                R.id.reset_saisie_nombre_utilisateur);
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
            editNombreUtilisateur.setText(
                    savedInstanceState.getString(CLE_editNombreUtilisateur));
            editNomUtilisateur.setText(
                    savedInstanceState.getString(CLE_editNomUtilisateur));
            addingUtilisateur = savedInstanceState.getParcelableArrayList(
                    CLE_addingUtilisateur);
            switchMode(savedInstanceState.getBoolean(CLE_editState));
        }

        textNomUtilisateur.setText(getString(R.string.reset_user) +
                                   (addingUtilisateur.size() + 1));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_creer:
                numUtilisateur = DataHelper.getInt(editNombreUtilisateur);
                // si un nombre positif est entré change de mode pour saisie le nom
                if (numUtilisateur <= 0) {
                    break;
                }
                switchMode(true);
                textNomUtilisateur.setText(getString(R.string.reset_user) +
                                           (addingUtilisateur.size() + 1));
                break;
            case R.id.btn_reset_ajouter:
                // récupère le nom saisie
                String nomUser = DataHelper.getString(editNomUtilisateur);

                // vérifie si il n'est pas null sinon ne fait rien
                if (!DataHelper.isStringEmptyOrNull(nomUser)) {
                    // crée l'utilisateur et l'ajout à la liste
                    addingUtilisateur.add(new Utilisateur(0, nomUser));

                    // vide le contenu de l'édit text
                    editNomUtilisateur.setText("");
                }

                // vérifie si le nombre d'utilisateur créer correspond au nomrbe souhaité
                if (addingUtilisateur.size() >= numUtilisateur) {
                    UtilisateurDao accesUtilisateur
                            = UtilisateurDao.getInstance(this);
                    accesUtilisateur.open();

                    // crée les utilisateur dans la base de donnée
                    for (Utilisateur utilisateur : addingUtilisateur) {
                        accesUtilisateur.insert(utilisateur);
                    }

                    accesUtilisateur.close();

                    // force le changement d'activité sur l'activité principale
                    Intent myIntent = new Intent(ResetActivity.this,
                                                 MainActivity.class);
                    ResetActivity.this.startActivity(myIntent);

                    // ferme cette activité (pour ne pas pouvoir y revenir)
                    finish();
                }

                // update le numéro de l'utilisateur devant être ajouté
                textNomUtilisateur.setText(getString(R.string.reset_user) +
                                           (addingUtilisateur.size() + 1) +
                                           " / " + numUtilisateur);
                break;
            case R.id.btn_reset_annuler:
                // vide la liste et remet l'état à la sélection du nombre
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
        etat.putString(CLE_editNombreUtilisateur,
                       DataHelper.getString(editNombreUtilisateur));
        etat.putString(CLE_editNomUtilisateur,
                       DataHelper.getString(editNomUtilisateur));
        etat.putParcelableArrayList(CLE_addingUtilisateur,
                                    new ArrayList<Utilisateur>(
                                            addingUtilisateur));
        etat.putBoolean(CLE_editState, editState);
        super.onSaveInstanceState(etat);
    }
}
