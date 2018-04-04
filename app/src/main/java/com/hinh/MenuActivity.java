package com.hinh;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends BaseActivity {

    private Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mtoolbar = activeToolBar(true);
    }

    public void rateApp(View v) {
        String url = "https://play.google.com/store/apps/details?id=com.son.hinhnendep";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void moreApp(View v) {
        String url = "https://play.google.com/store/apps/developer?id=LiveDev";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void setting(View v) {
        Intent intent = new Intent(this,
                ResSettingsActivity.class);
        startActivity(intent);
    }

    public void shareApp(View v) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Beautiful wallpapers");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.son.hinhnendep");
        startActivity(Intent.createChooser(share, "Share text to..."));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
