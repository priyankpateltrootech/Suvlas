package adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suvlas.OrderResumeActivity;
import com.suvlas.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.MenuOrderBean;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	//private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<Integer, List<String>> _listDataChild;
	ArrayList<MenuOrderBean> menuOrderBeen_shared_pre_array;

	public ExpandableListAdapter(Context context, ArrayList<MenuOrderBean> menuOrderBeen_shared_pre_array,
                                 HashMap<Integer, List<String>> listChildData) {
		this._context = context;
		this.menuOrderBeen_shared_pre_array = menuOrderBeen_shared_pre_array;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(groupPosition);

	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

		//final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(_listDataChild.get(groupPosition).get(0));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.menuOrderBeen_shared_pre_array.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.menuOrderBeen_shared_pre_array.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        TextView resume_exp_order_txt_price = (TextView)convertView.findViewById(R.id.resume_exp_order_txt_price);
        TextView txt_exp_qty = (TextView)convertView.findViewById(R.id.txt_exp_qty);

		final ImageView expandable_img_group = (ImageView)convertView.findViewById(R.id.expandable_img_group);

		lblListHeader.setTypeface(null, Typeface.BOLD);
        resume_exp_order_txt_price.setTypeface(null, Typeface.BOLD);
        txt_exp_qty.setTypeface(null, Typeface.BOLD);

        lblListHeader.setText(menuOrderBeen_shared_pre_array.get(groupPosition).getName());
        resume_exp_order_txt_price.setText(menuOrderBeen_shared_pre_array.get(groupPosition).getPrice());
        txt_exp_qty.setText(menuOrderBeen_shared_pre_array.get(groupPosition).getQuantity());


		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
