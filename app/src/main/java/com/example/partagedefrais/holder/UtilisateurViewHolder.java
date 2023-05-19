/* UtilisateurViewHolder.java                                     10 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.holder;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.R;
import com.example.partagedefrais.model.Depense;
import com.example.partagedefrais.model.Utilisateur;

/**
 * @author rayann.karon
 */
public class UtilisateurViewHolder extends RecyclerView.ViewHolder {

    private TextView utilisateur;
    private TextView nom_depense;
    private TextView valeur_depense;

    public UtilisateurViewHolder(
            @NonNull View itemView) {
        super(itemView);
        utilisateur = itemView.findViewById(R.id.utilisateur_name);
        nom_depense = itemView.findViewById(R.id.depense_name);
        valeur_depense = itemView.findViewById(R.id.depense_valeur);
    }

    /**
     * Permet de placer les informations contenues dans l'argument
     * dans les widgets d'un item de la liste
     * @param depense l'instance qui doit être affichée
     */
    public void bind(Depense depense) {
        utilisateur.setText(depense.getUtilisateur().getPrenom());
        nom_depense.setText(depense.getNom());
        valeur_depense.setText(depense.getMontant()+"€");
    }
}
