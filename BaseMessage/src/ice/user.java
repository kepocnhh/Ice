package ice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class user
        extends BaseMessage
{

    String name;
    String surname;
    String patronymic;
    String phone;
    String mail;
    String pass;
    String birth;
    Boolean superv;
    double salary = 110;

    public user(String line)
    {
        String[] insplits = line.split("\t");
        if (insplits.length == 8)
        {
            this.name = insplits[0];
            this.surname = insplits[1];
            this.patronymic = insplits[2];
            this.phone = insplits[3];
            this.mail = insplits[4];
            this.pass = insplits[5];
            this.birth = insplits[6];
            this.superv = Boolean.parseBoolean(insplits[7]);
            
        }
    }

    public user(String _name, String _surname, String _patronymic, String _phone, String _mail, String _pass, String _birth, boolean _superv)
    {
        this.name = _name;
        this.surname = _surname;
        this.patronymic = _patronymic;
        this.phone = _phone;
        this.mail = _mail;
        this.pass = _pass;
        this.birth = _birth;
        this.superv = _superv;
    }

    /**
     *
     * @param path - путь до файла с зарегистрированными пользователями.
     * @return true, если e-mail уже используется
     * false, если не используется.
     */
    public Boolean IsUsed(String path)
    {
        List<String> accounts = null;
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
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ObjectInputStream read = new ObjectInputStream(new FileInputStream(path));
            if ((accounts = (List<String>) read.readObject()) != null)
            {
                return FindInAccList(accounts);
            }
            else
            {
                return false;
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            return false;
            //Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String GetMail()
    {
        return this.mail;
    }

    public String GetPass()
    {
        return this.pass;
    }

    public boolean GetSuper()
    {
        return this.superv;
    }

    public String GetSurname()
    {
        return this.surname;
    }

    public String GetBirth()
    {
        return this.birth;
    }

    public String GetName()
    {
        return this.name;
    }

    public String GetPatronymic()
    {
        return this.patronymic;
    }

    public String GetPhone()
    {
        return this.phone;
    }

    public String GetStringSuper()
    {
        return super.toString();
    }

    @Override
    public String toString()
    {
        return this.name + "\t" + this.surname + "\t" + this.patronymic + "\t" + this.phone + "\t" + this.mail + "\t" + this.pass + "\t" + this.birth + "\t" + this.superv.toString();
    }

    public user()
    {
    }

    static user Parse(String suser)
    {
        user us = new user();

        String[] insplits = suser.split("\t");
        if (insplits.length == 8)
        {
            us.name = insplits[0];
            us.surname = insplits[1];
            us.patronymic = insplits[2];
            us.phone = insplits[3];
            us.mail = insplits[4];
            us.pass = insplits[5];
            us.birth = insplits[6];
            us.superv = Boolean.parseBoolean(insplits[7]);
        }
        return us;
    }

    @Override
    public void AddMessage(String path)
    //public void AddUser(String path)
    {
        List<String> loglist = null;
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
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream read = new ObjectInputStream(fis);
            if ((loglist = (List) read.readObject()) != null)
            {
                loglist.add(toString());
                read.close();
            }
            else
            {
                loglist = new ArrayList();
                loglist.add(toString());
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            loglist = new ArrayList();
            loglist.add(toString());
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (loglist != null)
        {
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(loglist);
                oos.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean FindInAccList(List<String> accounts)
    {
        user tmp;
        for (String string : accounts)
        {
            tmp = Parse(string);
            if (tmp.mail.equalsIgnoreCase(this.mail))    //mail is used
            {
                return true;
            }
        }
        return false;
    }

}

/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.user

 * JD-Core Version:    0.7.0.1

 */
