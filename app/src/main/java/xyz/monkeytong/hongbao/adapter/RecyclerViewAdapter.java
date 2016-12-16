package xyz.monkeytong.hongbao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xyz.monkeytong.hongbao.R;
import xyz.monkeytong.hongbao.bean.Item;

/**
 * Created by Zhongrong.Yu on 2016/12/16.
 */

public class RecyclerViewAdapter extends SimpleAdapter<Item> {



    public RecyclerViewAdapter(Context mContext, List datas) {
        super(mContext, R.layout.item_grid, datas);
    }



    @Override
    protected void convert(BaseViewHolder viewHoder, Item item) {
        ImageView imv= viewHoder.getImageView(R.id.item_img);
        TextView txt= viewHoder.getTextView(R.id.item_text);
        imv.setBackgroundResource(item.getImg());
        txt.setText(item.getName());

    }
}
