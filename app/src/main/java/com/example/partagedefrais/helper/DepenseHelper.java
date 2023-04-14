/* DepenseHelper.java                                             05 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Gestion la base de données qui contient les dépenses
 * @author rayann.karon
 */
public class DepenseHelper extends SQLiteOpenHelper {

    /** Nom du champ correspondant à l'identifiant de la dépense, la clé */
    public static final String CLE = "_id";

    /** Nom du champ correspondant au nom de la dépense */
    public static final String NOM = "nom";

    /** Nom du champ correspondant au montant de la dépense */
    public static final String MONTANT = "montant";

    /** Nom du champ correspondant a l'identifiant de l'utilisateur */
    public static final String CLE_UTILISATEUR = "id_utilisateur";

    /** Nom de la table qui contiendra la liste des dépenses de l'application */
    public static final String NOM_TABLE = "Depenses";

    /** Requete pour la création de la table */
    private static final String CREATION_TABLE =
            "CREATE TABLE " + NOM_TABLE + " ( "
            + CLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOM + " TEXT, "
            + MONTANT + " TEXT, "
            + CLE_UTILISATEUR + " INTEGER, "
            + "FOREIGN KEY("+CLE_UTILISATEUR+") REFERENCES "+UtilisateurHelper.NOM_TABLE+"("+UtilisateurHelper.CLE+")); ";

    /** Requete pour supprimer la table */
    private static final String SUPPRIMER_TABLE =
            "DROP TABLE IF EXISTS " + NOM_TABLE + " ;" ;

    /**
     * Instance de UtilisateurHelper : elle sera unique au sein de l'application
     */
    private static DepenseHelper instanceDepenseHelper;

    /**
     * Création si besoin d'une instance de type DepenseHelper, et renvoie de celle-ci
     * @param context contexte de l'activité à l'origine de l'appel
     * @return une instance de type DepenseHelper pour accéder ensuite à la base de données
     */
    public static synchronized DepenseHelper getInstance(Context context,
                                                             String nom,
                                                             SQLiteDatabase.CursorFactory fabrique,
                                                             int version) {
        if (instanceDepenseHelper == null) {
            // l'instance n'existe pas encore : on la crée
            instanceDepenseHelper = new DepenseHelper(
                    context.getApplicationContext(), nom, fabrique, version);
        }
        return instanceDepenseHelper;
    }

    /**
     * Constructeur de la classe
     * @param contexte contexte de l’appel
     * @param nom nom de la base de données
     * @param fabrique une fabrique de curseur ou le plus souvent null
     * @param version entier égal au numéro de version du schéma de la base de données
     */
    private DepenseHelper(Context contexte, String nom, SQLiteDatabase.CursorFactory fabrique, int version) {
        super(contexte, nom, fabrique, version);
    }

    @Override
    public void onCreate(SQLiteDatabase base) {
        // exécution de la requête pour créer la base
        base.execSQL(CREATION_TABLE);

        ContentValues enregistrement = new ContentValues();
        enregistrement.put(DepenseHelper.NOM, "bar");
        enregistrement.put(DepenseHelper.MONTANT, 10.0);
        enregistrement.put(DepenseHelper.CLE_UTILISATEUR, 1);

        // insertion de l'enregistrement dans la base
        base.insert(DepenseHelper.NOM_TABLE, DepenseHelper.NOM, enregistrement);

        enregistrement.put(DepenseHelper.NOM, "café");
        enregistrement.put(DepenseHelper.MONTANT, 19.6);
        enregistrement.put(DepenseHelper.CLE_UTILISATEUR, 2);
        // insertion de l'enregistrement dans la base
        base.insert(DepenseHelper.NOM_TABLE, DepenseHelper.NOM, enregistrement);

        enregistrement.put(DepenseHelper.NOM, "hôtel");
        enregistrement.put(DepenseHelper.MONTANT, 151.96);
        enregistrement.put(DepenseHelper.CLE_UTILISATEUR, 2);
        // insertion de l'enregistrement dans la base
        base.insert(DepenseHelper.NOM_TABLE, DepenseHelper.NOM, enregistrement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase base, int i, int i1) {
        // on détruit et on recrée la base des dépenses
        base.execSQL(SUPPRIMER_TABLE);
        onCreate(base);
    }
}
