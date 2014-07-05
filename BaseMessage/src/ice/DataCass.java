 package ice;
 
 public class DataCass
   extends BaseMessage
 {
   private String InkFam;
   private double InkCash;
   private double ProCash;
   private double CassCash;
   private TypeEvent TE;
   
   public static enum TypeEvent
   {
     inkasator,  promoter,  cass;
     
     private TypeEvent() {}
   }
   
   public DataCass(double d, String s, TypeEvent te)
   {
     if (te == TypeEvent.cass) {
       this.CassCash = d;
     }
     if (te == TypeEvent.promoter) {
       this.ProCash = d;
     }
     if (te == TypeEvent.inkasator)
     {
       this.InkCash = d;
       this.InkFam = s;
     }
     this.TE = te;
   }
   
   public void setTypeEvent(TypeEvent te)
   {
     this.TE = te;
   }
   
   public TypeEvent getTypeEvent()
   {
     return this.TE;
   }
   
   public double getCassCash()
   {
     return this.CassCash;
   }
   
   public double getProCash()
   {
     return this.ProCash;
   }
   
   public double getInkCash()
   {
     return this.InkCash;
   }
   
   public String getInkFam()
   {
     return this.InkFam;
   }
 }



/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.DataCass

 * JD-Core Version:    0.7.0.1

 */