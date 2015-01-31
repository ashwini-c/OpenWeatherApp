
package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenWeatherParser {

	public static WeatherData getWeather(String data, int mode) throws JSONException  {
		WeatherData weather = new WeatherData();

		JSONObject jObj = new JSONObject(data);
		if(getInt("cod", jObj) == 404)
		{
			return null;
		}

		JSONObject coordObj = getObject("coord", jObj);
		weather.location.setLatitude(getFloat("lat", coordObj));
		weather.location.setLongitude(getFloat("lon", coordObj));

		JSONObject sysObj = getObject("sys", jObj);
		weather.location.setCountry(getString("country", sysObj));
		weather.location.setCity(getString("name", jObj));


		JSONArray jArr = jObj.getJSONArray("weather");

		JSONObject JSONWeather = jArr.getJSONObject(0);
		weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
		weather.currentCondition.setDescr(getString("description", JSONWeather));
		weather.currentCondition.setCondition(getString("main", JSONWeather));
		weather.currentCondition.setIcon(getString("icon", JSONWeather));
		weather.currentCondition.setTime(getInt("dt", jObj));
		JSONObject mainObj = getObject("main", jObj);
		weather.currentCondition.setHumidity(getInt("humidity", mainObj));
		weather.currentCondition.setPressure(getInt("pressure", mainObj));
		weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
		weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
		weather.temperature.setTemp(getFloat("temp", mainObj));

		JSONObject wObj = getObject("wind", jObj);
		weather.wind.setSpeed(getFloat("speed", wObj));
		weather.wind.setDeg(getFloat("deg", wObj));

		return weather;
	}


	private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
		JSONObject subObj = jObj.getJSONObject(tagName);
		return subObj;
	}

	private static String getString(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getString(tagName);
	}

	private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
		return (float) jObj.getDouble(tagName);
	}

	private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getInt(tagName);
	}

}
