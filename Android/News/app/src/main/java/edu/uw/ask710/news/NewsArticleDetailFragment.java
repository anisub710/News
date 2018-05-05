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
import android.widget.Toast;

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
import java.util.ArrayList;

import edu.uw.ask710.news.dummy.DummyContent;

import static edu.uw.ask710.news.R.id.link;

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
    private ArrayList<NewsData> stories;
    private NewsData story;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsArticleDetailFragment() {
    }

    //setup interface to determine whether the activity has a collapsing toolbar or not.
     interface HasCollapsableImage{
         void setupToolbar(String imageUrl);
    }

    //setup interface to determine which activity to fill data in.
    interface whichArticle{
        void whichArticle(NewsData news, Context ctx);
    }

    //gets bundle and builds a new fragment for Detail Activity.
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

    //takes sourceID, container and inflater to get the reference links similar to the source of the
    //main article being displayed and making each of the reference links clickable and display on
    //activity based on orientation.
    public void getSources(String sourceId, final ViewGroup container, final LayoutInflater inflater){
        String api_key = getString(R.string.NEWS_API_KEY);
        String urlString = "http://beta.newsapi.org/v2/everything?language=en&sources="
                + sourceId + "&apiKey=" + api_key;
        Request request = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stories = new ArrayList<NewsData>();
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            for (int i = 0; i < 5; i++) {
                                TextView rootView = (TextView) inflater.inflate(R.layout.reference_links, null);
                                JSONObject article = articles.getJSONObject(i);
                                JSONObject source = article.getJSONObject("source");
                                String source_id = source.getString("id");
                                String source_name = source.getString("name");
                                String headline = article.getString("title");
                                String imageUrl = article.getString("urlToImage");
                                String description = article.getString("description");
                                String url = article.getString("url");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                long publishedTime = 0;
                                try {
                                    String pubDateString = article.getString("publishedAt");
                                    if (!pubDateString.equals("null"))
                                        publishedTime = formatter.parse(pubDateString).getTime();
                                } catch (ParseException e) {
//                                    Log.e(TAG, "Error parsing date", e); //Android log the error
                                }
                                story = new NewsData(headline, imageUrl, description,
                                        publishedTime, url, source_id, source_name);
                                stories.add(story);
                                rootView.setText(headline);
                                setOnClick(rootView, story);
                                if(container != null){
                                    container.addView(rootView);
                                }

                            }



                        } catch (JSONException e) {
//                            Log.e(, "Error parsing json", e);
                        }
                    }
                }, new Response.ErrorListener() {
//
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.toString());
            }
        });

        RequestSingleton.getInstance(getContext()).add(request);
    }

    //sets on click for a single reference link
    public void setOnClick(View rootView, final NewsData story){
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Context curr = getContext();
                articleCallback.whichArticle(story, curr);
            }
        });
    }

    //checks if whichArticle interface is implemented.
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            articleCallback= (whichArticle) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement whichArticle");
        }
    }


    //sets up detail activity with details about the article and reference links.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsarticle_detail, container, false);

        Bundle args = getArguments();
        if(args != null){
            NewsData news = args.getParcelable(NEWS_PARCEL_KEY);

            if(getContext() instanceof HasCollapsableImage) {
                imageCallback = (HasCollapsableImage) getContext();
                imageCallback.setupToolbar(news.imageUrl);
            }
            if(container != null){
                container.removeAllViews();
            }
            TextView headline = (TextView) rootView.findViewById(R.id.headline);
            TextView desc = (TextView) rootView.findViewById(R.id.description);
            TextView source = (TextView)rootView.findViewById(R.id.source_heading);
            getSources(news.source_id, container, inflater);
            headline.setText(news.headline);
            desc.setText(news.description);
            String sourceHeading = "More news from " + news.source_name;
            source.setText(sourceHeading);
        }


        return rootView;
    }

}
