package ir.appengine.adraaf;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import utils.ConnectToServer;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ObjectAnimator animator;
        float x1,y1;
        int repeat = 0;
        boolean responseReceived = false;

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
                                getLocationData();
                                animator = ObjectAnimator.ofFloat(markerImageView, "translationY", 0, -350);
                                animator.setDuration(500);
                                animator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        ObjectAnimator animator = ObjectAnimator.ofFloat(markerImageView, "translationY", -350, -100);
                                        animator.setDuration(300);
                                        animator.setRepeatMode(ObjectAnimator.REVERSE);
                                        animator.setRepeatCount(ValueAnimator.INFINITE);
                                        //animator.setInterpolator(new BounceInterpolator());
                                        animator.addListener(new Animator.AnimatorListener() {
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
                                                Log.d("repeat",String.valueOf(repeat));
                                                /*if(earthImageView.getVisibility()==View.VISIBLE)
                                                    earthImageView.setVisibility(View.INVISIBLE);
                                                else
                                                    earthImageView.setVisibility(View.VISIBLE);*/
                                                if (responseReceived) {
                                                    if (repeat % 2 == 0) {
                                                        ObjectAnimator animator = ObjectAnimator.ofFloat(markerImageView, "translationY", -350, height);
                                                        animator.setDuration(300);
                                                        animator.addListener(new Animator.AnimatorListener() {
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
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) {

                                                            }
                                                        });
                                                        animator.start();
                                                    }
                                                }
                                            }
                                        });
                                        animator.start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                animator.start();
                                touched[0] = true;
                                break;
                        }
                    }else{
                        responseReceived = true;
                    }
                    return true;
                }
            });

            return rootView;
        }

        private void getLocationData() {
            Map<String, String> params = new HashMap<>();
            //TODO: Add Params for location.
            ConnectToServer.post(
                    getActivity(),
                    false,
                    "",
                    "",
                    ConnectToServer.baseUri + "",
                    params,
                    false,
                    new ConnectToServer.PostListener() {
                        @Override
                        public void ResponseListener(String response) {

                        }

                        @Override
                        public void ErrorListener(VolleyError error) {

                        }
                    }
            );
        }
    }
}
