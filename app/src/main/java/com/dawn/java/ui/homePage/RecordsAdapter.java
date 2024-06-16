package com.dawn.java.ui.homePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dawn.java.R;
import com.dawn.java.database.AppDatabase;
import com.dawn.java.entities.Records;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder> {
    private List<Records> data;
    String text;
    public RecordsAdapter(List<Records> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí se inflar el layout de item y retorna un nuevo ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_record , parent, false);
        return new RecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        // Aquí se actualiza la vista del ViewHolder con los datos del item en la posición dada
        Records record = data.get(position);
        holder.recordView.setText(record.getSilo() + " " + record.getGuia() + " " + record.getBarcode() );

        if (record.getUid().toString().equals("1")) { // configuracion de estado
            holder.tv_status.setBackgroundResource(R.drawable.icon_waiting);
        } else {
            holder.tv_status.setBackgroundResource(R.drawable.icon_complete);
        }

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {  // Configuración del botón eliminar
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();  // Posición actual del elemento
                // Verifica si la posición es válida
                if (currentPosition != RecyclerView.NO_POSITION) {
                    // Obtiene el registro que se va a eliminar
                    Records recordToDelete = data.get(currentPosition);
                    new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Está seguro de eliminar el código " + recordToDelete.getBarcode() + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Elimina el registro de la base de datos
                                AppDatabase db = AppDatabase.getDatabase(v.getContext());
                                db.recordsDao().delete(recordToDelete.getId());

                                // Elimina el registro de la lista
                                data.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                notifyItemRangeChanged(currentPosition, data.size());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Records> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    static class RecordsViewHolder extends RecyclerView.ViewHolder {
        TextView recordView;
        TextView tv_status;
        TextView tv_delete;

        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            recordView = itemView.findViewById(R.id.recordView);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
