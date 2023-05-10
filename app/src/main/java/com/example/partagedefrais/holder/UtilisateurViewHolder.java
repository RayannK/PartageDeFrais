/* UtilisateurViewHolder.java                                     10 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.R;
import com.example.partagedefrais.adapter.DepenseAdapter;
import com.example.partagedefrais.adapter.UtilisateurAdapter;
import com.example.partagedefrais.model.Utilisateur;

/**
 * TODO commenter la classe
 * @author rayann.karon
 */
public class UtilisateurViewHolder extends RecyclerView.ViewHolder {

    private TextView utilisateur;
    private RecyclerView depenseRecyclerView;

    public UtilisateurViewHolder(
            @NonNull View itemView) {
        super(itemView);
        utilisateur = itemView.findViewById(R.id.utilisateur);
        depenseRecyclerView = itemView.findViewById(R.id.recycler_depense);

        /*
         * On crée un gestionnaire de layout linéaire, et on l'associe à la
         * liste de type RecyclerView
         */
        // TODO make nested Recycler view
        // https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
//        LinearLayoutManager gestionnaireLineaire = new LinearLayoutManager(
//                itemView.getContext());
//        depenseRecyclerView.setLayoutManager(gestionnaireLineaire);
    }

    /**
     * Permet de placer les informations contenues dans l'argument
     * dans les widgets d'un item de la liste
     * @param user l'instance qui doit être affichée
     */
    public void bind(Utilisateur user) {
        utilisateur.setText(user.getPrenom());
//        DepenseAdapter adaptateur = new DepenseAdapter(user.getDepenses());
//        depenseRecyclerView.setAdapter(adaptateur);
    }
}
