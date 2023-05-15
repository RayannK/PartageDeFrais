/* GestionFrais.java                                  14/04/2023 avr. 2023
 * IUT Rodez, No copyright
 */

package com.example.partagedefrais.model;

import android.content.Context;

import com.example.partagedefrais.dao.DepenseDao;
import com.example.partagedefrais.dao.UtilisateurDao;

import java.util.ArrayList;

/**
 * @author clement.bonaz
 */
public class GestionFrais {

    /**
     *
     * @param libelle
     * @param montant
     * @param utilisateur
     * @param context
     * @return
     */
    public long ajoutDepense(String libelle, Double montant, Utilisateur utilisateur, Context context) {
            Depense depense = new Depense(0, libelle, montant);
        return DepenseDao.getInstance(context).insert(depense,utilisateur.getId());
    }

    /**
     *
     * @param idUtilisateur
     * @param context
     * @return
     */
    public ArrayList<Depense> getDepenseByUtilisateurId(long idUtilisateur, Context context) {
        return DepenseDao.getInstance(context).getByUtilisateur(idUtilisateur);
    }

    /**
     *
     * @param nom
     * @param context
     * @return
     */
    public Depense getDepenseByNom(String nom, Context context) {
        return DepenseDao.getInstance(context).get(nom);
    }

    /**
     *
     * @param idUtilisateur
     * @param context
     * @return
     */
    public Utilisateur getUtilisateurById(long idUtilisateur, Context context) {
        return UtilisateurDao.getInstance(context).getById(idUtilisateur);
    }
}
