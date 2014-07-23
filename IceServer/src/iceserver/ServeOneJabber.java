
package iceserver;

//import com.itextpdf.text.DocumentException;
import com.itextpdf.text.DocumentException;
import ice.*;
import ice.DataForRecord.TypeEvent;
import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
//import javax.mail.MessagingException;
//import javax.mail.internet.AddressException;

/**
 *
 * @author alex
 */
public class ServeOneJabber extends Thread
{

    //static Date datelog = new Date();
    private static List<DataForRecord> FindEventInLog(DataForRecord.TypeEvent typeEvent, List<BaseMessage> loglist)
    {
        System.out.println(new Date().toString() + " DFR FindEventInLog");

        List<DataForRecord> arraydfr = new ArrayList();
        for (BaseMessage bm : loglist)
        {
            Class c = bm.getClass();
            if (c == DataForRecord.class)
            {
                DataForRecord dfr = (DataForRecord) bm;
                if (dfr.getTypeEvent() == typeEvent)
                {
                    arraydfr.add(dfr);
                }
            }
        }
        return arraydfr;
    }

    private static ping FindPingInLog(String message, List<BaseMessage> loglist)
    {
        System.out.println(new Date().toString() + " Find ping " + message);

        for (BaseMessage bm : loglist)
        {
            Class c = bm.getClass();
            if (c == ping.class)
            {
                if (((ping) bm).GetPing().equals(message))
                {
                    return (ping) bm;
                }
            }
        }
        return null;
    }

    private static List<DataCass> FindEventInLog(DataCass.TypeEvent typeEvent, List<BaseMessage> loglist)
    {
        //   System.out.println(new Date().toString() + " FingEventInLog");
        System.out.println(new Date().toString() + " Datacass FindEventInLog");

        List<DataCass> arraydc = new ArrayList();
        for (BaseMessage bm : loglist)
        {
            Class c = bm.getClass();
            if (c == DataCass.class)
            {
                DataCass dc = (DataCass) bm;
                if (dc.getTypeEvent() == typeEvent)
                {
                    arraydc.add(dc);
                }
            }
        }
        return arraydc;
    }
    private Socket socket;
//    args[0] - PORT
//     * args[1] - toreg
//     * args[2] - accounts
//     * args[3] - logdir
    String toreg;
    String accounts;
    String logdir;
    static String fonts;
    
    static String version;
    static List<String> maillist;
    static List<String> salarylist;
    static String StringsConfigFile;

    public ServeOneJabber(Socket s, String toreg, String accounts, String logdir, String fonts,  String version, String maillist, String salarylist, String StringSConfigFile) throws IOException
    {
        socket = s;
        this.toreg = toreg;
        this.accounts = accounts;
        this.logdir = logdir;
        this.fonts = fonts;
        
        this.version = version;
        this.maillist = GetMailList(maillist);
        this.salarylist = GetSalaryList(salarylist);
        this.StringsConfigFile = StringsConfigFile;

        start(); // вызываем run()
    }

    static private double getresultmass(double[] open, double[] drug, double[] steal, double[] close, int i)
    {
        return open[i] + drug[i] - steal[i] - close[i];
    }

    private void AuthMessaging(user authuser, ObjectOutputStream outputStream, ObjectInputStream inputStream)
    {
        System.out.println(new Date().toString() + " Auth Successful " + authuser.GetMail());

        //System.out.println(new Date().toString() + " Создание папки для пользователя " + authuser.GetMail());

        String dir = CreateLogDirName(authuser);
        File myPath = new File(dir);
        myPath.mkdirs();

        String pdfdir = dir + "/" + "pdf";
        File PdfPath = new File(pdfdir);
        PdfPath.mkdirs();

        String photodir = dir + "/" + "photo";
        File photoPath = new File(photodir);
        photoPath.mkdirs();

        String filename;


        BaseMessage StatusSession = GetStatusSession(authuser, dir); 
        
//        if (((ping) StatusSession).GetPing().equals("ErrorStatusSession"))
//        {
//            System.out.println(new Date().toString() + " фиксим Лох-Несский баг " + authuser.GetMail());//костыль
//            try 
//            {
//                outputStream.writeObject(StatusSession);
//                System.out.println(new Date().toString() + ((ping)StatusSession).GetPing() + " StatusSession will be send to " + authuser.GetMail());
//            } 
//            catch (IOException ex) 
//            {
//                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return;
//        }
        if (((ping) StatusSession).GetPing().equals("NewSession"))
        {
            System.out.println(new Date().toString() + " NewSession " + authuser.GetMail());    
            filename = CreateFileName(authuser);
        }
        else
        {
            if (((ping) StatusSession).GetPing().equals("opens"))
            {
                System.out.println(new Date().toString() + " Сессия открывается " + authuser.GetMail());

                StatusSession = new ping("NewSession");
                filename = FileName(authuser);

            }
            else
    //        //if (((ping) StatusSession).GetPing().equals("opens"))
            {
                System.out.println(new Date().toString() + " Not new session " + authuser.GetMail());
                filename = FileName(authuser);
            }
        }
        String fullname = dir + "/" + filename;

        try
        {
            outputStream.writeObject(StatusSession);

            //System.out.println(new Date().toString() + " StatusSession will be send to " + authuser.GetMail());
            System.out.println(new Date().toString() + ((ping)StatusSession).GetPing() + " StatusSession will be send to " + authuser.GetMail());

            BaseMessage bm;
            BaseMessage request;
            while ((bm = (BaseMessage) inputStream.readObject()) != null)
            {
                List<BaseMessage> logsession = BaseMessage.GetList(filename);
                if(bm.IsContaint(logsession))
                {
                    //уже есть
                    return;
                }
                
                //Сюда мы с Тошиком напишем реакцию сервера на каждый из классов, 
                //которые может принять сервер. И будет нам счастье!
                Class c = bm.getClass();
                if (c == ping.class)
                {
                    bm.AddMessage(fullname);
                    ping p = (ping) bm;
                    System.out.println(new Date().toString() + " ping " + p.GetPing() + " " + authuser.GetMail());
                    outputStream.writeObject(p);
                    continue;
                }

                if (c == DataForRecord.class)
                {
                    System.out.println(new Date().toString() + " DFR " + authuser.GetMail());

                    DataForRecord p = (DataForRecord) bm;
                    String pdfname = "";


                    if (p.getTypeEvent() == DataForRecord.TypeEvent.open)
                    {
                        System.out.println(new Date().toString() + " Is DFR.open " + authuser.GetMail());
                        try
                        {
                            //pdfname = p.nameshop + " " + filename + " " + p.getTypeEvent().toString();
                            pdfname = p.nameshop + " " + filename + " " + Translate(p.getTypeEvent());

                            System.out.println(new Date().toString() + " Create pdf " + authuser.GetMail());

                            //чтение лога
                            List<BaseMessage> loglist = GetLogList(fullname);
                            ping tmp = null;
                            if (loglist != null)
                            {
                                tmp = FindPingInLog(TypeEvent.open.toString(), loglist);
                                System.out.println(new Date().toString() + " " +TypeEvent.open.toString());
                                if (tmp == null)
                                {
                                    pdfname = "FindPingInLogError";
                                    System.out.println(new Date().toString() + " " + "DFRrequest" + pdfname);
                                    outputStream.writeObject((BaseMessage) new ping(pdfname));
                                    continue;
                                }
                            }
                            else
                            {
                                pdfname = "LogListIsEmpty";
                                System.out.println(new Date().toString() + " " + "DFRrequest " + pdfname + " to " + authuser.GetMail());
                                outputStream.writeObject((BaseMessage) new ping(pdfname));
                                continue;
                            }
                            CreatePDF._CreatePDF(new Strings(StringsConfigFile), authuser,
                                    //GetDFR(DataForRecord.TypeEvent.open,fullname),
                                    p, tmp.GetDate(),
                                    pdfdir + "/", pdfname);

                            System.out.println(new Date().toString() + " EmbeddedImageEmailUtil " + authuser.GetMail());

                            for (String mail : maillist) 
                            {
                                EmbeddedImageEmailUtil.send(
                                    mail,
                                    pdfname, //Тема сообщения
                                    pdfname,
                                    pdfdir + "/");
                                System.out.println(new Date().toString() + " " + pdfname + " send to " + mail);
                            }

                            bm.AddMessage(fullname);
                            pdfname = "recordok";
                            System.out.println(new Date().toString() + " " + "DFRrequest " + pdfname);
                            outputStream.writeObject((BaseMessage) new ping(pdfname));

                            System.out.println(new Date().toString() + " Recordok will be send to " + authuser.GetMail());

                            /*DEBUG*/ bm = null;

                            continue;
                        }
                        catch (DocumentException ex)
                        {
                            pdfname = "DocumentException";
                            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (AddressException ex)
                        {
                            pdfname = "AddressException";
                            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (MessagingException ex)
                        {
                            pdfname = "MessagingException";
                            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (p.getTypeEvent() == DataForRecord.TypeEvent.close)
                    {
                        System.out.println(new Date().toString() + " Ss DFR.close " + authuser.GetMail());
                        try
                        {
                            System.out.println(new Date().toString() + " GetDFR " + authuser.GetMail());

                            DataForRecord dfropen = GetDFR(DataForRecord.TypeEvent.open, fullname);
                            pdfname = dfropen.nameshop + " " + filename + " " + Translate(p.getTypeEvent());

                            System.out.println(new Date().toString() + " CreatePDF " + authuser.GetMail());

                            DataForRecord dfrdrug = GetDFR(DataForRecord.TypeEvent.drug, fullname);
                            DataForRecord dfrsteal = GetDFR(DataForRecord.TypeEvent.steal, fullname);

                            List<BaseMessage> loglist = GetLogList(fullname);
                            ping tmp_open = null;
                            ping tmp_close = null;
                            if (loglist != null)
                            {
                                tmp_open = FindPingInLog(TypeEvent.open.toString(), loglist);
                                tmp_close = FindPingInLog(TypeEvent.close.toString(), loglist);
                                if (tmp_open == null || tmp_close == null)
                                {
                                    pdfname = "FindPingInLogError";
                                    System.out.println(new Date().toString() + " ***** " + "DFRrequest" + pdfname);
                                    outputStream.writeObject((BaseMessage) new ping(pdfname));
                                    continue;
                                }
                            }
                            else
                            {
                                pdfname = "LogListIsEmpty";
                                System.out.println(new Date().toString() + " " + "DFRrequest" + pdfname);
                                outputStream.writeObject((BaseMessage) new ping(pdfname));
                                continue;
                            }

                            CreatePDF._CreatePDF(
                                    new Strings(StringsConfigFile),
                                    authuser,
                                    dfropen,
                                    dfrdrug,
                                    dfrsteal,
                                    p,
                                    tmp_open.GetDate(),
                                    tmp_close.GetDate(),
                                    GetDC(DataCass.TypeEvent.cass, fullname),
                                    GetDC(DataCass.TypeEvent.promoter, fullname),
                                    GetDC(DataCass.TypeEvent.inkasator, fullname),
                                    GetDC(fullname),
                                    pdfdir + "/",
                                    pdfname);
                            for (String mail : maillist) 
                            {
                                EmbeddedImageEmailUtil.send(
                                    mail,
                                    pdfname,
                                    pdfname,
                                    pdfdir + "/");

                            System.out.println(new Date().toString() + " " + pdfname + " send to " + mail);

                            }

                            bm.AddMessage(fullname);
                            pdfname = "recordok";

                            Calendar calendaropen1 = Calendar.getInstance();
                            calendaropen1.setTime(dfropen.GetDate());
                            Calendar calendarclose1 = Calendar.getInstance();
                            calendarclose1.setTime(p.GetDate());
                            long diff1 = calendarclose1.getTimeInMillis() - calendaropen1.getTimeInMillis();
                            long seconds1 = diff1 / 1000;
                            long minutes1 = seconds1 / 60;
                            //double salary = minutes1 * 110 / 60;
                            double salary = minutes1 * GetSalary(salarylist, authuser.GetMail()) / 60;
                            double massk[] = new double[4];//кепки
                            for (int i = 0; i < dfropen.matrix[4].length; i++)
                            {
                                massk[0] += dfropen.matrix[4][i];
                            }
                            for (int i = 0; i < dfrdrug.matrix[4].length; i++)
                            {
                                massk[1] += dfrdrug.matrix[4][i];
                            }
                            for (int i = 0; i < dfrsteal.matrix[4].length; i++)
                            {
                                massk[2] += dfrsteal.matrix[4][i];
                            }
                            for (int i = 0; i < p.matrix[4].length; i++)
                            {
                                massk[3] += p.matrix[4][i];
                            }
                            double salaryprcnt = (((massk[0] + massk[1] - massk[2] - massk[3]) * 150) / 100) * 5;
                            double salaryall = salaryprcnt + salary;
                            double mulct = 0;
                            double weightall[] = new double[4];//вес всего
                            for (int i = 0; i < dfropen.matrix[1].length; i++)
                            {
                                weightall[0] += dfropen.matrix[1][i];
                            }
                            for (int i = 0; i < dfrdrug.matrix[1].length; i++)
                            {
                                weightall[1] += dfrdrug.matrix[1][i];
                            }
                            for (int i = 0; i < dfrsteal.matrix[1].length; i++)
                            {
                                weightall[2] += dfrsteal.matrix[1][i];
                            }
                            for (int i = 0; i < p.matrix[1].length; i++)
                            {
                                weightall[3] += p.matrix[1][i];
                            }
                            double weightsell = weightall[0] + weightall[1] - weightall[2] - weightall[3];
                            double weightskt;//вес стаканы кепки термосы
                            double amounts = getresultmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], p.matrix[3], 0);
                            double amountk = massk[0] + massk[1] - massk[2] - massk[3];
                            double amountt = getresultmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], p.matrix[3], 1);
                            double weights[] = new double[1];//вес стаканы
                            double weightk[] = new double[1];//вес кепки
                            double weightt[] = new double[1];//вес термосы
                            weights[0] = amounts * 60;
                            weightk[0] = amountk * 80;
                            weightt[0] = amountt * 240;
                            weightskt = (weights[0] + weightk[0] + weightt[0]);
                            if (weightsell - weightskt > 200)
                            {
                                mulct = (weightskt - weightsell) * 1.66;
                            }
                            double mulctdelay;//штраф опоздание
                            int hoursbegin = 10;//начало раб дня
                            if (dfropen.GetDate().getHours() < hoursbegin)
                            {
                                mulctdelay = 0;
                            }
                            else
                            {
                                mulctdelay = (dfropen.GetDate().getHours() - hoursbegin) * 60 * 15;
                                if (mulctdelay < 0)
                                {
                                    mulctdelay = 0;
                                }
                                mulctdelay += dfropen.GetDate().getMinutes() * 15;
                            }
                            double mulctall = mulctdelay + mulct;
                            double s_m = salaryall - mulctall;
                            pdfname = "recordok\t"
                                    + salary//зарплата за рабочее время
                                    + "\t"
                                    + salaryprcnt//процент с проданых шапок
                                    + "\t"
                                    + salaryall//зарплата итого
                                    + "\t"
                                    + mulctdelay//штраф за опоздание
                                    + "\t"
                                    + mulct//штраф за перевес
                                    + "\t"
                                    + mulctall//штраф итого
                                    + "\t"
                                    + s_m;//итого на руки

                            System.out.println(new Date().toString() + " " + "DFRrequest " + pdfname);
                            outputStream.writeObject((BaseMessage) new ping(pdfname));

                            System.out.println(new Date().toString() + " recordok will be send to " + authuser.GetMail());
                            continue;
                        }
                        catch (DocumentException ex)
                        {
                            pdfname = "DocumentException ";
                            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (MessagingException ex) {
                            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (p.getTypeEvent() == DataForRecord.TypeEvent.drug || p.getTypeEvent() == DataForRecord.TypeEvent.steal)
                    {
                        System.out.println(new Date().toString() + " Is DFR.drug or steal " + authuser.GetMail());
                        try
                        {
                            bm.AddMessage(fullname);
                            pdfname = "recordok";
                            System.out.println(new Date().toString() + " recordok " + authuser.GetMail());
                        }
                        catch (Exception e)
                        {
                            pdfname = "ExceptionRecord";
                            System.out.println(new Date().toString() + " pdfname = ExceptionRecordok " + authuser.GetMail());
                        }
                    }
                    System.out.println(new Date().toString() + " " + "DFRrequest " + pdfname);
                    outputStream.writeObject((BaseMessage) new ping(pdfname));
                    System.out.println(new Date().toString() + " pdfname will be sending to " + authuser.GetMail());
                    continue;
                }

                if (c == DataCass.class)
                {
                    System.out.println(new Date().toString() + " IsDataCass" + authuser.GetMail());
                    
                    request = (BaseMessage) new ping("cassok");
                    outputStream.writeObject(request);
                    bm.AddMessage(fullname);

                    System.out.println(new Date().toString() + " cassok");

                    continue;
                }

                if (c == LastMessage.class)
                {
                    System.out.println(new Date().toString() + " Бабай!");
                    return;
                }
                else
                {
                    System.out.println(new Date().toString() + " Ну и ладно...");
                    return;
                }

            }
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }

        return;

    }

    //Реализация обработки сообщений и бизнес-логика
    private void Messaging() throws IOException
    {
        System.out.println(new Date().toString() + " Messaging()");

        try
        {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            try
            {
                BaseMessage bm;

                while ((bm = (BaseMessage) inputStream.readObject()) != null)
                {
                    Class c = bm.getClass();

                    if (c == login.class)//Авторизация пользователя
                    {
                        System.out.println(new Date().toString() + " login");

                        login log = (login) bm;
                        if (log.Authentication(accounts))
                        {
                            //System.out.println(new Date().toString() + " Auth Successful");

                            AuthMessaging(log.GetUser(), outputStream, inputStream);
                            return;
                        }
                        else
                        {
                            System.out.println(new Date().toString() + " Auth not successful");

                            BaseMessage request = (BaseMessage) new ping("sobed");
                            outputStream.writeObject(request);
                        }
                        continue;
                    }

                    if (c == ping.class)
                    {
                        System.out.println(new Date().toString() + " ping");

                        if(bm.GetVersion().equals(version))
                        {
                            ping p = (ping) bm;
                            BaseMessage request = (BaseMessage) new Strings(StringsConfigFile);
                            outputStream.writeObject(request);
                            System.out.println(p.GetPing() + " device ON");
                            continue;
                        }
                        else
                        {
                            System.out.println(new Date().toString() + " Еries to use the old version of the library");
                            BaseMessage request = (BaseMessage) new ping("UsedOldVersion");
                            outputStream.writeObject(request);
                        }
                    }

                    if (c == user.class)//Добавление заявки на регистрацию
                    {
                        System.out.println(new Date().toString() + " user");

                        user us = (user) bm;
                        //Если мыла нет в списке аккаунтов

                        boolean b = us.IsUsed(accounts);
                        if (!b)
                        {
                            us.AddMessage(toreg);

                            System.out.println(new Date().toString() + " Registration successful");

                            BaseMessage request = (BaseMessage) new ping("registrationok");
                            try
                            {
                                EmbeddedImageEmailUtil.sendTextmessage(us.GetMail(), "Регистрация", "Привет от ICENGO! \nВаша заявка успешно добавлена и будет обработана в течении нескольких минут. \nСпасибо."); //Запилить текст сообщения в файл

                                System.out.println(new Date().toString() + " Send Registration Mail");

                            }
                            catch (Exception e)
                            {
                                System.err.println(e.toString());
                                request = (BaseMessage) new ping("sobed");
                            }
                            outputStream.writeObject(request);
                            System.out.println(new Date().toString() + " Registration request send");
                        }
                        else
                        {
                            //Если мыло есть - ping("mailisused")
                            System.out.println(new Date().toString() + " Mail is used.");

                            BaseMessage request = (BaseMessage) new ping("mailisused");
                            outputStream.writeObject(request);

                            System.out.println(new Date().toString() + " Mail is used send");

                        }
                        continue;
                    }
                    if (c == forget.class)
                    {
                        System.out.println(new Date().toString() + " Forget");

                        forget f = (forget) bm;

                        //Поиск юзверя по мылуe
                        //EmbeddedImageEmailUtil.sendTextmessage(f.log, "Пароль", f.RecoveryPassword());
                        String s = f.RecoveryPassword(accounts);
                        BaseMessage request;
                        if (s.length() == 4)
                        {
                            System.out.println(new Date().toString() + " This is password");

                            request = (BaseMessage) new ping("forgetok");
                            try
                            {
                                EmbeddedImageEmailUtil.sendTextmessage(f.log, "Пароль", f.RecoveryPassword(accounts));

                                System.out.println(new Date().toString() + " Password will be send");
                            }
                            catch (Exception e)
                            {
                                System.err.println(e.toString());
                                request = (BaseMessage) new ping("sobed");
                            }
                            outputStream.writeObject(request);


                            System.out.println(new Date().toString() + " Forget request send");
                        }
                        else
                        {
                            request = (BaseMessage) new ping("sobed");
                            outputStream.writeObject(request);

                            System.out.println(new Date().toString() + " ForgetSobed");
                        }



                    }
                    if (c == LastMessage.class)
                    {
                        System.out.println(new Date().toString() + " LastMessage");
                        return;
                    }

                }
                return;
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Check your BaseMessage lib.");
            }
            //socket.close();
            return;
        }
        catch (IOException e)
        {
            System.err.println("IO Exception in Messaging! " + e.toString());
        }

        System.out.println(new Date().toString() + " WTF O_o");

        BaseMessage bad = (BaseMessage) new ping("GetOut!");


    }

    public void run()
    {
        try
        {
            Messaging();
            return;
        }
        catch (IOException e)
        {
            System.err.println("IO Exception " + e.toString());
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                System.err.println("Socket not closed " + e.toString());
            }
        }
    }

    private String CreateFileName(user us)
    {

        System.out.println(new Date().toString() + " CreateFileName");




        Date date = us.GetDate();   //DEBUG учесть часовой пояс!
        String mail = us.GetMail();
        String surname = us.GetSurname();

        int year = date.getYear() + 1900;
        int mounth = date.getMonth() + 1;
        int day = date.getDate();


        //dotname = mail;
        String FileName = surname + " " + day + "." + mounth + "." + year;
        if (!us.GetSuper())
        {
            return FileName;
        }
        else
        {
            String path = CreateLogDirName(us);
            String ls[] = new File(path).list();
//            if (ls.length == 0)
//            {
//                return FileName + "_0";
//            }
            return FileName + " " + (ls.length - 2);    //2 папки
        }
    }
//    private String CreateFileName(user us)
//    {
//
//        System.out.println(new Date().toString() + " CreateFileName");
//
//        String dotname = "Океанариум" + "_";
//
//
//        Date date = us.GetDate();   //DEBUG учесть часовой пояс!
//        String mail = us.GetMail();
//        String surname = us.GetSurname();
//
//        int year = date.getYear() + 1900;
//        int mounth = date.getMonth() + 1;
//        int day = date.getDate();
//
//        dotname = surname + "_" + mail;
//        //dotname = mail;
//        String FileName = dotname + day + "-" + mounth + "-" + year;
//        if (!us.GetSuper())
//        {
//            return FileName;
//        }
//        else
//        {
//            String path = CreateLogDirName(us);
//            String ls[] = new File(path).list();
////            if (ls.length == 0)
////            {
////                return FileName + "_0";
////            }
//            return FileName + "_" + (ls.length - 2);    //2 папки
//        }
//    }

    private String CreateLogDirName(user us)
    {
        Date date = us.GetDate();   //DEBUG учесть часовой пояс!
        String mail = us.GetMail();
        String surname = us.GetSurname();

        int year = date.getYear() + 1900;
        int mounth = date.getMonth() + 1;
        int day = date.getDate();

        //String DirName = mail + "_" + surname;

        if (!us.GetSuper())
        {

            return "../log/" + year + "/" + mounth + "/" + day;
        }
        else
        {
            String path = "../log/" + year + "/" + mounth + "/" + day + "/" + "supers" + "/" + mail;

            return path;

        }
    }

    private String FileName(user us)
    {
        Date date = us.GetDate();   //DEBUG учесть часовой пояс!
        String mail = us.GetMail();
        String surname = us.GetSurname();

        int year = date.getYear() + 1900;
        int mounth = date.getMonth() + 1;
        int day = date.getDate();


        String FileName = surname + " " + day + "." + mounth + "." + year;


        if (!us.GetSuper())
        {
            return FileName;
        }
        else
        {
            String path = CreateLogDirName(us);
            String ls[] = new File(path).list();
//            if (ls.length == 0)
//            {
//                return FileName + "_0";
//            }
//            return FileName + " " + (ls.length - 2);    //DEBUG для супера
            return FileName + " " + (ls.length - 3);    //DEBUG для супера
            //return FileName + " " + (ls.length - 4);    //DEBUG для супера
        }
    }

    private BaseMessage GetStatusSession(user authuser, String dir)
    {
        //String dir = CreateLogDirName(authuser);
        String filename = FileName(authuser);
        String fullname = dir + "/" + filename;

        String ls[] = new File(dir).list();
        if (ls.length != 2) //2 папки. Нет файлов
        {
            for (String string : ls)
            {
                if (string.equals(filename))
                {
                    //Файл есть. Открываем и ищем объекты открытия, закрытия итд
                    List<BaseMessage> loglist;
                    try
                    {
                        FileInputStream fis = new FileInputStream(fullname);
                        ObjectInputStream read = new ObjectInputStream(fis);
                        if ((loglist = (List<BaseMessage>) read.readObject()) != null)
                        {
                            Class c;
                            short open = 0, close = 0, drug = 0, steal = 0, opens = 0;
                            for (BaseMessage bm : loglist)//Подсчитать количество объектов каждого типа в листе лога
                            {
                                c = bm.getClass();
                                if (c == DataForRecord.class)
                                {
                                    DataForRecord data = (DataForRecord) bm;

                                    if (data.getTypeEvent() == DataForRecord.TypeEvent.open)
                                    {
                                        open++;
                                        continue;
                                    }
                                    if (data.getTypeEvent() == DataForRecord.TypeEvent.close)
                                    {
                                        close++;
                                        continue;
                                    }
                                    if (data.getTypeEvent() == DataForRecord.TypeEvent.drug)
                                    {
                                        drug++;
                                        continue;
                                    }
                                    if (data.getTypeEvent() == DataForRecord.TypeEvent.steal)
                                    {
                                        steal++;
                                        continue;
                                    }
                                }
                                if(c == ping.class)
                                {
                                    if(((ping)bm).GetPing().equals("open"))
                                    {
                                        opens++;
                                    }
                                }
                            }
                            if (!authuser.GetSuper())//Если это НЕ супер
                            {
                                if (open == 1 && close == 0) //Сессия открыта, но не закрыта
                                {
                                    return (BaseMessage) new ping("open"); //открывается
                                }
                                
                                //DEBUG Сессия в процессе открытия
                                
                                //
                                if (open == 0 && close == 0) //Новая сессия
                                {
                                    return (BaseMessage) new ping("NewSession");
                                }
                                if (open == 1 && close == 1) //Сессия закрыта
                                {
                                    return (BaseMessage) new ping("SessionClose");
                                }
                                return (BaseMessage) new ping("ErrorStatusSession"); //Лох-Несский баг
                            }
                            else
                            {
                                if(opens >= 1 && open == 0)
                                {
                                    return (BaseMessage) new ping("opens");
                                }
                                if (open == 1 && close == 0) //Сессия открыта, но не закрыта
                                {
                                    return (BaseMessage) new ping("open");
                                }
                                else //Новая сессия
                                {
                                    return (BaseMessage) new ping("NewSession");
                                }
                            }

                        }
                    }
                    catch (FileNotFoundException ex)
                    {

                        Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                        return (BaseMessage) new ping("FileNotFoundException");
                    }
                    catch (IOException ex)
                    {
                        System.err.println("IO Exception in GetStatusSession! " + ex.toString());
                        return (BaseMessage) new ping("IOException");
                    }
                    catch (ClassNotFoundException ex)
                    {
                        Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);
                        return (BaseMessage) new ping("ClassNotFoundException");
                    }

                }

            }
            return (BaseMessage) new ping("NewSession");
        }
        else
        {
            return (BaseMessage) new ping("NewSession");
        }
        //return (BaseMessage) new ping("FailInGetStatusSession");//Если всё просралось
    }

    //Возвращает объект заданного типа события
    private static DataForRecord GetDFR(DataForRecord.TypeEvent TE, String file) throws UnsupportedEncodingException, IOException
    {
        DataForRecord tempdfr = new DataForRecord(new Strings(StringsConfigFile));
        FileInputStream fis = null;
        ObjectInputStream read = null;
        try
        {
            List<BaseMessage> loglist = null;
            fis = new FileInputStream(file);
            read = new ObjectInputStream(fis);
            {
                try
                {
                    if ((loglist = (List) read.readObject()) != null)
                    {
                        if (TE == DataForRecord.TypeEvent.open || TE == DataForRecord.TypeEvent.close)
                        {
                            List<DataForRecord> dfrlist = FindEventInLog(TE, loglist);
                            if (dfrlist.size() != 0)
                            {
                                tempdfr = dfrlist.get(0);
                            }
                        }
                        else
                        {
                            List<DataForRecord> dfrlist = FindEventInLog(TE, loglist);
                            if (dfrlist.size() != 0)
                            {

                                for (DataForRecord curdfr : dfrlist)
                                {
                                    tempdfr.addData(curdfr, true);
                                }

                            }
                            else
                            {
                                //DEBUG
                                System.err.println("Не достать дфр " + TE.name());

                            }
                        }
                    }
                }
                catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                read.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tempdfr;
    }

    private static String[] GetDC(String file)
    {
        List<String> strlist = new ArrayList();
        FileInputStream fis = null;
        ObjectInputStream read = null;
        try
        {
            List<BaseMessage> loglist = null;
            fis = new FileInputStream(file);
            read = new ObjectInputStream(fis);
            {
                try
                {
                    if ((loglist = (List) read.readObject()) != null)
                    {
                        List<DataCass> dclist = FindEventInLog(DataCass.TypeEvent.inkasator, loglist);
                        if (dclist.size() != 0)
                        {
                            for (DataCass dc : dclist)
                            {
                                strlist.add(dc.getInkFam());
                            }
                        }
                        else
                        {
                            strlist.add("");
                        }
                    }
                }
                catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                read.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return strlist.toArray(new String[strlist.size()]);
    }

    private static double[] GetDC(DataCass.TypeEvent TE, String file)
    {
        double[] doublemass = new double[1];
        FileInputStream fis = null;
        ObjectInputStream read = null;
        try
        {
            List<BaseMessage> loglist = null;
            fis = new FileInputStream(file);
            read = new ObjectInputStream(fis);
            {
                try
                {
                    if ((loglist = (List) read.readObject()) != null)
                    {
                        List<DataCass> dclist = FindEventInLog(TE, loglist);
                        if (dclist.size() != 0)
                        {
                            doublemass = new double[dclist.size()];
                            if (TE == DataCass.TypeEvent.cass)
                            {
                                for (int i = 0; i < dclist.size(); i++)
                                {
                                    doublemass[i] = dclist.get(i).getCassCash();
                                }
                            }
                            if (TE == DataCass.TypeEvent.inkasator)
                            {
                                for (int i = 0; i < dclist.size(); i++)
                                {
                                    doublemass[i] = dclist.get(i).getInkCash();
                                }
                            }
                            if (TE == DataCass.TypeEvent.promoter)
                            {
                                for (int i = 0; i < dclist.size(); i++)
                                {
                                    doublemass[i] = dclist.get(i).getProCash();
                                }
                            }
                        }
                    }
                }
                catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                read.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return doublemass;
    }

    private static String Translate(TypeEvent typeEvent)
    {
        if (typeEvent == TypeEvent.open)
        {
            return "Открытие";
        }
        if (typeEvent == TypeEvent.close)
        {
            return "Закрытие";
        }
        return "";
    }

    private static List<BaseMessage> GetLogList(String fullname)
    {
        List<BaseMessage> loglist;
        try
        {
            FileInputStream fis = new FileInputStream(fullname);
            ObjectInputStream read = new ObjectInputStream(fis);
            if ((loglist = (List<BaseMessage>) read.readObject()) != null)
            {
                return loglist;
            }


        }
        catch (FileNotFoundException ex)
        {

            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);

        }
        catch (IOException ex)
        {
            System.err.println("IO Exception in GetStatusSession! " + ex.toString());

        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(BaseMessage.class.getName()).log(Level.SEVERE, null, ex);

        }
        return null;
    }

    private List<String> GetMailList(String maillist) 
    {
        List<String> mlist = null;
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(maillist));
            String str;
            mlist =  new ArrayList<String>();
            while((str = br.readLine()) != null)
            {
                mlist.add(str);
            }
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mlist;
    }

    private List<String> GetSalaryList(String salarylist) 
    {
        List<String> slist = null;
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(salarylist));
            String str;
            slist =  new ArrayList<String>();
            while((str = br.readLine()) != null)
            {
                slist.add(str);
            }
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServeOneJabber.class.getName()).log(Level.SEVERE, null, ex);
        }
        return slist;
    }

    private double GetSalary(List<String> salarylist, String mail) 
    {
        double s = Double.parseDouble((salarylist.get(0)).split("\t")[0]);
        for (String string : salarylist) 
        {
            String[] insplits = string.split("\t");
            if(insplits[0].equals(mail))
            {
                s =  Integer.parseInt(insplits[1]);
            }
        }
        return s;
    }
}