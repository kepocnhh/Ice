package meow.dogs.work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ice.BaseMessage;
import ice.DataForRecord;
import ice.Strings;
import ice.login;
import ice.ping;

public class MainActivity extends Activity implements LocationListener
{
    static LocationManager myManager;
    static AutoCompleteTextView acLogin;//поле для ввода логина
    EditText etPassword;//поле для ввода пароля
    Button buttest;//тестовая кнопка
    //настройки порта и адреса
    static String[] IPadr;//IP адресс
    static String getIPstr()
    {
        return IPadr[0]+"."+IPadr[1]+"."+IPadr[2]+"."+IPadr[3];
    }
    static String Portgen;//порт
    //получить версию программы
    static String getVersionName(Context context)
    {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (final PackageManager.NameNotFoundException e) {
        }
        if (versionName == null) {
            versionName = "unknown";
        }
        return versionName;
    }
    static int getVersionCode(Context context)
    {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (final PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }
    //объекты
    static Strings mainstrings;//используемые строки
    static DataForRecord DFR;//для заполнения и отправки данных
    //данные о регистрации
    static String[] toRegistration;//массив строк содержащих данные пользователя для регистрации
    //данные о входе пользователя
    static login toLogin;
    static String PathFile="/mnt/sdcard/ForIce/LoginsForIce";
    private CDlg forgetdlg;//диалог "Забыли пароль?"
    //создание формы MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttest=(Button)findViewById(R.id.buttest);
        etPassword=(EditText)findViewById(R.id.editText2);
        acLogin = (AutoCompleteTextView) findViewById(R.id.editText1);
        acLogin.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, readlogins()));
        buttest.setVisibility(buttest.INVISIBLE);//скрытие тестовой кнопки
        //назначение IP адреса по умолчанию
        IPadr=
                "46\t254\t20\t54".split("\t");
                //"192\t168\t1\t119".split("\t");
        //назначение порта по умолчанию
        Portgen="8082";
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        saymainhello(this);
    }
    static void saymainhello2(Activity ac)
    {
        DFR=new DataForRecord();
    }
    static void saymainhello1(Activity ac)
    {
        try {
            mainstrings=new Strings("/mnt/sdcard/ForIce/DataForIce.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DFR=new DataForRecord(mainstrings);

            Location location;
            location=myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location==null)
                location=myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location==null)
            {
                /*
                Toast.makeText(ac, "Невозможно получить координаты!\n" +
                        "Перепроверте настройки сети и GPS\n" +
                        "Или попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                */
                openNewGameDialog("Невозможно получить координаты!\nПопробовать ещё раз?",ac);
                return;
            }
    }
    static void saymainhello(Activity ac)
    {
        File myPath = new File("/mnt/sdcard/ForIce");
        myPath.mkdir();
        myPath.mkdirs();
        Location location;
        location=myManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location==null)
            location=myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null)
        {
            openNewGameDialog("Невозможно получить координаты!\nПопробовать ещё раз?",ac);
            return;
        }
        if(mainstrings==null)
        {
            BaseMessage bm;
            //создание объекта содержащего используемые строки
            //mainstrings=Scktmy.sayhello(((BaseMessage)new ping("sayhello")),ac);
            MainActivity.getVersionName(ac);
            bm=Scktmy.sayhello(((BaseMessage)new ping("sayhello",MainActivity.getVersionName(ac)+"."+MainActivity.getVersionCode(ac))),ac);
            if(bm!=null)
            {
                if(bm.getClass()==Strings.class)
                {
                    mainstrings=(Strings)bm;
                }
                else
                {
                    Toast.makeText(ac,"Вы используете устаревшую версию программы\n" +
                            "Обновитесь пожалуйста", Toast.LENGTH_LONG).show();
                    openNewGameDialog("Подключение не удалось\nПопробовать ещё раз?",ac);
                }
            }
            else
            {
                openNewGameDialog("Подключение не удалось\nПопробовать ещё раз?",ac);
            }
            //учитывая количество используемых строк в полученном объекте класса Strings
        }
    }
    //добавить логин если он новый
    static void addlogin(String newlogin)
    {
        List<String> liststring = new ArrayList<String>();
            ObjectInputStream read = null;
            try
            {
                read = new ObjectInputStream(new FileInputStream(PathFile));
                if ((liststring = (List<String>) read.readObject()) != null)
                {
                    for(int i=0;i<liststring.size();i++)
                    {
                        if(liststring.get(i).equals(newlogin))
                            return;
                    }
                    liststring.add(newlogin);
                    read.close();
                    ObjectOutputStream s;
                        s = new ObjectOutputStream(new FileOutputStream(PathFile));
                        s.writeObject(liststring);
                        s.close();
                }
            }
            catch (IOException e)
            {
            }
            catch (ClassNotFoundException ex)
            {
            }
    }
    //считать уже вводимые логины
    static String[] readlogins()
    {
        List<String> liststring = new ArrayList<String>();
            ObjectInputStream read = null;
            try
            {
                read = new ObjectInputStream(new FileInputStream(PathFile));
                if ((liststring = (List<String>) read.readObject()) != null)
                {
                    read.close();
                }
            }
            catch (IOException e)
            {
                ObjectOutputStream s;
                liststring.add("");
                try
                {
                    s = new ObjectOutputStream(new FileOutputStream(PathFile));
                    s.writeObject(liststring);
                    s.close();
                }
                catch (IOException ex)
                {
                }
            }
            catch (ClassNotFoundException e){}
        return liststring.toArray(new String[liststring.size()]);
    }
    //назначение функций кнопкам меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                new IPnPORT(this);
                return true;
            case R.id.about_settings:
                new About(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //функции тестовой кнопки
    public void buttest(View v)
    {
        //TakePicture.takePictureNP(MainActivity.this,MainActivity.DFR.getTypeEvent());
        /*
        toLogin=new login("nik@m","nnnn");
        DFR = new DataForRecord(mainstrings);//создание объекта для заполнения и отправки данных
        DFR.setTypeEvent(DataForRecord.TypeEvent.open);
        TakePicture.takePictureNP(MainActivity.this,DFR);
        */
        //TakePicture.takePictureNP(this);
        /*
        TakePicture tp = new TakePicture(this);
        if(tp.error)
        {
            Toast.makeText(this,"notOK :(", Toast.LENGTH_SHORT).show();
            return;
        }
        */
        //Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show();
    }
    //кнопка Вход
    public void butenter2(View v)
    {
        Arbeiten.ssarb=Arbeiten.SessionStatus.test;
        startActivity(new Intent(MainActivity.this,Arbeiten.class));
    }
    public void butenter(View v)
    {
        String login;
        String pass;
        login=acLogin.getText().toString();
        pass=etPassword.getText().toString();
        if(!checkemail(login))
        {
            Toast.makeText(MainActivity.this,"Логин введён неверно!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()!=4)
        {
            Toast.makeText(MainActivity.this, "Пароль введён неверно!", Toast.LENGTH_SHORT).show();
            return;
        }
        toLogin=new login(login,pass);
        String str = Scktmy.sendwithoutlogin((BaseMessage)toLogin,MainActivity.this);
        if(str==null)
            return;
        if(str.equals("sobed"))
        {
            Toast.makeText(MainActivity.this, "Такой пары логина и пароля нет\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            addlogin(login);
            acLogin.setAdapter(new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, readlogins()));
            DFR = new DataForRecord(mainstrings);//создание объекта для заполнения и отправки данных
            if(str.equals("SessionClose"))
            {
                Toast.makeText(MainActivity.this, "Смена уже закрыта на сегодня", Toast.LENGTH_LONG).show();
                Arbeiten.ssarb=Arbeiten.SessionStatus.close;
                return;
            }
            if(str.equals("NewSession"))
            {
                Toast.makeText(MainActivity.this, "Можно начинать работать", Toast.LENGTH_LONG).show();
                Arbeiten.ssarb=Arbeiten.SessionStatus.start;
                startActivity(new Intent(MainActivity.this,Arbeiten.class));
                return;
            }
            if(str.equals("open"))
            {
                Toast.makeText(MainActivity.this, "Смена уже была начата\nПродолжайте работать", Toast.LENGTH_LONG).show();
                Arbeiten.ssarb=Arbeiten.SessionStatus.open;
                startActivity(new Intent(MainActivity.this,Arbeiten.class));
                return;
            }
            Toast.makeText(MainActivity.this, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
        }
    }
    //проверка на правильность ввода электронной почты
    static boolean checkemail(String s1)
    {
        if (s1.length()<1)
            return false;
        if(s1.indexOf('@')!=s1.lastIndexOf('@')||s1.indexOf('@')==-1)
            return false;
        if(s1.indexOf('@')==0||s1.indexOf('@')==s1.length()-1)
            return false;
        if(s1.charAt(0)=='.'||s1.charAt(s1.length()-1)=='.')
            return false;
        if(s1.indexOf("..")!=-1)
            return false;
        return true;
    }
    //проверка на правильность ввода ФИО
    static boolean checkifo(String s1)
    {
        if (s1.length() < 1)
            return false;
        if (s1.charAt(0) >= 'А' && s1.charAt(0) <= 'Я')
        {
        }
        else
        {
            return false;
        }
        for(int i=1;i<s1.length();i++)
        {
            if (s1.charAt(i) >= 'а' && s1.charAt(i)<= 'я')
            {
            }
            else
            {
                return false;
            }
        }
        return true;
    }
    //проверка на правильность ввода числа double
    static double check_double(String str) throws InterruptedException
    {
        double n;
        try
        {
            n=Double.parseDouble(str);
            if(n<0||n>9999999.99999)
                return -1;
        }
        catch (Exception e)
        {
            return -1;
        }
        return n;
    }
    //проверка на правильность ввода числа int
    static int check_int(String str) throws InterruptedException
    {
        int n;
        try
        {
            n=Integer.parseInt(str);
            if(n<0||n>999999999)
                return -1;
        }
        catch (Exception e)
        {
            return -1;
        }
        return n;
    }
    //кнопка Регистрация
    public void butreg(View v)
    {
        toRegistration = new String[9];
        for(int i=0;i<7;i++)
            toRegistration[i] = "";
        toRegistration[7] = null;
        toRegistration[8] = null;
        //имя
        //фамилия
        //отчество
        //телефон
        //электронная почта (логин)
        //пароль
        //повтор пароля
        //дата рождения
        //супервайзер
        startActivity(new Intent(MainActivity.this,Registration.class));
    }
    //кнопка Забыли пароль?
    public void butforg(View v)
    {
        forgetdlg = new CDlg(MainActivity.this);
        forgetdlg.show();
    }
    static void openNewGameDialog(String s1, final Activity ac)
    {
        new AlertDialog.Builder(ac)
                .setCancelable(false)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saymainhello(ac);
                    }
                })
                .setNeutralButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .show();
    }
    //кнопка Выход
    public void butexit(View v)
    {
        System.exit(0);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
