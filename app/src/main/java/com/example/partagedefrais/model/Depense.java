/* Depense.java                                  05/04/2023 avr. 2023
 * IUT Rodez, No copyright
 */

package com.example.partagedefrais.model;

import com.example.partagedefrais.helper.DataHelper;

/**
 * Classe objet représentant une dépense réalisé
 * Depense.java 05/04
 * @author clement.bonaz
 */
public class Depense {

    /**
     * Clé primaire d'une dépense dans la base de donnée
     */
    private final long id;

    /**
     * nom de la dépense
     */
    private String nom;

    /**
     * montant de la dépense
     */
    private double montant;

    /**
     * Utilisateur de la dépense
     */
    private final Utilisateur utilisateur;

    /**
     * Initilaisation d'une dépense
     * @param id clé primaire de la dépense
     */
    public Depense(long id, Utilisateur utilisateur) {
        this.id = id;
        this.utilisateur = utilisateur;
    }

    /**
     * Initialisation d'une dépense
     * @param id      clé primaire de la dépense
     * @param nom     nom de la dépense
     * @param montant somme de la dépense
     */
    public Depense(long id, Utilisateur utilisateur, String nom,
                   double montant) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.nom = nom;
        this.montant = montant;
    }

    /**
     * Getter de l'attribut nom
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter de l'attribut nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de l'attribut montant
     * @return montant
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Setter de l'attribut montant
     */
    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Getter de l'attribut id
     * @return id
     */
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return utilisateur.getPrenom() + " " + nom + " : " +
               DataHelper.formatDouble(montant) + "€";
    }
}
