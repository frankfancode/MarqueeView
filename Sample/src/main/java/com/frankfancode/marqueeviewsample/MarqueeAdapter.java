package com.frankfancode.marqueeviewsample;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fxd on 01/01/2017.
 */

public class MarqueeAdapter extends RecyclerView.Adapter<MarqueeAdapter.ViewHolder> {
    private List<MarqueeEntity> marqueeEntities;

    public void setData(List<MarqueeEntity> marqueeEntities, int itemCount) {
        this.marqueeEntities = marqueeEntities;
        int remainder = marqueeEntities.size() % itemCount;
        if (remainder > 0) {
            for (int i = 0; i >= itemCount; i++) {
               this.marqueeEntities.remove(marqueeEntities.size()-1);
            }
        }
        if (marqueeEntities.size() > itemCount) {
            for (int i = 0; i < itemCount; i++) {
                this.marqueeEntities.add(marqueeEntities.size(), marqueeEntities.get(i));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marquee, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarqueeEntity item = marqueeEntities.get(position);
        if (!TextUtils.isEmpty(item.title)) {
            holder.title.setText(Html.fromHtml(item.title));
        }
    }

    @Override
    public int getItemCount() {
        if (marqueeEntities == null || marqueeEntities.size() == 0) {
            return 0;
        } else {
            return marqueeEntities.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.marquee_title);
        }

    }
}
