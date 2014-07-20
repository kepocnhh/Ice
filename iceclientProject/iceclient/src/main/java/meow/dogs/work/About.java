package meow.dogs.work;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity implements OnClickListener
{
    private Dialog dialog;
    private Activity activity;
    public About(Activity activity)
    {
        this.activity=activity;
        dialog=new Dialog(activity);
        dialog.setContentView(R.layout.about);
        ((Button)dialog.findViewById(R.id.dialog_button)).setOnClickListener(this);
        dialog.setTitle("О программе");
        ((TextView)dialog.findViewById(R.id.tvaboutn)).setText(MainActivity.getVersionName(this.activity));
        dialog.show();
    }
    @Override
    public void onClick(View arg0)
    {
        dialog.cancel();
    }
}
