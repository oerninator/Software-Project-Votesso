package com.example.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm.R;
import com.example.mvvm.model.Comment;
import com.example.mvvm.viewmodel.AppViewModel;

import java.util.ArrayList;

/**
 * Adapter-Klasse zum darstellen der Kommentare in der CommentActivity-RecyclerView.
 */
public class CommentViewAdapter extends RecyclerView.Adapter<CommentViewAdapter.MyViewHolder> {

    // Liste der darzustellenden Kommentare
    private ArrayList<Comment> comments;

    /**
     * Initialisieren der Kommentar-Liste.
     * Es wird eine Sortierung nach Zeitstempeln vorgenommen.
     *
     * @param comments Kommentar-Liste die dargestellt werden soll
     */
    public CommentViewAdapter(ArrayList<Comment> comments) {
        AppViewModel.sortCommentsBasedOnTimestamp(comments);
        this.comments = comments;
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
    public CommentViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comments, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Recycler-View nutzt diese Methode um die View-Holder mit den gewünschten Daten zu füllen.
     *
     * @param holder   Der zu füllende View-Holder
     * @param position Position des View-Holders
     */
    @Override
    public void onBindViewHolder(@NonNull CommentViewAdapter.MyViewHolder holder, int position) {
        String comment = comments.get(position).getBody();
        holder.commentView.setText(comment);
        int uID = (int) comments.get(position).getUserID();
        if (uID == AppViewModel.getUserID()) {
            holder.userName.setText(AppViewModel.getUserName());
        }
        String createdAtT = createReadableDateAndTimeString(comments.get(position).getCreatedAtT());
        holder.createdAtT.setText(createdAtT);
    }

    /**
     * Macht aus einem gegebenen Zeitstempel eine besser lesbare Zeitangabe.
     *
     * @param timestamp Auszuwertender Zeitstempel
     * @return Zeitangaabe als String
     */
    private String createReadableDateAndTimeString(String timestamp) {
        String year = timestamp.substring(0, 4);
        String month = timestamp.substring(5, 7);
        String day = timestamp.substring(8, 10);
        String time = timestamp.substring(11, 16);
        return day + "." + month + "." + year + "   " + time + " Uhr";
    }

    /**
     * Gibt die Anzahl der Kommentare zurück.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return this.comments != null ? comments.size() : 0;
    }

    /**
     * View-Holder-Klasse die die einzelnen Kommentare halten kann.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView commentView;
        private TextView createdAtT;
        private TextView userName;

        public MyViewHolder(@NonNull View view) {
            super(view);
            commentView = (TextView) view.findViewById(R.id.text_view_comments);
            createdAtT = (TextView) view.findViewById(R.id.text_view_comments_createdate);
            userName = (TextView) view.findViewById(R.id.text_view_comments_name);
        }
    }
}
