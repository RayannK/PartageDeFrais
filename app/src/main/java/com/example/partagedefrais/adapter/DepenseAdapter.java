/* DepenseAdapter.java                                            12 avr. 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.R;
import com.example.partagedefrais.holder.DepenseViewHolder;
import com.example.partagedefrais.model.Depense;

import java.util.List;

/**
 * Adaptateur spécifique pour afficher une liste de type RecyclerView
 * dont les items sont de type Depense
 * @author rayann.karon
 */
public class DepenseAdapter extends RecyclerView.Adapter<DepenseViewHolder> {
    /**
     * Source de données à afficher par la liste
     */
    private List<Depense> lesDonnees;

    /**
     * Constructeur avec en argument la liste source des données
     * @param donnees liste contenant les instances de type
     * Depense que l'adapteur sera chargé de gérer
     */
    public DepenseAdapter(List<Depense> donnees) {
        lesDonnees = donnees;
    }

    /**
     * Renvoie un contenant de type PhotoViewHolder qui permettra d'afficher
     * un élément de la liste
     */
    @Override
    public DepenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.vue_depense_liste,
                                                viewGroup, false);
        return new DepenseViewHolder(view);
    }

    /**
     * On remplit un item de la liste en fonction de sa position
     */
    @Override
    public void onBindViewHolder(DepenseViewHolder viewHolder, int position) {
        Depense depense = lesDonnees.get(position);
        viewHolder.bind(depense);
    }

    /**
     * Renvoie la taille de la liste
     */
    @Override
    public int getItemCount() {
        return lesDonnees.size();
    }
}