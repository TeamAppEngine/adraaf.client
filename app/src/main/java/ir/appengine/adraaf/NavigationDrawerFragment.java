package ir.appengine.adraaf;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationDrawerFragment extends Fragment {

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
        btnMyAdraf.setTypeface(typefaceYekan);
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
