package andresaraujo.github.io.designlibraryexample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import andresaraujo.github.io.designlibraryexample.R;

public class ChatDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);

        setupToolbar();
        setupCollapsingToolbar(name);

        loadToolbarBg();
    }

    private void loadToolbarBg() {
        ImageView imageView = (ImageView) findViewById(R.id.toolbar_bg);
        Glide.with(this)
                .load(R.drawable.octocat2)
                .centerCrop()
                .into(imageView);
    }

    private void setupCollapsingToolbar(String name) {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(name);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
