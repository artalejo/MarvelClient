package example.com.marvel.presentation.views.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import example.com.marvel.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ObjectViewHolder> {

    private final AppCompatActivity activity;
    private ActivityCallback activityCallback;
    private Context context;

    private List<Object> objects;

    public RecyclerAdapter(Context context, List<Object> objects, ActivityCallback callback) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.objects = objects;
        this.activityCallback = callback;
    }

    public void updateObjectsAdapter(List<Object> objects){
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public ObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_row, parent, false);
        ObjectViewHolder viewHolder = new ObjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ObjectViewHolder holder, final int position) {

        Object good = objects.get(position);

//        int goodImageID = context.getResources().getIdentifier(good.getResourceName() ,
//                            "drawable", context.getPackageName());
//        holder.characterImage.setBackgroundResource(goodImageID);
//        holder.characterName.setText(good.getName());
//        holder.characterDescription.setText(getPriceDescription(activity, good));
//        holder.goodQuantity.setText(good.getQuantityStr());
    }

    @Override
    public int getItemCount() {
        if (objects != null)
            return objects.size();
        return 0;
    }


    public class ObjectViewHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.good_image) ImageView characterImage;
//        @BindView(R.id.good_name) TextView characterName;
//        @BindView(R.id.price_description) TextView characterDescription;
//        @BindView(R.id.good_quantity) TextView goodQuantity;
//        @BindView(R.id.add_good) ImageView addObject;
//        @BindView(R.id.remove_good) ImageView removeObject;
//        @BindView(R.id.add_to_basket) ImageView addToBasket;

        public ObjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

//        @OnClick(R.id.add_good)
//        public void addObjectOnClick() {
//            int pos = getAdapterPosition();
//            Object good = objects.get(pos);
//            good.addObjectQuantity();
//            goodQuantity.setText(good.getQuantityStr());

//        }
    }

    public interface ActivityCallback {
        void sendSomethingToActivity(Object goodToAdd);
    }
}
