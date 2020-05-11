package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter dataItemAdapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        dataItemAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(dataItemAdapter);

        // set the empty view text
        emptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        // list view listener.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = dataItemAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEarthquake.getUrl()));
                startActivity(intent);
            }
        });


        // initiate loader manager
        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }


    // this called when loader is empty.
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        // set loading spinner.
        ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);

        // get no internet text view.
        TextView noInternet = (TextView) findViewById(R.id.no_internet_connection);

        // check connection state.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            network = connectivityManager.getActiveNetworkInfo();
        }
        if (network == null || !network.isConnected()) { // network is not available.
            emptyView.setVisibility(View.GONE);
            noInternet.setText(R.string.no_internet);
        }
        else { // network is available.
            noInternet.setVisibility(View.GONE);
            dataItemAdapter.clear();

            if (data != null && !data.isEmpty())
                dataItemAdapter.addAll(data);
            emptyView.setText(R.string.no_earthquakes);
       }
    }

    // is called when url changed or the app closed.
    @Override
    public void onLoaderReset(Loader loader) {
        dataItemAdapter.clear();
    }


    private static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        private String url;
        public EarthquakeLoader(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            if(url == null)
                return null;
            List<Earthquake> result = Utils.fetchEarthquakeData(USGS_REQUEST_URL);
            return result;
        }

    }




}
