package com.example.repositoriomarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    Button btnBuscarHeroes;
    SearchView searchViewHeroes;

    public static final String EXTRA_MESSAGE = "mensaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuscarHeroes = findViewById(R.id.btnSearcHeroes);
        searchViewHeroes = findViewById(R.id.searchViewHeroes);

        buscarHeroes();



    }

    public void buscarHeroes(){
        searchViewHeroes.setQueryHint("Buscar heroes");
        btnBuscarHeroes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buscarHeroe();
                if (!searchViewHeroes.getQuery().toString().equals("")){
                    System.out.println("hola: " + searchViewHeroes.getQuery());
                    Intent intent = new Intent(MainActivity.this, ResultadoBusqueda.class);
                    String message = (String) searchViewHeroes.getQuery().toString();
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                }
                else{
                    System.out.println("null search query ");
                }
            }
        });
    }
}