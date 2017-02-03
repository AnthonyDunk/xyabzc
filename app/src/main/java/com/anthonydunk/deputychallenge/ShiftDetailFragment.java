package com.anthonydunk.deputychallenge;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment representing a single Shift detail screen.
 * This fragment is either contained in a {@link ShiftListActivity}
 * in two-pane mode (on tablets) or a {@link ShiftDetailActivity}
 * on handsets.
 */
public class ShiftDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ListContent.Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShiftDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ListContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shift_detail_layout, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {

            // Load image from URL
            ImageView iv = (ImageView) rootView.findViewById(R.id.detailImageView);
            Utility.loadAndDisplayWebImage(mItem.details.image,iv,this.getActivity());

            // Set text details
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM yyyy, HH:mm");
            Date startDateTime = Utility.timeStringtoTime(mItem.details.start);
            String startTime = simpleDateFormat.format(startDateTime);

            String endTime = "";
            if (mItem.details.end!=null && mItem.details.end.length()>0) {
                Date endDateTime = Utility.timeStringtoTime(mItem.details.end);
                endTime = simpleDateFormat.format(endDateTime);
            }
            String text = "Shift start:\n"+startTime+"\n"+mItem.details.startLatitude+","+mItem.details.startLongitude+
                    "\n\nShift end:\n"+endTime+"\n"+
                    mItem.details.endLatitude+","+mItem.details.endLongitude;
            ((TextView) rootView.findViewById(R.id.shift_detail)).setText(text);
        }

        return rootView;
    }
}
