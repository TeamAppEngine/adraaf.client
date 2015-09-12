package ir.appengine.adraaf;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class IntroFragment extends Fragment{
	
	private Activity activity;
	private int viewId;
    TextView textBig;
    TextView textSmall;

	public void setViewId(int viewId){
		this.viewId = viewId;
	}

    public IntroFragment(){

    }

    public IntroFragment(int viewId){
        this.viewId = viewId;
    }

	public IntroFragment(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view;
		
		view = inflater.inflate(viewId, container, false);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        if(viewId == R.layout.fragment_intro1) {
            textBig = (TextView) getActivity().findViewById(R.id.text11);
            textSmall = (TextView) getActivity().findViewById(R.id.text12);
        }
        if(viewId == R.layout.fragment_intro2) {
            textBig = (TextView) getActivity().findViewById(R.id.text21);
            textSmall = (TextView) getActivity().findViewById(R.id.text22);
        }
        if(viewId == R.layout.fragment_intro3) {
            textBig = (TextView) getActivity().findViewById(R.id.text31);
            textSmall = (TextView) getActivity().findViewById(R.id.text32);
        }

        Typeface typefaceYekan = Typeface.createFromAsset(getActivity().getAssets(), "B Yekan.ttf");
        textBig.setTypeface(typefaceYekan);
        textSmall.setTypeface(typefaceYekan);
    }
}
