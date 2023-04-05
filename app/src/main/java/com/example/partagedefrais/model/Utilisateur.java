/* Utilisateur.java                                               05 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.model;

import com.example.partagedefrais.model.Depense;

import java.util.List;

/**
 * Objet métier représentant un Utilisateur de l'application
 * @author rayann.karon
 */
public class Utilisateur {

    /** Identifiant de l'utilisateur dans la table qui le contient */
    private final long id;

    /** Prénom de l'utilisateur ayant effectuer la dépense */
    private String prenom;

    /** Liste des dépenses de l'Utilisateur */
    private List<Depense> depenses;

    /**
     * Initialisation d'un Utilisateur
     * @param id Identifiant de l'utilisateur dans la table qui le contient
     */
    public Utilisateur(long id) {
        this.id = id;
    }

    /**
     * Initialisation d'un Utilisateur
     * @param id identifiant de l'utilisateur dans la table qui le contient
     * @param prenom prénom de l'utilisateur ayant effectuer la dépense
     * @param depenses liste des dépenses de l'Utilisateur
     */
    public Utilisateur(long id, String prenom, List<Depense> depenses) {
        this.id = id;
        this.prenom = prenom;
        this.depenses = depenses;
    }

    /**
     * @return l'identifiant de l'utilisateur dans la table
     */
    public long getId() {
        return id;
    }

    /**
     * @return le prénom de l'utilisateur
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modifie le prénom de l'utilisateur
     * @param prenom nouveau prénom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return les dépenses de l'utilisateur
     */
    public List<Depense> getDepenses() {
        return depenses;
    }

    /**
     * Modifie les dépenses de l'utilisateur
     * @param depenses nouvelle liste des dépenses de l'utilisateur
     */
    public void setDepenses(List<Depense> depenses) {
        this.depenses = depenses;
    }

    /**
     * Calcul de la somme des dépenses de l'utilisateur
     * @return somme des dépenses
     */
    public double sommeDepense() {
        double somme = 0.0;
        for (Depense depense: depenses) {
            somme = somme + depense.getMontant();
        }
        return somme;
    }

    /**
     * Calcul du solde de l'utilisateur pour le voyage
     * @param partDepenseVoyage part de l'utilisateur pour les dépenses du voyage
     * Cas 1 : renvoie d'un résultat négatif donc l'utilisateur doit de l'argent
     * Cas 2 : renvoie d'un résultat positif donc l'utilisateur à dépensé plus que ça part pour le voyage
     * Cas 3 : renvoie de 0 donc l'utilisateur a payé ça part pour les dépenses du voyage
     * @return solde de l'utilisateur
     */
    public double soldeUtilisateur(double partDepenseVoyage) {
        return sommeDepense() - partDepenseVoyage;
    }
}
