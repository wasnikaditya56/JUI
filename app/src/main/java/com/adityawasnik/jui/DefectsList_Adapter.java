package com.adityawasnik.jui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DefectsList_Adapter extends ArrayAdapter<DefectsList_DataModel>
{
	Context mContext;
	
	ArrayList<DefectsList_DataModel> dataSet = new ArrayList<DefectsList_DataModel>();
	
	// View lookup cache
    private static class ViewHolder 
    {
        TextView txtDate;
        TextView txtAction;
        TextView txtTicket;
       // ImageView ivAction;
    }
	
	public DefectsList_Adapter(ArrayList<DefectsList_DataModel> data, Context context)
	{
		super(context,R.layout.recyclerview_device_layout,data);
		this.mContext=context;
		this.dataSet = data;
	}


	@Override
	public View getView(int position, View row, ViewGroup parent)
	{
		ViewHolder viewHolder;
		final View result;
		DefectsList_DataModel dataModel = getItem(position);
		
		if(row == null)
		{
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.recyclerview_device_layout, parent,false);
			
			viewHolder.txtTicket = (TextView)row.findViewById(R.id.txt_recl_ticket);
			viewHolder.txtDate = (TextView)row.findViewById(R.id.txt_recl_date);
			viewHolder.txtAction = (TextView) row.findViewById(R.id.txt_recl_action);

			result=row;
			 
            row.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) row.getTag();
            result=row;
		}
		
		viewHolder.txtTicket.setText(dataModel.getTicket());
        viewHolder.txtDate.setText(dataModel.getDate());
        viewHolder.txtAction.setText(dataModel.getAction());
        //viewHolder.txtTotalKm.setTextColor(Color.RED);
	//	viewHolder.ivAction.setImageBitmap(convertToBitmap(dataModel.getStatusImage()));

		
		return row;
	}


	//get bitmap image from byte array
	private Bitmap convertToBitmap(byte[] b){

		return BitmapFactory.decodeByteArray(b, 0, b.length);

	}
}
