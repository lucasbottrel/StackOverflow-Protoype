public interface Registro {
    public int getID();
    public void setID(int i);
    public byte[] toByteArray();
    public void fromByteArray(byte ba[]);
    public String chaveSecundaria();
}