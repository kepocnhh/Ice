 package ice;
 
 import java.io.BufferedReader;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.List;
 
 public class Strings
   extends BaseMessage
 {
   public List<String> DataSale = new ArrayList();
   public List<String> DataSubSale = new ArrayList();
   public List<String> DataCass = new ArrayList();
   public List<String> DataSubCass = new ArrayList();
   public List<String> PDFheader = new ArrayList();
   public List<String> SessionTypes = new ArrayList();
   private String PathFile = "../config"; //default
   
   public Strings()
     throws UnsupportedEncodingException, IOException
   {
     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.PathFile), "UTF-8"));
     init(br);
   }
   public Strings(String pth)
     throws UnsupportedEncodingException, IOException
   {
     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pth), "UTF-8"));
     init(br);
   }
   
   private void init(BufferedReader br)
     throws UnsupportedEncodingException, IOException
   {
     String s = "";
     String subs = "";
     while ((s = br.readLine()) != null)
     {
       if (s.equals("*")) {
         break;
       }
       subs = "";
       this.DataSale.add(s);
       s = br.readLine();
       subs = subs + s;
       for (;;)
       {
         s = br.readLine();
         if (s.equals("-")) {
           break;
         }
         subs = subs + "\t" + s;
       }
       this.DataSubSale.add(subs);
     }
     while ((s = br.readLine()) != null)
     {
       subs = "";
       this.DataCass.add(s);
       s = br.readLine();
       subs = subs + s;
       for (;;)
       {
         s = br.readLine();
         if (s == null) {
           break;
         }
         if (s.equals("-")) {
           break;
         }
         subs = subs + "\t" + s;
       }
       this.DataSubCass.add(subs);
     }
     br.close();
     //
     this.PDFheader.add("ФИО");
     this.PDFheader.add("Место");
     this.PDFheader.add("Дата");
     this.PDFheader.add("Время откр.");
     this.PDFheader.add("Время закрыт.");
     this.SessionTypes.add(" ");
     this.SessionTypes.add("ОТКРЫТИЕ");
     this.SessionTypes.add("ПРИХОД");
     this.SessionTypes.add("УХОД");
     this.SessionTypes.add("ЗАКРЫТИЕ");
   }
 }



/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.Strings

 * JD-Core Version:    0.7.0.1

 */