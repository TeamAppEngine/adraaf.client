package ir.appengine.adraaf;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.ConnectToServer;
import utils.Session;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationDrawerFragment extends Fragment {

    private int levelPoint[] = {
            0,
            101,
            222,
            373,
            554,
            765,
            1016,
            1317,
            1678,
            2119,
            2640,
            3271,
            4022,
            4923,
            6004,
            7305,
            8866,
            10737,
            12978,
            15669
    };
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private OnFragmentInteractionListener mListener;
    private View containerView;
    private TextView txtLevel;
    private TextView lblPointsNeeded;
    private TextView txtPointsNeeded;
    private TextView lblOffersUsed;
    private TextView txtOffersUsed;
    private TextView lblOffersValue;
    private TextView txtOffersValue;
    private TextView btnSearchAdraf;
    private TextView btnMyAdraf;
    private String result;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavigationDrawerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationDrawerFragment newInstance() {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        return fragment;
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setUp(int fragmentNavigationDrawerId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentNavigationDrawerId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        getViewElements();
    }

    public void updateInfo(int level, int points){
        if(level != 19)
            txtPointsNeeded.setText(levelPoint[level] - points);
        txtLevel.setText(level);
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

    private void getViewElements(){

        txtLevel        = (TextView)getActivity().findViewById(R.id.text_level);
        lblPointsNeeded = (TextView)getActivity().findViewById(R.id.label_points_needed);
        txtPointsNeeded = (TextView)getActivity().findViewById(R.id.text_points_needed);
        lblOffersUsed   = (TextView)getActivity().findViewById(R.id.label_offers_used);
        txtOffersUsed   = (TextView)getActivity().findViewById(R.id.text_offers_used);
        lblOffersValue  = (TextView)getActivity().findViewById(R.id.label_offers_value);
        txtOffersValue  = (TextView)getActivity().findViewById(R.id.text_offers_value);
        btnSearchAdraf  = (TextView)getActivity().findViewById(R.id.btn_search_adraf);
        btnMyAdraf      = (TextView)getActivity().findViewById(R.id.btn_my_adraf);

        Typeface typefaceYekan = Typeface.createFromAsset(getActivity().getAssets(), "B Yekan.ttf");
        txtLevel.setTypeface(typefaceYekan);
        lblPointsNeeded.setTypeface(typefaceYekan);
        txtPointsNeeded.setTypeface(typefaceYekan);
        lblOffersUsed.setTypeface(typefaceYekan);
        txtOffersUsed.setTypeface(typefaceYekan);
        lblOffersValue.setTypeface(typefaceYekan);
        txtOffersValue.setTypeface(typefaceYekan);
        btnSearchAdraf.setTypeface(typefaceYekan);
        btnSearchAdraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToServer.get(
                        getActivity(),
                        true,
                        "ارتباط با سرور",
                        "دریافت آفرهای اطراف شما",
                        ConnectToServer.baseUri + "api/users/offers?x=35.7552336&y=51.367946",
                        false,
                        new ConnectToServer.GetListener() {
                            @Override
                            public void ResponseListener(String response) {
                                result = response;
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

                            @Override
                            public void ErrorListener(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                );
            }
        });
        btnMyAdraf.setTypeface(typefaceYekan);
        btnMyAdraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session(getActivity().getApplicationContext());
                if(session.readUserID() == null){
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    ConnectToServer.get(
                            getActivity(),
                            true,
                            "",
                            "",
                            ConnectToServer.baseUri + "api/users/" + session.readUserID() + "/saved_offers",
                            false,
                            new ConnectToServer.GetListener() {
                                @Override
                                public void ResponseListener(String response) {
                                    try {
                                        JSONObject result = new JSONObject(response);
                                        Intent intent = new Intent(getActivity(), MyOffersActivity.class);
                                        intent.putExtra("my_offers", result.getJSONArray("offers").toString());
                                        getActivity().startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void ErrorListener(VolleyError error) {

                                }
                            }
                    );
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
