/* Utilisateur.java                                               05 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.model;

import com.example.partagedefrais.model.Depense;

import java.util.ArrayList;
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

    /**
     * Initialisation d'un Utilisateur
     * @param id Identifiant de l'utilisateur dans la table qui le contient
     */
    public Utilisateur(long id) {
        this.id = id;
        this.prenom = null;
    }

    /**
     * Initialisation d'un Utilisateur
     * @param id identifiant de l'utilisateur dans la table qui le contient
     * @param prenom prénom de l'utilisateur ayant effectuer la dépense
     */
    public Utilisateur(long id, String prenom) {
        this.id = id;
        this.prenom = prenom;
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
}
