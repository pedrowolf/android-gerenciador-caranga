package com.pedro.gerenciadorcaranga.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.adapter.VeiculoAdapter;
import com.pedro.gerenciadorcaranga.dao.RoomDatabaseFactory;
import com.pedro.gerenciadorcaranga.dao.SQLiteFactoryOpenHelper;
import com.pedro.gerenciadorcaranga.dao.VeiculoDAO;
import com.pedro.gerenciadorcaranga.domain.Gasto;
import com.pedro.gerenciadorcaranga.domain.Veiculo;
import com.pedro.gerenciadorcaranga.util.CarangaConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerVeiculos;
    private RecyclerView.LayoutManager layoutManager;
    private VeiculoAdapter adapter;
    private List<Veiculo> veiculos = new ArrayList<>();

    private VeiculoDAO dao;

    public int positionSelected = -1;

    private ActionMode actionMode;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contextual_principal_activity,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if(item.getItemId() ==R.id.menuitem_editar_contextual){
                Intent it = new Intent(PrincipalActivity.this, CadastroActivity.class);
                Bundle b = new Bundle();
                b.putString("ACTIVITY_MODE","UPDATE");
                b.putSerializable("VEICULO",veiculos.get(positionSelected));
                it.putExtras(b);
                startActivityForResult(it,1);
            }else if(item.getItemId() == R.id.menuitem_excluir_contextual){
                if(positionSelected != -1) {
                    new AlertDialog.Builder(PrincipalActivity.this).setTitle("").setMessage(getString(R.string.messageRemoverVeiculo))
                            .setPositiveButton(getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    List<Gasto> gastosVeiculo = RoomDatabaseFactory.getInstance(PrincipalActivity.this).gastoDAO().loadAll(veiculos.get(positionSelected).getId());
                                    for(Gasto g: gastosVeiculo){
                                        RoomDatabaseFactory.getInstance(PrincipalActivity.this).gastoDAO().remove(g);
                                    }
                                    dao.excluir(veiculos.get(positionSelected).getId(), SQLiteFactoryOpenHelper.getInstance(PrincipalActivity.this).getWritableDatabase());
                                    veiculos = dao.listar(SQLiteFactoryOpenHelper.getInstance(PrincipalActivity.this).getReadableDatabase());
                                    adapter.setVeiculos(veiculos);
                                }
                            })
                            .setNegativeButton(getString(R.string.negativeButton), null)
                            .show();

                }
            }

            mode.finish();

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            recyclerVeiculos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        recyclerVeiculos = findViewById(R.id.recyclerViewVeiculos);

        layoutManager = new LinearLayoutManager(this);

        recyclerVeiculos.setLayoutManager(layoutManager);
        recyclerVeiculos.setHasFixedSize(true);
        recyclerVeiculos.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        dao = new VeiculoDAO();

        veiculos = dao.listar(SQLiteFactoryOpenHelper.getInstance(this).getReadableDatabase());

        adapter = new VeiculoAdapter(veiculos,this);

        recyclerVeiculos.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences(CarangaConstants.sharedPreferecensName, Context.MODE_PRIVATE);

        preferences.getBoolean("defaultStatusVehicle", true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menuitem_novo){
            btCadVeiculoClick();
        }else if(item.getItemId() == R.id.menuitem_sobre){
            btSobreApp();
        }else if(item.getItemId() == R.id.menuitem_principal_configuracoes){
            Intent it = new Intent(this, ConfiguracoesActivity.class);
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startContextualMenu(){
        if(actionMode == null){
            recyclerVeiculos.setEnabled(false);
            actionMode = startActionMode(mActionModeCallback);
        }
    }

    private void btCadVeiculoClick() {
        Intent it = new Intent(this, CadastroActivity.class);
        Bundle b = new Bundle();
        b.putString("ACTIVITY_MODE","INSERT");
        it.putExtras(b);
        startActivityForResult(it,1);
    }

    private void btSobreApp() {
        Intent it = new Intent(this, AutoriaActivity.class);
        startActivity(it);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            final Bundle b = data.getExtras();
            if(b != null){
                if(b.getString("ACTIVITY_MODE").equals("INSERT")) {
                    Veiculo v = (Veiculo) b.getSerializable("VEICULO");
                    dao.inserir(v, SQLiteFactoryOpenHelper.getInstance(this).getWritableDatabase());
                    veiculos = dao.listar(SQLiteFactoryOpenHelper.getInstance(this).getReadableDatabase());
                    adapter.setVeiculos(veiculos);
                }else if(b.getString("ACTIVITY_MODE").equals("UPDATE")){
                    Veiculo v = (Veiculo) b.getSerializable("VEICULO");
                    Log.d("PrincipalActivity","position adapter selecionado apra editar: "+positionSelected);
                    dao.alterar(v, SQLiteFactoryOpenHelper.getInstance(this).getWritableDatabase());
                    veiculos = dao.listar(SQLiteFactoryOpenHelper.getInstance(this).getReadableDatabase());
                    adapter.setVeiculos(veiculos);
                }
            }
        }
    }
}
