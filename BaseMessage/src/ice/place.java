/*  1:   */ package ice;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ public class place
/*  6:   */   implements Serializable
/*  7:   */ {
/*  8:   */   private double x;
/*  9:   */   private double y;
/* 10:   */   
/* 11:   */   public place(String sx, String sy)
/* 12:   */   {
/* 13:22 */     this.x = Double.parseDouble(sx);
/* 14:23 */     this.y = Double.parseDouble(sy);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public place(double sx, double sy)
/* 18:   */   {
/* 19:27 */     this.x = sx;
/* 20:28 */     this.y = sy;
/* 21:   */   }
/* 22:   */   
/* 23:   */   boolean Check(double curx, double cury)
/* 24:   */   {
/* 25:34 */     return true;
/* 26:   */   }
/* 27:   */ }


/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar
 * Qualified Name:     ice.place
 * JD-Core Version:    0.7.0.1
 */