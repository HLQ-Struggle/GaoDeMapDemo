package cn.hlq.gaodemapdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

import cn.hlq.gaodemapdemo.R;

/**
 * Created by HLQ on 2017/5/15
 */

public class GaoDeSearchResultAdapter extends BaseAdapter {

    private List<PoiItem> data;
    private Context context;

    private int selectedPosition = 0;

    public GaoDeSearchResultAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<PoiItem> data) {
        this.data = data;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gaode_location_search_info, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bindView(position);
        return convertView;
    }

    class ViewHolder {
        TextView textTitle;
        TextView textSubTitle;
        ImageView imageCheck;

        public ViewHolder(View view) {
            textTitle = (TextView) view.findViewById(R.id.id_gaode_location_search_title);
            textSubTitle = (TextView) view.findViewById(R.id.id_gaode_location_search_content);
            imageCheck = (ImageView) view.findViewById(R.id.id_gaode_location_search_confirm_icon);
        }

        public void bindView(int position) {
            if (position >= data.size())
                return;
            PoiItem poiItem = data.get(position);
            textTitle.setText(poiItem.getTitle());
            textSubTitle.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
            imageCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
            textSubTitle.setVisibility((position == 0 && poiItem.getPoiId().equals("regeo")) ? View.GONE : View.VISIBLE);
        }
    }
}