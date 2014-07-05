package ice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class login
        extends BaseMessage
{

    String log;
    String pass;
    place place;

    public login(String curlog, String curpass, place curplace)
    {
        this.log = curlog;
        this.pass = this.pass;
        this.place = curplace;
    }

    public login(String curlog, String curpass)
    {
        this.log = curlog;
        this.pass = curpass;
    }

    public boolean Authentication(String path)
    {
        ObjectInputStream read = null;
        try
        {
            //ObjectInputStream read = new ObjectInputStream(new FileInputStream("..//accounts//account"));
            //read = new ObjectInputStream(new FileInputStream("..//accounts//account"));
            read = new ObjectInputStream(new FileInputStream(path));
            List<String> loglist;
            if ((loglist = (List) read.readObject()) != null)
            {
                for (String object : loglist)
                {
                    user nextuser = user.Parse(object);
                    if ((nextuser.mail.equals(this.log)) && (nextuser.pass.equals(this.pass)))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error opening file for authentication. FileNotFound.");
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                read.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(forget.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

//    public boolean OLDAuthentication()
//    {
//        try
//        {
//            ObjectInputStream read = new ObjectInputStream(new FileInputStream("..//accounts//account"));
//            List<BaseMessage> loglist;
//            if ((loglist = (List) read.readObject()) != null)
//            {
//                for (BaseMessage object : loglist)
//                {
//                    user nextuser = (user) object;
//                    if (nextuser.mail.equals(this.log))
//                    {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//        catch (FileNotFoundException ex)
//        {
//            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
//            System.err.println("Error opening file for authentication. FileNotFound.");
//        }
//        catch (IOException | ClassNotFoundException ex)
//        {
//            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }

    public user GetUser()
    {
        try
        {
            ObjectInputStream read = new ObjectInputStream(new FileInputStream("..//accounts//account"));
            List<String> loglist;
            if ((loglist = (List) read.readObject()) != null)
            {
                for (String object : loglist)
                {
                    user nextuser = user.Parse(object);
                    if ((nextuser.mail.equals(this.log)) && (nextuser.pass.equals(this.pass)))
                    {
                        return nextuser;
                    }
                }
            }
            return null;
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error opening file for authentication. FileNotFound.");
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
