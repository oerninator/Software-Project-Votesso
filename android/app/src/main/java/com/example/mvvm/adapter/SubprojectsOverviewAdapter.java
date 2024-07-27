package com.example.mvvm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm.R;
import com.example.mvvm.activities.MapActivity;
import com.example.mvvm.activities.SubprojectActivity;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.viewmodel.GeoDataViewModel;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Adapter-Klasse zum darstellen der Teilprojekte in der ProjectdetailsActivity-RecyclerView.
 */
public class SubprojectsOverviewAdapter extends RecyclerView.Adapter<SubprojectsOverviewAdapter.MyViewHolder> {

    // Context der Activity
    private Context context;

    // Liste der darzustellenden Teilprojekte
    private List<Subproject> subprojects;

    // Projekt-ID zu welchem die Teilprojekte gehören
    private long currentProject;

    /**
     * Initialisieren der Teilprojekt-Liste
     *
     * @param context
     * @param subprojects Teilprojekt-Liste die dargestellt werden soll
     */
    public SubprojectsOverviewAdapter(Context context, List<Subproject> subprojects) {
        this.context = context;
        this.subprojects = subprojects;
    }

    /**
     * Setter für die Teilprojekt-Liste
     *
     * @param subprojects
     */
    public void setSubprojects(List<Subproject> subprojects) {
        this.subprojects = subprojects;
        notifyDataSetChanged();
    }

    /**
     * Setter für die Projekt-ID
     *
     * @param currentProject
     */
    public void setCurrentProject(long currentProject) {
        this.currentProject = currentProject;
        notifyDataSetChanged();
    }

    /**
     * Recycler-View nutzt diese Methode um einen neuen View-Holder zu erstellen, falls
     * keine zum wiederverwerten Existieren.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SubprojectsOverviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_subproject, parent, false);
        return new SubprojectsOverviewAdapter.MyViewHolder(view);
    }

    /**
     * Recycler-View nutzt diese Methode um die View-Holder mit den gewünschten Daten zu füllen.
     *
     * @param holder   Der zu füllende View-Holder
     * @param position Position des View-Holders
     */
    @Override
    public void onBindViewHolder(@NonNull SubprojectsOverviewAdapter.MyViewHolder holder, int position) {
        holder.subprojectName.setText(subprojects.get(position).getTitle());
        holder.subprojectName.setOnClickListener(new View.OnClickListener() {

            /**
             * Ein Listener der durch antippen des Teilprojekt-Namen das Starten der
             * Teilprojekt-Activity vollzieht.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubprojectActivity.class);
                SubprojectActivity.setSid(subprojects.get(holder.getBindingAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

        holder.onClickMap.setOnClickListener(new View.OnClickListener() {

            /**
             * Ein Listener der durch antippen des Map-Symbols das Starten der
             * Map-Activity vollzieht.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapActivity.class);
                GeoDataViewModel geo = GeoDataViewModel.getInstance();
                geo.setSid(subprojects.get(holder.getBindingAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

    }

    /**
     * Gibt die Anzahl der Teilprojekte in der Teilprojekt-liste zurück.
     *
     * @return Teilprojekt-Listengröße
     */
    @Override
    public int getItemCount() {
        if (this.subprojects != null) {
            return this.subprojects.size();
        }
        return 0;
    }

    /**
     * View-Holder-Klasse die die einzelnen Teilprojekte halten kann.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subprojectName;
        ImageView onClickMap;

        public MyViewHolder(View itemView) {
            super(itemView);
            subprojectName = (TextView) itemView.findViewById(R.id.text_view_recycler_subproject_ID);
            onClickMap = itemView.findViewById(R.id.image_view_map);
        }
    }
}
