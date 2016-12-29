package pl.edu.agh.kt.aradoszek.meterreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Measurement;
import pl.edu.agh.kt.aradoszek.meterreader.R;

/**
 * Created by doszek on 29.11.2016.
 */

public class MeasurementsListAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private List<Measurement> listData;
    private LayoutInflater layoutInflater;

    static class ViewHolder {
        TextView valueView;
        TextView meterNameView;
        TextView createdAtView;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public MeasurementsListAdapter(Context aContext, List<Measurement> listData) {
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
        MeasurementsListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.measurments_list_row_layout, null);
            holder = new MeasurementsListAdapter.ViewHolder();
            holder.valueView = (TextView) convertView.findViewById(R.id.value);
            holder.meterNameView = (TextView) convertView.findViewById(R.id.meter_name);
            holder.createdAtView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (MeasurementsListAdapter.ViewHolder) convertView.getTag();
        }

        holder.valueView.setText(Double.toString(listData.get(position).getValue()));
        holder.createdAtView.setText("Date: " + listData.get(position).getDate());
        holder.meterNameView.setText("Meter: " + listData.get(position).getMeterName());
        return convertView;
    }
}

