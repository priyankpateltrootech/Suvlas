package common;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.suvlas.R;


public class Request_loader {
	private Context context;
	private Dialog please_wait_dialog;

	public Request_loader(Context context) {
		super();
		this.context = context;
		please_wait_dialog = new Dialog(context);
		please_wait_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		please_wait_dialog.setContentView(R.layout.progress);

		please_wait_dialog.setCancelable(false);
		final Window window = please_wait_dialog.getWindow();
	    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
	    //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		

	}


	public void showpDialog() {
		if (!please_wait_dialog.isShowing()){
			please_wait_dialog.show();}
	}

	public void hidepDialog() {
		try {
			if (please_wait_dialog.isShowing()){
				please_wait_dialog.dismiss();}
		}
		catch (Exception e)
		{

		}
		}

	
	 
}
