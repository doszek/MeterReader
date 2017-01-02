package pl.edu.agh.kt.aradoszek.meterreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Model.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;

/**
 * Created by doszek on 19.10.2016.
 */

public class PlacesListAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private List<Place> listData;
    private LayoutInflater layoutInflater;

    static class ViewHolder {
        TextView nameView;
        TextView descriptionView;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public PlacesListAdapter(Context aContext, List<Place> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    //================================================================================
    // Adapter Implementation
    //================================================================================

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.descriptionView.setText(listData.get(position).getDescription());
        return convertView;
    }
}
