package com.example.repositoriomarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class EstadisticaHeroe extends AppCompatActivity {

    BarChart barChartHeroes;
    String habilidadesHero = "";
    String[] arrayHabilidadesHero;
    TextView heroesName, heroesIdentity;
    ImageView imageHero;
    // heroesName, full-name, url, intelligence, strength, speed, durability, power, combat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica_heroe);

        // get info from previous activity, heroes info
        Intent intent = getIntent();
        habilidadesHero = intent.getStringExtra(ResultadoBusqueda.EXTRA_MESSAGE_2);
        if (habilidadesHero != null || !habilidadesHero.equals("")){
            arrayHabilidadesHero = habilidadesHero.split(",");
        }

        // cerciorarse que cada habilidad no sea nula
        for(int i = 0; i< arrayHabilidadesHero.length ; i++){
            if (arrayHabilidadesHero[i]== null || arrayHabilidadesHero[i].equals("null") || arrayHabilidadesHero[i].equals("")){
                arrayHabilidadesHero[i] = "0";
            }
        }

        // init views
        heroesName = findViewById(R.id.txtViewHeroeName);
        heroesIdentity = findViewById(R.id.txtViewHeroeIdentity);
        //imageHero = findViewById(R.id.imageViewHero);

        // modify view
        setData();

        //init the chart
        barChartHeroes = findViewById(R.id.chartHeroeHability);
        renderChartData();
    }

    public void setData(){
        heroesName.setText(arrayHabilidadesHero[0]);
        heroesIdentity.setText(arrayHabilidadesHero[1]);

        new DownloadImageFromInternet (findViewById(R.id.imageViewHero)).execute(arrayHabilidadesHero[2]);
        //imageHero.setImageDrawable(LoadImageFromWebOperations(arrayHabilidadesHero[2]));
    }
    public void renderChartData(){
        // init data/values
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        // add data
        valueList.add(Double.valueOf(arrayHabilidadesHero[3]));
        valueList.add(Double.valueOf(arrayHabilidadesHero[4]));
        valueList.add(Double.valueOf(arrayHabilidadesHero[5]));
        valueList.add(Double.valueOf(arrayHabilidadesHero[6]));
        valueList.add(Double.valueOf(arrayHabilidadesHero[7]));
        valueList.add(Double.valueOf(arrayHabilidadesHero[8]));

        // fit data into a bar
        for ( int i = 0 ; i< valueList.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        // seteo de columnas de las barras en el grafico
        BarDataSet barDataSet = new BarDataSet(entries, arrayHabilidadesHero[0]);
        BarData data = new BarData(barDataSet);
        barChartHeroes.setData(data);
        barChartHeroes.invalidate();

        // seteo de legend / hero nombre
        Legend legendHero = barChartHeroes.getLegend();
        legendHero.setTextColor(Color.RED);

        // seteo de hailidades de heroe
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("intelligence");
        xAxisLabel.add("strength");
        xAxisLabel.add("speed");
        xAxisLabel.add("durability");
        xAxisLabel.add("power");
        xAxisLabel.add("combat");
        barChartHeroes.getXAxis().setValueFormatter(new ValueFormatter(xAxisLabel));
        XAxis xAxis = barChartHeroes.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(270);
        xAxis.setTextSize(15f);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            Toast.makeText(getApplicationContext(), "Por favor espere a que la imagen se cargue...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}