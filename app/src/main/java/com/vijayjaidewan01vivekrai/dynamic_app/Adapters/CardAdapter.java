package com.vijayjaidewan01vivekrai.dynamic_app.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vijayjaidewan01vivekrai.dynamic_app.DatabaseHelper;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Data;
import com.vijayjaidewan01vivekrai.dynamic_app.OnClickSet;
import com.vijayjaidewan01vivekrai.collapsingtoolbar_github.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    private ArrayList<Data> cardData;
    ArrayList<Data> filterData;
    private Context context;
    private int pos;
    private int ONE = 1;
    private int TWO = 2;
    private int THREE = 3;
    private int FOUR = 4;
    DatabaseHelper db = null;
    public CardAdapter(ArrayList<Data> cardData,ArrayList<Data> list, Context context, int position) {
        this.cardData = cardData;
        this.filterData = list;
        this.context = context;
        this.pos = position;
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card1, parent, false);
                return new ViewHolder1(v);
            }
            case 2: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card2, parent, false);
                return new ViewHolder2(v);
            }
            case 3: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card3, parent, false);
                return new ViewHolder3(v);
            }
            case 4: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card4, parent, false);
                return new ViewHolder4(v);
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder1) {
            ((ViewHolder1) holder).head.setText(cardData.get(position).getText1());
            ((ViewHolder1) holder).head.setTextColor(Color.parseColor(cardData.get(position).getText_header_color()));
            ((ViewHolder1) holder).head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_header_size()));

            ((ViewHolder1) holder).sub_head.setText(cardData.get(position).getText2());
            ((ViewHolder1) holder).sub_head.setTextColor(Color.parseColor(cardData.get(position).getText_subheader_color()));
            ((ViewHolder1) holder).sub_head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_subheader_size()));

            ((ViewHolder1) holder).desc.setText(cardData.get(position).getText3());
            ((ViewHolder1) holder).desc.setTextColor(Color.parseColor(cardData.get(position).getText_description_color()));
            ((ViewHolder1) holder).desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_description_size()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(onClickSetListener != null)
                        onClickSetListener.onClickFunction(cardData.get(position).getUrl());
                    Toast.makeText(context,cardData.get(position).getUrl(),Toast.LENGTH_SHORT).show();
                    Log.i("URL in adapter",cardData.get(position).getUrl());
                }
            });
            ((ViewHolder1) holder).card.setCardBackgroundColor(Color.parseColor(cardData.get(position).getBg_color()));
        }
        if (holder instanceof ViewHolder2) {
            ((ViewHolder2) holder).head.setText(cardData.get(position).getText1());
            ((ViewHolder2) holder).head.setTextColor(Color.parseColor(cardData.get(position).getText_header_color()));
            ((ViewHolder2) holder).head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_header_size()));

            ((ViewHolder2) holder).sub_head.setText(cardData.get(position).getText2());
            ((ViewHolder2) holder).sub_head.setTextColor(Color.parseColor(cardData.get(position).getText_subheader_color()));
            ((ViewHolder2) holder).sub_head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_subheader_size()));

            ((ViewHolder2) holder).desc.setText(cardData.get(position).getText3());
            ((ViewHolder2) holder).desc.setTextColor(Color.parseColor(cardData.get(position).getText_description_color()));
            ((ViewHolder2) holder).desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_description_size()));

            ((ViewHolder2) holder).card.setCardBackgroundColor(Color.parseColor(cardData.get(position).getBg_color()));

            Glide.with(context)
                    .load(cardData.get(position).getImage())
                    .placeholder(R.drawable.grey)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ViewHolder2) holder).iconImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickSetListener != null)
                        onClickSetListener.onClickFunction(cardData.get(position).getUrl());
//                    Toast.makeText(context,cardData.get(position).getUrl(),Toast.LENGTH_SHORT).show();
                    Log.i("URL in adapter",cardData.get(position).getUrl());
                }
            });
        }
        if (holder instanceof ViewHolder3) {
            ((ViewHolder3) holder).head.setText(cardData.get(position).getText1());
            ((ViewHolder3) holder).head.setTextColor(Color.parseColor(cardData.get(position).getText_header_color()));
            ((ViewHolder3) holder).head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_header_size()));

            ((ViewHolder3) holder).sub_head.setText(cardData.get(position).getText2());
            ((ViewHolder3) holder).sub_head.setTextColor(Color.parseColor(cardData.get(position).getText_subheader_color()));
            ((ViewHolder3) holder).sub_head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_subheader_size()));

            ((ViewHolder3) holder).desc.setText(cardData.get(position).getText3());
            ((ViewHolder3) holder).desc.setTextColor(Color.parseColor(cardData.get(position).getText_description_color()));
            ((ViewHolder3) holder).desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_description_size()));

            ((ViewHolder3) holder).card.setCardBackgroundColor(Color.parseColor(cardData.get(position).getBg_color()));

            Glide.with(context)
                    .load(cardData.get(position).getImage())
                    .placeholder(R.drawable.grey)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ViewHolder3) holder).iconImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickSetListener != null)
                        onClickSetListener.onClickFunction(cardData.get(position).getUrl());
//                    Toast.makeText(context,cardData.get(position).getUrl(),Toast.LENGTH_SHORT).show();
                    Log.i("URL in adapter",cardData.get(position).getUrl());
                    //context.sendBroadcast(i);
                }
            });


            //((ViewHolder3)holder).iconImage.setImageBitmap(CardAdapter.getImage(cardData.get(position).getImage()));
        }
        if (holder instanceof ViewHolder4) {
            ((ViewHolder4) holder).head.setText(cardData.get(position).getText1());
            ((ViewHolder4) holder).head.setTextColor(Color.parseColor(cardData.get(position).getText_header_color()));
            ((ViewHolder4) holder).head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_header_size()));

            ((ViewHolder4) holder).sub_head.setText(cardData.get(position).getText2());
            ((ViewHolder4) holder).sub_head.setTextColor(Color.parseColor(cardData.get(position).getText_subheader_color()));
            ((ViewHolder4) holder).sub_head.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_subheader_size()));

            ((ViewHolder4) holder).desc.setText(cardData.get(position).getText3());
            ((ViewHolder4) holder).desc.setTextColor(Color.parseColor(cardData.get(position).getText_description_color()));
            ((ViewHolder4) holder).desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(cardData.get(position).getText_description_size()));

//            ((ViewHolder4) holder).card.setCardBackgroundColor(Color.parseColor(cardData.get(position).getBg_color()));
            Glide.with(context)
                    .load(cardData.get(position).getImage())
                    .placeholder(R.drawable.grey)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ViewHolder4) holder).background);

            //View.OnClickListener listener = new ScrollingActivity();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    OnClickSet set =  new ScrollingActivity();
                    if(onClickSetListener != null)
                    onClickSetListener.onClickFunction(cardData.get(position).getUrl());
//                    Toast.makeText(context,cardData.get(position).getUrl(),Toast.LENGTH_SHORT).show();
//                    Intent i=new Intent(context,ScrollingActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("URL",cardData.get(position).getUrl());
//                    context.startActivity(i);
//                    //Intent i = new Intent("");
                    Log.i("URL in adapter",cardData.get(position).getUrl());
                    //context.sendBroadcast(i);
                }
            });
//            ((ViewHolder4)holder).iconImage.setImageBitmap(RetrofitClient.getImage(cardData.get(position).getImage()));
        }
    }

    private OnClickSet onClickSetListener;
    public void setClickListener(OnClickSet onClickSet){
        this.onClickSetListener = onClickSet;
    }

    @Override
    public int getItemViewType(int position) {
        switch (pos) {
            case 1: {
                return ONE;
            }
            case 2: {
                return TWO;
            }
            case 3: {
                return THREE;
            }
            case 4: {
                return FOUR;
            }
            default:
                return Integer.parseInt(null);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterData = cardData;
                } else {
                    ArrayList<Data> filteredList = new ArrayList<>();
                    for (Data i: cardData) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (i.getText1().toLowerCase().contains(charString.toLowerCase())
                                || i.getText2().toLowerCase().contains(charString.toLowerCase())
                                || i.getText3().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(i);
                        }
                    }

                    filterData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterData = (ArrayList<Data>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        public TextView head, sub_head, desc;
        CardView card;

        public ViewHolder1(View itemView) {
            super(itemView);

            head = itemView.findViewById(R.id.tv_recycler_item_1);
            sub_head = itemView.findViewById(R.id.tv_recycler_item_2);
            desc = itemView.findViewById(R.id.tv_recycler_item_3);
            card = itemView.findViewById(R.id.card_1);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        public TextView head, sub_head, desc;
        public AppCompatImageView iconImage;
        public CardView card;

        public ViewHolder2(View itemView) {
            super(itemView);

            head = itemView.findViewById(R.id.tv_recycler_item_1);
            sub_head = itemView.findViewById(R.id.tv_recycler_item_2);
            desc = itemView.findViewById(R.id.tv_recycler_item_3);
            iconImage = itemView.findViewById(R.id.icon_image);
            card = itemView.findViewById(R.id.card_2);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {

        public TextView head, sub_head, desc;
        public AppCompatImageView iconImage;
        public CardView card;
        public Resources r;

        public ViewHolder3(View itemView) {
            super(itemView);

            r=itemView.getResources();
            head = itemView.findViewById(R.id.tv_recycler_item_1);
            sub_head = itemView.findViewById(R.id.tv_recycler_item_2);
            desc = itemView.findViewById(R.id.tv_recycler_item_3);
            iconImage = itemView.findViewById(R.id.icon_image);
            card = itemView.findViewById(R.id.card_3);
        }
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder {

        public TextView head, sub_head, desc;
        public AppCompatImageView background;

        public ViewHolder4(View itemView) {
            super(itemView);

            head = itemView.findViewById(R.id.tv_recycler_item_1);
            sub_head = itemView.findViewById(R.id.tv_recycler_item_2);
            desc = itemView.findViewById(R.id.tv_recycler_item_3);
            background = itemView.findViewById(R.id.background);
        }
    }
}