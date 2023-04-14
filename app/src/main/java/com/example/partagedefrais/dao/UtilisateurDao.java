/* UtilisateurDao.java                                            11 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.partagedefrais.helper.UtilisateurHelper;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;

/**
 * Gestion de l'ouverture de la base de données et sa fermeture (ainsi que la fermeture de la connexion).
 * Intérogation base de données et permet des mises à jour sur la table des utilisateurs.
 * @author rayann.karon
 */
public class UtilisateurDao {

    /**
     * Numéro de version de la base de donnée
     */
    private static final int VERSION = 1;
    /**
     * Nom de la base de données qui contiendra les utilisateurs gérés
     */
    private static final String NOM_BD = "utilisateur.db";
    /**
     * Numéro de la colonne contenant l'identifiant de l'utilisateur, la clé
     */
    public static final int COLONNE_CLE = 0;
    /**
     * Numéro de la colonne contenant le prenom de l'utilisateur
     */
    public static final int COLONNE_PRENOM = 1;
    /**
     * Gestionnaire permettant de créer la base de donnée
     */
    private UtilisateurHelper helper;
    /**
     * Base de données contenant la description des utilisateurs
     */
    private SQLiteDatabase base;
    /**
     * DAO utilisé pour chercher les dépenses
     */
    private DepenseDao depenseDao;

    /**
     * Requete pour sélectionner tous les enregistrements de la table
     */
    public static final String REQUETE_TOUT_SELECTIONNER =
            "select *"
            + " from " + UtilisateurHelper.NOM_TABLE
            + ";";

    /** Instance de UtilisateurDao : elle sera unique au sein de l'application */
    private static UtilisateurDao instanceUtilisateurDao;

    /**
     * Création si besoin d'une instance de type UtilisateurDao, et renvoie de celle-ci
     * @param context contexte de l'activité à l'origine de l'appel
     * @return une instance de type UtilisateurDao pour accéder ensuite à la base de données
     */
    public static synchronized UtilisateurDao getInstance(Context context) {
        if (instanceUtilisateurDao == null) {
            // l'instance n'existe pas encore : on la crée
            instanceUtilisateurDao = new UtilisateurDao(context.getApplicationContext());
        }
        return instanceUtilisateurDao;
    }

    /**
     * Constructeur permettant de créer l'objet gestionnaire de la BD
     * * @param leContexte contexte de l'activité créatrice
     */
    private UtilisateurDao(Context leContexte) {
        helper = UtilisateurHelper.getInstance(leContexte, NOM_BD, null, VERSION);
        depenseDao = DepenseDao.getInstance(leContexte);
    }

    /**
     * Ouverture de la base de données
     */
    public void open() {
        base = helper.getWritableDatabase();
    }

    /**
     * Fermeture de la base de données et de la connexion
     */
    public void close() {
        helper.close();
        base.close();
    }

    /**
     * Renvoie d'un curseur sur la totalité des utilisateurs
     * @return un curseur référençant tous les utilisateurs de la base
     */
    public Cursor getCurseur() {
        return base.rawQuery(REQUETE_TOUT_SELECTIONNER, null );
    }

    /**
     * Renvoie tous les utilisateurs présents dans la table, sous la forme d'une liste
     * @return une ArrayList contenant tous les utilisateurs de la table
     */
    public ArrayList<Utilisateur> getAll() {
        Cursor curseurTous = getCurseur();
        ArrayList<Utilisateur> tmp = cursorToListeUtilisateurs(curseurTous);
        curseurTous.close();
        return tmp;
    }

    /**
     * Insère l'utilisateur argument dans la table des utilisateurs
     * @param aInserer utilisateur à insérer dans la table
     * @return l'identifiant de l'enregistrement inséré (ou -1 si ajout impossible)
     */
    public long insert(Utilisateur aInserer) {
        ContentValues enregistrement = new ContentValues();
        enregistrement.put(UtilisateurHelper.PRENOM, aInserer.getPrenom());

        // insertion de l'enregistrement dans la base
        return base.insert(UtilisateurHelper.NOM_TABLE,
                           UtilisateurHelper.PRENOM, enregistrement);
    }

    /**
     * Supprime un utilisateur ansi que ses dépenses de la table des utilisateurs
     * @param prenom nom de l'utilisateur à supprimer
     * @return un entier égal au nombre de lignes supprimées
     */
    public int delete(String prenom) {
        Utilisateur aSupprimer = get(prenom);
        depenseDao.deleteByUtilisateur(aSupprimer.getId());

        return base.delete(UtilisateurHelper.NOM_TABLE,
                           UtilisateurHelper.CLE + " = ?",
                               new String[] { Long.toString(aSupprimer.getId()) });
    }

    /**
     * Modifie un utilisateur avec l'instance de type Utilisateur argument.
     * L'enregistrement à modifier est recherché selon le nom de l'utilisateur argument
     * @param aModifier nouvelle valeur pour l'utilisateur à modifier
     * @return un entier égal au nombre d'enregistrements modifiés
     */
    public int update(Utilisateur aModifier) {
        ContentValues nouveau = new ContentValues();
        nouveau.put(UtilisateurHelper.PRENOM, aModifier.getPrenom());
        return base.update(UtilisateurHelper.NOM_TABLE , nouveau,
                               UtilisateurHelper.PRENOM + " = ?",
                               new String[] {aModifier.getPrenom()} );
    }

    /**
     * Recherche dans la table des utilisateurs, l'utilisateur dont le nom est donné en argument
     * @param prenom nom de l'utilisateur à chercher dans la table des utilisateurs
     * @return l'instance Utilisateur dont le nom est donné en arugment (null si non trouvé)
     */
    public Utilisateur get(String prenom) {
        Cursor c = base.query(UtilisateurHelper.NOM_TABLE,
                                  new String[] {UtilisateurHelper.CLE,
                                                UtilisateurHelper.PRENOM},
                              UtilisateurHelper.PRENOM + " = ? ",
                                  new String[] {prenom}, null, null, null);
        Utilisateur tmp =  cursorToUtilisateurs(c);
        c.close();
        return tmp;
    }

    /**
     * Transforme l'ensemble des utilisateur contenus dans un curseur en une liste d'utilisateur
     * @param c un curseur sur un ensemble d'utilisateur
     * @return une liste contenant les utilisateurs référencés par le curseur
     */
    private ArrayList<Utilisateur> cursorToListeUtilisateurs(Cursor c) {
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<>();
        if (c.getCount() != 0) {
            Utilisateur aAjouter;
            c.moveToFirst();
            /* on parcourt toutes les lignes du curseur, et on ajoute
             * l'utilisateur référencé par le curseur à la liste
             */
            do {
                aAjouter = cursorToUtilisateurs(c);
                listeUtilisateur.add(aAjouter);
            } while (c.moveToNext());
        }
        return listeUtilisateur;
    }
    /**
     * Transforme la première ligne référencée par un curseur
     * (sur la table des utilisateur) en instance de type Utilisateur
     * @param c curseur qui référence une ligne dans la table des utilisateurs
     * @return une instance de type Utilisateur correspondant à celui référencé par
     * le curseur (éventuellement null)
     */
    private Utilisateur cursorToUtilisateurs(Cursor c) {
        Utilisateur aRenvoyer;
        if (c.getCount() == 0) {
            aRenvoyer = null;
        } else {
            c.moveToFirst();
            // on initialise l'instance Utilisateur avec les valeurs des colonnes
            aRenvoyer = new Utilisateur(c.getInt(COLONNE_CLE));
            aRenvoyer.setPrenom(c.getString(COLONNE_PRENOM));
            aRenvoyer.setDepenses(depenseDao.getByUtilisateur(aRenvoyer.getId()));
        }
        return aRenvoyer;
    }
}
