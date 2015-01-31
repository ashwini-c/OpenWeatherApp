package com.example.weatherapp;



import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {

	private TextView cityName;
	private TextView descr;
	private TextView temp;
	private TextView pressure;
	private TextView wind;
	private TextView maxT;
	private TextView minT;
	private TextView humidity;
	WeatherData weatherData = null;
	String city;
	int mode;
	double lat,longi;
	int unit;
	Animation zoom_out;
	RelativeLayout parent;

	private static String IMG_URL = "http://openweathermap.org/img/w/";
	private static String Append_URL = ".png";

	private ProgressBar spinner;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		cityName = (TextView) findViewById(R.id.city);
		descr = (TextView) findViewById(R.id.descr);
		temp = (TextView) findViewById(R.id.temp);
		humidity = (TextView) findViewById(R.id.humid);
		pressure = (TextView) findViewById(R.id.press);
		wind = (TextView) findViewById(R.id.windDeg);
		maxT = (TextView)findViewById(R.id.maxT);
		minT = (TextView) findViewById(R.id.minT);
		city = getIntent().getStringExtra("cityname");
		mode = getIntent().getIntExtra("mode", -1);
		unit = getIntent().getIntExtra("unit", -1);
		lat = getIntent().getDoubleExtra("lat", -3333);
		longi = getIntent().getDoubleExtra("long",-3333);
		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(city);
		parent = (RelativeLayout) findViewById(R.id.layout);
		zoom_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom); 


	}

	private void setIcon()
	{
		new DownloadImageTask((ImageView) findViewById(R.id.icon))
		.execute(IMG_URL+weatherData.currentCondition.getIcon()+Append_URL);
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			spinner.setVisibility(View.GONE);
			parent.setVisibility(View.VISIBLE);
			//parent.startAnimation(zoom_out);
			bmImage.setImageBitmap(Bitmap.createScaledBitmap(result, 300, 300, false));
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	private class JSONWeatherTask extends AsyncTask<String, Void, WeatherData> {

		@Override
		protected WeatherData doInBackground(String... params) {
			WeatherData weather = new WeatherData();
			String data = ( (new HttpClient(mode,city,lat,longi,unit)).getWeatherData());
			Log.d("WeatherAcitivty"," data ........" + data);
			try {
				weather = OpenWeatherParser.getWeather(data,mode);



			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;

		}




		@Override
		protected void onPostExecute(WeatherData weather) {			
			super.onPostExecute(weather);
			if(weather == null)
			{
				spinner.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "Please enter a valid city name!!!", Toast.LENGTH_SHORT).show();
				finish();
			}
			else
			{


				String tempUnit;
				String speedUnit;
				if(unit == 1)
				{
					tempUnit = " \u2103";
					speedUnit = " M/S";
				}
				else
				{
					tempUnit = " \u2109";
					speedUnit = " MPH";
				}
				weatherData = weather;
				cityName.setText(weather.location.getCity() + "," + weather.location.getCountry());
				descr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
				temp.setText("" + weather.temperature.getTemp());
				temp.setText(temp.getText()+tempUnit);
				humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
				pressure.setText("Pressure: " + weather.currentCondition.getPressure() + " hPa");
				wind.setText("Wind: " + weather.wind.getSpeed() );
				wind.setText(wind.getText()+ speedUnit);
				maxT.setText("Max Temperature: " + weather.temperature.getMaxTemp());
				maxT.setText(maxT.getText()+tempUnit);
				minT.setText("Min Temperature: "+ weather.temperature.getMinTemp());
				minT.setText(minT.getText()+tempUnit);

				setIcon();

			}

		}

	}
}
