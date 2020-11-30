import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;

/**
 * Classe Pergunta
 * 05 / 10 / 2020
 * 
 * @version 1.0
 * @author Lucas Bottrel Lopes de Moura
 * 
 */
public class Pergunta implements Registro{
    
    protected int idUsuario; // id do usuário que criou a pergunta
    protected int idPergunta; // id da pergunta no arquivo
    protected long criacao; // data e hora de criação da pergunta
    protected short nota; // avaliação da pergunta
    protected String pergunta; // pergunta propriamente dita
    protected boolean ativa; // boolean verificando se a pergunta está ativa ou não
    protected String palavrasChave; // palavras chave relacionadas a pergunta

    /**
     * Construtor com parâmetros
     * @param iu
     * @param ip
     * @param c
     * @param n
     * @param p
     * @param a
     * @param pC
     */
    public Pergunta (int iu, int ip, long c, short n, String p, boolean a, String pC) {
        this.idUsuario = iu;
        this.idPergunta = ip;
        this.criacao = c;
        this.nota = n;
        this.pergunta = p;
        this.ativa = a;
        this.palavrasChave = pC;
    }

    /**
     * Construtor sem parâmetros
     */
    public Pergunta() {
        this.idUsuario = -1;
        this.idPergunta = -1;
        this.criacao = -1;
        this.nota = -1;
        this.pergunta = "";
        this.ativa = true;
        this.palavrasChave = "";
    }

    /**
     * Transforma a pergunta em uma String para ser printada
     */
    public String toString() {
        String resp = "";
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy' 'HH':'mm':'ss"); // modifica o long 

        resp += "\nPergunta: " + pergunta + "\nNota: " + nota + "\nAtiva: ";
        if (this.ativa == true){
            resp += "sim";
        } else resp += "não";
        resp += "\nCriação: " + d.format(criacao);
        resp += "\nPalavras Chave: " + palavrasChave;
        
        return resp;
    }

    /**
     * @return idPergunta
     */
    public int getID() {
        return this.idPergunta;
    }

    /**
     * Define o ID Pergunta
     * @param IDPergunta
     */
    public void setID(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    /**
     * @return idUsuario
     */
    public int getIDUsuario() {
        return this.idUsuario;
    }

    /**
     * define o ID Usuario
     * @param idUsuario
     */
    public void setIDUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return Data de Criação
     */
    public String getCriacao() {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy' 'HH':'mm':'ss");

        return d.format(criacao);
    }

    /**
     * Define Criação
     * @param c data e hora
     */
    public void setCriacao(long c) {
        this.criacao = c;
    }

    /**
     * @return nota da pergunta
     */
    public int getNota() {
        return (int)this.nota;
    }

    /**
     * Define a nota da pergunta
     * @param nota
     */
    public void setNota(int nota){
        this.nota = (short)nota;
    }

    /**
     * @return Pergunta
     */
    public String getPergunta(){
        return this.pergunta;
    }

    /**
     * Define a pergunta
     * @param pergunta
     */
    public void setPergunta(String pergunta){
        this.pergunta = pergunta;
    }

    /**
     * @return Ativa
     */
    public boolean getAtiva() {
        return this.ativa;
    }

    /**
     * Define se a pergunta está ativa
     * @param ativa
     */
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    /**
     * Define as palavras chave
     * @param pC
     */
    public void setPalavrasChave(String pC){
        this.palavrasChave = pC;
    }

    /**
     * @return palavras chave separadas por ;
     */
    public String getPalavrasChave(){
        return this.palavrasChave;
    }

    /**
     * Passa o objeto para um array de bytes
     * @return Array de Bytes que define o objeto
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(idUsuario);
            dos.writeInt(idPergunta);
            dos.writeLong(criacao);
            dos.writeShort(nota);
            dos.writeUTF(pergunta);
            dos.writeBoolean(ativa);
            dos.writeUTF(palavrasChave);
        } catch (Exception e) {
        }

        return baos.toByteArray();
    }

    /**
     * Define os atributos do objeto a partir de um array de bytes
     * @param ba Array de bytes que definem o objeto
     */
    public void fromByteArray(byte[] ba) {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        try {
            this.idUsuario = dis.readInt();
            this.idPergunta = dis.readInt();
            this.criacao = dis.readLong();
            this.nota = dis.readShort();
            this.pergunta = dis.readUTF();
            this.ativa = dis.readBoolean();
            this.palavrasChave = dis.readUTF();
            
        } catch (Exception e) {
        }

    }

    public String chaveSecundaria(){
        return null;
    }
}