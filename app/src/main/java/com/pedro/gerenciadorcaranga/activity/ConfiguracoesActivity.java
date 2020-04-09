package com.pedro.gerenciadorcaranga.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.pedro.gerenciadorcaranga.R;
import com.pedro.gerenciadorcaranga.util.CarangaConstants;

public class ConfiguracoesActivity extends AppCompatActivity {

    private Switch switchConfigStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        switchConfigStatus = findViewById(R.id.switchConfigStatus);

        SharedPreferences preferences = getSharedPreferences(CarangaConstants.sharedPreferecensName, Context.MODE_PRIVATE);
        switchConfigStatus.setChecked(preferences.getBoolean("defaultStatusVehicle",true));

        if(switchConfigStatus.isChecked()){
            switchConfigStatus.setText(getString(R.string.chkCheckedTrue));
        }else{
            switchConfigStatus.setText(getString(R.string.chkCheckedFalse));
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void changeSwitchDefaultValueForVehicleStatus(View view) {
        if(switchConfigStatus.isChecked()){
            switchConfigStatus.setText(getString(R.string.chkCheckedTrue));
        }else{
            switchConfigStatus.setText(getString(R.string.chkCheckedFalse));
        }

        SharedPreferences preferences = getSharedPreferences(CarangaConstants.sharedPreferecensName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putBoolean("defaultStatusVehicle", switchConfigStatus.isChecked());
        editor.commit();

    }
}
