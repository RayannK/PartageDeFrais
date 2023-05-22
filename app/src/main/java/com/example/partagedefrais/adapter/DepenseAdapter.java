/* DepenseAdapter.java                                        10 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partagedefrais.R;
import com.example.partagedefrais.holder.UtilisateurViewHolder;
import com.example.partagedefrais.model.Depense;

import java.util.List;

/**
 * @author rayann.karon
 */
public class DepenseAdapter
        extends RecyclerView.Adapter<UtilisateurViewHolder> {
    /**
     * Source de données à afficher par la liste
     */
    private List<Depense> lesDonnees;

    /**
     * Constructeur avec en argument la liste source des données
     * @param donnees liste contenant les instances de type
     *                Depense que l'adapteur sera chargé de gérer
     */
    public DepenseAdapter(List<Depense> donnees) {
        lesDonnees = donnees;
    }

    @Override
    public UtilisateurViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                    int viewType) {
        View view = LayoutInflater.from(
                                          viewGroup.getContext())
                                  .inflate(R.layout.vue_depense_utilisateur,
                                           viewGroup, false);
        return new UtilisateurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UtilisateurViewHolder viewHolder,
                                 int position) {
        Depense depense = lesDonnees.get(position);
        viewHolder.bind(depense);
    }

    @Override
    public int getItemCount() {
        return lesDonnees.size();
    }

    public void SetList(List<Depense> depenses) {
        this.lesDonnees = depenses;
    }
}
