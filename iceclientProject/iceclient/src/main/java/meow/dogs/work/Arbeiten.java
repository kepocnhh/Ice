package meow.dogs.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ice.BaseMessage;
import ice.DataForRecord;
import ice.ping;

public class Arbeiten extends Activity
{
    static Button EXIT;
    static double matrix_toopen[][];
    static int flags_toopen[];
    static int matrix_link[];
    static ArrayList<HashMap<String, Object>>myNames=null;
    static String[] sm=MainActivity.mainstrings.DataSale.toArray(new String[MainActivity.mainstrings.DataSale.size()]);
    static final String NAMEKEY = "name";
    static final String IMGKEY = "icon";
    static enum SessionStatus {start,open,drug,steal,close,test}//состояние смены
    static SessionStatus ssarb=SessionStatus.start;
    //
    static Button OPEN;
    static Button CASS;
    static Button IN;
    static Button OUT;
    static Button CLOSE;
    static void startsession()
    {
        OPEN.setVisibility(View.VISIBLE);
        CASS.setVisibility(View.INVISIBLE);
        IN.setVisibility(View.INVISIBLE);
        OUT.setVisibility(View.INVISIBLE);
        CLOSE.setVisibility(View.INVISIBLE);
    }
    static void hideall()
    {
        OPEN.setVisibility(View.INVISIBLE);
        CASS.setVisibility(View.INVISIBLE);
        IN.setVisibility(View.INVISIBLE);
        OUT.setVisibility(View.INVISIBLE);
        CLOSE.setVisibility(View.INVISIBLE);
    }
    static void opensession()
    {
        OPEN.setVisibility(View.INVISIBLE);
        CASS.setVisibility(View.VISIBLE);
        IN.setVisibility(View.VISIBLE);
        OUT.setVisibility(View.VISIBLE);
        CLOSE.setVisibility(View.VISIBLE);
    }
    static void closesession()
    {
        OPEN.setVisibility(View.INVISIBLE);
        CASS.setVisibility(View.INVISIBLE);
        IN.setVisibility(View.INVISIBLE);
        OUT.setVisibility(View.INVISIBLE);
        CLOSE.setVisibility(View.INVISIBLE);
    }
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arbeiten);
        EXIT=(Button)findViewById(R.id.barbexit);
        OPEN=(Button)findViewById(R.id.barbopen);
        CASS=(Button)findViewById(R.id.barbcass);
        IN=(Button)findViewById(R.id.barbdrug);
        OUT=(Button)findViewById(R.id.barbsteal);
        CLOSE=(Button)findViewById(R.id.barbclose);
        if(ssarb==SessionStatus.start)
            startsession();
        if(ssarb==SessionStatus.open||ssarb==SessionStatus.drug||ssarb==SessionStatus.steal)
            opensession();
        if(ssarb==SessionStatus.close)
            closesession();
    }
    private boolean checkansver(String checkstr)
    {
        String str = Scktmy.sendping(new ping(checkstr), this);
        if(str==null)
        {
            return false;
        }
        if(!str.equals(checkstr))
        {
            return false;
        }
        return true;
    }
    public void butopen(View v)
    {
        if(!checkansver("open"))
        {
            Toast.makeText(this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        setoptions("Открыть смену",-1,false,R.drawable.down,2, DataForRecord.TypeEvent.open);
        ssarb= SessionStatus.start;
        //Toast.makeText(Arbeiten.this, test(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Arbeiten.this, OpenArb.class));
    }
    public void butcass(View v)
    {
        if(!checkansver("open"))
        {
            Toast.makeText(this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        new CDgencass(Arbeiten.this);
    }
    public void butdrug(View v)
    {
        if(!checkansver("drug"))
        {
            Toast.makeText(this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        setoptions("Приход",0,true,R.drawable.compl,0,DataForRecord.TypeEvent.drug);
        ssarb= SessionStatus.drug;
        startActivity(new Intent(Arbeiten.this, OpenArb.class));
    }
    public void butsteal(View v)
    {
        if(!checkansver("steal"))
        {
            Toast.makeText(this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        setoptions("Уход",0,true,R.drawable.compl,0,DataForRecord.TypeEvent.steal);
        ssarb= SessionStatus.steal;
        startActivity(new Intent(Arbeiten.this, OpenArb.class));
    }
    public void butclose(View v)
    {
        if(!checkansver("close"))
        {
            Toast.makeText(this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        setoptions("Закрыть смену",-1,false,R.drawable.down,2, DataForRecord.TypeEvent.close);
        ssarb= SessionStatus.open;
        //Toast.makeText(Arbeiten.this, test(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Arbeiten.this, OpenArb.class));
    }
    public void butexit(View v)
    {
        openNewGameDialog("Будет необходимо зайти в систему снова\nВыйти?");
    }
    static HashMap<String, Object> addar(int i,int img)
    {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put(NAMEKEY,sm[i]);
        hm.put(IMGKEY,img);
        return hm;
    }
    private void openNewGameDialog(String s1)
    {
        new AlertDialog.Builder(Arbeiten.this)
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
    private void setoptions(String ttl,int amount,boolean flag,int img,int fltoopren,DataForRecord.TypeEvent te)
    {
        int len=MainActivity.mainstrings.DataSale.size();
        flags_toopen=new int[len];
        matrix_link=new int[len];
        matrix_toopen = new double [len][];
        myNames=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<len;i++)
        {
            int len2=MainActivity.mainstrings.DataSubSale.get(i).split("\t").length;
            matrix_toopen[i] = new double [len2];
            for(int j=0;j<len2;j++)
            {
                matrix_toopen[i][j]=amount;
            }
            if(sm[i].charAt(0)!='['||!flag)
            {
                myNames.add(addar(i,img));
                matrix_link[myNames.size()-1]=i;
                flags_toopen[myNames.size()-1]=fltoopren;
            }
        }
        MainActivity.DFR.setTypeEvent(te);
        MainActivity.DFR.setCash(amount);
        OpenArb.titleactivity=ttl;
    }

    private String test()
    {
        double matrix_arb[][];
        matrix_arb=Arbeiten.matrix_toopen;
        MainActivity.DFR.nameshop="qwertyu";
        if(MainActivity.DFR.getCash()<0)
        {
            try {
                MainActivity.DFR.setCash(MainActivity.check_double("999"));
            } catch (InterruptedException e) {
                return "Ошибка с кассой\nПопробуйте ещё раз";
            }
        }
        Location location;
        location=MainActivity.myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location==null)
            location=MainActivity.myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null)
        {
            return "Невозможно получить координаты!\n" +
                    "Перепроверте настройки сети и GPS\n" +
                    "Или попробуйте ещё раз";
        }
        MainActivity.DFR.SetPlace(location.getLatitude(),location.getLongitude());
        MainActivity.DFR.SetDate(new Date());
        MainActivity.DFR.matrix=matrix_arb;
        String str = Scktmy.sendobject((BaseMessage) MainActivity.DFR, Arbeiten.this);
        if(str==null)
            return "Что-то пошло не так\nПопробуйте ещё раз";
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
            Toast.makeText(Arbeiten.this, str, Toast.LENGTH_SHORT).show();
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
            return str;
        }
        else
        {
            return "Что-то пошло не так\nПопробуйте ещё раз";
        }
    }
}