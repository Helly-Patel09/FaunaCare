package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Vaibhavi on 12-Dec-17.
 */

public class Register extends AppCompatActivity {
    Button btnfinder, btnvol, btnvet;
    TextView txtvol,txtvet;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        btnfinder = (Button) findViewById(R.id.btnFinder);
        //btnvol = (Button) findViewById(R.id.btnVol);
        //btnvet = (Button) findViewById(R.id.btnVet);
        txtvol= (TextView) findViewById(R.id.txtregvol);
        txtvet= (TextView) findViewById(R.id.txtregvet);
        btnfinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinder = new Intent(Register.this, RegFinder.class);
                startActivity(intentFinder);
            }
        });
        txtvol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinder = new Intent(Register.this, RegVol.class);
                startActivity(intentFinder);
            }
        });

        txtvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinder = new Intent(Register.this, RegVet.class);
                startActivity(intentFinder);
            }
        });
        /*btnvol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinder = new Intent(Register.this, RegVol.class);
                startActivity(intentFinder);
            }
        });

        btnvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinder = new Intent(Register.this, RegVet.class);
                startActivity(intentFinder);
            }
        });*/
    }
}

