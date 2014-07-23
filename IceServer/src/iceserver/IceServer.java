package iceserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author alex
 */
public class IceServer
{
    //static final int PORT = 8085;
    

    /**
     * @param args the command line arguments
     * args[0] - PORT
     * args[1] - toreg
     * args[2] - accounts
     * args[3] - logdir
     * args[4] - fonts
     * args[5] - version
     * args[6] - mail list (mail.txt)
     * args[7] - Salary list
     * args[8] - String config (config.txt)
     */
    public static void main(String[] args) throws IOException
    {
        // TODO code application logic here
        //ServerSocket s = new ServerSocket(PORT);
        //int PORT = Integer.parseInt(args[0]);
        if(args.length!=10)
        {
            System.out.println("\nargs[0] - PORT\nargs[1] - toreg\nargs[2] - accounts\nargs[3] - logdir\nargs[4] - fonts\nargs[5] - version\nargs[6] - mail list\nargs[7] - salary list\nargs[8] - config strings\nargs[9] - time correct");
            return;
        }
        
        PrintStream st = new PrintStream(new FileOutputStream(args[3] + "DEBUG.txt",true));
        System.setErr(st);
        System.setOut(st);
        
        ServerSocket s = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Server Started");
        
       
        try
        {
            while (true)
            {
                // Блокируется до возникновения нового соединения:
                Socket socket = s.accept();
                try
                {
                    System.out.println("Goto ServeOneJabber");
                    new ServeOneJabber(socket, args[1], args[2], args[3], args[4],  args[5], args[6], args[7], args[8], args[9]);
                }
                catch (IOException e)
                {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        }
        finally
        {
            s.close();
        }
    }
}
