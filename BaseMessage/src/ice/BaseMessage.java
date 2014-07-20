package ice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseMessage
        implements Serializable
{

    private String version;
    private Date date;
    private double placex;
    private double placey;
    protected int UI;

    public void SetDate(Date d)
    {
        date = d;
    }

    public Date GetDate()
    {
        return date;
    }

    public void SetPlace(double x, double y)
    {
        placex = x;
        placey = y;
    }

    public double GetX()
    {
        return placex;
    }

    public double GetY()
    {
        return placey;
    }

    public BaseMessage()
    {
        this.date = new Date();
        this.UI = this.hashCode();
    }

    public String GetVersion()
    {
        return this.version;
    }

    public void SetVersion(String v)
    {
        this.version = v;
    }

    public void AddMessage(String path)
    {
        List<BaseMessage> loglist = null;
        try
        {
            File f = new File(path);
            if (!f.exists())
            {
                f.createNewFile();
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream read = new ObjectInputStream(fis);
            if ((loglist = (List) read.readObject()) != null)
            {
                loglist.add(this);
                read.close();
            }
            else
            {
                loglist = new ArrayList();
                loglist.add(this);
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            loglist = new ArrayList();
            loglist.add(this);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (loglist != null)
        {
            try
            {
                //ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path, true));
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path)); // Файл нужно перезаписывать новым листом
                oos.writeObject(loglist);
                oos.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static List<BaseMessage> GetList(String path)
    {
        List<BaseMessage> loglist = null;
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream read = new ObjectInputStream(fis);
            if ((loglist = (List) read.readObject()) != null)
            {
                read.close();
                fis.close();
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loglist;
    }
    
    public boolean IsContaint(List<BaseMessage> list)
    {
        for (BaseMessage baseMessage : list) 
        {
            if(this.equals(baseMessage))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean equals(BaseMessage bm)
    {
        if(this.UI == bm.UI)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
}



/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.BaseMessage

 * JD-Core Version:    0.7.0.1

 */
