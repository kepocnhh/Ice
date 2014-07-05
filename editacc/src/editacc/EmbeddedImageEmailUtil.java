package editacc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmbeddedImageEmailUtil
{

    static String userName = "iceandgoit@gmail.com";
    static String password = "delaemicecreame";
    static Properties properties = new Properties();
    public EmbeddedImageEmailUtil()
    {
        
    }
    private static Properties SetProp()
    {
        String host = "smtp.gmail.com";
        String port = "587";

        properties.put(
                "mail.smtp.host", host);
        properties.put(
                "mail.smtp.port", port);
        properties.put(
                "mail.smtp.auth", "true");
        properties.put(
                "mail.smtp.starttls.enable", "true");
        properties.put(
                "mail.user", userName);
        properties.put(
                "mail.password", password);
        return properties;
    }

    public static void send(
            String toAddress,
            String subject,
            String imaya,
            String imgpath)
            throws AddressException, MessagingException
    {
        Authenticator auth = new Authenticator()
        {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(SetProp(), auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses =
        {
            new InternetAddress(toAddress)
        };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        String fileName = imaya + ".pdf";
        String file = imgpath + fileName;
        DataSource source = new FileDataSource(file);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
    public static void sendimagepluspdf(
            String toAddress,
            String subject,
            String text,
            String pdfname,
            String pdfpath,
            String imgname,
            String imgpath)
            throws AddressException, MessagingException
    {
        Authenticator auth = new Authenticator()
        {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(SetProp(), auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses =
        {
            new InternetAddress(toAddress)
        };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        String fileName = pdfname + ".pdf";
        String file = pdfpath + fileName;
        DataSource source = new FileDataSource(file);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        fileName = imgname + ".jpg";
        file = imgpath + fileName;
        source = new FileDataSource(file);
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void sendimage(
            String toAddress,
            String subject,
            String text,
            String textend,
            String imgpath
            )
                throws AddressException, MessagingException {
        Map<String, String> mapInlineImages = new HashMap<String, String>();
        mapInlineImages.put("image1",imgpath);
        String host = "smtp.gmail.com";
        String port = "587";
        final String userName="iceandgoit@gmail.com";
        final String password="delaemicecreame";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        StringBuffer body = new StringBuffer("<html> <br>");
        body.append(text+"<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append(textend+"<br>");
        body.append("</html>");
        messageBodyPart.setContent(body.toString(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if (mapInlineImages != null && mapInlineImages.size() > 0)
        {
            Set<String> setImageID = mapInlineImages.keySet();
            for (String contentId : setImageID)
            {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                multipart.addBodyPart(imagePart);
            }
        }
        msg.setContent(multipart);
        Transport.send(msg);
    }
    public static void sendTextmessage(
            String toAddress,
            String subject,
            String content)
            throws AddressException, MessagingException
    {
        Session session = Session.getInstance(SetProp(), new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        });
        try
        {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(userName));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(content);
            //message.setFileName("C:\\IMG.jpg");
            //Отправляем сообщение
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }
}