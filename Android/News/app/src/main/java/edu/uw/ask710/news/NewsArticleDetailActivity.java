package edu.uw.ask710.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.android.volley.toolbox.NetworkImageView;

import static edu.uw.ask710.news.R.id.fab;

/**
 * An activity representing a single NewsArticle detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link NewsArticleListActivity}.
 */
public class NewsArticleDetailActivity extends AppCompatActivity
        implements NewsArticleDetailFragment.HasCollapsableImage, NewsArticleDetailFragment.whichArticle{

    private NewsData news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsarticle_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity

        if (savedInstanceState == null) {

            news = getIntent().getExtras().getParcelable(NewsArticleDetailFragment.NEWS_PARCEL_KEY);
            buildFragment(news);

        }
    }

    //builds fragment and replaces the fragment manager to the reference container in the detail
    //activity. Also sets the share FAB.
    public void buildFragment(final NewsData news){

        NewsArticleDetailFragment fragment = NewsArticleDetailFragment.newInstance(news);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.reference_container, fragment)
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SEND URL + HEADLINE
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, news.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }


    //method implemented for whichArticle interface.
    //builds the fragment for Detail Activity and setup image in collapsing toolbar.
    @Override
    public void whichArticle(NewsData news, Context ctx) {
        buildFragment(news);
        setupToolbar(news.imageUrl);
    }

    //Takes imageurl from news and sets the network image in the collapsing toolbar.
    @Override
    public void setupToolbar(String imageUrl) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        NetworkImageView big_image = (NetworkImageView) findViewById(R.id.big_image);
        big_image.setImageUrl(imageUrl, RequestSingleton.getInstance(NewsArticleDetailActivity.this).getImageLoader());
        big_image.setErrorImageResId(R.drawable.broken_link);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            navigateUpTo(new Intent(this, NewsArticleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
