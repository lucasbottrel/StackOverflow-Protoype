import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Classe Usuário
 * 22 / 10 /2020
 * 
 * @author Lucas Bottrel Lopes de Moura
 * @version 1.0
 */
public class Usuario implements Registro{
    
    protected int idUsuario; // ID que identifica o usuário
    protected String nome; // nome de usuário
    protected String email; // email do usuário
    protected String senha; // senha do usuário

    /**
     * Construtor com parâmetros
     * @param i ID
     * @param n Nome
     * @param e Email
     * @param s Senha
     */
    public Usuario (int i, String n, String e, String s) {
        this.idUsuario = i;
        this.nome = n;
        this.email = e;
        this.senha = s;
    }

    /**
     * Construtor Padrão
     */
    public Usuario() {
        this.idUsuario = -1;
        this.nome = "";
        this.email = "";
        this.senha = "";
    }

    /** 
     * @return String com todos atributos do Usuário
     */
    public String toString() {
        return "\nNome de Usuario: " + nome + "\nEmail: " + email + "\nSenha: " + senha;
    }

    /**
     * @return ID Usuário
     */
    public int getID() {
        return this.idUsuario;
    }

    /**
     * @param ID 
     */
    public void setID(int ID) {
        this.idUsuario = ID;
    }

    /**
     * @return Nome do Usuário
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return Email do usuário
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * @return Senha do Usuário
     */
    public String getSenha(){
        return this.senha;
    }

    /**
     * @param senha
     */
    public void setSenha(String senha){
        this.senha = senha;
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
            dos.writeUTF(nome);
            dos.writeUTF(email);
            dos.writeUTF(senha);
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
            this.nome = dis.readUTF();
            this.email = dis.readUTF();
            this.senha = dis.readUTF();
        } catch (Exception e) {
        }

    }

    public String chaveSecundaria(){
        return this.email;
    }
}