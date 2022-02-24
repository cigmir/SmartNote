package com.example.smartnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<Modello> noteLista;
    List<Modello> nuovaLista;

    public Adapter(Context context, Activity activity, List<Modello> notesList) {
        this.context = context;
        this.activity = activity;
        this.noteLista = notesList;
        nuovaLista = new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int posizione) {

        holder.titolo.setText(noteLista.get(posizione).getTitolo());
        holder.descrizione.setText(noteLista.get(posizione).getDescrizione());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AggiornaNoteActivity.class);

                intent.putExtra("title", noteLista.get(posizione).getTitolo());
                intent.putExtra("description", noteLista.get(posizione).getDescrizione());
                intent.putExtra("id", noteLista.get(posizione).getId());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteLista.size();
    }

    @Override
    public Filter getFilter() {
        return esempiofiltro;
    }

    private Filter esempiofiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Modello> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(nuovaLista);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Modello item : nuovaLista) {
                    if (item.getTitolo().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteLista.clear();
            noteLista.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titolo, descrizione;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titolo = itemView.findViewById(R.id.titolo);
            descrizione = itemView.findViewById(R.id.descrizione);
            layout = itemView.findViewById(R.id.note_layout);
        }
    }


    public List<Modello> getList() {
        return noteLista;
    }

    public void removeItem(int position) {
        noteLista.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Modello item, int position) {
        noteLista.add(position, item);
        notifyItemInserted(position);
    }
}
