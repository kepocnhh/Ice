/*  1:   */ package ice;
/*  2:   */ 
/*  3:   */ public class ping
/*  4:   */   extends BaseMessage
/*  5:   */ {
/*  6:15 */   String ping = "ping";
/*  7:   */   
/*  8:   */   public String GetPing()
/*  9:   */   {
/* 10:18 */     return this.ping;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ping() {}
/* 14:   */   
/* 15:   */   public ping(String message)
/* 16:   */   {
/* 17:23 */     this.ping = message;
/* 18:   */   }
/* 19:   */ }


/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar
 * Qualified Name:     ice.ping
 * JD-Core Version:    0.7.0.1
 */