package com.pedro.gerenciadorcaranga.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.domain.Veiculo;

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

    public static Veiculo veiculo = null;

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

        ArrayList<String> montadoras = new ArrayList<>();
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

        editApelido.requestFocus();
    }

    public void limparCampos(View view) {
        editAno.setText("");
        editDono.setText("");
        editApelido.setText("");
        editKmRodado.setText("");

        rbtGasolina.setChecked(false);
        rbtDiesel.setChecked(false);
        rbtFlex.setChecked(false);

        chkAtivo.setChecked(true);

        editApelido.requestFocus();

        Toast.makeText(this,"Formulário Limpo!",Toast.LENGTH_SHORT).show();
    }

    public void salvarVeiculo(View view) {
        String apelido = editApelido.getText().toString();
        String ano = editAno.getText().toString();
        String dono = editDono.getText().toString();
        String km = editKmRodado.getText().toString();

        boolean gasolina = rbtGasolina.isChecked();
        boolean diesel = rbtDiesel.isChecked();
        boolean flex = rbtFlex.isChecked();

        if((apelido.isEmpty() || ano.isEmpty() || dono.isEmpty() || km.isEmpty()) || (gasolina == false && diesel == false && flex == false)){
            Toast.makeText(this, "Preencha os Campos!",Toast.LENGTH_SHORT).show();
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
            veiculo = new Veiculo(editApelido.getText().toString(),editDono.getText().toString(),Integer.parseInt(editKmRodado.getText().toString()),combustivel, spinnerMontadora.getSelectedItem().toString(), Integer.parseInt(editAno.getText().toString()),chkAtivo.isChecked());
            Toast.makeText(this,"Veículo "+apelido+" foi Salvo!",Toast.LENGTH_SHORT).show();
            Intent it = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("VEICULO", veiculo);
            it.putExtras(b);
            setResult(Activity.RESULT_OK,it);
            finish();
        }
    }

    public void changeAtivo(View view) {
        if(chkAtivo.isChecked()){
            chkAtivo.setText("Ativo");
        }else{
            chkAtivo.setText("Inativo");
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        setResult(Activity.RESULT_CANCELED, it);
        super.onBackPressed();
    }
}
