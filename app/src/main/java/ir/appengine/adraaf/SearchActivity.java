package ir.appengine.adraaf;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.ConnectToServer;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try{
            forceRTLIfSupported();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(
                R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ObjectAnimator animator1;
        ObjectAnimator animator2;
        ObjectAnimator animator3;
        float x1,y1;
        int repeat = 0;
        boolean responseReceived = false;
        boolean mapsStarted = false;
        String result;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.fragment_search_scroll_view);
            final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.fragment_search_linear_layout);
            RelativeLayout relativeLayoutMarker = (RelativeLayout) rootView.findViewById(R.id.fragment_search_relative_layout_marker);
            final ImageView markerImageView = (ImageView) rootView.findViewById(R.id.fragment_search_image_view_marker);
            final ImageView earthImageView = (ImageView) rootView.findViewById(R.id.fragment_search_image_view_earth);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;
            final int height = size.y;

            relativeLayoutMarker.getLayoutParams().width = width;
            relativeLayoutMarker.getLayoutParams().height = height/2;
            relativeLayoutMarker.requestLayout();

            earthImageView.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
            earthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(earthImageView,
                    "rotation", 0f, 360f);
            imageViewObjectAnimator.setDuration(5000); // miliseconds
            imageViewObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            imageViewObjectAnimator.setInterpolator(new LinearInterpolator());
            imageViewObjectAnimator.start();


            final boolean[] touched = {false};
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!touched[0]) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                y1 = event.getY();
                                Log.d("Action Down", "x1: " + String.valueOf(x1) + ", y1: " + String.valueOf(y1));
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            case MotionEvent.ACTION_UP:
                                Log.d("Action Move", "x2: " + String.valueOf(event.getX()) + ", y2: " + String.valueOf(event.getY()));
                                getLocationData(35.7552336, 51.367946);
                                animator1 = ObjectAnimator.ofFloat(markerImageView, "translationY", 0, -350);
                                animator1.setDuration(500);
                                animator1.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        animator2 = ObjectAnimator.ofFloat(markerImageView, "translationY", -350, -100);
                                        animator2.setDuration(300);
                                        animator2.setRepeatMode(ObjectAnimator.REVERSE);
                                        animator2.setRepeatCount(ValueAnimator.INFINITE);
                                        //animator.setInterpolator(new BounceInterpolator());
                                        animator2.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {
                                                repeat++;
                                                if (responseReceived) {
                                                    if (repeat % 2 == 0) {
                                                        animator3 = ObjectAnimator.ofFloat(markerImageView, "translationY", -350, height);
                                                        animator3.setDuration(300);
                                                        animator3.addListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animation) {
                                                                ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, height);
                                                                animator.setDuration(300);
                                                                animator.start();
                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                markerImageView.setVisibility(View.INVISIBLE);
                                                                linearLayout.setVisibility(View.INVISIBLE);
                                                                if(!mapsStarted){
                                                                    startMapsActivity();
                                                                    mapsStarted = true;
                                                                }
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) {

                                                            }
                                                        });
                                                        animator3.start();
                                                    }
                                                }
                                            }
                                        });
                                        animator2.start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                animator1.start();
                                touched[0] = true;
                                break;
                        }
                    }else{
                        //responseReceived = true;
                    }
                    return true;
                }
            });

            return rootView;
        }

        private void getLocationData(final double x1, final double y1) {
            //TODO: Add Params for location.
            ConnectToServer.get(
                    getActivity(),
                    false,
                    "",
                    "",
                    ConnectToServer.baseUri + "api/users/offers?x="+x1+"&y="+y1,
                    false,
                    new ConnectToServer.GetListener() {
                        @Override
                        public void ResponseListener(String response) {
                            result = response;
                            responseReceived = true;
                        }

                        @Override
                        public void ErrorListener(VolleyError error) {
                            error.printStackTrace();
                            getLocationData(x1,y1);
                        }
                    }
            );
        }
        private void startMapsActivity(){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("offers");
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("offers", jsonArray.toString());
                getActivity().startActivity(intent);
                getActivity().finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
