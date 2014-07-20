package meow.dogs.work;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class dlg_dtp extends Activity implements OnClickListener
{
    private DatePicker dp;
    private Dialog dialog;
    private Activity activity;
    public dlg_dtp(Activity activity)
    {
        this.activity=activity;
        dialog=new Dialog(activity);
        dialog.setContentView(R.layout.dlg_date);
        dp=(DatePicker)dialog.findViewById(R.id.cddP);
        ((Button)dialog.findViewById(R.id.dial_but)).setOnClickListener(this);
        dialog.setTitle("Введите дату рождения");
        dialog.show();
    }
    @Override
    public void onClick(View v)
    {
        int todayyear=Calendar.getInstance().get(Calendar.YEAR);
        int year=dp.getYear();
        if(todayyear-year<17||todayyear-year>65)
        {
            Toast.makeText(activity,"Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        Registration.setDATE(dp);
        dialog.cancel();
    }
}
