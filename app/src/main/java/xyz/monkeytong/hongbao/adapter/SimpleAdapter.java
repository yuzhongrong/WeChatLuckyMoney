package xyz.monkeytong.hongbao.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 * 在这里传进BaseViewHolder 所以子类SimpleAdapter 只需要传递T
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

    public SimpleAdapter(Context mContext, int layout) {
        super(mContext, layout);
    }

    public SimpleAdapter(Context mContext, int layout, List<T> datas) {
        super(mContext, layout, datas);
    }
}
