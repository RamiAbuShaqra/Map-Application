package com.gmail.rami.abushaqra79.mapapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.rami.abushaqra79.mapapplication.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> mItems;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public ItemAdapter(List<Item> items, ListItemClickListener listener) {
        mItems = items;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new ItemViewHolder(itemView, mItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItemList(List<Item> itemList) {
        this.mItems = itemList;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mItemImage;
        private final TextView mItemTitle;

        public ItemViewHolder(View itemView, List<Item> items) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.item_image);
            mItemTitle = itemView.findViewById(R.id.item_title);
            mItems = items;

            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            mItemTitle.setText(item.getTitle());
            mItemImage.setImageResource(item.getImageResource());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getLayoutPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
