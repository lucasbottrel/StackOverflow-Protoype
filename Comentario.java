import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;

/**
 * Classe Comentario
 * 03 / 11 / 2020
 * 
 * @version 1.0
 * @author Lucas Bottrel Lopes de Moura
 * 
 */
public class Comentario implements Registro {
    
    protected int idComentario; // id do comentario
    protected int idUsuario; // id do usuario que Comentariou
    protected char tipo; // Comentario de resposta 'R' ou pergunta 'P'
    protected int idPR; // ID de pergunta ou resposta do Comentario
    protected String comentario; // texto do comentario
    protected long criacao; // data e hora de criação do comentario

    /**
     * Construtor com parâmetros
     * @param idComentario
     * @param idUsuario
     * @param tipo
     * @param idPR
     * @param comentario
     * @param criacao
     */
    public Comentario (int idComentario, int idUsuario, char tipo, int IdPR, String comentario, long criacao) {
        this.idComentario = idComentario;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.idPR = idPR;
        this.comentario = comentario;
        this.criacao = criacao;
    }

    /**
     * Construtor sem parâmetros
     */
    public Comentario() {
        this.idComentario = -1;
        this.idUsuario = -1;
        this.tipo = ' ';
        this.idPR = -1;
        this.comentario = "";
        this.criacao = -1;
    }

    public String toString(){
        return "idComentario: " + idComentario + "\nidUsuario: " + idUsuario + "\nTipo: " + tipo + "\nidPR: " + idPR + "\nComentario: " + comentario;
    }

    /**
     * @return idComentario
     */
    public int getID() {
        return this.idComentario;
    }

    /**
     * Define o ID Comentario
     * @param IDComentario
     */
    public void setID(int idComentario) {
        this.idComentario = idComentario;
    }

    /**
     * @return idUsuario
     */
    public int getIDUsuario() {
        return this.idUsuario;
    }

    /**
     * Define o IDUsuario
     * @param IDUsuario
     */
    public void setIDUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return tipo do Comentario
     */
    public char getTipo() {
        return this.tipo;
    }

    /**
     * Define o tipo do Comentario
     * @param tipo
     */
    public void setTipo(char tipo){
        this.tipo = tipo;
    }

    /**
     * @return id Pergunta ou Resposta
     */
    public int getIDPR() {
        return this.idPR;
    }

    /**
     * Define id Pergunta ou Resposta
     * @param idPR
     */
    public void setIDPR(int idPR){
        this.idPR = idPR;
    }

    /**
     * @return Comentario
     */
    public String getComentario() {
        return this.comentario;
    }

    /**
     * Define o Comentario
     * @param tipo
     */
    public void setComentario(String comentario){
        this.comentario = comentario;
    }

    /**
     * @return criação
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
     * Passa o objeto para um array de bytes
     * @return Array de Bytes que define o objeto
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(idComentario);
            dos.writeInt(idUsuario);
            dos.writeChar(tipo);
            dos.writeInt(idPR);
            dos.writeUTF(comentario);
            dos.writeLong(criacao);
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
            this.idComentario = dis.readInt();
            this.idUsuario = dis.readInt();
            this.tipo = dis.readChar();
            this.idPR = dis.readInt();
            this.comentario = dis.readUTF();
            this.criacao = dis.readLong();
            
        } catch (Exception e) {
        }

    }

    public String chaveSecundaria() {
        return null;
    }
}