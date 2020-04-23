package com.pedro.gerenciadorcaranga.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.domain.Gasto;
import com.pedro.gerenciadorcaranga.domain.Veiculo;
import com.pedro.gerenciadorcaranga.util.DateInputMask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.DataFormatException;

public class CadGastoActivity extends AppCompatActivity {

    private EditText editDescricao;
    private EditText editData;
    private EditText editValor;

    private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    private static Integer idUpdate = 0;
    private static Integer veiculoId = 0;
    private static Long timestampGasto = 0L;
    private static String activityMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_gasto);

        editDescricao = findViewById(R.id.editDescricaoCadGasto);
        editData = findViewById(R.id.editDataGastoCad);
        editValor = findViewById(R.id.editValorCadGasto);

        editData.setText(fmt.format(new Date()));

        new DateInputMask(editData);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        activityMode = b.getString("ACTIVITY_MODE");

        if(activityMode.equals("INSERT")){
            setTitle(getString(R.string.titleAcitivityCadastroVeiculo));
            veiculoId = b.getInt("veiculoId");
        }else{
            setTitle(getString(R.string.titleAcitivityEditarVeiculo));
            Gasto g = (Gasto) b.getSerializable("GASTO");
            timestampGasto = g.getTimestamp();
            idUpdate = g.getId();
            veiculoId = b.getInt("veiculoId");
            initActivityModeEdit(g);
        }

    }

    private void initActivityModeEdit(Gasto g) {
        editDescricao.setText(g.getDescricao());
        editValor.setText(String.format("%.2f",g.getValor()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_veiculo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_item_salvar_cadveiculo){
            salvarGasto();
        }else if(item.getItemId() == R.id.menuitem_cad_veiculo_limpar){
            limparCampos();
        }else if(item.getItemId() == android.R.id.home){
            Intent it = new Intent();
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvarGasto() {
        if(!editDescricao.getText().toString().isEmpty() && !editValor.getText().toString().isEmpty()){
            Gasto g = new Gasto();
            if (activityMode.equals("INSERT")){
                //g.setId(new Integer());
                g.setDescricao(editDescricao.getText().toString());
                g.setValor(Float.parseFloat(editValor.getText().toString()));
                try {
                    Date dt = fmt.parse(editData.getText().toString());
                    g.setTimestamp(dt.getTime());
                }catch (ParseException ex){
                    ex.printStackTrace();
                    g.setTimestamp(System.currentTimeMillis());
                }
                g.setVeiculoId(veiculoId);
            }else{
                g.setDescricao(editDescricao.getText().toString());
                g.setId(idUpdate);
                g.setTimestamp(timestampGasto);
                g.setVeiculoId(veiculoId);
                g.setValor(Float.parseFloat(editValor.getText().toString().replaceAll(",",".")));
            }
            Intent it = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("GASTO", g);
            b.putString("ACTIVITY_MODE",activityMode);
            it.putExtras(b);
            setResult(Activity.RESULT_OK,it);
            finish();
        }else{
            Toast.makeText(this, getString(R.string.messagePreenchaCampos),Toast.LENGTH_SHORT).show();
            editDescricao.requestFocus();
        }

    }

    private void limparCampos() {
        editDescricao.setText("");
        editValor.setText("");
        editData.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        editDescricao.requestFocus();
    }
}
