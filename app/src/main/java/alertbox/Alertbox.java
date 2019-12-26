package alertbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectgithub.R;

public class Alertbox {
    Context context;

    public Alertbox(Context con) {
        // TODO Auto-generated constructor stub
        this.context = con;
    }

    public void sucess(String msg, final Intent intent) {
        final AlertDialog alertDialog = new Builder(
                context).create();

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox, null);
        alertDialog.setView(dialogView);

        TextView alertmsg = (TextView) dialogView.findViewById(R.id.alerttext);
        TextView okay = (TextView) dialogView.findViewById(R.id.alertok);
		alertmsg.setText(msg);

        okay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
                ((Activity) context).startActivity(intent);

            }
        });
        alertDialog.show();
    }


	public void alertdismiss(String msg) {
		final AlertDialog alertDialog = new Builder(
			context).create();

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.alertbox, null);
		alertDialog.setView(dialogView);

		TextView alertmsg = (TextView) dialogView.findViewById(R.id.alerttext);
		TextView okay = (TextView) dialogView.findViewById(R.id.alertok);
		alertmsg.setText(msg);

		okay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();


			}
		});
		alertDialog.show();
	}



}
