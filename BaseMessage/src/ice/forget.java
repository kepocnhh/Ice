package ice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class forget
        extends BaseMessage
{

    public String log;

    public forget(String curlog)
    {
        this.log = curlog;
    }

    public String RecoveryPassword(String path)
    {
        ObjectInputStream read = null;
        try
        {
            //read = new ObjectInputStream(new FileInputStream("..//accounts//account"));
            read = new ObjectInputStream(new FileInputStream(path));
            List<String> list;
            if ((list = (List) read.readObject()) != null)
            {
                user us;
                for(String object : list)
                {
                    us = user.Parse(object);
                    if(us.mail.equals(this.log))
                    {
                        return us.GetPass();
                    }
                }
            }
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(forget.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(forget.class.getName()).log(Level.SEVERE, null, ex);
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
        return "Восстановление невозможно.";
    }
}

/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.forget

 * JD-Core Version:    0.7.0.1

 */
