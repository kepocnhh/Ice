package ice;

public class Photo extends BaseMessage
{
    public byte[] log;
    private DataForRecord.TypeEvent TE;
    public Photo()
    {
    }
    public Photo(byte[] curlog)
    {
        log = curlog;
    }
    public Photo(byte[] curlog,DataForRecord.TypeEvent te)
    {
        TE=te;
        log = curlog;
    }
    public void setTypeEvent(DataForRecord.TypeEvent te)
    {
        TE = te;
    }
    public DataForRecord.TypeEvent getTypeEvent()
    {
        return TE;
    }
}
