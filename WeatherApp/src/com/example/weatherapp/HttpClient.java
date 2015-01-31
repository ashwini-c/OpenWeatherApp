
package com.example.weatherapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpClient {

	int mode=-1;
	String location;
	double lat;
	double longi;
	int unit;

	public HttpClient(int mode,String location,double lat,double longi,int unit)
	{
		this.mode = mode;
		this.lat = lat;
		this.longi = longi;
		this.location = location;
		this.unit = unit;
	}

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String END_URL_METRIC = "&mode=json&units=metric";
	private static String END_URL_IMPERIAL = "&mode=json&units=imperial";
	private static String LOCATION_URL = "http://api.openweathermap.org/data/2.5/weather?lat=";
	private static String APPEND = "&lon=";

	public String getWeatherData() {
		HttpURLConnection con = null ;
		InputStream is = null;
		String END_URL;

		try {

			URL url =null ;

			if(unit ==1)
				END_URL = END_URL_METRIC;
			else
				END_URL = END_URL_IMPERIAL;
			if(mode == 1)
			{
				url =new URL(BASE_URL + location + END_URL);
			}
			else
			{
				url = new URL(LOCATION_URL + lat + APPEND + longi + END_URL);
			}

			Log.d("androidruntime"," url   "+ url);
			con = (HttpURLConnection) ( url).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (  (line = br.readLine()) != null )
				buffer.append(line + "\r\n");

			is.close();
			con.disconnect();
			return buffer.toString();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;

	}

}
