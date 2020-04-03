package com.pedro.gerenciadorcaranga.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.adapter.VeiculoAdapter;
import com.pedro.gerenciadorcaranga.domain.Veiculo;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerVeiculos;
    private RecyclerView.LayoutManager layoutManager;
    private VeiculoAdapter adapter;
    private ArrayList<Veiculo> veiculos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        recyclerVeiculos = findViewById(R.id.recyclerViewVeiculos);

        layoutManager = new LinearLayoutManager(this);

        recyclerVeiculos.setLayoutManager(layoutManager);
        recyclerVeiculos.setHasFixedSize(true);
        recyclerVeiculos.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        adapter = new VeiculoAdapter(veiculos);

        recyclerVeiculos.setAdapter(adapter);
    }

    public void btCadVeiculoClick(View view) {
        Intent it = new Intent(this, CadastroActivity.class);
        startActivityForResult(it,1);
    }

    public void btSobreApp(View view) {
        Intent it = new Intent(this, AutoriaActivity.class);
        startActivity(it);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            final Bundle b = data.getExtras();
            if(b != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Veiculo v = (Veiculo) b.getSerializable("VEICULO");
                        veiculos.add(v);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }
    }
}
