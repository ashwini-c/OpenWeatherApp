package com.example.weatherapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements LocationListener{


	EditText searchText;
	ImageView searchBtn;
	Button findMe;
	String city;
	private LocationManager locationManager;
	private String provider;
	double latitude = -3333;
	double longitude = -3333;
	int unit =1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);


		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		}
		searchText = (EditText)findViewById(R.id.editText1);
		searchBtn = (ImageView)findViewById(R.id.imageView1);
		findMe = (Button)findViewById(R.id.button1);
		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


				if(!isNetworkConnected())
				{
					Toast.makeText(getApplicationContext(), "Check your internet connection and try again!!", Toast.LENGTH_LONG).show();
					return;
				}


				city = searchText.getEditableText().toString();
				if(city.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Enter a valid city name!!", Toast.LENGTH_LONG).show();
				}
				else
				{
					//search intent
					Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
					intent.putExtra("cityname", city);
					intent.putExtra("mode", 1);
					intent.putExtra("unit", unit);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}

			}
		});

		findMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isNetworkConnected())
				{
					Toast.makeText(getApplicationContext(), "Check your internet connection and try again!!", Toast.LENGTH_LONG).show();
					return;
				}

				if(!isGPSEnabled())
				{
					Toast.makeText(getApplicationContext(), "GPS signal not found!!", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
					return;
				}
				else
				{

					Log.d("MainActivity"," lat "+ latitude + " "+ longitude);
					if(latitude!=-3333 && longitude!=-3333)
					{
						Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
						intent.putExtra("lat", latitude);
						intent.putExtra("long", longitude);
						intent.putExtra("unit", unit);
						intent.putExtra("mode", 2);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);	
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Your Current Location unavailable!!", Toast.LENGTH_LONG).show();

						return;
					}
				}



			}
		});

		//search intent

	}

	public void onRadioButtonClicked(View view) {

		boolean checked = ((RadioButton) view).isChecked();


		switch(view.getId()) {
		case R.id.radio_metric:
			if (checked)
				unit=1;
			break;
		case R.id.radio_imperial:
			if (checked)
				unit=2;
			break;

		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}


	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	private boolean isGPSEnabled()
	{
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabledGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return enabledGPS;
	}


	private boolean isNetworkConnected() {
		boolean isWifiConnected = false;
		boolean isMobileConnected = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					isWifiConnected = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					isMobileConnected = true;
		}
		return isWifiConnected || isMobileConnected;
	}


	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();

	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
