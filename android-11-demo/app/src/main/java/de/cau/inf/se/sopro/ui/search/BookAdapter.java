package de.cau.inf.se.sopro.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.cau.inf.se.sopro.R;
import de.cau.inf.se.sopro.model.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    private List<Book> books = new ArrayList<>();
    private final ListItemClickListener onClickListener;

    public BookAdapter(ListItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book currentBook = books.get(position);
        holder.textViewTitle.setText(currentBook.getTitle());
        holder.textViewAuthorname.setText(currentBook.getAuthor().getForename() + " " + currentBook.getAuthor().getSurname());
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public Book getBookById(int position) {
        return this.books.get(position);
    }

    class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewAuthorname;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.text_view_title);
            this.textViewAuthorname = itemView.findViewById(R.id.text_view_author_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickListener.onListItemClick(position);
        }
    }

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

}
