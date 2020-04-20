package com.pedro.gerenciadorcaranga.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.activity.GastosActivity;
import com.pedro.gerenciadorcaranga.activity.PrincipalActivity;
import com.pedro.gerenciadorcaranga.domain.Gasto;
import com.pedro.gerenciadorcaranga.domain.Veiculo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.MyHolder> {

    private List<Gasto> gastos;
    private GastosActivity activity;
    private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    public GastoAdapter(List<Gasto> gastos, GastosActivity activity){
        this.gastos = gastos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gasto_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Gasto g = gastos.get(position);

        Date dt = new Date(g.getTimestamp());

        holder.txtDescricao.setText(g.getDescricao());
        holder.txtData.setText(fmt.format(dt));
        holder.txtValor.setText(String.format("%.2f",g.getValor()));
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
        notifyDataSetChanged();
    }

    public List<Gasto> getGastos() {
        return gastos;
    }



    public class MyHolder extends RecyclerView.ViewHolder  {

        protected TextView txtDescricao, txtData, txtValor;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txtDescricaoGastoItem);
            txtData = itemView.findViewById(R.id.txtDataGastoItem);
            txtValor = itemView.findViewById(R.id.txtValorGastoItem);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Gasto g = gastos.get(getAdapterPosition());
                    activity.positionSelected = getAdapterPosition();
                    activity.startContextualMenu();
                    return true;
                }
            });
        }


    }
}
