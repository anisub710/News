package edu.uw.ask710.news;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.LruCache;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
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

import edu.uw.ask710.news.dummy.DummyContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.apiKey;
import static android.R.attr.author;
import static android.R.attr.fragment;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * An activity representing a list of NewsArticles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NewsArticleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NewsArticleListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static final String TAG = "NewsArticleListActivity";
    private ArrayList<NewsData> stories;
    private NewsAdapter newsAdapter;
    private boolean mTwoPane;
    private FloatingActionButton fab;
    private RecyclerView list;
    private String searchQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsarticle_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        handleIntent(getIntent());

        fillData("");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
                list.smoothScrollToPosition(0);

            }
        });

        if (findViewById(R.id.newsarticle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    //SEARCH
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        //set search icon color to white.
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.iconColor));
        menu.findItem(R.id.action_search).setIcon(drawable);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    protected void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getSearchData(query);
        }
    }

    protected void getSearchData(String query) {
        searchQuery = query;
        fillData(searchQuery);
    }

    protected void fillData(String query) {
        stories = new ArrayList<NewsData>();
        downloadNewsData(query);
        list = (RecyclerView) findViewById(R.id.newsarticle_list);
        newsAdapter = new NewsAdapter(stories);
        list.setAdapter(newsAdapter);

    }


    public void downloadNewsData(String query) {
        String api_key = getString(R.string.NEWS_API_KEY);
        String urlString = "http://beta.newsapi.org/v2/top-headlines?country=us&language=en&q="
                + query + "&apiKey=" + api_key;
        Request request = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        newsAdapter.clear();
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject article = articles.getJSONObject(i);

                                String headline = article.getString("title");
                                String imageUrl = article.getString("urlToImage");
                                String description = article.getString("description");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                long publishedTime = 0;
                                try {
                                    String pubDateString = article.getString("publishedAt");
                                    if (!pubDateString.equals("null"))
                                        publishedTime = formatter.parse(pubDateString).getTime();
                                } catch (ParseException e) {
                                    Log.e(TAG, "Error parsing date", e); //Android log the error
                                }
                                NewsData story = new NewsData(headline, imageUrl, description, publishedTime);
                                stories.add(story);
//                                Log.v(TAG, story.description);
                                newsAdapter.notifyDataSetChanged();
//                                JSONObject sources = article.getJSONObject("source");
//                                String author = article.getString("author");
//                                Toast.makeText(NewsArticleListActivity.this, author, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing json", e);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestSingleton.getInstance(this).add(request);
    }


    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

        private final ArrayList<NewsData> mValues;

        public NewsAdapter(ArrayList<NewsData> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newsarticle_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.newsItem = mValues.get(position);
            TextView headline = (TextView) holder.mView.findViewById(R.id.id);
            NetworkImageView image = (NetworkImageView) holder.mView.findViewById(R.id.image);
            headline.setText(mValues.get(position).headline);
            image.setImageUrl(mValues.get(position).imageUrl, RequestSingleton.getInstance(NewsArticleListActivity.this).getImageLoader());
            image.setErrorImageResId(R.drawable.broken_link);
//            content.setText(mValues.get(position).imageUrl);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(NewsArticleDetailFragment.NEWS_PARCEL_KEY, holder.newsItem);
                        NewsArticleDetailFragment fragment = new NewsArticleDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.newsarticle_detail_container, fragment)
                                .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void clear() {
            int size = this.mValues.size();
            this.mValues.clear();
            notifyItemRangeRemoved(0, size);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public NewsData newsItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;

            }

//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
        }
    }
}




