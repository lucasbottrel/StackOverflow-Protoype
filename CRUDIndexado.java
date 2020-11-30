
/**
 * CLASSE CRUD
 * @author grupo 10
 * @version CRUD v1.0
 * 
 * Estrutura: 
 * 4 bytes iniciais: Ultimo ID registrado.
 * 
 * Byte Lapide
 * Int Tamanho Registro
 * Int ID
 * Byte[] Array de Bytes
 */

import java.lang.reflect.Constructor;
import java.io.RandomAccessFile;
import java.io.IOException;

public class CRUDIndexado<T extends Registro> {

  // Atributos
  Constructor<T> construtor;
  String nomeDoArquivo;
  RandomAccessFile arquivo;
  HashExtensivel indexDireto;
  ArvoreBMais indexIndireto;

  // Métodos

  /**
   * Construtor CRUD genérico
   * 
   * @param construtor    Construtor da classe de arquivos
   * @param nomeDoArquivo
   * @throws IOException
   */
  public CRUDIndexado(Constructor<T> construtor, String nomeDoArquivo) throws IOException {
    this.nomeDoArquivo = nomeDoArquivo;
    this.construtor = construtor;
    arquivo = new RandomAccessFile("dados/" + nomeDoArquivo + ".db", "rw");
    try {
      indexDireto = new HashExtensivel(4, "dados/" + nomeDoArquivo + ".diretorio.idx",
          "dados/" + nomeDoArquivo + ".cestos.idx");
      indexIndireto = new ArvoreBMais(5, "dados/" + nomeDoArquivo + ".arvorebmais.idx");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (arquivo.length() < 4) {
      arquivo.writeInt(0); // escreve o novo cabeçalho
    }
  }

  /**
   * Escreve o objeto no final do arquivo
   * 
   * @param objeto Objeto que será inserido no arquivo
   * @return (int) Id do Objeto
   * @throws IOException
   */
  public int create(T objeto) throws IOException {

    arquivo.seek(0); // inicio do arquivo
    int id = arquivo.readInt() + 1; // leitura do ultimo id no cabeçalho
    objeto.setID(id);

    try {

      long pos = arquivo.length();
      arquivo.seek(pos); // vai para o final do arquivo

      byte ba[] = objeto.toByteArray(); // array de bytes (registro)
      arquivo.writeByte(0);
      arquivo.writeInt(ba.length); // escreve tamanho do registro
      arquivo.write(ba); // escreve registro

      arquivo.seek(0); // volta pro inicio do arquivo
      arquivo.writeInt(id); // atualiza o cabeçalho

      indexDireto.create(objeto.getID(), pos); // atualiza indice direto [ID, posição]
      indexIndireto.create(objeto.chaveSecundaria(), objeto.getID()); // atualiza indice indireto [chave secundaria, ID]

    } catch (Exception e) {
      //System.out.println(e.getMessage());
    }

    return id;
  }

  /**
   * Procura um registro através do ID
   * 
   * @param idChave Id de busca
   * @return (T) Objeto buscado
   * @throws Exception
   */
  public T read(int idChave) throws Exception {
    T objeto = construtor.newInstance(); // Cria objeto

    try {
      long pos = indexDireto.read(idChave); // posição do objeto

      if (pos == -1) {
        objeto = null; // caso não tenha encontrado o registro
        throw new Exception("ERRO! Registro não encontrado\n");
      }

      arquivo.seek(pos); // vai para a posição no arquivo
      byte lapide = arquivo.readByte(); // leitura da lápide

      if (lapide == 1) {
        System.out.println("ERRO! Registro Excluído!\n"); // Verifica se o registro foi excluído
      }

      int tamCampo = arquivo.readInt(); // Leitura do tamanho do registro
      byte[] ba = new byte[tamCampo];
      arquivo.read(ba); // leitura do registro em bytes
      objeto.fromByteArray(ba); // criação do objeto

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return objeto;
  }

  /**
   * Procura um registro dada a Chave secundaria dele
   * 
   * @param chaveSecundaria String que será buscada no Index
   * @return objeto criado
   * @throws Exception Se houver algum erro na busca
   */
  public T read(String chaveSecundaria) throws Exception {
    try {
      int ID = indexIndireto.read(chaveSecundaria);
      if (ID != -1)
        return read(ID);
    } catch (Exception e) {
    }

    return null;
  }

  /**
   * Atualiza os dados do registro
   * 
   * @param objeto Objeto que será atualizado
   * @return (boolean) Booleano indicando sucesso da operação
   * @throws IOException
   */
  public boolean update(T objetoNovo) throws IOException {
    boolean success = false; // controle

    try {
      long pos = indexDireto.read(objetoNovo.getID()); // posição de atualização

      if (pos != -1) {
        arquivo.seek(pos); // vai para posição no arquivo

        byte lapide = arquivo.readByte(); // leitura da lápide
        int tamAntigo = arquivo.readInt(); // leitura do tamanho do registro antigo
        byte[] baAntigo = new byte[tamAntigo];
        arquivo.read(baAntigo); // leitura do registro em bytes

        T objetoAntigo = construtor.newInstance();
        objetoAntigo.fromByteArray(baAntigo); // criação do objeto

        byte baNovo[] = objetoNovo.toByteArray();
        int tamNovo = baNovo.length; // tamanho do novo registro

        if (tamAntigo < tamNovo) { // Verifica se o tamanho do antigo é menor do que o do novo registro
          arquivo.seek(pos);
          arquivo.writeByte(1);

          pos = arquivo.length();
          arquivo.seek(pos); // vai para o final do arquivo

          // byte ba[] = objetoNovo.toByteArray(); // array de bytes (registro)
          arquivo.writeByte(0);
          arquivo.writeInt(baNovo.length); // escreve tamanho do registro
          arquivo.write(baNovo); // escreve registro

          indexDireto.update(objetoNovo.getID(), pos);
          success = true;

        } else {
          arquivo.seek(pos + 5);
          // arquivo.writeInt(baNovo.length);
          arquivo.write(baNovo);

          success = true;
        }

        if (!objetoNovo.chaveSecundaria().equals(objetoAntigo.chaveSecundaria())) {
          indexIndireto.delete(objetoAntigo.chaveSecundaria());
          indexIndireto.create(objetoNovo.chaveSecundaria(), objetoNovo.getID());
        }

      } else {
        System.out.println("Registro não encontrado!");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return success;
  }

  /**
   * Excluí, mudando a lápide, o registro
   * 
   * @param ID ID do registro a ser excluído
   * @return (boolean) booleano indicando o sucesso da operação
   * @throws IOException
   */
  public boolean delete(int ID) throws IOException {

    boolean success = false; // controle
    try {

      long pos = indexDireto.read(ID);
      if (pos == -1)
        return false;

      arquivo.seek(pos); // posição para excluír registro
      arquivo.writeByte(1); // muda lápide

      T objeto = read(ID);
      indexDireto.delete(ID);
      indexIndireto.delete(objeto.chaveSecundaria());

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return success;
  }

}