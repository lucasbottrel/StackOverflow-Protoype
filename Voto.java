import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;

/**
 * Classe Voto
 * 29 / 10 / 2020
 * 
 * @version 1.0
 * @author Lucas Bottrel Lopes de Moura
 * 
 */
public class Voto implements Registro {
    
    protected int idVoto; // id do voto
    protected int idUsuario; // id do usuario que votou
    protected char tipo; // voto de resposta 'R' ou pergunta 'P'
    protected int idPR; // ID de pergunta ou resposta do voto
    protected boolean voto; // positivo(true) ou negativo(false)
    protected long criacao; // data e hora de criação do comentario

    /**
     * Construtor com parâmetros
     * @param idVoto
     * @param idUsuario
     * @param tipo
     * @param idPR
     * @param voto
     */
    public Voto (int idVoto, int idUsuario, char tipo, int IdPR, boolean voto, long criacao) {
        this.idVoto = idVoto;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.idPR = idPR;
        this.voto = voto;
        this.criacao = criacao;
    }

    /**
     * Construtor sem parâmetros
     */
    public Voto() {
        this.idVoto = -1;
        this.idUsuario = -1;
        this.tipo = ' ';
        this.idPR = -1;
        this.voto = false;
        this.criacao = -1;
    }

    public String toString(){
        return "idVoto: " + idVoto + "\nidUsuario: " + idUsuario + "\nTipo: " + tipo + "\nidPR: " + idPR + "\nVoto: " + voto + "\nCriação" + criacao;
    }

    /**
     * @return idVoto
     */
    public int getID() {
        return this.idVoto;
    }

    /**
     * Define o ID Voto
     * @param IDVoto
     */
    public void setID(int idVoto) {
        this.idVoto = idVoto;
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
     * @return tipo do voto
     */
    public char getTipo() {
        return this.tipo;
    }

    /**
     * Define o tipo do voto
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
     * @return voto
     */
    public boolean getVoto() {
        return this.voto;
    }

    /**
     * Define o voto
     * @param tipo
     */
    public void setVoto(boolean voto){
        this.voto = voto;
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
            dos.writeInt(idVoto);
            dos.writeInt(idUsuario);
            dos.writeChar(tipo);
            dos.writeInt(idPR);
            dos.writeBoolean(voto);
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
            this.idVoto = dis.readInt();
            this.idUsuario = dis.readInt();
            this.tipo = dis.readChar();
            this.idPR = dis.readInt();
            this.voto = dis.readBoolean();
            
        } catch (Exception e) {
        }

    }

    public String chaveSecundaria() {
        return idUsuario + "|" + tipo + "|" + idPR;
    }
}