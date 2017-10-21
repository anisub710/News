//package edu.uw.ask710.news;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.util.LruCache;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//
//import edu.uw.ask710.news.dummy.DummyContent;
//
///**
// * A fragment representing a single NewsArticle detail screen.
// * This fragment is either contained in a {@link NewsArticleListActivity}
// * in two-pane mode (on tablets) or a {@link NewsArticleDetailActivity}
// * on handsets.
// */
//public class NewsArticleDetailFragment extends Fragment {
//    /**
//     * The fragment argument representing the item ID that this fragment
//     * represents.
//     */
//    public static final String ARG_ITEM_ID = "item_id";
//    public static final String TAG = "NewsArticleDetailFragment";
//    /**
//     * The dummy content this fragment is presenting.
//     */
//    private DummyContent.DummyItem mItem;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public NewsArticleDetailFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//
//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.newsarticle_detail, container, false);
//
//        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.newsarticle_detail)).setText(mItem.details);
//        }
//
//        return rootView;
//    }
//
//}
