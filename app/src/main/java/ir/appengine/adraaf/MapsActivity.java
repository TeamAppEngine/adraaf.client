package ir.appengine.adraaf;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzf;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import utils.DataType;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static ArrayList<DataType.Offer> offerArrayList = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        String offers = intent.getStringExtra("offers");
        offerArrayList.clear();
        try {
            JSONArray offersJSONArray = new JSONArray(offers);
            /*
            double x;
            double y;
            int category;
            long expire;
            int percent;
            String title;
            String description;
            String img_url;
            int id;
            int point;
            int level;
            */
            for(int i=0; i<offersJSONArray.length();i++){
                DataType.Offer offer = new DataType.Offer();
                offer.x = offersJSONArray.getJSONObject(i).getDouble("x");
                offer.y = offersJSONArray.getJSONObject(i).getDouble("y");
                offer.category = offersJSONArray.getJSONObject(i).getInt("category");
                offer.expire = offersJSONArray.getJSONObject(i).getLong("expire");
                offer.percent = offersJSONArray.getJSONObject(i).getInt("percent");
                offer.title = offersJSONArray.getJSONObject(i).getString("title");
                offer.description = offersJSONArray.getJSONObject(i).getString("description");
                offer.img_url = offersJSONArray.getJSONObject(i).getString("img");
                offer.id = offersJSONArray.getJSONObject(i).getInt("id");
                offerArrayList.add(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"خطا!",Toast.LENGTH_LONG).show();
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        markers.clear();
        for(int i=0;i<offerArrayList.size();i++){
            Log.d("Markers", String.valueOf(offerArrayList.get(i).percent));
            Marker marker;
            //mMap.addMarker(new MarkerOptions().position(new LatLng(offerArrayList.get(i).x, offerArrayList.get(i).y)).title(offerArrayList.get(i).title).icon(BitmapDescriptorFactory.fromResource(R.drawable.percent15)));
            if(0<=offerArrayList.get(i).percent && offerArrayList.get(i).percent<=10)
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(offerArrayList.get(i).x, offerArrayList.get(i).y)).title(offerArrayList.get(i).title).icon(BitmapDescriptorFactory.fromResource(R.drawable.percent10)));
            else if(10<offerArrayList.get(i).percent && offerArrayList.get(i).percent<=15)
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(offerArrayList.get(i).x, offerArrayList.get(i).y)).title(offerArrayList.get(i).title).icon(BitmapDescriptorFactory.fromResource(R.drawable.percent15)));
            else if(15<offerArrayList.get(i).percent && offerArrayList.get(i).percent<=20)
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(offerArrayList.get(i).x, offerArrayList.get(i).y)).title(offerArrayList.get(i).title).icon(BitmapDescriptorFactory.fromResource(R.drawable.percent20)));
            else
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(offerArrayList.get(i).x, offerArrayList.get(i).y)).title(offerArrayList.get(i).title).icon(BitmapDescriptorFactory.fromResource(R.drawable.percent25)));
            markers.add(marker);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int offer_id = markers.indexOf(marker);
                    Intent intent = new Intent(MapsActivity.this, OffersActivity.class);
                    intent.putExtra("offer_id", offer_id);
                    startActivity(intent);
                    return false;
                }
            });
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        mMap.moveCamera(cu);
    }

}
