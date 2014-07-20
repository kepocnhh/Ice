package meow.dogs.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ice.BaseMessage;
import ice.DataForRecord;
import ice.login;

public class OpenArb extends Activity
{
    static private SimpleAdapter adapter2;
    private double matrix_arb[][];
    private Activity activity_arb;
    static ListView lv;
    private CustomDialog cd;
    static int len;
    private EditText etCass;//поле для ввода суммы денег на кассе
    private TextView tvCass;//поле для ввода суммы денег на кассе
    private EditText etShop;//поле для ввода суммы денег на кассе
    private TextView tvShop;//поле для ввода суммы денег на кассе
    static String titleactivity="ICENGO";
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openarb);
        setTitle(titleactivity);
        activity_arb=this;
        //
        matrix_arb=Arbeiten.matrix_toopen;
        lv = (ListView)findViewById(R.id.listView1);
        adapter2 = new SimpleAdapter(this,
                Arbeiten.myNames,
                R.layout.listla, new String[]{
                Arbeiten.NAMEKEY,
                Arbeiten.IMGKEY
                }, new int[]{
                        R.id.text2,
                        R.id.img});
        lv.setAdapter(adapter2);
        len=Arbeiten.sm.length;
        adapter2.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked, int position, long id) {
                //cd = new CustomDialog(activity_arb,Arbeiten.matrix_link[position]);
                //cd.show(matrix_arb[Arbeiten.matrix_link[position]]);
                cd = new CustomDialog(activity_arb,position);
                cd.show(matrix_arb[Arbeiten.matrix_link[position]]);
            }
        });
        etCass=(EditText)findViewById(R.id.etopen);
        tvCass=(TextView)findViewById(R.id.tvopen);
        etShop=(EditText)findViewById(R.id.etshopopen);
        tvShop=(TextView)findViewById(R.id.tvshopopen);
        if(titleactivity.equals("Приход")||titleactivity.equals("Уход"))
        {
            etCass.setVisibility(View.GONE);
            tvCass.setVisibility(View.GONE);
            etShop.setVisibility(View.GONE);
            tvShop.setVisibility(View.GONE);
        }
        else
        {
            etCass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvCass.setTextColor(Color.parseColor("#3399CC"));
                }
            });
            etCass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (v == etCass) {
                        tvCass.setTextColor(Color.parseColor("#3399CC"));
                    }
                }
            });
            if(titleactivity.equals("Закрыть смену"))
            {
                etShop.setVisibility(View.GONE);
                tvShop.setVisibility(View.GONE);
            }
            else
            {
                etShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvShop.setTextColor(Color.parseColor("#3399CC"));
                    }
                });
                etShop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (v == etShop) {
                            tvShop.setTextColor(Color.parseColor("#3399CC"));
                        }
                    }
                });
            }
        }
    }
    static void set_flags(int i)
    {
        //i=Arbeiten.matrix_link[i];
        int img=R.drawable.compl;
        if(Arbeiten.flags_toopen[i]==1)
                img=R.drawable.danger;
        Arbeiten.myNames.set(i,Arbeiten.addar(Arbeiten.matrix_link[i],img));
        adapter2.notifyDataSetChanged();
    }
	public void butcancel(View v)
    {
        dialog_close("Вы не отправили данные\nВыйти?");
    }
	public void butok(View v)
    {
        for(int i=0;i<len;i++)
        {
            if(Arbeiten.flags_toopen[i]!=0)
            {
                Toast.makeText(OpenArb.this,"Поля ["+Arbeiten.sm[Arbeiten.matrix_link[i]]+"] не заполнены!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(MainActivity.DFR.getTypeEvent()== DataForRecord.TypeEvent.open||
                MainActivity.DFR.getTypeEvent()==DataForRecord.TypeEvent.close)
        {
            try
            {
                double n=MainActivity.check_double(etCass.getText().toString());
                if(n<0)
                {
                    tvCass.setTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(OpenArb.this,"Сумма денег на кассе введена неверно!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    MainActivity.DFR.setCash(n);
                }
            }
            catch (InterruptedException e)
            {
                Toast.makeText(OpenArb.this,"Сумма денег на кассе введена неверно!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(MainActivity.DFR.getTypeEvent()== DataForRecord.TypeEvent.open)
            {
                if(etShop.getText().toString().length()==0||tvShop.getText().toString().length()>40)
                {
                    tvShop.setTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(OpenArb.this,"Название магазина введено неверно!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    MainActivity.DFR.nameshop=etShop.getText().toString();
                }
            }
        }
        dialog_send("Вы уверены в том, что хотите отправить данные?");
    }
    private void dialog_send(String s1)
    {
        new AlertDialog.Builder(OpenArb.this)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(MainActivity.DFR.getCash()<0)
                        {
                            try {
                                MainActivity.DFR.setCash(MainActivity.check_double(etCass.getText().toString()));
                            } catch (InterruptedException e) {
                                Toast.makeText(OpenArb.this, "Ошибка с кассой\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Location location;
                        location=MainActivity.myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location==null)
                            location=MainActivity.myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location==null)
                        {
                            Toast.makeText(OpenArb.this, "Невозможно получить координаты!\n" +
                                    "Перепроверте настройки сети и GPS\n" +
                                    "Или попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MainActivity.DFR.SetPlace(location.getLatitude(),location.getLongitude());
                        MainActivity.DFR.SetDate(new Date());
                        MainActivity.DFR.matrix=matrix_arb;
                            String str = Scktmy.
                                    //sendwithoutlogin
                                            sendobject
                                            ((BaseMessage) MainActivity.DFR, OpenArb.this);
                            if(str==null)
                                return;
                            String[] strm=str.split("\t");
                            //if(str.equals("recordok"))
                            if(strm[0].equals("recordok")&&((MainActivity.DFR.getTypeEvent()==DataForRecord.TypeEvent.close&&strm.length==8)||strm.length==1))
                            {
                                if(MainActivity.DFR.getTypeEvent()==DataForRecord.TypeEvent.close)
                                {
                                    str =
                                            "Зарплата за рабочее время"
                                            + strm[1]//зарплата за рабочее время
                                            + "\n"
                                            + "Процент с проданых шапок - "
                                            + strm[2]//процент с проданых шапок
                                            + "\n"
                                                    + "Зарплата итого - "
                                            + strm[3]//зарплата итого
                                            + "\n"
                                                    + "Штраф за опоздание - "
                                            + strm[4]//штраф за опоздание
                                            + "\n"
                                                    + "Штраф за перевес - "
                                            + strm[5]//штраф за перевес
                                            + "\n"
                                                    + "Штраф итого - "
                                            + strm[6]//штраф итого
                                            + "\n"
                                                    + "Итого на руки - "
                                            + strm[7];//итого на руки
                                }
                                else
                                {
                                    str="Отправка прошла успешно";
                                }
                                Toast.makeText(OpenArb.this, str, Toast.LENGTH_SHORT).show();
                                if(Arbeiten.ssarb==Arbeiten.SessionStatus.open)
                                {
                                    Arbeiten.closesession();
                                    Arbeiten.ssarb= Arbeiten.SessionStatus.close;
                                }
                                if(Arbeiten.ssarb==Arbeiten.SessionStatus.start)
                                {
                                    Arbeiten.opensession();
                                    Arbeiten.ssarb= Arbeiten.SessionStatus.open;
                                }
                                //TakePicture.takePictureNP(OpenArb.this,MainActivity.DFR.getTypeEvent());
                                //new TakePicture(OpenArb.this,MainActivity.DFR.getTypeEvent());
                                finish();
                                return;
                            }
                            else
                            {
                                Toast.makeText(OpenArb.this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        /*
                        TakePicture.takePictureNP(OpenArb.this,MainActivity.DFR);
                        finish();
                        return;
                        */
                    }
                })
                .setNeutralButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    private void dialog_close(String s1)
    {
        new AlertDialog.Builder(OpenArb.this)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
