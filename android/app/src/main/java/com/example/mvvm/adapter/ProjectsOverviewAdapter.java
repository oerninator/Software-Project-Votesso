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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm.activities.ProjectdetailsActivity;
import com.example.mvvm.R;
import com.example.mvvm.model.Project;

import java.util.List;

/**
 * Adapter-Klasse zum darstellen der Projekte in der MainActivity-RecyclerView.
 */
public class ProjectsOverviewAdapter extends RecyclerView.Adapter<ProjectsOverviewAdapter.MyViewHolder> {

    // Context der Activity
    private Context context;

    // Liste der darzustellenden Projekte
    private List<Project> projectModelList;

    /**
     * Initialisieren der Projekt-Liste
     *
     * @param context
     * @param projectModelList Projekt-Liste die dargestellt werden soll
     */
    public ProjectsOverviewAdapter(Context context, List<Project> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;

    }

    /**
     * Setter für die Projekt-Liste
     *
     * @param projectModelList
     */
    public void setProjectModelList(List<Project> projectModelList) {
        this.projectModelList = projectModelList;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_project, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Recycler-View nutzt diese Methode um die View-Holder mit den gewünschten Daten zu füllen.
     *
     * @param holder Der zu füllende View-Holder
     * @param position Position des View-Holders
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.projectName.setText(projectModelList.get(position).getTitle());

        // Darstellen der Bilddatei über die Projekt URL
        Glide.with(context).
                load(this.projectModelList.get(position).getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            /**
             * Ein Listener der durch antippen des Projekt-Namen das Starten der
             * Projektdetail-Activity vollzieht.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProjectdetailsActivity.class);
                ProjectdetailsActivity.setPid(projectModelList.get(holder.getBindingAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    /**
     * Gibt die Anzahl der Projekte in der Teilprojekt-liste zurück.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (this.projectModelList != null) {
            return this.projectModelList.size();
        }
        return 0;
    }

    /**
     * View-Holder-Klasse die die einzelnen Projekte halten kann.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView projectName;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.titleViewID);
            imageView = (ImageView) itemView.findViewById((R.id.imageViewID));

        }
    }
}
