package adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suvlas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import bean.ExpandableChild;
import bean.ExpandableGroup;
import bean.ExpandableOrderChild;
import bean.ExpandableOrderGroup;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandableOrderGroup> groups;
	DecimalFormat df;

	public ExpandListAdapter(Context context, ArrayList<ExpandableOrderGroup> groups) {
		this.context = context;
		this.groups = groups;
		df = new DecimalFormat("00.00");
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ExpandableOrderChild> chList = groups.get(groupPosition).getExpandableOrderChild();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

		ExpandableOrderChild child = (ExpandableOrderChild) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.lblListItem);
		TextView tv_price = (TextView)convertView.findViewById(R.id.resume_exp_order_item_txt_price);
		TextView tv_quantity = (TextView)convertView.findViewById(R.id.txt_exp_item_qty);
		tv.setText(child.getName());
		tv_price.setText(child.getPrice());
		tv_quantity.setText(child.getQuantity());

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ExpandableOrderChild> chList = groups.get(groupPosition).getExpandableOrderChild();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		ExpandableOrderGroup group = (ExpandableOrderGroup) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.expandable_list_group, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.lblListHeader);
		TextView txt_exp_qty = (TextView)convertView.findViewById(R.id.txt_exp_qty);
		TextView resume_exp_order_txt_price = (TextView)convertView.findViewById(R.id.resume_exp_order_txt_price);
		ImageView expandable_img_group = (ImageView)convertView.findViewById(R.id.expandable_img_group);

		tv.setText(group.getName());
		txt_exp_qty.setText(group.getQuantity());
		Double price = Double.valueOf(group.getPrice());
		Log.e("price",price+"");
		resume_exp_order_txt_price.setText(df.format(price));

		if (isExpanded)
		{
			//Log.e("expand","expand");
			expandable_img_group.setImageResource(R.drawable.gray_circle_minus);
		}
		else
		{
			//Log.e("collapse","collapse");
			expandable_img_group.setImageResource(R.drawable.gray_circle);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}