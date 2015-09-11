package ir.appengine.adraaf;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import utils.Calender;
import utils.ConnectToServer;
import utils.DataType;

public class OffersActivity extends AppCompatActivity {
    static public int current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        current_id = intent.getIntExtra("offer_id",0);
        setContentView(R.layout.activity_offers);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        TextView textViewPercent;
        TextView textViewDiscount;
        TextView textViewExpireTime;
        TextView textViewTitle;
        TextView textViewAddress;

        ImageView imageViewReserve;
        ImageView imageViewFav;
        ImageView imageViewShare;

        Typeface typefaceYekan;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_offers, container, false);

            final DataType.Offer current_offer = MapsActivity.offerArrayList.get(current_id);

            typefaceYekan = Typeface.createFromAsset(getActivity().getAssets(), "B Yekan.ttf");

            textViewPercent = (TextView) rootView.findViewById(R.id.fragment_offers_text_view_percent);
            textViewDiscount = (TextView) rootView.findViewById(R.id.fragment_offers_text_view_discount);
            textViewExpireTime = (TextView) rootView.findViewById(R.id.fragment_offers_text_view_expire_time);
            textViewTitle = (TextView) rootView.findViewById(R.id.fragment_offers_text_view_title);
            textViewAddress = (TextView) rootView.findViewById(R.id.fragment_offers_text_view_address);

            imageViewReserve = (ImageView) rootView.findViewById(R.id.fragment_offers_image_view_reserve);
            imageViewFav = (ImageView) rootView.findViewById(R.id.fragment_offers_image_view_fav);
            imageViewShare = (ImageView) rootView.findViewById(R.id.fragment_offers_image_view_share);

            textViewPercent.setTypeface(typefaceYekan);
            textViewDiscount.setTypeface(typefaceYekan);
            textViewExpireTime.setTypeface(typefaceYekan);
            textViewTitle.setTypeface(typefaceYekan);
            textViewAddress.setTypeface(typefaceYekan);

            textViewPercent.setText("%" + String.valueOf(current_offer.percent));
            Date date = new Date();
            date.setTime(current_offer.expire);
            textViewExpireTime.setText("مهلت استفاده تا " + Calender.getShamsiDate(date));
            textViewTitle.setText(current_offer.title);
            textViewAddress.setText(current_offer.description);

            imageViewReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            imageViewFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*ConnectToServer.post(
                            getActivity(),
                            false,
                            "","",ConnectToServer.baseUri + "api/users/{user_id}/share/{offer_id}"
                    );*/
                    
                    String shareBody = "تخفیف های ویژه در برنامه اندرویدی ادراف: "+current_offer.title+"؛ "+current_offer.description;
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "پیشنهاد های ویژه در ادراف");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "اشتراک"));
                }
            });

            return  rootView;
        }
    }
}
