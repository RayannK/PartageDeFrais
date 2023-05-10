/* DepenseViewHolder.java                                         12 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.holder;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.R;
import com.example.partagedefrais.model.Depense;

/**
 * Description du contenant des items de la liste de type RecyclerView
 * permettant d'afficher les dépenses.
 * @author rayann.karon
 */
public class DepenseViewHolder  extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private final TextView utilisateur;
    private final TextView nomDepense;
    private final TextView montantDepense;

    /**
     * Constructeur avec en argument une vue correspondant
     * à un item de la liste
     * Le constructeur permet d'initialiser les identifiants des
     * widgets déclarés en tant qu'attributs
     * @param itemView vue décrivant l'affichage d'un item de la liste
     */
    public DepenseViewHolder(View itemView) {
        super(itemView);
        utilisateur = itemView.findViewById(R.id.utilisateur);
        nomDepense = itemView.findViewById(R.id.nom_depense);
        montantDepense = itemView.findViewById(R.id.montant_depense);
        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Permet de placer les informations contenues dans l'argument
     * dans les widgets d'un item de la liste
     * @param depense l'instance qui doit être affichée
     */
    public void bind(Depense depense){
        utilisateur.setText("test");
        nomDepense.setText(depense.getNom());
        montantDepense.setText(depense.getMontant() + "€");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        new MenuInflater(this.itemView.getContext()).inflate(R.menu.menu_context, menu);
    }
}