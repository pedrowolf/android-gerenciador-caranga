package com.pedro.gerenciadorcaranga.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.domain.Veiculo;
import com.pedro.gerenciadorcaranga.util.CarangaConstants;

import java.util.ArrayList;

public class CadastroActivity extends AppCompatActivity {

    private EditText editApelido;
    private EditText editDono;
    private EditText editAno;
    private EditText editKmRodado;
    private RadioButton rbtGasolina;
    private RadioButton rbtDiesel;
    private RadioButton rbtFlex;
    private CheckBox chkAtivo;
    private Spinner spinnerMontadora;

    private ArrayList<String> montadoras;

    public static Veiculo veiculo = null;
    private static String activityMode = "";
    private static Integer idUpdate = 0;
    private static int indexOfEditItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editApelido = findViewById(R.id.editTextApelidoCadVeiculo);
        editDono = findViewById(R.id.editTextDonoCadVeiculo);
        editAno = findViewById(R.id.editTextAnoCadVeiculo);
        editKmRodado = findViewById(R.id.editTextKmCadVeiculo);

        rbtGasolina = findViewById(R.id.radioButtonGasolinaCadVeiculo);
        rbtDiesel = findViewById(R.id.radioButtonDieselCadVeiculo);
        rbtFlex = findViewById(R.id.radioButtonFlexCadVeiculo);

        chkAtivo = findViewById(R.id.checkboxAtivoCadVeiculo);

        spinnerMontadora = findViewById(R.id.spinnerMontadoraCadVeiculo);

        montadoras = new ArrayList<>();
        montadoras.add("GM");
        montadoras.add("Volkswagen");
        montadoras.add("Chery");
        montadoras.add("Volvo");
        montadoras.add("Fiat");
        montadoras.add("Ford");
        montadoras.add("JAC");
        montadoras.add("Audi");
        montadoras.add("Outras");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        montadoras); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerMontadora.setAdapter(spinnerArrayAdapter);

        SharedPreferences preferences = getSharedPreferences(CarangaConstants.sharedPreferecensName, Context.MODE_PRIVATE);

        Bundle b = getIntent().getExtras();
        activityMode = b.getString("ACTIVITY_MODE");

        if(activityMode.equals("INSERT")){
            setTitle(getString(R.string.titleAcitivityCadastroVeiculo));
            chkAtivo.setChecked(preferences.getBoolean("defaultStatusVehicle",true));
            if(chkAtivo.isChecked()){
                chkAtivo.setText(getString(R.string.chkCheckedTrue));
            }else{
                chkAtivo.setText(getString(R.string.chkCheckedFalse));
            }
        }else{
            setTitle(getString(R.string.titleAcitivityEditarVeiculo));
            Veiculo v = (Veiculo) b.getSerializable("VEICULO");
            idUpdate = v.getId();
            initActivityModeEdit(v);
        }

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editApelido.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_veiculo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_item_salvar_cadveiculo){
            salvarVeiculo();
        }else if(item.getItemId() == R.id.menuitem_cad_veiculo_limpar){
            limparCampos();
        }else if(item.getItemId() == android.R.id.home){
            Intent it = new Intent();
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initActivityModeEdit(Veiculo v){
        editApelido.setText(v.getApelido());
        editDono.setText(v.getDono());
        editKmRodado.setText(String.valueOf(v.getKmRodado()));
        editAno.setText(String.valueOf(v.getAno()));
        spinnerMontadora.setSelection(getPositionMontadora(v.getMontadora()));
        chkAtivo.setChecked(v.getAtivo());
        if(v.getTipoCombustivel().equals("Gasolina")){
            rbtGasolina.setChecked(true);
        }else if(v.getTipoCombustivel().equals("Flex")){
            rbtFlex.setChecked(true);
        }else if(v.getTipoCombustivel().equals("Diesel")){
            rbtDiesel.setChecked(true);
        }
    }

    private void limparCampos() {
        editAno.setText("");
        editDono.setText("");
        editApelido.setText("");
        editKmRodado.setText("");

        rbtGasolina.setChecked(false);
        rbtDiesel.setChecked(false);
        rbtFlex.setChecked(false);

        chkAtivo.setChecked(true);

        editApelido.requestFocus();
    }

    private int getPositionMontadora(String m){
        for(int i=0;i<montadoras.size();i++){
            if(montadoras.get(i).equals(m)){
                return i;
            }
        }
        return -1;
    }

    private void salvarVeiculo() {
        String apelido = editApelido.getText().toString();
        String ano = editAno.getText().toString();
        String dono = editDono.getText().toString();
        String km = editKmRodado.getText().toString();

        boolean gasolina = rbtGasolina.isChecked();
        boolean diesel = rbtDiesel.isChecked();
        boolean flex = rbtFlex.isChecked();

        if((apelido.isEmpty() || ano.isEmpty() || dono.isEmpty() || km.isEmpty()) || (gasolina == false && diesel == false && flex == false)){
            Toast.makeText(this, getString(R.string.messagePreenchaCampos),Toast.LENGTH_SHORT).show();
            if(apelido.isEmpty()){
                editApelido.requestFocus();
            }else if(dono.isEmpty()){
                editDono.requestFocus();
            }else if(km.isEmpty()){
                editKmRodado.requestFocus();
            }else if(ano.isEmpty()){
                editAno.requestFocus();
            }
        }else{
            String combustivel = "";
            if(rbtDiesel.isChecked()){
                combustivel = "Diesel";
            }else if(rbtFlex.isChecked()){
                combustivel = "Flex";
            }else{
                combustivel = "Gasolina";
            }
            veiculo = new Veiculo(idUpdate, editApelido.getText().toString(),editDono.getText().toString(),Integer.parseInt(editKmRodado.getText().toString()),combustivel, spinnerMontadora.getSelectedItem().toString(), Integer.parseInt(editAno.getText().toString()),chkAtivo.isChecked());

            Toast.makeText(this,getString(R.string.messageVeiculoSalvo),Toast.LENGTH_SHORT).show();
            Intent it = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("VEICULO", veiculo);
            b.putString("ACTIVITY_MODE",activityMode);
            it.putExtras(b);
            setResult(Activity.RESULT_OK,it);
            finish();
        }
    }

    public void changeAtivo(View view) {
        if(chkAtivo.isChecked()){
            chkAtivo.setText(getString(R.string.chkCheckedTrue));
        }else{
            chkAtivo.setText(getString(R.string.chkCheckedFalse));
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        setResult(Activity.RESULT_CANCELED, it);
        super.onBackPressed();
    }
}
