package com.example.repositoriomarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class ResultadoBusqueda extends AppCompatActivity {

    // token: 4455601557858712
    // result example: https://www.superheroapi.com/api.php/4455601557858712/search/batman
    String urlHeroe =  "https://www.superheroapi.com/api.php/4455601557858712/search/batman";
    String urlSearchResult = "https://www.superheroapi.com/api.php/4455601557858712/search/";
    public static final String EXTRA_MESSAGE_2 = "habilidadesHeroe";

    private ListView mListView;
    private ArrayAdapter aAdapter;
    //private String[] users = { "Suresh Dasari", "Rohini Alavala", "Trishika Dasari", "Praveen Alavala", "Madav Sai", "Hamsika Yemineni"};
    private String[] arrayHeroesNames;
    private TextView textViewHeroesEncontrados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busqueda);

        textViewHeroesEncontrados = findViewById(R.id.textViewHeroesEncontrados);
        mListView = (ListView) findViewById(R.id.heroesList);

        Intent intent = getIntent();
        urlSearchResult = urlSearchResult + intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //System.out.println("urlSearchResult: " + urlSearchResult);


        //aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users); // last parameter is a simple string list
        //mListView.setAdapter(aAdapter);

        new MyTask().execute();

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result = "";
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            try {
                url = new URL(urlSearchResult);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;
                ///
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            // get json from api
            JSONObject jsonResultadosAPI = null;
            try {
                jsonResultadosAPI = new JSONObject(result);
                JSONArray heroesArray = jsonResultadosAPI.getJSONArray("results");

                // si hay resultados entonces se mostraran en el listView
                if (heroesArray.length() >0 ){
                    arrayHeroesNames = new String[heroesArray.length()];
                    for (int i = 0; i< heroesArray.length() ; i++ )
                    {
                        arrayHeroesNames[i] = heroesArray.getJSONObject(i).getString("name");
                    }

                    // set textview con info del numero de heroes enocntrados
                    String heroesEncontrados ="Resultados: "+ heroesArray.length() ;
                    textViewHeroesEncontrados.setText(heroesEncontrados);

                    // actualizar list view
                    aAdapter = new ArrayAdapter(ResultadoBusqueda.this, android.R.layout.simple_list_item_1, arrayHeroesNames); // last parameter is a simple string list
                    mListView.setAdapter(aAdapter);

                    // interactive list view
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Intent intent = new Intent(ResultadoBusqueda.this, EstadisticaHeroe.class);
                            String message = " ";
                            try {
                                //System.out.println("heroe: " + heroesArray.getJSONObject(position).toString());
                                message = heroesArray.getJSONObject(position).getString("name");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("biography").getString("full-name");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("image").getString("url");
                                message +=" ,";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("intelligence");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("strength");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("speed");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("durability");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("power");
                                message +=",";
                                message += heroesArray.getJSONObject(position).getJSONObject("powerstats").getString("combat");
                                //System.out.println("message: " + message);
                                // heroesName, full-name, url, intelligence, strength, speed, durability, power, combat
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra(EXTRA_MESSAGE_2, message);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //textMessage.setText(result);
            //textLoad.setText("Finished");
            super.onPostExecute(aVoid);
        }
    }

    public void parseJSON(String jsonString){

    }
}