package edu.uw.ask710.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Anirudh Subramanyam on 10/26/2017.
 */

//custom fragment to display welcome message.
public class WelcomeFragment extends Fragment {

    public WelcomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView rootView = (TextView) inflater.inflate(R.layout.welcome, container, false);
        Bundle args = getArguments();
        if(args != null){
            String welcome = args.getString(NewsArticleListActivity.ARG_PARAM_KEY);
            if(container != null){
                container.removeAllViews();
                rootView.setText("Welcome to Material News! This app is a great source of news using Android's" +
                        "Material Design. This was made using NewsAPI (http://beta.newsapi.org/ )");
            }

        }

        return rootView;
    }
}
