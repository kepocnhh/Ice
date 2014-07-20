package meow.dogs.work;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import ice.BaseMessage;
import ice.LastMessage;
import ice.Strings;
import ice.ping;
import ice.user;

public class Scktmy
{
    public WatchData myWd;
    public Scktmy()
    {

    }
    public Scktmy(BaseMessage bm,Activity ac)
    {
        this.myWd = new WatchData(bm,ac);
        this.new WatchSocket().execute(this.myWd);
    }
    public Scktmy(ping bm,Activity ac)
    {
        this.myWd = new WatchData(bm,new LastMessage(),ac);
        this.new SendPing().execute(this.myWd);
    }
    public Scktmy(BaseMessage bm,BaseMessage bm2,Activity ac)
    {
        this.myWd = new WatchData(bm,bm2,ac);
        this.new TwoBM().execute(this.myWd);
    }
    public class WatchData
    {
        Socket mysoc;
        ObjectInputStream inputStream=null;
        ObjectOutputStream outputStream = null;
        BaseMessage myBM;
        BaseMessage myBM2;
        String myErrors=null;
        Activity myAc;
        public WatchData()
        {

        }
        public WatchData(BaseMessage bm,Activity ac)
        {
            this.myBM=bm;
            this.myAc=ac;
        }
        public WatchData(BaseMessage bm,BaseMessage bm2,Activity ac)
        {
            this.myBM=bm;
            this.myBM2=bm2;
            this.myAc=ac;
        }
    }
    @SuppressLint("NewApi")
    class WatchSocket extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                if(param[0].mysoc==null)
                {
                    SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
                    param[0].mysoc = new Socket();
                    param[0].mysoc.connect(sockaddr,5000);
                    param[0].outputStream = new ObjectOutputStream(param[0].mysoc.getOutputStream());
                    param[0].inputStream = new ObjectInputStream(param[0].mysoc.getInputStream());
                }
                try
                {
                    param[0].outputStream.writeObject((BaseMessage)MainActivity.toLogin);
                    param[0].inputStream.readObject();
                    param[0].outputStream.writeObject(param[0].myBM);
                    param[0].myBM =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject((BaseMessage)new LastMessage());
                    param[0].mysoc.close();
                    //showtoastsoc("send_message_ok", param[0].myAc);
                }
                catch(Exception e)
                {
                    String error=e.toString();
                    if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                        error="Сеть недоступна\nПроверьте соединение";
                    showtoastsoc(error, param[0].myAc);
                    param[0].myErrors=error;
                }
                return 0;
            }
            catch (Exception e)
            {
                String error=e.toString();
                if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                    error="Сеть недоступна\nПроверьте соединение";
                if(error.indexOf("SocketTimeoutException")!=-1)
                    error="Неполадки с сервером\nПопробуйте подключиться через несколько минут ещё раз";
                showtoastsoc(error, param[0].myAc);
                param[0].myErrors=error;
                return -1;
            }
        }
    }
    @SuppressLint("NewApi")
    class SendPing extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                if(param[0].mysoc==null)
                {
                    SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
                    param[0].mysoc = new Socket();
                    param[0].mysoc.connect(sockaddr,5000);
                    param[0].outputStream = new ObjectOutputStream(param[0].mysoc.getOutputStream());
                    param[0].inputStream = new ObjectInputStream(param[0].mysoc.getInputStream());
                }
                try
                {
                    param[0].outputStream.writeObject((BaseMessage)MainActivity.toLogin);
                    param[0].inputStream.readObject();
                    param[0].outputStream.writeObject(param[0].myBM);
                    param[0].myBM2 =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject((BaseMessage)new LastMessage());
                    param[0].mysoc.close();
                }
                catch(Exception e)
                {
                    String error=e.toString();
                    if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                        error="Сеть недоступна\nПроверьте соединение";
                    showtoastsoc(error, param[0].myAc);
                    param[0].myErrors=error;
                }
                return 0;
            }
            catch (Exception e)
            {
                String error=e.toString();
                if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                    error="Сеть недоступна\nПроверьте соединение";
                if(error.indexOf("SocketTimeoutException")!=-1)
                    error="Неполадки с сервером\nПопробуйте подключиться через несколько минут ещё раз";
                showtoastsoc(error, param[0].myAc);
                param[0].myErrors=error;
                return -1;
            }
        }
    }
    @SuppressLint("NewApi")
    class TwoBM extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                if(param[0].mysoc==null)
                {
                    SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
                    param[0].mysoc = new Socket();
                    param[0].mysoc.connect(sockaddr,5000);
                    param[0].outputStream = new ObjectOutputStream(param[0].mysoc.getOutputStream());
                    param[0].inputStream = new ObjectInputStream(param[0].mysoc.getInputStream());
                }
                try
                {
                    param[0].outputStream.writeObject((BaseMessage)MainActivity.toLogin);
                    param[0].inputStream.readObject();
                    param[0].outputStream.writeObject(param[0].myBM);
                    param[0].myBM =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject(param[0].myBM2);
                    param[0].myBM2 =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject((BaseMessage)new LastMessage());
                    param[0].mysoc.close();
                    //showtoastsoc("send_message_ok", param[0].myAc);
                }
                catch(Exception e)
                {
                    String error=e.toString();
                    if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                        error="Сеть недоступна\nПроверьте соединение";
                    showtoastsoc(error, param[0].myAc);
                    param[0].myErrors=error;
                }
                return 0;
            }
            catch (Exception e)
            {
                String error=e.toString();
                if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                    error="Сеть недоступна\nПроверьте соединение";
                if(error.indexOf("SocketTimeoutException")!=-1)
                    error="Неполадки с сервером\nПопробуйте подключиться через несколько минут ещё раз";
                showtoastsoc(error, param[0].myAc);
                param[0].myErrors=error;
                return -1;
            }
        }
    }
    @SuppressLint("NewApi")
    class HelloSocket extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                if(param[0].mysoc==null)
                {
                    SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
                    param[0].mysoc = new Socket();
                    param[0].mysoc.connect(sockaddr,5000);
                    param[0].outputStream = new ObjectOutputStream(param[0].mysoc.getOutputStream());
                    param[0].inputStream = new ObjectInputStream(param[0].mysoc.getInputStream());
                }
                try
                {
                    param[0].outputStream.writeObject(param[0].myBM);
                    param[0].myBM2 =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject((BaseMessage)new LastMessage());
                    param[0].mysoc.close();
                }
                catch(Exception e)
                {
                    String error=e.toString();
                    if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                        error=errors[0];
                    showtoastsoc(error, param[0].myAc);
                    param[0].myErrors=error;
                }
                return 0;
            }
            catch (Exception e)
            {
                String error=e.toString();
                if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                    error=errors[0];
                if(error.indexOf("SocketTimeoutException")!=-1)
                    error=errors[1];
                showtoastsoc(error, param[0].myAc);
                param[0].myErrors=error;
                return -1;
            }
        }
    }
    @SuppressLint("NewApi")
    class NewSocket extends AsyncTask<WatchData , Integer, Integer>
    {
        protected Integer doInBackground(WatchData... param)
        {
            try
            {
                if(param[0].mysoc==null)
                {
                    SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(MainActivity.getIPstr()),Integer.parseInt(MainActivity.Portgen));
                    param[0].mysoc = new Socket();
                    param[0].mysoc.connect(sockaddr,5000);
                    param[0].outputStream = new ObjectOutputStream(param[0].mysoc.getOutputStream());
                    param[0].inputStream = new ObjectInputStream(param[0].mysoc.getInputStream());
                }
                try
                {
                    param[0].outputStream.writeObject(param[0].myBM);
                    param[0].myBM =(BaseMessage)param[0].inputStream.readObject();
                    param[0].outputStream.writeObject((BaseMessage)new LastMessage());
                    param[0].mysoc.close();
                }
                catch(Exception e)
                {
                    String error=e.toString();
                    if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                        error=errors[0];
                    showtoastsoc(error, param[0].myAc);
                    param[0].myErrors=error;
                }
                return 0;
            }
            catch (Exception e)
            {
                String error=e.toString();
                if(error.indexOf("connect failed: ENETUNREACH (Network is unreachable)")!=-1)
                    error=errors[0];
                if(error.indexOf("SocketTimeoutException")!=-1)
                    error=errors[1];
                showtoastsoc(error, param[0].myAc);
                param[0].myErrors=error;
                return -1;
            }
        }
    }
    void showtoastsoc(final String s,final Activity MActivity)
    {
        MActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast toast = Toast.makeText(MActivity,s, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
    static String[] errors =(
            "Сеть недоступна\nПроверьте соединение"
            +"\t"+
            "Неполадки с сервером\nПопробуйте подключиться через несколько минут ещё раз"
            ).split("\t");
    //отправка объекта BaseMessage
    static String sendobject(BaseMessage obj,Activity activity)
    {
        Scktmy newsckt=new Scktmy(obj,activity);
        while(newsckt.myWd.myBM.getClass()!= ping.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        return ((ping)newsckt.myWd.myBM).GetPing();
    }
    static String sendping(ping obj,Activity activity)
    {
        Scktmy newsckt=new Scktmy(obj,activity);
        while(newsckt.myWd.myBM2.getClass()!= ping.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        return ((ping)newsckt.myWd.myBM).GetPing();
    }
    static String sendTWOobject(BaseMessage dfr,BaseMessage pht,Activity activity)
    {
        Scktmy newsckt=new Scktmy(dfr,pht,activity);
        while(newsckt.myWd.myBM.getClass()!= ping.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        while(newsckt.myWd.myBM2.getClass()!= ping.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        return ((ping)newsckt.myWd.myBM2).GetPing();
    }
    static String sendwithoutlogin(BaseMessage obj,Activity activity)
    {
        Scktmy newsckt=new Scktmy();
        newsckt.myWd = newsckt.new WatchData(obj,activity);
        newsckt.new NewSocket().execute(newsckt.myWd);
        while(newsckt.myWd.myBM.getClass()!= ping.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        return ((ping)newsckt.myWd.myBM).GetPing();
    }
    static BaseMessage sayhello(BaseMessage obj,Activity activity)
    {
        Scktmy newsckt=new Scktmy();
        newsckt.myWd = newsckt.new WatchData(obj,activity);
        newsckt.myWd.myBM2 = new LastMessage();
        newsckt.new HelloSocket().execute(newsckt.myWd);
        while(newsckt.myWd.myBM2.getClass() == LastMessage.class)
        {
            if(newsckt.myWd.myErrors!=null)
                return null;
        }
        return newsckt.myWd.myBM2;
    }
}
