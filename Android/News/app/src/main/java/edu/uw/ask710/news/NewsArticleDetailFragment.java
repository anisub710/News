package edu.uw.ask710.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.uw.ask710.news.dummy.DummyContent;

/**
 * A fragment representing a single NewsArticle detail screen.
 * This fragment is either contained in a {@link NewsArticleListActivity}
 * in two-pane mode (on tablets) or a {@link NewsArticleDetailActivity}
 * on handsets.
 */
public class NewsArticleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String TAG = "NewsArticleDetailFragment";
    public static final String NEWS_PARCEL_KEY = "news_parcel";
    private HasCollapsableImage imageCallback;
    private whichArticle articleCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsArticleDetailFragment() {
    }

     interface HasCollapsableImage{
         void setupToolbar(String imageUrl);
    }

    interface whichArticle{
        void whichArticle(NewsData news);
    }

    public static NewsArticleDetailFragment newInstance(NewsData news){
        Bundle args = new Bundle();
        args.putParcelable(NEWS_PARCEL_KEY, news);
        NewsArticleDetailFragment fragment = new NewsArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getSources(String sourceId, final View rootView){
        String api_key = getString(R.string.NEWS_API_KEY);
        String urlString = "http://beta.newsapi.org/v2/everything?language=en&sources="
                + sourceId + "&apiKey=" + api_key;
//        Request request = new JsonObjectRequest(Request.Method.GET, urlString, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            JSONArray articles = response.getJSONArray("articles");
//                            for (int i = 0; i < 5; i++) {
//                                JSONObject article = articles.getJSONObject(i);
//                                String headline = article.getString("title");
//                            }
//
//                        } catch (JSONException e) {
//                            Log.e(getContext(), "Error parsing json", e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.toString());
//            }
//        });
//
//        RequestSingleton.getInstance(this).add(request);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            articleCallback= (whichArticle) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement whichArticle");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsarticle_detail, container, false);

        Bundle args = getArguments();
        if(args != null){
            NewsData news = args.getParcelable(NEWS_PARCEL_KEY);

            if(getContext() instanceof HasCollapsableImage){
                imageCallback= (HasCollapsableImage) getContext();
                imageCallback.setupToolbar(news.imageUrl);
            }

            TextView headline = (TextView) rootView.findViewById(R.id.headline);
            TextView desc = (TextView) rootView.findViewById(R.id.description);
            TextView source = (TextView)rootView.findViewById(R.id.source_heading);
            headline.setText(news.headline);
            desc.setText(news.description);
            String sourceHeading = "More news from " + news.source_name;
            source.setText(sourceHeading);
            getSources(news.source_id, rootView);
        }


        return rootView;
    }

}
