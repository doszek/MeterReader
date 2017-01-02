package pl.edu.agh.kt.aradoszek.meterreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Model.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.R;

/**
 * Created by doszek on 20.10.2016.
 */

public class MetersListAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private List<Meter> listData;
    private LayoutInflater layoutInflater;

    static class ViewHolder {
        TextView nameView;
        TextView descriptionView;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public MetersListAdapter(Context aContext, List<Meter> listData) {
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
        MetersListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new MetersListAdapter.ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (MetersListAdapter.ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.descriptionView.setText(listData.get(position).getDescription());
        return convertView;
    }
}
