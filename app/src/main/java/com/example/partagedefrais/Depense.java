/* Depense.java                                  05/04/2023 avr. 2023
 * IUT Rodez, No copyright
 */

package com.example.partagedefrais;

/**
 *
 *  Classe objet représentant une dépense réalisé
 *  Depense.java 05/04
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
    private Double montant;

    /**
     * Initilaisation d'une dépense
     * @param id clé primaire de la dépense
     */
    public Depense(long id) {
        this.id = id;
    }

    /**
     * Initialisation d'une dépense
     * @param id clé primaire de la dépense
     * @param nom nom de la dépense
     * @param montant somme de la dépense
     */
    public Depense(long id, String nom, Double montant) {
        this.id = id;
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
    public Double getMontant() {
        return montant;
    }

    /**
     * Setter de l'attribut montant
     */
    public void setMontant(Double montant) {
        this.montant = montant;
    }

    /**
     * Getter de l'attribut id
     * @return id
     */
    public long getId() {
        return id;
    }
}
