package meow.dogs.work;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ice.BaseMessage;
import ice.DataCass;

public class CDcass extends Activity implements OnClickListener
{
    private Activity activity;
    private Dialog dialoggen;
    private Button dbutton;
    private Button dbutcncl;
    private EditText det1;
    private EditText det2;
    public CDcass(Activity activity)
    {
        this.activity=activity;
        dialoggen=new Dialog(activity);
        dialoggen.setContentView(R.layout.dlgcass);
        det1=(EditText)dialoggen.findViewById(R.id.etcasssum);
        det2=(EditText)dialoggen.findViewById(R.id.etcassfam);
        dbutton=(Button)dialoggen.findViewById(R.id.bcassok);
        dbutcncl=(Button)dialoggen.findViewById(R.id.bcasscnclc);
        dbutton.setOnClickListener(this);
        dbutcncl.setOnClickListener(this);
        dbutton.setText("Отправить");
        dbutcncl.setText("Отмена");
        String title=null;
        if(CDgencass.DC== DataCass.TypeEvent.inkasator)
            title=MainActivity.mainstrings.DataCass.get(0);
        if(CDgencass.DC==DataCass.TypeEvent.promoter)
            title=MainActivity.mainstrings.DataCass.get(1);
        if(CDgencass.DC==DataCass.TypeEvent.cass)
            title=MainActivity.mainstrings.DataCass.get(2);
        dialoggen.setTitle(title);
        dialoggen.show();
        if(!title.equals(MainActivity.mainstrings.DataCass.get(0)))
        {
            det2.setVisibility(View.GONE);
            ((TextView)dialoggen.findViewById(R.id.tvcassfam)).setVisibility(View.GONE);
        }
    }
    private void buton_Cancel()
    {
        dialoggen.cancel();
    }
    private void buton_Ok()
    {
        double numb=-1;
        try {
            numb=MainActivity.check_double(det1.getText().toString());
        } catch (InterruptedException e) {
        }
        if(numb<0)
        {
            Toast.makeText(this.activity,"Сумма введена неверно!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(CDgencass.DC==DataCass.TypeEvent.inkasator)
        {
            if(!MainActivity.checkifo(det2.getText().toString()))
            {
                Toast.makeText(this.activity,"Фамилия введена неверно!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        openNewGameDialog("Вы уверены в том, что хотите отправить данные?");
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.bcassok:
                buton_Ok();
                break;
            case R.id.bcasscnclc:
                buton_Cancel();
        }
    }
    private void openNewGameDialog(String s1)
    {
        new AlertDialog.Builder(this.activity)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DataCass newdc=new DataCass(Double.parseDouble
                                (det1.getText().toString()),det2.getText().toString(),CDgencass.DC);
                        Location location;
                        location=MainActivity.myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location==null)
                            location=MainActivity.myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location==null)
                        {
                            Toast.makeText(activity, "Невозможно получить координаты!\n" +
                                    "Перепроверте настройки сети и GPS\n" +
                                    "Или попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        newdc.SetPlace(location.getLatitude(),location.getLongitude());
                        newdc.SetDate(new Date());
                        String str = Scktmy.
                                //sendwithoutlogin
                                sendobject
                                ((BaseMessage)newdc,activity);
                        if(str==null)
                            return;
                        if(str.equals("cassok"))
                        {
                            Toast.makeText(activity, "Отправка прошла успешно", Toast.LENGTH_SHORT).show();
                            dialoggen.cancel();
                            return;
                        }
                        else
                        {
                            Toast.makeText(activity, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                })
                .setNeutralButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}