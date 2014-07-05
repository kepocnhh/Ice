/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package editacc;

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

/**
 *
 * @author alex
 */
public class Editacc
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        // TODO code application logic here
        //List reg = ReadUser("/home/alex/NetBeansProjects/IceServer/Testreg");
        List reg = ReadUser(args[0]);
        PrintList(reg);
        System.out.println("Кого надо скопировать?");
        int num = System.in.read() - '0';

        if (num < reg.size() && num >= 0)
        {
            //AddAcc((String) reg.get(num), "/home/alex/NetBeansProjects/accounts/account");
            AddAcc((String) reg.get(num), args[1]);
        }
        else
        {
            System.out.println("Open your eyes!");
        }
        List acc = ReadUser(args[1]);
        PrintList(acc);
        try
        {
            String s = (String) reg.get(num);
            EmbeddedImageEmailUtil.sendTextmessage(s.split("\t")[4], "Регистрация", "Поздравляю! Вы успешно зарегистрированы. \nУспехов в работе =)");
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }

    }
    

    static void AddAcc(String acc, String path)
    {
        List<String> loglist = null;

        //Пытаемся открыть файл для чтения
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream read = new ObjectInputStream(fis); //пытаемся прочитать объект
            if ((loglist = (List<String>) read.readObject()) != null)
            {
//                loglist.add(this.toString());
//                read.close(); //WTF?! O_o
                loglist.add(acc);
                read.close();
            }
            else
            {
                loglist = new ArrayList<String>();
                loglist.add(acc);
            }

        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            loglist = new ArrayList<String>();
            loglist.add(acc);
            //Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Записываем лист с объектом
        if (loglist != null)
        {
            try
            {
                ObjectOutputStream oos;
                oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(loglist);
                oos.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void PrintList(List list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            System.out.println(i + "  " + list.get(i));
        }
    }

    static List ReadUser(String path)
    {
        List<String> loglist = null;


        //Пытаемся открыть файл для чтения
        try
        {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream read = new ObjectInputStream(fis); //пытаемся прочитать объект
            if ((loglist = (List<String>) read.readObject()) != null)
            {
                return loglist;
            }

        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {

            Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Editacc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loglist;
    }
}
