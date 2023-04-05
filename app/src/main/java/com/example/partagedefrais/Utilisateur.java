/* Utilisateur.java                                               05 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais;

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
}
