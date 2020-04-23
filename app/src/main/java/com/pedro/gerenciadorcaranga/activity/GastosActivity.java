package com.pedro.gerenciadorcaranga.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.adapter.GastoAdapter;
import com.pedro.gerenciadorcaranga.dao.RoomDatabaseFactory;
import com.pedro.gerenciadorcaranga.dao.SQLiteFactoryOpenHelper;
import com.pedro.gerenciadorcaranga.domain.Gasto;
import com.pedro.gerenciadorcaranga.domain.Veiculo;
import com.pedro.gerenciadorcaranga.util.DateInputMask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GastosActivity extends AppCompatActivity {

    private TextView txtApelidoVeiculo;
    private TextView txtTotal;
    private EditText edtDtInicio;
    private EditText edtDtFim;
    private RecyclerView recyclerGastos;

    private RecyclerView.LayoutManager layoutManager;
    private GastoAdapter adapter;
    private List<Gasto> gastos = new ArrayList<>();

    public static int positionSelected = -1;

    private  Veiculo veiculo;

    private RoomDatabaseFactory dbFactory;

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
                Intent it = new Intent(GastosActivity.this, CadGastoActivity.class);
                Bundle b = new Bundle();
                b.putString("ACTIVITY_MODE","UPDATE");
                b.putSerializable("GASTO",gastos.get(positionSelected));
                b.putInt("veiculoId",gastos.get(positionSelected).getVeiculoId());
                it.putExtras(b);
                startActivityForResult(it,1);
            }else if(item.getItemId() == R.id.menuitem_excluir_contextual){
                if(positionSelected != -1) {
                    new AlertDialog.Builder(GastosActivity.this).setTitle("").setMessage(getString(R.string.messageRemoverGasto))
                            .setPositiveButton(getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbFactory.gastoDAO().remove(gastos.get(positionSelected));
                                    gastos = dbFactory.gastoDAO().loadAll(veiculo.getId());
                                    adapter.setGastos(gastos);
                                    calcTotal();
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
            recyclerGastos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        txtApelidoVeiculo = findViewById(R.id.textViewApelidoVeiculoGastos);
        txtTotal = findViewById(R.id.txtTotalGastos);
        edtDtInicio = findViewById(R.id.editTextDtInicialGasto);
        edtDtFim = findViewById(R.id.editTextDtFinalGasto);
        recyclerGastos = findViewById(R.id.recyclerViewGastos);

        new DateInputMask(edtDtFim);
        new DateInputMask(edtDtInicio);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.titleActivityGastos));
        }

        Bundle b = getIntent().getExtras();
        veiculo = (Veiculo) b.getSerializable("veiculo");

        txtApelidoVeiculo.setText(getString(R.string.txtApelidoVeiculo)+": "+veiculo.getApelido());

        dbFactory = RoomDatabaseFactory.getInstance(getApplicationContext());

        layoutManager = new LinearLayoutManager(this);

        recyclerGastos.setLayoutManager(layoutManager);
        recyclerGastos.setHasFixedSize(true);
        recyclerGastos.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        gastos = dbFactory.gastoDAO().loadAll(veiculo.getId());

        calcTotal();

        adapter = new GastoAdapter(gastos,this);

        recyclerGastos.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gastos_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menuitem_novo_gastos){
            Intent it = new Intent(this, CadGastoActivity.class);
            Bundle b = new Bundle();
            b.putString("ACTIVITY_MODE","INSERT");
            b.putInt("veiculoId",veiculo.getId());
            it.putExtras(b);
            startActivityForResult(it,1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startContextualMenu(){
        if(actionMode == null){
            recyclerGastos.setEnabled(false);
            actionMode = startActionMode(mActionModeCallback);
        }
    }

    private void calcTotal(){
        Float tot = 0f;
        for(Gasto g: gastos){
            tot += g.getValor();
        }
        txtTotal.setText("Total: "+String.format("%.2f",tot));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            final Bundle b = data.getExtras();
            if(b != null){
                if(b.getString("ACTIVITY_MODE").equals("INSERT")) {
                    Gasto g = (Gasto) b.getSerializable("GASTO");
                    dbFactory.gastoDAO().insert(g);
                    gastos = dbFactory.gastoDAO().loadAll(veiculo.getId());
                    adapter.setGastos(gastos);
                    calcTotal();
                }else if(b.getString("ACTIVITY_MODE").equals("UPDATE")){
                    Gasto g = (Gasto) b.getSerializable("GASTO");
                    dbFactory.gastoDAO().update(g);
                    gastos = dbFactory.gastoDAO().loadAll(veiculo.getId());
                    adapter.setGastos(gastos);
                    calcTotal();
                }
            }
        }

    }

    public void filterSpents(View view) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            Date dtInit = fmt.parse(edtDtInicio.getText().toString()+" 00:00:00");
            Date dtFim = fmt.parse(edtDtFim.getText().toString()+" 23:59:59");
            gastos = dbFactory.gastoDAO().loadByDates(veiculo.getId(),dtInit.getTime(),dtFim.getTime());
            adapter.setGastos(gastos);
            calcTotal();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this,getString(R.string.messageInvalidDates),Toast.LENGTH_SHORT).show();
            edtDtInicio.setText("");
            edtDtFim.setText("");
            gastos = dbFactory.gastoDAO().loadAll(veiculo.getId());
            adapter.setGastos(gastos);
            calcTotal();
        }
    }
}
