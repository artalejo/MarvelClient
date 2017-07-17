package example.com.marvel.presentation.views.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import example.com.marvel.R;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.network.models.MarvelCharacter;

import static example.com.marvel.network.models.MarvelCharacter.LOADING_TYPE;
import static example.com.marvel.network.models.MarvelCharacter.MARVEL_CHARACTER_TYPE;

public class MarvelCharactersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_MARVEL_CHARACTER = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final MarvelRepository marvelRepository;
    private CharacterFavoriteActionCallback favoriteCharacterActionCallback;
    private Context context;

    private List<MarvelCharacter> allCharacters;
    private HashSet<Long> favoriteIDs;
    private boolean isLoading = false;

    public MarvelCharactersAdapter(Context context, MarvelRepository marvelRepository,
                                   CharacterFavoriteActionCallback callback) {
        this.context = context;
        this.allCharacters = new ArrayList<>();
        this.favoriteCharacterActionCallback = callback;
        this.marvelRepository = marvelRepository;
        this.favoriteIDs = marvelRepository.getFavoriteMarvelCharactersIds();
    }

    public MarvelCharactersAdapter(Context context, List<MarvelCharacter> characters,
                                   MarvelRepository marvelRepository, CharacterFavoriteActionCallback callback) {
        this.context = context;
        this.allCharacters = characters;
        this.favoriteCharacterActionCallback = callback;
        this.marvelRepository = marvelRepository;
        this.favoriteIDs = marvelRepository.getFavoriteMarvelCharactersIds();
    }

    public void refreshMarvelCharactersAdapter(){
        this.favoriteIDs = marvelRepository.getFavoriteMarvelCharactersIds();
        notifyDataSetChanged();
    }

    public void updateMarvelCharactersAdapter(List<MarvelCharacter> newCharacters){
        removeLoadingView();
        this.favoriteIDs = marvelRepository.getFavoriteMarvelCharactersIds();
        int previousCount = getItemCount();
        this.allCharacters.addAll(newCharacters);
        notifyItemRangeInserted(previousCount, allCharacters.size() - 1);
    }

    public void addLoadingView() {
        if (!isLoading) {
            allCharacters.add(new MarvelCharacter(LOADING_TYPE));
            notifyItemInserted(allCharacters.size() - 1);
            isLoading = true;
        }
    }

    private void removeLoadingView() {
        if (allCharacters.size() > 0) {
            allCharacters.remove(allCharacters.size() - 1);
            notifyItemRemoved(allCharacters.size() - 1);
            isLoading = false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MARVEL_CHARACTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marvel_character_row, parent, false);
            return new MarvelCharacterViewHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_characters_loading, parent, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MarvelCharacterViewHolder) {
            MarvelCharacterViewHolder marvelHolder = (MarvelCharacterViewHolder) holder;
            MarvelCharacter character = allCharacters.get(position);

            Glide.with(context)
                    .load(character.getCharacterThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(marvelHolder.characterImage);

            initializeFavoriteIcon(marvelHolder, character);
            marvelHolder.characterName.setText(character.getName());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(allCharacters.get(position).getType().equals(MARVEL_CHARACTER_TYPE))
            return VIEW_TYPE_MARVEL_CHARACTER;
        else
            return VIEW_TYPE_LOADING;
    }

    @Override
    public int getItemCount() {
        if (allCharacters != null)
            return allCharacters.size();
        return 0;
    }

    private void initializeFavoriteIcon(MarvelCharacterViewHolder holder, MarvelCharacter character){

        if (favoriteIDs.contains(character.getId())) {
            character.setFavorited(true);
            holder.characterFavoriteIcon.setBackgroundResource(R.drawable.ic_favorite);
        }
        else {
            character.setFavorited(false);
            holder.characterFavoriteIcon.setBackgroundResource(R.drawable.ic_non_favorite);
        }
    }

    private void toggleFavoriteIcon(MarvelCharacterViewHolder holder, boolean isFavorite){
        if (isFavorite) {
            holder.characterFavoriteIcon.setBackgroundResource(R.drawable.ic_favorite);
        }
        else {
            holder.characterFavoriteIcon.setBackgroundResource(R.drawable.ic_non_favorite);

        }
    }

    public void removeCharacterById(long characterId) {
        for (int i = 0; i <= allCharacters.size() - 1; i++) {
            MarvelCharacter character = allCharacters.get(i);
            if (character.getId() == characterId) {
                favoriteCharacterActionCallback.setCharacterFavoriteStatus(character, false);
                allCharacters.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, allCharacters.size());
                break;
            }
        }
    }

    public List<MarvelCharacter> getUpdatedMarvelCharacters() {
        return allCharacters;
    }

                                /* VIEW HOLDERS */

    public class MarvelCharacterViewHolder extends RecyclerView.ViewHolder
                                           implements View.OnClickListener {

        @Nullable @BindView(R.id.marvel_character_image) ImageView characterImage;
        @Nullable @BindView(R.id.marvel_character_favorite) ImageView characterFavoriteIcon;
        @Nullable @BindView(R.id.marvel_character_name) TextView characterName;

        public MarvelCharacterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Optional @OnClick(R.id.marvel_character_favorite)
        public void favoriteActionOnClick() {
            int pos = getAdapterPosition();
            MarvelCharacter character = allCharacters.get(pos);
            boolean isFavorite = !character.isFavorited();
            character.setFavorited(isFavorite);
            toggleFavoriteIcon(this, isFavorite);
            favoriteCharacterActionCallback.setCharacterFavoriteStatus(character, isFavorite);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            MarvelCharacter characterToShow = allCharacters.get(pos);
            favoriteCharacterActionCallback.showCharacterDetail(characterToShow);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.more_characters_progress_bar) ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface CharacterFavoriteActionCallback {
        void setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite);
        void showCharacterDetail(MarvelCharacter character);
    }
}
