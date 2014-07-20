package meow.dogs.work;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ice.BaseMessage;
import ice.user;

public class Registration extends Activity
{
    static Button DateButton;
    EditText etMass[];
    TextView tvMass[];
    CheckBox checkboxBold;
    static int dymd[];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //
        etMass=new EditText[7];
        tvMass=new TextView[7];
        checkboxBold = (CheckBox)findViewById(R.id.rc);
        if(MainActivity.toRegistration[8]!=null)
        checkboxBold.setChecked(true);
        etMass[0] = (EditText) findViewById(R.id.rei);
        etMass[1] = (EditText) findViewById(R.id.ref);
        etMass[2] = (EditText) findViewById(R.id.reo);
        etMass[3] = (EditText) findViewById(R.id.ret);
        etMass[4] = (EditText) findViewById(R.id.rel);
        etMass[5] = (EditText) findViewById(R.id.rep);
        etMass[6] = (EditText) findViewById(R.id.repp);
        tvMass[0] = (TextView) findViewById(R.id.rtvi);
        tvMass[1] = (TextView) findViewById(R.id.rtvf);
        tvMass[2] = (TextView) findViewById(R.id.rtvo);
        tvMass[3] = (TextView) findViewById(R.id.rtvt);
        tvMass[4] = (TextView) findViewById(R.id.rtvl);
        tvMass[5] = (TextView) findViewById(R.id.rtvp);
        tvMass[6] = (TextView) findViewById(R.id.rtvpp);
        for(int i=0;i<etMass.length;i++)
        {
            etMass[i].setText(MainActivity.toRegistration[i]);
            final int finalI = i;
            etMass[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvMass[finalI].setTextColor(Color.parseColor("#3399CC"));
                }
            });
            etMass[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (v == etMass[finalI]) {
                        tvMass[finalI].setTextColor(Color.parseColor("#3399CC"));
                    }
                }
            });
        }
        DateButton = (Button) findViewById(R.id.datebutton);
        if(MainActivity.toRegistration[7]!=null)
        DateButton.setText(MainActivity.toRegistration[7]);
        dymd=new int[3];
    }
    static void setDATE(DatePicker dp)
    {
        dymd[0]=dp.getYear();
        dymd[1] =dp.getMonth()+1;
        dymd[2] =dp.getDayOfMonth();
        MainActivity.toRegistration[7]=String.valueOf(dymd[0])+"."+String.valueOf(dymd[1])+"."+String.valueOf(dymd[2]);
        DateButton.setText(MainActivity.toRegistration[7]);
        DateButton.setTextColor(Color.parseColor("#FFFFFF"));
    }
    public void butreg_dlgd(View v)
    {
        new dlg_dtp(this);
    }
    public void butok(View v)
    {
        if(checkall())
        {
            openNewGameDialog("Вы уверены в том что хотите отправить данные?");
        }
        else
        {
            Toast.makeText(Registration.this, "Данные введены неверно!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkall()
    {
        boolean flag=true;
        for(int i=0;i<3;i++)
        {
            if(!MainActivity.checkifo(etMass[i].getText().toString()))
            {
                tvMass[i].setTextColor(Color.parseColor("#FF0000"));
                flag= false;
            }
            else
                tvMass[i].setTextColor(Color.parseColor("#3399CC"));
        }
        if(!checkphone(etMass[3].getText().toString()))
        {
            tvMass[3].setTextColor(Color.parseColor("#FF0000"));
            flag= false;
        }
        else
            tvMass[3].setTextColor(Color.parseColor("#3399CC"));
        if(!MainActivity.checkemail(etMass[4].getText().toString()))
        {
            tvMass[4].setTextColor(Color.parseColor("#FF0000"));
            flag= false;
        }
        else
            tvMass[4].setTextColor(Color.parseColor("#3399CC"));
        if(!checkpasswords(etMass[5].getText().toString(), etMass[6].getText().toString()))
        {
            tvMass[5].setTextColor(Color.parseColor("#FF0000"));
            flag= false;
        }
        else
            tvMass[5].setTextColor(Color.parseColor("#3399CC"));
        if(MainActivity.toRegistration[7]==null)
        {
            DateButton.setTextColor(Color.parseColor("#FF0000"));
            flag= false;
        }
        /*
        LocationManager myManager;
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        location=myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location==null)
            location=myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null)
        {
            flag= false;
            Toast.makeText(this,"Невозможно получить координаты!\nПерепроверте настройки сети и GPS\nИли попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        }
        else
        {
        }
        */
        if(checkboxBold.isChecked())
        {
            MainActivity.toRegistration[8]="super";
        }
        else
        {
            MainActivity.toRegistration[8]=null;
        }
        for(int i=0;i<etMass.length;i++)
        {
            MainActivity.toRegistration[i]=etMass[i].getText().toString();
        }
        return flag;
    }
    private boolean checkphone(String s1)
    {
        if (s1.length()==10)
        {

            try
            {
                if(MainActivity.check_int(s1.substring(0,s1.length()-4))<0)
                    return false;
                else
                {
                    if(MainActivity.check_int(s1.substring(s1.length()-4,s1.length()))<0)
                        return false;
                    else
                        return true;
                }
            }
            catch (InterruptedException e)
            {
                return false;
            }
        }
            return false;
    }
    private boolean checkpasswords(String s1,String s2)//
    {
        if (s1.length()!=4)
            return false;
        else
        {
            if (s2.length()!=4)
                return false;
            else
            {
                if (s1.equals(s2))
                    return true;
                else
                    return false;
            }
        }
    }
    public void butcancel(View v)
    {
        finish();
    }
    private void openNewGameDialog(String s1)
    {
        new AlertDialog.Builder(Registration.this)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user newuser=new user(MainActivity.toRegistration[0],MainActivity.toRegistration[1],MainActivity.toRegistration[2],
                                MainActivity.toRegistration[3],MainActivity.toRegistration[4],MainActivity.toRegistration[5],
                                MainActivity.toRegistration[7],checkboxBold.isChecked()
                                //,""+location.getLatitude(),""+location.getLatitude());
                                );
                        newuser.SetDate(new Date());
                        Location location;
                        location=MainActivity.myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location==null)
                            location=MainActivity.myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location==null)
                        {
                            Toast.makeText(Registration.this, "Невозможно получить координаты!\n" +
                                    "Перепроверте настройки сети и GPS\n" +
                                    "Или попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        newuser.SetPlace(location.getLatitude(),location.getLongitude());
                        String str = Scktmy.sendwithoutlogin((BaseMessage)newuser,Registration.this);
                        if(str==null)
                            return;
                        if(str.equals("registrationok"))
                        {
                            Toast.makeText(Registration.this,"Регистрация прошла успешно\nПодождите пока вас добавят в систему", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if(str.equals("mailisused"))
                        {
                            Toast.makeText(Registration.this,"Регистрация не прошла\nЭлектронный адрес уже используется!", Toast.LENGTH_SHORT).show();
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
