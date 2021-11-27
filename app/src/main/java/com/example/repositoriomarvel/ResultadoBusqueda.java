package com.example.repositoriomarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        System.out.println("urlSearchResult: " + urlSearchResult);


        //aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users); // last parameter is a simple string list
        //mListView.setAdapter(aAdapter);

        new MyTask().execute();

    }

    //

    public void GetHeoreJson(){
        try {
            URL url = new URL(urlSearchResult);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                System.out.println(inline);

                //Using the JSON simple library parse the string into a json object
                //JSONParser parse = new JSONParser();
                //JSONObject data_obj = (JSONObject) parse.parse(inline);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                System.out.println("json string" + result);

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
                System.out.println("json obj" + jsonResultadosAPI);
                JSONArray heroesArray = jsonResultadosAPI.getJSONArray("results");

                // si hay resultados
                if (heroesArray.length() >0 ){
                    arrayHeroesNames = new String[heroesArray.length()];
                    for (int i = 0; i< heroesArray.length() ; i++ )
                    {
                        arrayHeroesNames[i] = heroesArray.getJSONObject(i).getString("name");
                    }

                    //JSONObject heroeJson = heroesArray.getJSONObject(0);
                    //String idHeroe = heroeJson.getString("id");
                    //System.out.println("json heroe ID: " + idHeroe);

                    // set textview con infor de heroes enocntrados
                    String heroesEncontrados ="Resultados: "+ heroesArray.length() ;
                    textViewHeroesEncontrados.setText(heroesEncontrados);

                    // modify list view
                    aAdapter = new ArrayAdapter(ResultadoBusqueda.this, android.R.layout.simple_list_item_1, arrayHeroesNames); // last parameter is a simple string list
                    mListView.setAdapter(aAdapter);
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