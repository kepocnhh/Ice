package ice;

import java.util.List;

public class DataForRecord
        extends BaseMessage
{
    public String nameshop;
    public double[][] matrix;
    private double cashcass;
    private TypeEvent TE;

    public static enum TypeEvent
    {

        open, close, drug, steal;

        private TypeEvent()
        {
        }
    }

    public DataForRecord()
    {
    }

    public DataForRecord(Strings mystr)
    {
        int len = mystr.DataSale.size();
        this.matrix = new double[len][];
        for (int i = 0; i < len; i++)
        {
            this.matrix[i] = new double[((String) mystr.DataSubSale.get(i)).split("\t").length];
        }
    }

    public void setTypeEvent(TypeEvent te)
    {
        this.TE = te;
    }

    public TypeEvent getTypeEvent()
    {
        return this.TE;
    }

    public void setCash(double d)
    {
        this.cashcass = d;
    }

    public double getCash()
    {
        return this.cashcass;
    }

    public void addData(DataForRecord DFR, boolean fl)
    {
        int sign = 1;
        if (!fl)
        {
            sign *= -1;
        }
        int len = this.matrix.length;
        for (int i = 0; i < len; i++)
        {
            int len2 = this.matrix[i].length;
            for (int j = 0; j < len2; j++)
            {
                this.matrix[i][j] += DFR.matrix[i][j] * sign;
            }
        }
    }
}

/* Location:           D:\Documents\NetBeans\IceTestClient\lib\BaseMessage.jar

 * Qualified Name:     ice.DataForRecord

 * JD-Core Version:    0.7.0.1

 */
