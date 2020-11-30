import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;

/**
 * Classe Resposta
 * 14 / 10 / 2020
 * 
 * @version 1.0
 * @author Lucas Bottrel Lopes de Moura
 * 
 */
public class Resposta implements Registro{
    
    protected int idUsuario; // id do usuário que criou a pergunta
    protected int idPergunta; // id da pergunta no arquivo
    protected int idResposta; // id da resposta
    protected long criacao; // data e hora de criação da pergunta
    protected short nota; // avaliação da pergunta
    protected String resposta; // resposta propriamente dita
    protected boolean ativa; // boolean verificando se a pergunta está ativa ou não

    /**
     * Construtor com parâmetros
     * @param iu
     * @param ip
     * @param ir
     * @param c
     * @param n
     * @param p
     * @param a
     */
    public Resposta (int iu, int ip, int ir, long c, short n, String r, boolean a) {
        this.idUsuario = iu;
        this.idPergunta = ip;
        this.idResposta = ir;
        this.criacao = c;
        this.nota = n;
        this.resposta = r;
        this.ativa = a;
    }

    /**
     * Construtor sem parâmetros
     */
    public Resposta() {
        this.idUsuario = -1;
        this.idPergunta = -1;
        this.idResposta = -1;
        this.criacao = -1;
        this.nota = -1;
        this.resposta = "";
        this.ativa = true;
    }

    /**
     * Transforma a pergunta em uma String para ser printada
     */
    public String toString() {
        String resp = "";
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy' 'HH':'mm':'ss"); // modifica o long 

        resp += "\nResposta: " + resposta + "\nNota: " + nota + "\nAtiva: ";
        if (this.ativa == true){
            resp += "sim";
        } else resp += "não";
        resp += "\nCriação: " + d.format(criacao);
        
        return resp;
    }

    /**
     * @return idResposta
     */
    public int getID() {
        return this.idResposta;
    }

    /**
     * Define o ID Resposta
     * @param IDResposta
     */
    public void setID(int idResposta) {
        this.idResposta = idResposta;
    }

    /**
     * @return idPergunta
     */
    public int getIDPergunta() {
        return this.idPergunta;
    }

    /**
     * Define o ID Pergunta
     * @param IDPergunta
     */
    public void setIDPergunta(int idPergunta) {
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
     * @return Resposta
     */
    public String getResposta(){
        return this.resposta;
    }

    /**
     * Define a resposta
     * @param resposta
     */
    public void setResposta(String resposta){
        this.resposta = resposta;
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
     * Passa o objeto para um array de bytes
     * @return Array de Bytes que define o objeto
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(idUsuario);
            dos.writeInt(idPergunta);
            dos.writeInt(idResposta);
            dos.writeLong(criacao);
            dos.writeShort(nota);
            dos.writeUTF(resposta);
            dos.writeBoolean(ativa);
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
            this.idResposta = dis.readInt();
            this.criacao = dis.readLong();
            this.nota = dis.readShort();
            this.resposta = dis.readUTF();
            this.ativa = dis.readBoolean();
            
        } catch (Exception e) {
        }

    }

    public String chaveSecundaria(){
        return null;
    }
}