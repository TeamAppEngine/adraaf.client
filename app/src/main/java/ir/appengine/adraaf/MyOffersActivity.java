package ir.appengine.adraaf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

import utils.Calender;
import utils.DataType;

public class MyOffersActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<DataType.Offer> offers = new ArrayList<>();
    private Typeface typefaceYekan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);

        typefaceYekan = Typeface.createFromAsset(getAssets(), "B Yekan.ttf");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();
        ImageLoader.getInstance().init(config);

        Intent intent = getIntent();

        try {
            JSONArray offersJSONArray = new JSONArray(intent.getStringExtra("my_offers"));
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
            boolean isExpired;
            int state;
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
                offer.isExpired = offersJSONArray.getJSONObject(i).getBoolean("isExpired");
                offer.state = offersJSONArray.getJSONObject(i).getInt("state");
                offers.add(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "خطا!", Toast.LENGTH_LONG).show();
        }

        listView = (ListView) findViewById(R.id.activity_my_offers_list_view);
        MyAdapter adapter = new MyAdapter(getApplicationContext());
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_offers, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends BaseAdapter{

        LayoutInflater inflater;
        Context context;

        public MyAdapter(Context context){
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return offers.size();
        }

        @Override
        public Object getItem(int position) {
            return offers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;
            /*if(convertView==null){*/
            convertView = inflater.inflate(R.layout.item_my_offer,parent,false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
            /*}else{
                mViewHolder = (MyViewHolder) convertView.getTag();
            }*/

            mViewHolder.textViewTitle.setTypeface(typefaceYekan);
            mViewHolder.textViewExpireTitle.setTypeface(typefaceYekan);
            mViewHolder.textViewExpire.setTypeface(typefaceYekan);
            mViewHolder.textViewPercent.setTypeface(typefaceYekan);
            mViewHolder.textViewDesc.setTypeface(typefaceYekan);


            DataType.Offer current_offer = offers.get(position);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(current_offer.img_url, mViewHolder.imageView);

            mViewHolder.textViewTitle.setText(current_offer.title);
            Date date = new Date(current_offer.expire);
            mViewHolder.textViewExpire.setText(Calender.getShamsiDate(date));
            mViewHolder.textViewPercent.setText(String.valueOf(current_offer.percent)+"%");
            if(current_offer.state == 1)
                mViewHolder.textViewDesc.setText("رزرو شده");
            if(current_offer.state == 2)
                mViewHolder.textViewDesc.setText("استفاده شده");
            if(current_offer.isExpired){
                mViewHolder.textViewTitle.setTextColor(Color.parseColor("#BBBBBB"));
                mViewHolder.textViewExpireTitle.setTextColor(Color.parseColor("#BBBBBB"));
                mViewHolder.textViewExpire.setTextColor(Color.parseColor("#BBBBBB"));
                mViewHolder.textViewPercent.setTextColor(Color.parseColor("#BBBBBB"));
                mViewHolder.textViewDesc.setTextColor(Color.parseColor("#BBBBBB"));
            }
            return convertView;
        }

        private class MyViewHolder{
            ImageView imageView;
            TextView textViewTitle;
            TextView textViewExpireTitle;
            TextView textViewExpire;
            TextView textViewPercent;
            TextView textViewDesc;
            public MyViewHolder(View item){
                imageView = (ImageView) item.findViewById(R.id.item_my_offer_image_view);
                textViewTitle = (TextView) item.findViewById(R.id.item_my_offer_text_view_title);
                textViewExpireTitle = (TextView) item.findViewById(R.id.item_my_offer_text_view_expire_title);
                textViewExpire = (TextView) item.findViewById(R.id.item_my_offer_text_view_expire);
                textViewPercent = (TextView) item.findViewById(R.id.item_my_offer_text_view_percent);
                textViewDesc = (TextView) item.findViewById(R.id.item_my_offer_text_view_desc);
            }
        }
    }
}
