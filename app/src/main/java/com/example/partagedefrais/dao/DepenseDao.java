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
    private DepenseHelper gestionnaireBase;
    /**
     * Base de données contenant la description des dépenses
     */
    private SQLiteDatabase base;

    /**
     * Requete pour sélectionner tous les enregistrements de la table
     */
    public static final String REQUETE_TOUT_SELECTIONNER =
            "select *"
            + " from " + DepenseHelper.NOM_TABLE
            + " order by " + DepenseHelper.NOM;

    /** Instance de DepenseDao : elle sera unique au sein de l'application */
    private static DepenseDao instanceDepenseDao;

    /**
     * Création si besoin d'une instance de type DepenseDao, et renvoie de celle-ci
     * @param context contexte de l'activité à l'origine de l'appel
     * @return une instance de type DepenseDao pour accéder ensuite à la base de données
     */
    public static synchronized DepenseDao getInstance(Context context) {
        if (instanceDepenseDao == null) {
            // l'instance n'existe pas encore : on la crée
            instanceDepenseDao = new DepenseDao(context.getApplicationContext());
        }
        return instanceDepenseDao;
    }

    /**
     * Constructeur permettant de créer l'objet gestionnaire de la BD
     * * @param leContexte contexte de l'activité créatrice
     */
    private DepenseDao(Context leContexte) {
        gestionnaireBase = DepenseHelper.getInstance(leContexte, NOM_BD, null, VERSION);
    }

    /**
     * Ouverture de la base de données
     */
    public void open() {
        base = gestionnaireBase.getWritableDatabase();
    }

    /**
     * Fermeture de la base de données et de la connexion
     */
    public void close() {
        gestionnaireBase.close();
        base.close();
    }

    /**
     * Renvoie d'un curseur sur la totalité des dépenses
     * @return un curseur référençant toutes les dépenses de la base
     */
    public Cursor getCurseur() {
        return base.rawQuery(REQUETE_TOUT_SELECTIONNER, null );
    }

    /**
     * Renvoie toutes les dépenses présentes dans la table, sous la forme d'une liste
     * @return une ArrayList contenant toutes les dépenses de la table
     */
    public ArrayList<Depense> getAll() {
        Cursor curseurTous = getCurseur();
        return cursorToListeDepenses(curseurTous);
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
                           DepenseHelper.NOM, enregistrement);
    }

    /**
     * Supprime une dépense de la table des dépense
     * @param nom nom de la dépense à supprimer
     * @return un entier égal au nombre de lignes supprimées
     */
    public int delete(String nom) {
        return base.delete(DepenseHelper.NOM_TABLE,
                           DepenseHelper.NOM + " = ?",
                           new String[] { nom });
    }

    /**
     * Supprime une dépense de la table des dépense
     * @param id identifiant de l'utilisateur dont les dépenses sont a supprimer
     * @return un entier égal au nombre de lignes supprimées
     */
    public int deleteByUtilisateur(long id) {
        return base.delete(DepenseHelper.NOM_TABLE,
                           DepenseHelper.CLE_UTILISATEUR + " = ?",
                           new String[] { Long.toString(id) });
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
        return base.update(DepenseHelper.NOM_TABLE , nouveau,
                           DepenseHelper.NOM + " = ?",
                           new String[] {aModifier.getNom()} );
    }

    /**
     * Recherche dans la table des dépenses, la dépense dont le nom est donné en argument
     * @param nom nom de la dépense à chercher dans la table des dépenses
     * @return l'instance Depense dont le nom est donné en arugment (null si non trouvé)
     */
    public Depense get(String nom) {
        Cursor c = base.query(DepenseHelper.NOM_TABLE,
                              new String[] {DepenseHelper.CLE,
                                            DepenseHelper.NOM,
                                            DepenseHelper.CLE_UTILISATEUR},
                              DepenseHelper.NOM + " = ? ",
                              new String[] {nom}, null, null, null);
        return cursorToDepenses(c);
    }

    /**
     * Recherche dans la table des dépenses, la dépense dont le nom est donné en argument
     * @param id identifiant de l'utilisateur à chercher dans la table des dépenses
     * @return l'instance Depense dont le nom est donné en arugment (null si non trouvé)
     */
    public ArrayList<Depense> getByUtilisateur(long id) {
        Cursor c = base.query(DepenseHelper.NOM_TABLE,
                              new String[] {DepenseHelper.CLE,
                                            DepenseHelper.NOM,
                                            DepenseHelper.CLE_UTILISATEUR},
                              DepenseHelper.CLE_UTILISATEUR + " = ? ",
                              new String[] {Long.toString(id)}, null, null, null);
        return cursorToListeDepenses(c);
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
                aAjouter = cursorToDepenses(c);
                listeDepense.add(aAjouter);
            } while (c.moveToNext());
        }
        c.close();
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
            // on initialise l'instance Depense avec les valeurs des colonnes
            aRenvoyer = new Depense(c.getInt(COLONNE_CLE));
            aRenvoyer.setNom(c.getString(COLONNE_NOM));
            aRenvoyer.setMontant(c.getDouble(COLONNE_MONTANT));
        }
        c.close();
        return aRenvoyer;
    }
}
