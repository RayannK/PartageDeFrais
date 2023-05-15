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
import com.example.partagedefrais.model.Utilisateur;

/**
 * @author rayann.karon
 */
public class UtilisateurViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {

    private TextView utilisateur;
    private ListView listeDepense;

    public UtilisateurViewHolder(
            @NonNull View itemView) {
        super(itemView);
        utilisateur = itemView.findViewById(R.id.utilisateur);
        listeDepense = itemView.findViewById(R.id.listeDepense);

        listeDepense.setOnCreateContextMenuListener(this);
    }

    /**
     * Permet de placer les informations contenues dans l'argument
     * dans les widgets d'un item de la liste
     * @param user l'instance qui doit être affichée
     */
    public void bind(Utilisateur user) {
        utilisateur.setText(user.getPrenom());
        String [] depenses = new String[user.getDepenses().size()];

        for (int i = 0; i < user.getDepenses().size(); i++) {
            depenses[i] = user.getDepenses().get(i).toString();
        }

        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(this.itemView.getContext(), android.R.layout.simple_list_item_1, depenses);
        listeDepense.setAdapter(adaptateur);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        new MenuInflater(this.itemView.getContext()).inflate(R.menu.menu_context, menu);
    }
}
