/* GestionFrais.java                                  14/04/2023 avr. 2023
 * IUT Rodez, No copyright
 */

package com.example.partagedefrais.model;

import android.content.Context;

import com.example.partagedefrais.dao.DepenseDao;

import java.util.ArrayList;

/**
 * TODO Commenter la classe
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
    public ArrayList<Depense> getDepenseUtilisateur(long idUtilisateur, Context context) {
        return DepenseDao.getInstance(context).getByUtilisateur(idUtilisateur);
    }
}
