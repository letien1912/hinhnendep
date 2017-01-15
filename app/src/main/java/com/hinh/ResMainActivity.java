package com.hinh;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.ResAppController;
import helper.ResNavDrawerListAdapter;
import model.ResCategory;

public class ResMainActivity extends BaseActivity {
    private static final String TAG = ResMainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation drawer title
    private List<ResCategory> albumsList;
    private ArrayList<ResNavDrawerItem> navDrawerItems;
    private ResNavDrawerListAdapter adapter;
    int save = -1;
    private Toolbar mtoolbar;
    private String bincode = null;
    private static final String LOAD_ALL_ITEMS = "11111111111";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xxm_main);

        mtoolbar = activeToolBar(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

//        ViewPager viewPager = findViewById(R.id.view)

        navDrawerItems = new ArrayList<>();

        // Getting the albums from shared preferences
        albumsList = ResAppController.getInstance().getPrefManger().getCategories();

        // Insert "Recently Added" in navigation drawer first position
        ResCategory recentAlbum = new ResCategory(null, getString(R.string.nav_drawer_recently_added));
        albumsList.add(0, recentAlbum);
        getListThemeFromServer();

        // them item tu tao len dau
        mDrawerToggle = new ActionBarDrawerToggle(ResMainActivity.this, mDrawerLayout, mtoolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    private void addAllItems(String bincode) {
        // Loop through albums in add them to navigation drawer adapter
        // bo di vi tru cuoi cung, them item tu tao tlen dau
        navDrawerItems = new ArrayList<>();
        Integer bin = null;
        try {
            bincode = new StringBuilder(bincode).reverse().toString();
            Log.d(TAG + "bin", bincode);
            bin = Integer.valueOf(bincode);
            for (int i = 0; i < albumsList.size() - 1; i++) {
                if (i == 0) {
                    navDrawerItems.add(new ResNavDrawerItem(albumsList.get(i).getId(), albumsList.get(i).getTitle(), image[i]));
                    continue;
                }
                if (bin % 10 == 1) {
                    navDrawerItems.add(new ResNavDrawerItem(albumsList.get(i).getId(), albumsList.get(i).getTitle(), image[i]));
                }
                bin /= 10;
            }
        } catch (Exception e) {
            for (int i = 0; i < albumsList.size() - 1; i++) {
                navDrawerItems.add(new ResNavDrawerItem(albumsList.get(i).getId(), albumsList.get(i).getTitle(), image[i]));
            }
        }

        // Setting the nav drawer list adapter
        adapter = new ResNavDrawerListAdapter(getApplicationContext(),
                navDrawerItems, save);
        mDrawerList.setAdapter(adapter);
        // su kien onclick tai day
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    private String getListThemeFromServer() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("bincode");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bincode = dataSnapshot.getValue(String.class);
                if (bincode != null)
                    addAllItems(bincode);
                Log.d("TAG", bincode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                addAllItems(LOAD_ALL_ITEMS);
            }
        });
        return null;
    }

    private class SlideMenuClickListener implements
            AdapterView.OnItemClickListener {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
            adapter.setSave(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * On menu item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Selected settings menu item
                // launch Settings activity
                Intent intent = new Intent(ResMainActivity.this,
                        ResSettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.rateapp: {
                String url = "https://play.google.com/store/apps/details?id=com.son.hinhnendep";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
            return true;

            case R.id.more: {
                String url = "https://play.google.com/store/apps/developer?id=LiveDev";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
            return true;

            case R.id.share: {
                shareTextUrl();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Beautiful wallpapers");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.son.hinhnendep");
        startActivity(Intent.createChooser(share, "Share text to..."));
    }

	/* *
     * Called when invalidateOptionsMenu() is triggered
	 */
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// if nav drawer is opened, hide the action items
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
//		return super.onPrepareOptionsMenu(menu);
//	}

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case 0:
                // Recently added item selected
                // don't pass album id to home fragment
                fragment = ResGridFragment.newInstance(null);
                break;

            default:
                // selected wallpaper category
                // send album id to home fragment to list all the wallpapers
                String albumId = navDrawerItems.get(position).getAlbumId();
                fragment = ResGridFragment.newInstance(albumId);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_containers, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//			setTitle(albumsList.get(position).getTitle());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawers();
                }
            }, 200);

        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

//	@Override
//	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
//	}

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    int image[] = {
            R.drawable.newpic,
            R.drawable.animee,
            R.drawable.girll,
            R.drawable.animall,
            R.drawable.cutee,
            R.drawable.flowerr,
            R.drawable.foodd,
            R.drawable.gamee,
            R.drawable.lovee,
            R.drawable.naturee,
            R.drawable.travell,
            R.drawable.banh,
            R.drawable.chim,
            R.drawable.dongho,
            R.drawable.chimngu,
            R.drawable.nato,
            R.drawable.hoadep,
            R.drawable.chim
    };

    private class myAsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
