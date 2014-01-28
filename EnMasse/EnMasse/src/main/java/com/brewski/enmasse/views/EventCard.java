package com.brewski.enmasse.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.activities.EventActivity;
import com.brewski.enmasse.activities.Globals;
import com.brewski.enmasse.activities.OldViewEventActivity;
import com.brewski.enmasse.controllers.ForecastIOController;
import com.brewski.enmasse.controllers.WeatherController;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.ForecastIOReading;
import com.brewski.enmasse.models.WeatherReading;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by matt on 10/7/13.
 */
public class EventCard extends Card {

    Context context;
    Event event;

    public EventCard(Context context, Event event) {
        super(context);
        this.context = context;
        this.event = event;
        init();
    }

    public EventCard(Context context, int innerLayout, Event event) {
        super(context, innerLayout);
        this.context = context;
        this.event = event;
        init();
    }

    private void init(){
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                openEvent();
            }
        });

        // call for a weather update
        if(event.HasCoordinates() && event.shouldRequestWeatherUpdate()) {
            //WeatherController weatherController = new WeatherController();
            //weatherController.getWeatherInfo(this, event);

            ForecastIOController forecastIOController = new ForecastIOController();
            forecastIOController.getWeatherInfo(this, event);
        }
    }

    private void openEvent() {
        Globals g = (Globals) context.getApplicationContext();
        g.event = event;
        context.startActivity(new Intent(context, EventActivity.class));
    }

    private static int color_going = 0xff99cc00;
    private static int color_notgoing = 0xffffbb33;
    private static int color_undecided = 0xffbbbbbb;

    ImageView weather;
    TextView weather_text;
    TextView temperature;
    //TextView weatherCode;
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView foo = (TextView) parent.findViewById(R.id.event_name);
        foo.setText(event.GetName());

        PieGraph pie = (PieGraph) parent.findViewById(R.id.graph);

        pie.removeSlices();

        Random r = new Random();

        PieSlice slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_no));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_undecided));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_maybe));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_going));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        pie.setThickness(22);

        TextView date = (TextView) parent.findViewById(R.id.event_date);
        date.setText(event.GetDateTime());

        TextView location = (TextView) parent.findViewById(R.id.event_location);
        location.setText(event.GetLocation());

        weather = (ImageView) parent.findViewById(R.id.weather);
        temperature = (TextView) parent.findViewById(R.id.temperature);
        //weatherCode = (TextView) parent.findViewById(R.id.weather_code);

        weather_text = (TextView) parent.findViewById(R.id.weather_text);

        if(event.alreadyHappened()) {
            parent.findViewById(R.id.card_background).setBackgroundColor(0xffe8e8e8);
        }

        UpdateWeather(event.GetForecastIO());
    }

    /*public void UpdateWeather(WeatherReading w) {
        event.SetWeather(w);

        if(event.GetWeather() == null)
            return;

        weather.setImageResource(event.GetWeather().GetWeatherResource(event.GetDateMillis()));
        temperature.setText( " " + event.GetWeather().GetTemperature(event.GetDateMillis()) + (char) 0x00B0);
        weatherCode.setText(Integer.toString(event.GetWeather().GetDebugWeatherCode(event.GetDateMillis())));
    }*/

    public void UpdateWeather(ForecastIOReading r) {

        event.SetForecastIO(r);

        if(event.GetForecastIO() == null)
            return;

        if(weather_text == null)
            return; // Not currently in view

        weather_text.setTypeface(((Globals)context.getApplicationContext()).getWeatherTypeface());

        //weather_text.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`~!@#$%^&*()-_=+[]{}<>,./?");
        weather_text.setText(event.GetForecastIO().getWeatherResource());
        temperature.setText( " " + event.GetForecastIO().getTemperature() + (char) 0x00B0);
        //weatherCode.setText(event.GetForecastIO().getPrecipitationChance());
    }
}
