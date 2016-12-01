package com.life.ammar.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Main extends AppCompatActivity {
    SharedPreferences sharedPreferences;
	Context context;
	ConnectivityManager connectivityManager;
	RequestQueue requestQueue;
	FragmentManager fragmentManager = getSupportFragmentManager();
	private InterstitialAd mInterstitialAd;

	static final String baseUrl = "http://api.themoviedb.org/3/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    newInterstitialAd();
	    requestQueue = Volley.newRequestQueue(getBaseContext());
        context = getBaseContext();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        /*if(isTablet()) {
            Realm realm = Realm.getInstance(this);
            int type;
            if(sharedPreferences.getString("OrderBy","Most Popular").equals("Most Popular")) {
                type = 0;
            } else {
                type = 1;
            }
            MovieEntry movieEntry = realm.where(MovieEntry.class).equalTo("type",type).findFirst();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DetailsFragment detailFragment=new DetailsFragment();
            Bundle args = new Bundle();
            if(movieEntry!= null) {
                args.putInt("idAsInt", movieEntry.getId());
            }
            detailFragment.setArguments(args);
            transaction.add(R.id.container2, detailFragment);
            transaction.commit();
        }*/
        /*if(isConnected()) {
            loadMoviesF();
        } else {
            Toast.makeText(getBaseContext(), "Please enable network connection", Toast.LENGTH_SHORT).show();
        }*/
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.add(R.id.main_container, new MainFragment());
	    fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getBaseContext(), settingActivity.class));
            return true;
        }
        return true;
    }
    public void loadMoviesF() {
	    String order_by = "";
	    final int type;
	    if(sharedPreferences.getString("OrderBy","Most Popular").equals("Most Popular")) {
		    order_by += "popular?";
		    type = 0;
	    } else {
		    order_by += "top_rated?";
		    type = 1;
	    }
	    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, baseUrl + order_by + "api_key=" + getString(R.string.apiKey), null, new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
			    // Clear database
				Realm realm = Realm.getInstance(getBaseContext());
			    realm.beginTransaction();
			    RealmResults old = realm.where(MovieEntry.class).equalTo("type", type).equalTo("favourite",false).findAll();
			    old.clear();
			    realm.commitTransaction();
			    try {
				    // Initialize Gson and start new transaction
				    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
					    @Override
					    public boolean shouldSkipField(FieldAttributes f) {
						    return f.getDeclaringClass().equals(RealmObject.class);
					    }

					    @Override
					    public boolean shouldSkipClass(Class<?> clazz) {
						    return false;
					    }
				    }).create();
				    realm.beginTransaction();
				    JSONArray jsonArray = response.getJSONArray("results");
				    for (int i=0; i<jsonArray.length(); i++) {
					    MovieEntry movieEntry = gson.fromJson(jsonArray.get(i).toString(), MovieEntry.class);
					    movieEntry.setType(type);
					    if(realm.where(MovieEntry.class).equalTo("id", movieEntry.getId()).equalTo("favourite", true).findFirst() != null) {
						    movieEntry.setFavourite(true);
					    }
					    realm.copyToRealmOrUpdate(movieEntry);
					    loadMovieData(movieEntry);
				    }
				    realm.commitTransaction();
			    } catch (JSONException e) {
				    e.printStackTrace();
			    }
			    loadInFrag();
		    }
	    }, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {

		    }
	    });
	    requestQueue.add(request);
    }

	@Override
	protected void onResume() {
		super.onResume();
		loadMoviesF();
	}

	private void loadInFrag() {
		MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
		mainFragment.setMovieList();
	}

	public void loadMovieData(final MovieEntry movieEntry) {
		JsonObjectRequest requestTrailers = new JsonObjectRequest(Request.Method.GET, baseUrl + movieEntry.getId() + "/" + "videos?api_key=" + getString(R.string.apiKey), null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							return f.getDeclaringClass().equals(RealmObject.class);
						}

						@Override
						public boolean shouldSkipClass(Class<?> clazz) {
							return false;
						}
					}).create();
					Realm realm = Realm.getInstance(getBaseContext());
					realm.beginTransaction();
					JSONArray jsonArray = response.getJSONArray("results");
					for(int i=0; i<jsonArray.length(); i++) {
						MovieTrailer movieTrailer = gson.fromJson(jsonArray.get(i).toString(), MovieTrailer.class);
						movieTrailer.setMovieId(movieEntry.getId());
						realm.copyToRealmOrUpdate(movieTrailer);
					}
					realm.commitTransaction();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		JsonObjectRequest requestReviews = new JsonObjectRequest(Request.Method.GET, baseUrl + movieEntry.getId() + "/" + "reviews?api_key=" + getString(R.string.apiKey), null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							return f.getDeclaringClass().equals(RealmObject.class);
						}

						@Override
						public boolean shouldSkipClass(Class<?> clazz) {
							return false;
						}
					}).create();
					Realm realm = Realm.getInstance(getBaseContext());
					realm.beginTransaction();
					JSONArray jsonArray = response.getJSONArray("results");
					for(int i=0; i<jsonArray.length(); i++) {
						MovieReview movieReview = gson.fromJson(jsonArray.get(i).toString(), MovieReview.class);
						movieReview.setMovieId(movieEntry.getId());
						realm.copyToRealmOrUpdate(movieReview);
					}
					realm.commitTransaction();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		requestQueue.add(requestReviews);
		requestQueue.add(requestTrailers);
	}

    public boolean isTablet() {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

	@Override
	protected void onStop() {
		super.onStop();
		showInterstitial();
	}

	private void newInterstitialAd() {
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_key));
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				newInterstitialAd();
			}

			@Override
			public void onAdClosed() {
				// Proceed to the next level.

			}
		});
		loadInterstitial();
	}

	private void showInterstitial() {
		// Show the ad if it's ready. Otherwise toast and reload the ad.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}
	private void loadInterstitial() {
		// Disable the next level button and load the ad.
		AdRequest adRequest = new AdRequest.Builder()
				.setRequestAgent("android_studio:ad_template").build();
		mInterstitialAd.loadAd(adRequest);
	}
}
