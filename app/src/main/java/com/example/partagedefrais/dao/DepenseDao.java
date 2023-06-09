/* DepenseDao.java                                                11 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.partagedefrais.helper.DepenseHelper;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

import java.util.ArrayList;

/**
 * Gestion de l'ouverture de la base de données et sa fermeture (ainsi que la fermeture de la connexion).
 * Intérogation base de données et permet des mises à jour sur la table des dépenses.
 * @author rayann.karon
 */
public class DepenseDao {

    /**
     * Numéro de version de la base de donnée
     */
    private static final int VERSION = 1;
    /**
     * Nom de la base de données qui contiendra les dépenses gérés
     */
    private static final String NOM_BD = "depense.db";
    /**
     * Numéro de la colonne contenant l'identifiant de depense, la clé
     */
    public static final int COLONNE_CLE = 0;
    /**
     * Numéro de la colonne contenant le nom de la depense
     */
    public static final int COLONNE_NOM = 1;
    /**
     * Numéro de la colonne contenant le montant de la depense
     */
    public static final int COLONNE_MONTANT = 2;
    /**
     * Numéro de la colonne contenant la clé de l'utilisateur de la depense
     */
    public static final int COLONNE_CLE_UTILISATEUR = 3;
    /**
     * Gestionnaire permettant de créer la base de donnée
     */
    private DepenseHelper helper;
    /**
     * Base de données contenant la description des dépenses
     */
    private SQLiteDatabase base;

    /**
     * DAO utilisé pour chercher les dépenses
     */
    private UtilisateurDao utilisateurDao;

    /**
     * Requete pour sélectionner tous les enregistrements de la table
     */
    public static final String REQUETE_TOUT_SELECTIONNER =
            "select *"
            + " from " + DepenseHelper.NOM_TABLE
            + ";";

    /**
     * Instance de DepenseDao : elle sera unique au sein de l'application
     */
    private static DepenseDao instanceDepenseDao;

    /**
     * Création si besoin d'une instance de type DepenseDao, et renvoie de celle-ci
     * @param context contexte de l'activité à l'origine de l'appel
     * @return une instance de type DepenseDao pour accéder ensuite à la base de données
     */
    public static synchronized DepenseDao getInstance(Context context) {
        if (instanceDepenseDao == null) {
            // l'instance n'existe pas encore : on la crée
            instanceDepenseDao = new DepenseDao(
                    context.getApplicationContext());
        }
        return instanceDepenseDao;
    }

    /**
     * Constructeur permettant de créer l'objet gestionnaire de la BD
     * * @param leContexte contexte de l'activité créatrice
     */
    private DepenseDao(Context leContexte) {
        helper = DepenseHelper.getInstance(leContexte, NOM_BD, null, VERSION);
        utilisateurDao = UtilisateurDao.getInstance(leContexte);
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
     * Renvoie d'un curseur sur la totalité des dépenses
     * @return un curseur référençant toutes les dépenses de la base
     */
    public Cursor getCurseur() {
        return base.rawQuery(REQUETE_TOUT_SELECTIONNER, null);
    }

    /**
     * Renvoie toutes les dépenses présentes dans la table, sous la forme d'une liste
     * @return une ArrayList contenant toutes les dépenses de la table
     */
    public ArrayList<Depense> getAll() {
        Cursor curseurTous = getCurseur();
        ArrayList<Depense> tmp = cursorToListeDepenses(curseurTous);
        curseurTous.close();
        return tmp;
    }

    /**
     * Insère la dépense argument dans la table des dépenses
     * @param aInserer dépense à insérer dans la table
     * @return l'identifiant de l'enregistrement inséré (ou -1 si ajout impossible)
     */
    public long insert(Depense aInserer, long idUtilisateur) {
        ContentValues enregistrement = new ContentValues();
        enregistrement.put(DepenseHelper.NOM, aInserer.getNom());
        enregistrement.put(DepenseHelper.MONTANT, aInserer.getMontant());
        enregistrement.put(DepenseHelper.CLE_UTILISATEUR, idUtilisateur);

        // insertion de l'enregistrement dans la base
        return base.insert(DepenseHelper.NOM_TABLE,
                           DepenseHelper.CLE, enregistrement);
    }

    /**
     * Supprime une dépense de la table des dépense
     * @return un entier égal au nombre de lignes supprimées
     */
    public int deleteAll() {
        utilisateurDao.deleteAll();

        return base.delete(DepenseHelper.NOM_TABLE,
                           "",
                           new String[]{});
    }

    /**
     * Modifie une dépense avec l'instance de type Depense argument.
     * L'enregistrement à modifier est recherché selon le nom de la dépense argument
     * @param aModifier nouvelle valeur pour la dépense à modifier
     * @return un entier égal au nombre d'enregistrements modifiés
     */
    public int update(Depense aModifier) {
        ContentValues nouveau = new ContentValues();
        nouveau.put(DepenseHelper.NOM, aModifier.getNom());
        nouveau.put(DepenseHelper.MONTANT, aModifier.getMontant());
        return base.update(DepenseHelper.NOM_TABLE, nouveau,
                           DepenseHelper.NOM + " = ?",
                           new String[]{aModifier.getNom()});
    }

    /**
     * Recherche dans la table des dépenses, la dépense dont le nom est donné en argument
     * @param id identifiant de la dépense à chercher dans la table des dépenses
     * @return l'instance Depense dont le nom est donné en arugment (null si non trouvé)
     */
    public Depense getById(long id) {
        Cursor c = base.query(DepenseHelper.NOM_TABLE,
                              new String[]{
                                      DepenseHelper.CLE,
                                      DepenseHelper.NOM,
                                      DepenseHelper.MONTANT,
                                      DepenseHelper.CLE_UTILISATEUR
                              },
                              DepenseHelper.CLE + " = ? ",
                              new String[]{id + ""}, null, null, null);
        Depense tmp = cursorToDepenses(c);
        c.close();
        return tmp;
    }

    /**
     * Transforme l'ensemble des dépenses contenus dans un curseur en une liste de dépense
     * @param c un curseur sur un ensemble de dépense
     * @return une liste contenant les dépenses référencés par le curseur
     */
    private ArrayList<Depense> cursorToListeDepenses(Cursor c) {
        ArrayList<Depense> listeDepense = new ArrayList<>();
        if (c.getCount() != 0) {
            Depense aAjouter;
            c.moveToFirst();
            /* on parcourt toutes les lignes du curseur, et on ajoute
             * le dépense référencé par le curseur à la liste
             */
            do {
                Utilisateur user = utilisateurDao.getById(
                        c.getLong(COLONNE_CLE_UTILISATEUR));

                aAjouter = new Depense(c.getLong(COLONNE_CLE), user);
                aAjouter.setNom(c.getString(COLONNE_NOM));
                aAjouter.setMontant(c.getDouble(COLONNE_MONTANT));
                listeDepense.add(aAjouter);
            } while (c.moveToNext());
        }
        return listeDepense;
    }

    /**
     * Transforme la première ligne référencée par un curseur
     * (sur la table des dépenses) en instance de type Dépense
     * @param c curseur qui référence une ligne dans la table des dépenses
     * @return une instance de type Dépense correspondant à celui référencé par
     * le curseur (éventuellement null)
     */
    private Depense cursorToDepenses(Cursor c) {
        Depense aRenvoyer;
        if (c.getCount() == 0) {
            aRenvoyer = null;
        } else {
            c.moveToFirst();
            Utilisateur user = utilisateurDao.getById(
                    c.getLong(COLONNE_CLE_UTILISATEUR));

            // on initialise l'instance Depense avec les valeurs des colonnes
            aRenvoyer = new Depense(c.getLong(COLONNE_CLE), user);
            aRenvoyer.setNom(c.getString(COLONNE_NOM));
            aRenvoyer.setMontant(c.getDouble(COLONNE_MONTANT));
        }
        return aRenvoyer;
    }
}
