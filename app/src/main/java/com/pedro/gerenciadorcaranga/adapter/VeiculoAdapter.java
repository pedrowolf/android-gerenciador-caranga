package com.pedro.gerenciadorcaranga.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.domain.Veiculo;

import java.util.List;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.MyHolder> {

    private List<Veiculo> veiculos;

    public VeiculoAdapter(List<Veiculo> veiculos){
        this.veiculos = veiculos;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.veiculo_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Veiculo v = veiculos.get(position);

        holder.txtApelido.setText(v.getApelido());
        holder.txtAno.setText(String.valueOf(v.getAno()));
        holder.txtMontadora.setText(v.getMontadora());
        holder.txtCombustivel.setText(v.getTipoCombustivel());

    }

    @Override
    public int getItemCount() {
        return veiculos.size();
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }



    public class MyHolder extends RecyclerView.ViewHolder  {

        protected TextView txtApelido, txtAno, txtMontadora, txtCombustivel;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txtApelido = itemView.findViewById(R.id.txtApelidoVeiculoItem);
            txtAno = itemView.findViewById(R.id.txtAnoVeiculoItem);
            txtMontadora = itemView.findViewById(R.id.txtMontadoraVeiculoItem);
            txtCombustivel = itemView.findViewById(R.id.txtCombustivelVeiculoItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Veiculo veic = veiculos.get(getAdapterPosition());
                    Toast.makeText(v.getContext(), "Selecionou "+veic.getApelido()+" - "+veic.getMontadora(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
