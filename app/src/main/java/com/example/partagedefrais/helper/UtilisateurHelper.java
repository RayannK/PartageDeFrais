/* DepenseHelper.java                                             05 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Gestion la base de données qui contient les utilisateurs
 * @author rayann.karon
 */
public class UtilisateurHelper extends SQLiteOpenHelper {

    /**
     * Nom du champ correspondant à l'identifiant de l'utilisateur, la clé
     */
    public static final String CLE = "_id";

    /**
     * Nom du champ correspondant au prénom de l'utilisateur
     */
    public static final String PRENOM = "prenom";

    /**
     * Nom de la table qui contiendra la liste des utilisateurs de l'application
     */
    public static final String NOM_TABLE = "Utilisateurs";

    /**
     * Requete pour la création de la table
     */
    private static final String CREATION_TABLE =
            "CREATE TABLE " + NOM_TABLE + " ( "
            + CLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRENOM + " TEXT); ";

    /**
     * Requete pour supprimer la table
     */
    private static final String SUPPRIMER_TABLE =
            "DROP TABLE IF EXISTS " + NOM_TABLE + " ;";

    /**
     * Instance de UtilisateurHelper : elle sera unique au sein de l'application
     */
    private static UtilisateurHelper instanceUtilisateurHelper;

    /**
     * Création si besoin d'une instance de type UtilisateurHelper, et renvoie de celle-ci
     * @param context contexte de l'activité à l'origine de l'appel
     * @return une instance de type UtilisateurHelper pour accéder ensuite à la base de données
     */
    public static synchronized UtilisateurHelper getInstance(Context context,
                                                             String nom,
                                                             SQLiteDatabase.CursorFactory fabrique,
                                                             int version) {
        if (instanceUtilisateurHelper == null) {
            // l'instance n'existe pas encore : on la crée
            instanceUtilisateurHelper = new UtilisateurHelper(
                    context.getApplicationContext(), nom, fabrique, version);
        }
        return instanceUtilisateurHelper;
    }

    /**
     * Constructeur de la classe
     * @param contexte contexte de l’appel
     * @param nom      nom de la base de données
     * @param fabrique une fabrique de curseur ou le plus souvent null
     * @param version  entier égal au numéro de version du schéma de la base de données
     */
    private UtilisateurHelper(Context contexte, String nom,
                             SQLiteDatabase.CursorFactory fabrique,
                             int version) {
        super(contexte, nom, fabrique, version);
    }

    @Override
    public void onCreate(SQLiteDatabase base) {
        // exécution de la requête pour créer la base
        base.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase base, int i, int i1) {
        // on détruit et on recrée la base des utilisateurs
        base.execSQL(SUPPRIMER_TABLE);
        onCreate(base);
    }
}
