package adapters;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.farnektest.R;
import com.example.farnektest.ShowLocationActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import models.LocationInfo;

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private List<LocationInfo> mTotalDataList;
    private List<LocationInfo> mFilteredList;

    public LocationListAdapter(Context context) {

        mContext = (Activity) context;
        mTotalDataList = new ArrayList<>();
        mFilteredList = new ArrayList<>();

    }

    public void setDataInList(ArrayList<LocationInfo> mDataList_) {
        mTotalDataList.addAll(mDataList_);
        mFilteredList.addAll(mDataList_);
        notifyDataSetChanged();
    }

    public void setFilterbyDate(String date) {
        mFilteredList=  mTotalDataList.stream()
                .filter(val -> getDateOnly(val.date).equals(getDateOnly( date)))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }
    public String getTimeOnly(String dateStr){

        // Mon Apr 05 03:29:29 GMT+04:00 2021
        //String dateStr = "21/20/2011";

        try {
            DateFormat srcDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            // parse the date string into Date object
            Date date = srcDf.parse(dateStr);

            DateFormat destDf = new SimpleDateFormat("dd MMM HH:mm:ss");

            // format the date into another format
            dateStr = destDf.format(date);
            return  dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;

    }
    public String getDateOnly(String dateStr){

        // Mon Apr 05 03:29:29 GMT+04:00 2021
        //String dateStr = "21/20/2011";

        try {
            DateFormat srcDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            // parse the date string into Date object
            Date date = srcDf.parse(dateStr);

            DateFormat destDf = new SimpleDateFormat("dd MMM yyyy");

            // format the date into another format
            dateStr = destDf.format(date);
            return  dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
      return dateStr;

    }
    @Override
    public int getItemCount() {
        return null == mFilteredList ? 0 : mFilteredList.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View rowView = mContext.getLayoutInflater().inflate(R.layout.item_location, viewGroup, false);
        return new ViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            ((ViewHolder) holder).mDateTimeTV.setText(getTimeOnly( mFilteredList.get(position).date));
            String userName=((ShowLocationActivity)mContext).getNameByID(mFilteredList.get(position).deviceID);
            ((ViewHolder) holder).mNameTV.setText(userName);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDateTimeTV;
        TextView mNameTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateTimeTV = itemView.findViewById(R.id.date_time_tv);
            mNameTV = itemView.findViewById(R.id.name_et);

        }
    }


}