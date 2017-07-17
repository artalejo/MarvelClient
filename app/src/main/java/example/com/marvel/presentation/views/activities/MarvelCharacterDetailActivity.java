package example.com.marvel.presentation.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.marvel.R;
import example.com.marvel.network.models.MarvelCharacter;

import static example.com.marvel.Constants.MARVEL_CHARACTER;

/**
 * Activity that displays the detail of a given marvel character.
 */

public class MarvelCharacterDetailActivity extends AppCompatActivity {

    @BindView(R.id.marvel_character_detail_toolbar) Toolbar marvelCharacterToolbar;
    @BindView(R.id.marvel_character_image) ImageView marvelCharacterImage;
    @BindView(R.id.marvel_character_name) TextView marvelCharacterName;
    @BindView(R.id.marvel_character_description) TextView marvelCharacterDescription;
    private MarvelCharacter character;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marvel_character_detail_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            character = extras.getParcelable(MARVEL_CHARACTER);

        if (character == null)
            // Not showing the activity if there is no character.
            finish();

        if (marvelCharacterToolbar != null) {
            marvelCharacterToolbar.setTitle(character.getName());
            setSupportActionBar(marvelCharacterToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Glide.with(this)
                .load(character.getCharacterThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(marvelCharacterImage);

        marvelCharacterName.setText(character.getName());
        marvelCharacterDescription.setText(character.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
