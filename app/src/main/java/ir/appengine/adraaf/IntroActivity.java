package ir.appengine.adraaf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;

import utils.Session;


public class IntroActivity extends FragmentActivity {
	
	public PageIndicator mIndicator;
	private ViewPager awesomePager;
	private PagerAdapter pm;
    private TextView berim;

	String deviceNames[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        awesomePager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (PageIndicator) findViewById(R.id.pagerIndicator);

        berim = (TextView)findViewById(R.id.bashe);
        Typeface typefaceYekan = Typeface.createFromAsset(getResources().getAssets(), "B Yekan.ttf");
        berim.setTypeface(typefaceYekan);
        berim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session(getApplicationContext());
                session.insertOrUpdateIntro(true);
                Intent intent = new Intent(IntroActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        List<IntroFragment> introFragments = new ArrayList<IntroFragment>();
		IntroFragment fragment1 = new IntroFragment(R.layout.fragment_intro1);
//		fragment1.setViewId(R.layout.fragment_intro1);
		IntroFragment fragment2 = new IntroFragment(R.layout.fragment_intro2);
//		fragment2.setViewId(R.layout.fragment_intro2);
		IntroFragment fragment3 = new IntroFragment(R.layout.fragment_intro3);
//		fragment3.setViewId(R.layout.fragment_intro3);
		introFragments.add(fragment1);
		introFragments.add(fragment2);
		introFragments.add(fragment3);

        pm = new PagerAdapter(getSupportFragmentManager(), introFragments);
        awesomePager.setAdapter(pm);
        mIndicator.setViewPager(awesomePager);
    }

    @Override
    public void onBackPressed() {
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

    	private List<IntroFragment> fragments;
    	
		public PagerAdapter(FragmentManager fm, List<IntroFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int pos) {
			return this.fragments.get(pos);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
    }
}
