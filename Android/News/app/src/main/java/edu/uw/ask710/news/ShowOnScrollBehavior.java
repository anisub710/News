package edu.uw.ask710.news;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by anirudhsubramanyam on 10/21/17.
 */

public class ShowOnScrollBehavior extends AppBarLayout.ScrollingViewBehavior{

    public ShowOnScrollBehavior(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof FloatingActionButton || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout,
                child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);
        if(dyConsumed > 0){
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for(View view : dependencies){
                if(view instanceof FloatingActionButton){
                    ((FloatingActionButton) view).show();
                }
            }
        }else if(dyConsumed < 0){
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for(View view : dependencies){
                if(view instanceof FloatingActionButton){
                    ((FloatingActionButton) view).hide();
                }
            }
        }
    }
}
