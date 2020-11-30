import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

    // Arquivo declarado fora de main() para poder ser usado por outros métodos
    private static CRUDIndexado<Usuario> arqUsuarios;
    private static CRUDIndexado<Pergunta> arqPerguntas;
    private static CRUDIndexado<Resposta> arqRespostas;
    private static CRUDIndexado<Voto> arqVotos;
    private static CRUDIndexado<Comentario> arqComentarios;
    private static ArvoreBMaisIntInt indicePerguntasUsuarios;
    private static ArvoreBMaisIntInt indicePerguntasRespostas;
    private static ArvoreBMaisIntInt indiceUsuariosRespostas;
    private static ArvoreBMaisIntInt indicePerguntasComentarios;
    private static ArvoreBMaisIntInt indiceRespostasComentarios;
    private static ListaInvertida indicePalavrasChave;

    // Instanciamento de objetos úteis para o programa
    private static Scanner leitor = new Scanner(System.in);
    private static InputStreamReader isr = new InputStreamReader(System.in);
    private static BufferedReader br = new BufferedReader(isr);

    // --------------------------- MENU INICIAL ------------------------------------

    /**
     * Menu Inicial do programa
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        try {

            File d = new File("dados"); // cria a pasta dados se não existir
            if (!d.exists())
                d.mkdir();

            // Abre (cria) os arquivos de usuarios, perguntas e os indices
            arqUsuarios = new CRUDIndexado<>(Usuario.class.getConstructor(), "usuarios");
            arqPerguntas = new CRUDIndexado<>(Pergunta.class.getConstructor(), "perguntas");
            arqRespostas = new CRUDIndexado<>(Resposta.class.getConstructor(), "respostas");
            arqVotos = new CRUDIndexado<>(Voto.class.getConstructor(),"votos");
            arqComentarios = new CRUDIndexado<>(Comentario.class.getConstructor(),"comentarios");
            indicePerguntasUsuarios = new ArvoreBMaisIntInt(20, "dados/PerguntasUsuarios.idx");
            indicePerguntasRespostas = new ArvoreBMaisIntInt(20, "dados/PerguntasRespostas.idx");
            indiceUsuariosRespostas = new ArvoreBMaisIntInt(20, "dados/UsuariosRespostas.idx");
            indiceRespostasComentarios = new ArvoreBMaisIntInt(20,"dados/RespostasComentarios.idx");
            indicePerguntasComentarios = new ArvoreBMaisIntInt(20,"dados/PerguntasComentarios.idx");
            indicePalavrasChave = new ListaInvertida(10, "dados/dicionario.idx", "dados/blocos.idx");

            System.out.println("\n======================\n   PROJETO USUÁRIOS\n======================");
            byte op = -1;

            while (op != 0) {
                System.out.println(
                        "\n||||| ACESSO |||||||\n\n1) Login\n2) Crie sua conta\n3) Esqueci minha senha\n\n0) Encerrar");
                System.out.print("\nOperacao: ");

                op = leitor.nextByte(); // leitura da operação

                switch (op) {
                    case 1:
                        Login();
                        break;

                    case 2:
                        Cadastro();
                        break;

                    case 3:
                        EsqueciMinhaSenha();
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("\nERRO! valor invalido!");
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método Cadastro de novo Usuário
     */
    public static void Cadastro() {

        System.out.println("\n|||||||| CADASTRO ||||||||");
        System.out.println("\n       NOVO USUARIO");
        System.out.print("\nEmail: ");

        String email = "";

        Usuario user = new Usuario(); // criação do objeto

        try {
            email = br.readLine().toLowerCase(); // leitura do email do usuário
            if (email != "" && Biblioteca.isEmail(email)) { // verifica se o email não está vazio e é um email válido
                if (arqUsuarios.read(email) == null) {

                    System.out.println("\nEMAIL VÁLIDO! Insira os dados a seguir");
                    user.setEmail(email); // define o email do usuário

                    System.out.print("\nNome de Usuario: ");
                    user.setNome(br.readLine()); // leitura do nome do usuário

                    System.out.print("Senha: ");
                    user.setSenha(leitor.next()); // leitura da senha do usuário

                    byte op = -1;
                    do {

                        System.out.println("\nCONFIRME OS DADOS\n" + user); // confirmação de dados
                        System.out.println("\n1) Confirmar\n2) Cancelar\n");
                        System.out.print("Operacao: ");

                        op = leitor.nextByte(); // operação de confirmação

                        if (op == 1) {
                            arqUsuarios.create(user); // cria registro do usuário no arquivo
                            
                            System.out.println("\nCADASTRO CONCLUÍDO!");
                        } else if (op == 2) {
                            System.out.println("\nCadastro cancelado!");

                        } else {
                            System.out.println("\nERRO! Operacao inválida!\n");
                        }

                    } while (op != 1 && op != 2);

                } else { // se já existir o email no arquivo
                    System.out.println("ERRO! Email já cadastrado.");

                }
            } else { // se o email for inválido
                System.out.println("ERRO! Email inválido!");

            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    static int IDLogado = -1; // ID do usuário logado declarado globalmente para facilitar operações

    /**
     * Método para efetuar o login no sistema
     */
    public static void Login() {

        System.out.println("\n|||||||| LOGIN ||||||||");
        System.out.print("\nEmail: ");

        try {

            String email = br.readLine().toLowerCase(); // leitura do email
            Usuario user = arqUsuarios.read(email); // recupera usuário a partir do email
            boolean success = false;

            if (user != null && user.getEmail().equals(email)) {
                // System.out.println("\nEmail Válido!");
                int i = 3; // quantidade de tentativas disponíveis

                do {
                    System.out.print("\nSenha: ");
                    String senha = leitor.next(); // solicitação a senha

                    if (senha.compareTo(user.getSenha()) == 0) { // verifica se a senha digitada é igual a senha do usuário

                        System.out.print("\nSucesso! Entrando");
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".\n");
                        success = true;

                        System.out
                                .println("\n----------------------------\n     Bem vindo(a) " + user.getNome() + "!\n");

                        IDLogado = user.getID(); // define o ID global como o ID do usuário logado
                        MenuInicial(); // Chama a função da próxima página do sistema

                    } else {
                        i--;
                        System.out.println("\nERRO! Senha Inválida!");
                        if (i > 0)
                            System.out.println(i + " tentativa(s) restante(s).");
                    }
                } while (i > 0 && success == false);
            } else {
                if (!Biblioteca.isEmail(email))
                    System.out.println("\nERRO! Email inválido!");
                else System.out.println("\nERRO! Email não cadastrado.");
                
                System.out.print("\nDigite enter para continuar...");
                br.read();
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Caso o usuário esqueça a senha
     */
    public static void EsqueciMinhaSenha() {

        System.out.println("\n|||||| ESQUECI MINHA SENHA ||||||");
        System.out.println("\nDigite seu email para enviarmos uma senha temporária. ");
        System.out.print("\nEmail: ");

        try {
            String email = br.readLine(); // leitura do email para enviar a nova senha
            Usuario user = arqUsuarios.read(email); // recupera o usuário

            if (user != null) { // se encontrar o usuário no arquivo
                System.out.print("\nEnviando email com nova senha");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                System.out.println("\n\nEMAIL ENVIADO!");
            } else {
                System.out.println("\nEmail não cadastrado!");
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ----------------------------- MENU INICIO -----------------------------------

    static int notificacoes = 0;

    /**
     * Menu inicial depois do Login
     */
    public static void MenuInicial() {

        int op = -1;

        while (op != 9) {

            System.out.println("\n         INICIO      ");
            System.out.println("\n1) Criação de Perguntas\n2) Consultar/Responder Perguntas");
            System.out.print("3) Notificacoes: " + notificacoes);

            System.out.println("\n\n0) Encerrar\n9) Voltar");

            System.out.print("\nOperacao: ");

            op = leitor.nextByte();

            switch (op) {
                case 1:
                    MenuCriacao();
                    break;

                case 2:
                    MenuRespostas();
                    break;

                // case 3: Notificacoes();
                // break;

                case 0:
                    System.exit(0);

                case 9:
                    break;

                default:
                    System.out.println("\nERRO! valor invalido!");
                    break;
            }
        }
    }

    // --------------------- CRIAÇÃO DE PERGUNTAS --------------------------------
    /**
     * Menu de Criação de novas perguntas
     */
    public static void MenuCriacao() {

        int op = -1;
        while (op != 9) {

            System.out.println("\n        INICIO > CRIAÇÃO DE PERGUNTAS      ");
            System.out.println("\n1) Listar\n2) Incluir\n3) Alterar\n4) Arquivar");

            System.out.println("\n0) Encerrar\n9) Voltar");

            System.out.print("\nOperacao: ");

            op = leitor.nextByte();

            switch (op) {
                case 1:
                    ListarPerguntas();
                    break;

                case 2:
                    IncluirPerguntas();
                    break;

                case 3:
                    AlterarPerguntas();
                    break;

                case 4:
                    ArquivarPerguntas();
                    break;

                case 9:
                    break;

                case 0:
                    System.exit(0);

                default:
                    System.out.println("\nERRO! valor invalido!");
                    break;
            }
        }
    }

    /**
     * Lista as perguntas criadas pelo usuário
     */
    public static void ListarPerguntas() {

        System.out.println("\n----------- LISTAGEM -------------");
        try {
            int[] indices = indicePerguntasUsuarios.read(IDLogado); // recupera os IDs das perguntas criadas pelo usuário
            if (indices.length == 0) { // caso não tenha nenhum ID registrado
                System.out.println("Nenhuma pergunta cadastrada!");

            } else {

                // pra cada ID gera uma pergunta
                for (int i = 0; i < indices.length; i++) {
                    Pergunta p = arqPerguntas.read(indices[i]);

                    if (p.getAtiva() == false) { // Se ela estiver arquivada
                        System.out.print("\n-----| ARQUIVADA |-----");
                    }

                    // Escreve a pergunta na tela
                    System.out.print("\nQUESTÃO N." + (i + 1));
                    System.out.println(p.toString());
                }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inclui novas perguntas
     */
    public static void IncluirPerguntas() {

        long time = System.currentTimeMillis() - 10800000; // Hora de criação da pergunta

        System.out.println("\n----------- INCLUSÃO -------------\n");
        System.out.print("\nPergunta: ");

        try {
            String pergunta = br.readLine(); // leitura da pergunta

            System.out.print("\nDigite as palavras chave da sua pergunta, separadas por (;): ");
            String pc = Biblioteca.toKeyWord(br.readLine());

            if ((!pergunta.equals("")) && (!pc.equals(""))) { // se a pergunta não estiver vazia

                Pergunta question = new Pergunta(); // criação da pergunta

                System.out.println("\nPergunta Válida!\n");
                question.setPergunta(pergunta); // define o atributo pergunta
                question.setPalavrasChave(pc); // define o atributo palavras chave

                System.out.println("Pergunta: " + pergunta);
                System.out.println("Palavras Chaves: " + pc);

                int op = -1;
                while (op != 1 && op != 2) {
                    System.out.println("\nConfirmar pergunta?");

                    System.out.println("\n1) Sim\n2) Não");
                    System.out.print("\nOperação: ");
                    op = leitor.nextInt();
                    if (op == 1) {
                        question.setNota((short) 0); // define o atributo nota
                        question.setCriacao(time); // define o atributo criação
                        question.setIDUsuario(IDLogado); // devine o atributo id do usuario

                        String[] keyWords = pc.split(";"); // separa as palavras chave

                        int idPergunta = arqPerguntas.create(question); // cria a pergunta no arquivo
                        indicePerguntasUsuarios.create(IDLogado, idPergunta); // cria a relação pergunta : usuario

                        for (int i = 0; i < keyWords.length; i++) {
                            indicePalavrasChave.create(keyWords[i], idPergunta); // cria a relação palavra chave : pergunta
                        }

                        System.out.println("\nPERGUNTA CADASTRADA!");

                    } else if (op == 2) {
                        System.out.println("Operação Cancelada.");

                    } else
                        System.out.println("Opção Inválida!");
                }
            } else {
                System.out.println("\nERRO! Pergunta ou palavras-chave vazias!");
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Altera perguntas já existentes
     */
    public static void AlterarPerguntas() {

        System.out.println("\n----------- ALTERAR -------------\n");
        try {

            int[] indices = indicePerguntasUsuarios.read(IDLogado); // recupera os IDs registrados pelo usuário logado
            Pergunta p;
            Pergunta ativas[] = new Pergunta[indices.length];

            for (int i = 0, j = 0; i < indices.length; i++) {
                p = arqPerguntas.read(indices[i]);

                if (p.getAtiva() == true) {
                    ativas[j] = p;
                    j++;
                }
            }
            // Pergunta p;

            if (indices.length == 0 || ativas[0] == null) { // se não houver nenhuma pergunta
                System.out.println("Nenhuma pergunta cadastrada!");

            } else {

                // escreve as perguntas na tela através dos IDs
                for (int i = 0; i < ativas.length; i++) {

                    System.out.print("\nQUESTÃO N." + (i + 1));
                    System.out.println(ativas[i]);

                }

                String aux = "";
                int op = -1;

                System.out.println("\n0) Voltar");
                System.out.print("\nInforme o número da questão que deseja modificar: ");

                op = leitor.nextInt() - 1;

                // erro se o usuário digitar um valor fora do domínio
                while (op < -1 && op >= ativas.length) {

                    System.out.print("ERRO: Pergunta inexistente. Por favor, informe uma operação válida: ");
                    op = leitor.nextInt() - 1;
                }

                if (op != -1) {
                    System.out.println("\nPergunta que deseja modificar:");

                    p = ativas[op]; // criação da pergunta através da posição escolhida pelo usuário
                    System.out.println(p);

                    System.out.print("\nNOVA PERGUNTA: ");
                    String novaPergunta = br.readLine();

                    if (novaPergunta.equals("")) { // se a pergunta estiver vazia
                        System.out.println("ERRO! Pergunta vazia.");

                    } else { // confirmação da alteração da pergunta

                        System.out.println("\nDeseja alterar as palavras chave também?");
                        System.out.println("\n1) Sim\n2) Não");
                        System.out.print("\nOperação: ");
                        int cf = leitor.nextInt();

                        while (cf != 1 && cf != 2) {

                            System.out.println("ERRO! Operação Inválida!");

                            System.out.println("\nDeseja alterar as palavras chave também?");
                            System.out.println("\n1) Sim\n2) Não");

                            cf = leitor.nextInt();
                        }

                        if (cf == 1) {

                            System.out.print("\nDigite as palavras chave da nova pergunta, separadas por (;): ");
                            aux = Biblioteca.toKeyWord(br.readLine());

                            while (aux == "") {
                                System.out.println("ERRO! Nenhuma palavra escrita!");

                                System.out.print("\nDigite as palavras chave da nova pergunta, separadas por (;): ");
                                aux = Biblioteca.toKeyWord(br.readLine());
                            }

                            System.out.println("\nDeseja confirmar a atualização da pergunta e das palavras chave?");
                            System.out.println("\nPergunta alterada: " + novaPergunta);
                            System.out.println("Palavras chave atualizadas: " + aux);
                            System.out.println("\n1) Sim\n2) Não");
                            System.out.print("\nOperação: ");

                            cf = leitor.nextInt();

                            while (cf != 1 && cf != 2) {
                                System.out.print("ERRO: Operação inválida! Você deseja confirmar a atualização? ");
                                cf = leitor.nextInt();
                            }

                            if (cf == 1) {
                                // palavras chave antigas
                                String[] palavrasChaves = p.getPalavrasChave().split(";"); // recupera as palavras chave

                                for (int i = 0; i < palavrasChaves.length; i++) {
                                    indicePalavrasChave.delete(palavrasChaves[i], p.getID()); // apaga cada palavra chave
                                }

                                // palavras chave atuais
                                String[] keyWords = aux.split(";");
                                for (int i = 0; i < keyWords.length; i++) {
                                    indicePalavrasChave.create(keyWords[i], p.getID()); // cria a relação palavra chave : pergunta
                                }

                                p.setPergunta(novaPergunta); // muda a pergunta
                                p.setPalavrasChave(aux); // muda as palavras chave
                                // arqPerguntas.update(p); // atualiza o arquivo

                                if (arqPerguntas.update(p)) {
                                    System.out.println("Alteração realizada com sucesso!");
                                } else {
                                    System.out.println("Erro na alteração!");
                                }

                            } else if (cf == 2) {
                                System.out.println("Operação Cancelada.");
                            }
                        } else {

                            System.out.println("\nDeseja confirmar a alteração da pergunta?");
                            System.out.println("\nPergunta alterada: " + novaPergunta);
                            System.out.println("\n1) Sim\n2) Não");
                            System.out.print("\nOperação: ");

                            cf = leitor.nextInt();

                            while (cf != 1 && cf != 2) {
                                System.out.print("ERRO: Operação inválida! Você deseja confirmar a atualização? ");
                                cf = leitor.nextInt();
                            }

                            if (cf == 1) {
                                p.setPergunta(novaPergunta); // muda a pergunta
                                // arqPerguntas.update(p); // atualiza o arquivo

                                if (arqPerguntas.update(p)) {
                                    System.out.println("Alteração realizada com sucesso!");
                                } else {
                                    System.out.println("Erro na alteração!");
                                }
                            } else {
                                System.out.println("Operação Cancelada.");
                            }
                        }
                    }
                }
            }
            
            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Arquiva perguntas
     */
    public static void ArquivarPerguntas() {

        System.out.println("\n------------ARQUIVAR--------------\n");

        try {
            int[] indices = indicePerguntasUsuarios.read(IDLogado); // recupera os IDs das perguntas criadas pelo usuário

            Pergunta p;
            Pergunta ativas[] = new Pergunta[indices.length];

            for (int i = 0, j = 0; i < indices.length; i++) {
                p = arqPerguntas.read(indices[i]);

                if (p.getAtiva() == true) {
                    ativas[j] = p;
                    j++;
                }
            }

            if (indices.length == 0 || ativas[0] == null) { // se nenhuma pergunta estiver sido cadastrada
                System.out.println("Nenhuma pergunta cadastrada!");

            } else {

                // escreve as perguntas na tela
                for (int i = 1; i <= ativas.length; i++) {

                    System.out.print("\nQUESTÃO N." + i);
                    System.out.println(ativas[i - 1]);

                }

                int op = -1;

                System.out.println("\n0) Voltar");
                System.out.print("\nInforme o número da questão que deseja arquivar: ");

                op = leitor.nextInt() - 1;

                while (op < -1 || op > ativas.length) {

                    System.out.print("ERRO: Pergunta inexistente. Por favor, informe uma operação válida: ");
                    op = leitor.nextInt() - 1;
                }

                if (op != -1) {
                    System.out.println("\nPergunta que deseja arquivar:");

                    // escreve a pergunta que o usuário escolheu
                    p = arqPerguntas.read(indices[op]);
                    System.out.println(p);

                    int cf = -1;

                    System.out.println("\nConfirmar arquivamento da pergunta.");

                    System.out.println("\n1) Sim\n2) Não");
                    System.out.print("Operação: ");
                    cf = leitor.nextInt();

                    if (cf == 1) {
                        p.setAtiva(false); // define a pergunta como inativa
                        // arqPerguntas.update(p); // atualiza o registro no arquivo

                        String[] palavrasChaves = p.getPalavrasChave().split(";"); // recupera as palavras chave

                        for (int i = 0; i < palavrasChaves.length; i++) {
                            indicePalavrasChave.delete(palavrasChaves[i], p.getID()); // apaga cada palavra chave
                        }

                        if (arqPerguntas.update(p) == true) {
                            System.out.println("Pergunta arquivada!");
                        } else
                            System.out.println("Erro ao arquivar.");

                    } else if (cf == 2) {
                        System.out.println("Operação Cancelada.");
                    } else {
                        System.out.println("ERRO! Operação inválida!");
                    }
                }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ------------------ CONSULTA/RESPOSTA DE PERGUNTAS
    // -----------------------------------

    static int idPergunta = -1;

    public static void MenuRespostas() {

        System.out.println("\n        INICIO > PERGUNTAS      ");
        System.out.println("\nBusque as perguntas por palavra chave separadas por ponto e vírgula (;)");
        System.out.println("\nEXEMPLO: Java;DesenvolvimentoWEB;GCC");

        System.out.print("\nPalavras Chave: ");

        try {
            String[] busca = Biblioteca.toKeyWord(br.readLine()).split(";");

            int[] IDSPerguntas = indicePalavrasChave.read(busca[0]);

            for (int i = 1; i < busca.length; i++) {
                int[] aux = indicePalavrasChave.read(busca[i]);
                IDSPerguntas = Biblioteca.intersecao(IDSPerguntas, aux);
            }

            Pergunta perguntas[] = new Pergunta[IDSPerguntas.length];

            for (int i = 0; i < perguntas.length; i++) {
                perguntas[i] = arqPerguntas.read(IDSPerguntas[i]);
            }

            perguntas = Biblioteca.ordenacao(perguntas.length, perguntas);

            if (perguntas.length > 0) {

                System.out.println("\n\n||| PERGUNTAS ENCONTRADAS |||");
                for (int i = 0, j = 1; i < perguntas.length; i++, j++) {
                    System.out.print("\nQUESTÃO N." + j);
                    System.out.println(perguntas[i]);
                }

                System.out.println("\n0) Voltar");
                System.out.print("\nSelecione uma pergunta: ");

                int op = leitor.nextInt() - 1;

                while (op < -1 || op >= perguntas.length) {
                    System.out.println("ERRO! Operação Inválida!");

                    System.out.print("\nSelecione uma pergunta: ");
                    op = br.read() - 1;
                }

                Pergunta p = perguntas[op];

                Usuario user = arqUsuarios.read(p.getIDUsuario());
                idPergunta = p.getID();

                // while (op < 0 && op > 3){

                System.out.println("\n        INICIO > RESPOSTAS      ");
                System.out.println(
                        "\n\n" + p.getPergunta().toUpperCase() + "\n-----------------------------------------");
                System.out.println("Criada em " + p.getCriacao() + " por " + user.getNome() + ".");
                System.out.println("Palavras Chaves: " + p.getPalavrasChave().replaceAll(";"," "));
                System.out.println("Nota: " + p.getNota());

                System.out.println("\nCOMENTÁRIOS\n-------------------------\n");

                int IDSPerguntasComentarios[] = indicePerguntasComentarios.read(idPergunta);
                Comentario[] comentariosPerguntas = new Comentario[IDSPerguntasComentarios.length];

                for (int i = 0; i < comentariosPerguntas.length; i++){
                    comentariosPerguntas[i] = arqComentarios.read(IDSPerguntasComentarios[i]);
                    Comentario c = comentariosPerguntas[i];
                    Usuario user1 = arqUsuarios.read(c.getIDUsuario());

                    System.out.println(c.getComentario());
                    System.out.println("Comentario feito em " + c.getCriacao() + " por " + user1.getNome() + "\n");
                }

                System.out.println("\nRESPOSTAS\n-------------------------");

                int respostas[] = indicePerguntasRespostas.read(idPergunta);
                Resposta[] respostasAtivas = new Resposta[respostas.length];

                for (int i = 0, j =0; i < respostas.length; i++){
                    Resposta r = arqRespostas.read(respostas[i]);
                    if (r.getAtiva() == true){
                        respostasAtivas[j] = r;
                        j++;
                    }
                }
                
                for (int i = 0; i < respostasAtivas.length; i++) {

                    Resposta r = respostasAtivas[i];
                    Usuario user1 = arqUsuarios.read(r.getIDUsuario());

                    System.out.print("\nN." + (i+1));
                    System.out.print("\n" + r.getResposta());
                    System.out.println("\nRespondida em " + r.getCriacao() + " por " + user1.getNome() + "\nNota: " + r.getNota() + "\n");

                    int[] IDSRespostasComentarios = indiceRespostasComentarios.read(r.getID());
                    Comentario[] comentariosRespostas = new Comentario[IDSRespostasComentarios.length];

                    for (int j = 0; j < comentariosRespostas.length; j++){
                        comentariosRespostas[j] = arqComentarios.read(IDSRespostasComentarios[j]);
                        Comentario c = comentariosRespostas[j];
                        Usuario user2 = arqUsuarios.read(c.getIDUsuario());
                        
                        System.out.println("\t" + c.getComentario());
                        System.out.println("\tComentario feito em " + c.getCriacao() + " por " + user2.getNome() + "\n");
                    }
                }

                do {

                    System.out.println("\n1) Responder\n2) Comentar\n3) Avaliar\n\n0) Voltar");

                    System.out.print("\nOperação: ");
                    op = leitor.nextInt();

                    switch (op) {
                        case 0:
                            break;

                        case 1:
                            Responder();
                            break;

                        case 2: Comentar(respostasAtivas);
                            break;

                        case 3: Avaliar(respostasAtivas);
                        break;

                        default:
                            System.out.println("\nERRO! Valor Inválido!");
                    }
                } while (op < 0 || op > 3);
            } else {
                System.out.println("\nNenhuma pergunta encontrada!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Responder() {

        int op = -1;
        while (op != 0) {

            System.out.println("\n        INICIO > PERGUNTAS > RESPONDER      ");

            try {
                Pergunta p = arqPerguntas.read(idPergunta);
                Usuario user = arqUsuarios.read(p.getIDUsuario());

                System.out.println(
                        "\n\n" + p.getPergunta().toUpperCase() + "\n-----------------------------------------");
                System.out.println("Criada em " + p.getCriacao() + " por " + user.getNome() + ".");
                System.out.println("Palavras Chaves: " + p.getPalavrasChave());
                System.out.println("Nota: " + p.getNota() + "\n");

                System.out.println(
                        "\n1) Listar suas respostas\n2) Incluir nova resposta\n3) Alterar uma resposta\n4) Arquivar uma resposta\n\n0) Retornar ao menu anterior");

                System.out.print("\nOperação: ");
                op = leitor.nextInt();

                switch (op) {
                    case 1:
                        ListarRespostas();
                        break;

                    case 2:
                        IncluirRespostas();
                        break;

                    case 3:
                        AlterarRespostas();
                        break;

                    case 4:
                        ArquivarRespostas();
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("\nERRO! valor invalido!");
                        break;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void ListarRespostas() {

        try {
            System.out.println("\n\n--------------- LISTAR RESPOSTAS ---------------");

            int indices[] = indicePerguntasRespostas.read(idPergunta);
            Resposta respostasUsuario[] = new Resposta[indices.length];

            if (indices.length == 0) { // caso não tenha nenhum ID registrado
                System.out.println("Nenhuma resposta cadastrada!");

            } else {

                for (int i = 0, j = 0; i < indices.length; i++) {

                    Resposta r = arqRespostas.read(indices[i]);

                    if (r.getIDUsuario() == IDLogado) {
                        respostasUsuario[j] = r;
                        j++;
                    }
                }

                // pra cada ID gera uma pergunta
                for (int i = 0; i < respostasUsuario.length; i++) {

                    Usuario user = arqUsuarios.read(IDLogado);

                    if (respostasUsuario[i].getAtiva() == false) { // Se ela estiver arquivada
                        System.out.print("\n-----| ARQUIVADA |-----");
                    }

                    if (respostasUsuario[i] != null) {
                        // Escreve a resposta na tela
                        System.out.print("\nN." + (i + 1) + "\n" + respostasUsuario[i].getResposta());
                        System.out.println("\nRespondida em " + respostasUsuario[i].getCriacao() + " por "
                                + user.getNome() + "\nNota: " + respostasUsuario[i].getNota());
                    }
                }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void IncluirRespostas() {

        long time = System.currentTimeMillis() - 10800000; // Hora de criação da pergunta

        try {
            System.out.println("\n\n--------------- INCLUIR RESPOSTA ---------------");
            System.out.print("\nResposta: ");
            String resposta = br.readLine();

            if (!resposta.equals("")) {

                int op = -1;
                while (op != 1 && op != 2) {

                    System.out.println("\nConfirmar resposta?");

                    System.out.println(resposta);
                    System.out.println("\n1) Sim\n2) Não");

                    System.out.print("\nOperação: ");
                    op = leitor.nextByte();

                    if (op == 1) {
                        Resposta resp = new Resposta();
                        resp.setResposta(resposta);

                        resp.setNota((short) 0); // define o atributo nota
                        resp.setCriacao(time); // define o atributo criação
                        resp.setIDUsuario(IDLogado); // define o atributo id Usuario
                        resp.setIDPergunta(idPergunta); // define o atributo ID Pergunta

                        int id = arqRespostas.create(resp);

                        indicePerguntasRespostas.create(idPergunta, id);
                        indiceUsuariosRespostas.create(IDLogado, id);

                        System.out.println("\nRESPOSTA INCLUIDA!");

                    } else if (op == 2) {
                        System.out.println("\nInclusão cancelada!");
                    } else
                        System.out.println("\nOperação Inválida!");
                }
            } else {
                System.out.println("\nPergunta vazia!");
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void AlterarRespostas() {

        try {
            System.out.println("\n\n--------------- ALTERAR RESPOSTA ---------------\n");

            int indices[] = indicePerguntasRespostas.read(idPergunta);

            if (indices.length == 0) { // caso não tenha nenhum ID registrado
                System.out.println("Nenhuma resposta cadastrada!");

            } else {

                Resposta respostasUsuario[] = new Resposta[indices.length];

                for (int i = 0, j = 0; i < indices.length; i++) {

                    Resposta r = arqRespostas.read(indices[i]);
                    if (r.getIDUsuario() == IDLogado && r.getAtiva()) {
                        respostasUsuario[j] = r;
                        j++;
                    }
                }

                // pra cada ID gera uma pergunta
                for (int i = 0; i < respostasUsuario.length; i++) {
                    Resposta r = respostasUsuario[i];
                    Usuario user = arqUsuarios.read(IDLogado);

                    // Escreve a pergunta na tela
                    System.out.print("N." + (i + 1) + "\n" + r.getResposta());
                    System.out.println(
                            "\nRespondida em " + r.getCriacao() + " por " + user.getNome() + "\nNota: " + r.getNota());

                }

                System.out.println("\n0) Voltar");
                System.out.print("\nSelecione uma resposta: ");

                int op = leitor.nextInt() - 1;

                while (op < -1 || op >= indices.length) {
                    System.out.println("ERRO! Operação Inválida!");

                    System.out.print("\nSelecione uma pergunta: ");
                    op = leitor.nextByte() - 1;
                }

                if (op != -1) {

                    Resposta resp = respostasUsuario[op];

                    System.out.println(resp);
                    System.out.print("\nNova resposta: ");

                    String novaResposta = br.readLine();

                    if (!novaResposta.equals("")) {

                        System.out.println("Confirmar alteração?");
                        System.out.println("\n1) Sim\n2) Não");

                        System.out.print("Operação: ");
                        op = leitor.nextByte();

                        if (op == 1) {

                            resp.setResposta(novaResposta);
                            if (arqRespostas.update(resp) == true) {
                                System.out.println("\nRESPOSTA ALTERADA!");
                            } else
                                System.out.println("ERRO! Resposta não foi alterada");

                        } else if (op == 2) {
                            System.out.println("Alteração cancelada!");

                        } else
                            System.out.println("ERRO! Valor inválido");

                    } else {
                        System.out.println("ERRO! Resposta vazia!");
                    }
                }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void ArquivarRespostas() {

        try {
            System.out.println("\n\n--------------- ARQUIVAR RESPOSTA ---------------\n");

            int indices[] = indicePerguntasRespostas.read(idPergunta);

            if (indices.length == 0) { // caso não tenha nenhum ID registrado
                System.out.println("Nenhuma resposta cadastrada!");

            } else {

                Resposta respostasUsuario[] = new Resposta[indices.length];

                for (int i = 0, j = 0; i < indices.length; i++) {

                    Resposta r = arqRespostas.read(indices[i]);
                    if (r.getIDUsuario() == IDLogado && r.getAtiva()) {
                        respostasUsuario[j] = r;
                        j++;
                    }
                }

                // pra cada ID gera uma pergunta
                for (int i = 0; i < respostasUsuario.length; i++) {
                    Resposta r = respostasUsuario[i];
                    Usuario user = arqUsuarios.read(IDLogado);

                    // Escreve a pergunta na tela
                    System.out.print("\nN." + (i + 1) + "\n" + r.getResposta());
                    System.out.println(
                            "\nRespondida em " + r.getCriacao() + " por " + user.getNome() + "\nNota: " + r.getNota());

                }

                System.out.println("\n0) Voltar");
                System.out.print("\nSelecione uma pergunta: ");

                int op = leitor.nextInt() - 1;

                while (op < -1 || op >= indices.length) {
                    System.out.println("ERRO! Operação Inválida!");

                    System.out.print("\nSelecione uma pergunta: ");
                    op = leitor.nextByte() - 1;
                }

                if (op != -1) {

                    Resposta resp = respostasUsuario[op];

                    System.out.println(resp);

                    System.out.println("Confirmar arquivamento?");
                    System.out.println("\n1) Sim\n2) Não");

                    System.out.print("Operação: ");
                    op = leitor.nextByte();

                    if (op == 1) {

                        resp.setAtiva(false);
                        if (arqRespostas.update(resp) == true) {
                            System.out.println("\nRESPOSTA ARQUIVADA!");

                            indicePerguntasRespostas.delete(idPergunta, resp.getID());
                            indiceUsuariosRespostas.delete(IDLogado, resp.getID());

                        } else System.out.println("ERRO! Resposta não foi arquivada");

                        
                    } else if (op == 2) {
                        System.out.println("Arquivamento cancelado!");

                    } else System.out.println("ERRO! Valor inválido");
                }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void Comentar(Resposta[] respostasAtivas){
        try {

            int op = -1;
        
            while (op < 0 || op > 2) {
                
                System.out.println("\n        INICIO > PERGUNTAS > COMENTAR     ");

                    System.out.println("\n1) Comentar Pergunta\n2) Comentar Resposta\n\n0) Voltar");

                    System.out.print("\nOperação: ");
                    op = leitor.nextInt();

                    switch (op) {
                        case 1: ComentarPergunta();
                                break;

                        case 2: ComentarResposta(respostasAtivas);
                                break;

                        case 0: break;

                        default: System.out.println("Valor Inválido");
                                 break;
                    }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void ComentarPergunta(){
        try {

            long time = System.currentTimeMillis() - 10800000; // Hora de criação da pergunta
            
            System.out.println("\n        INICIO > PERGUNTAS > COMENTAR > PERGUNTA      ");
            int op = -1;

            Comentario comentario = new Comentario();

            comentario.setIDUsuario(IDLogado);
            comentario.setIDPR(idPergunta);
            comentario.setTipo('P');

            System.out.println("\n------ COMENTE ------");
            System.out.println("\n0) Voltar");
            System.out.print("\nComentario: ");
            String comment = br.readLine();

            if (comment != "0" && comment != "") {
                    
                System.out.print("\n\nSEU COMENTÁRIO: " + comment);

                System.out.println("\n\nDESEJA CONFIRMAR O COMENTÁRIO?");

                System.out.println("\n1) Sim\n2) Não");
                System.out.print("\nOperação: ");

                op = leitor.nextInt();

                if (op == 1) {

                    comentario.setComentario(comment);
                    comentario.setCriacao(time);
                   
                    int IDComentario = arqComentarios.create(comentario);

                    Pergunta p = arqPerguntas.read(comentario.getIDPR());
                    indicePerguntasComentarios.create(p.getID(),IDComentario);

                    System.out.println("\nCOMENTÁRIO CONFIRMADO!");

                } else {
                    System.out.println("Operação Cancelada!");
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void ComentarResposta(Resposta[] respostasAtivas){
        try {
            
            long time = System.currentTimeMillis() - 10800000; // Hora de criação da pergunta

            System.out.println("\n        INICIO > PERGUNTAS > COMENTAR > RESPOSTA      ");            
            int numResposta = -1;
            Comentario comentario = new Comentario();

            System.out.println("\nDIGITE O NUMERO DA RESPOSTA\n\n0) Voltar");
            System.out.print("\nOperação: ");
            numResposta = leitor.nextInt() - 1;

            if (numResposta > -1 && numResposta < respostasAtivas.length) {
                comentario.setIDUsuario(IDLogado);
                comentario.setIDPR(respostasAtivas[numResposta].getID());
                comentario.setTipo('R');

                System.out.println("\n------ COMENTE ------");
                System.out.println("\n0) Voltar");
                System.out.print("\nComentário: ");

                String comment = br.readLine();

                if (comment != "0" && comment != "") {
                        
                    System.out.print("\n\nSEU COMENTÁRIO: " + comment);

                    System.out.println("\n\nDESEJA CONFIRMAR O COMENTÁRIO?");

                    System.out.println("\n1) Sim\n2) Não");
                    System.out.print("\nOperação: ");

                    int op = leitor.nextInt();

                    if (op == 1) {

                        comentario.setComentario(comment);
                        comentario.setCriacao(time);
                    
                        int IDComentario = arqComentarios.create(comentario);

                        Pergunta p = arqPerguntas.read(comentario.getIDPR());
                        indiceRespostasComentarios.create(p.getID(),IDComentario);

                        System.out.println("\nCOMENTÁRIO CONFIRMADO!");

                    } else {
                        System.out.println("Operação Cancelada!");
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void Avaliar(Resposta[] respostasAtivas) {
        
        int op = -1;
        
        try {
            while (op < 0 || op > 2) {
                
                System.out.println("\n        INICIO > PERGUNTAS > AVALIAR      ");

                    System.out.println("\n1) Votar na Pergunta\n2) Votar na Resposta\n\n0) Voltar");

                    System.out.print("\nOperação: ");
                    op = leitor.nextInt();

                    switch (op) {
                        case 1: VotoPergunta();
                                break;

                        case 2: VotoResposta(respostasAtivas);
                                break;

                        case 0: break;

                        default: System.out.println("Valor Inválido");
                                 break;
                    }
            }

            System.out.print("\nDigite enter para continuar...");
            br.read();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void VotoPergunta(){
        try {
            
            System.out.println("\n        INICIO > PERGUNTAS > AVALIAR > PERGUNTA      ");

            int op = -1;
            Voto voto = new Voto();

            voto.setIDUsuario(IDLogado);
            voto.setIDPR(idPergunta);
            voto.setTipo('P');

            if (arqVotos.read(voto.chaveSecundaria()) == null){
                System.out.println("\n------ AVALIE ------");
                System.out.println("\n1) GOSTEI\n2) NÃO GOSTEI\n\n0) Voltar");
                System.out.print("\nOperação: ");
                op = leitor.nextInt();
                
                if (op!=0) {
                    
                    System.out.print("\n\nSEU VOTO: ");
                    if (op == 1) System.out.print(" GOSTEI");
                    else if (op == 2) System.out.print(" NÃO GOSTEI");

                    System.out.println("\n\nDESEJA CONFIRMAR O VOTO?");

                    System.out.println("\n1) Sim\n2) Não");
                    System.out.print("\nOperação: ");

                    int conf = leitor.nextInt();

                    if (conf == 1) {
                        if (op == 1){
                            voto.setVoto(true);
                        } else if (op == 2){
                            voto.setVoto(false);
                        } else {
                            System.out.println("Operação cancelada!");
                        }
                            
                        voto.setIDUsuario(IDLogado);
                        arqVotos.create(voto);

                        Pergunta p = arqPerguntas.read(voto.getIDPR());
                        p.setNota(p.getNota() + 1);
                        arqPerguntas.update(p);

                        System.out.println("\nVOTO CONFIRMADO!");
                    } else System.out.println("\nOperação Cancelada");
                }

            } else {
                System.out.println("\nVocê já votou nessa Pergunta!");
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void VotoResposta(Resposta[] respostasAtivas){
        try {
        
            System.out.println("\n        INICIO > PERGUNTAS > AVALIAR > RESPOSTA      ");
            int numResposta = -1;
            Voto voto = new Voto();

            System.out.println("\nDIGITE O NUMERO DA RESPOSTA\n\n0) Voltar");
            System.out.print("\nOperação: ");
            numResposta = leitor.nextInt() - 1;

            if (numResposta > -1 && numResposta < respostasAtivas.length) {
                
                voto.setIDUsuario(IDLogado);
                voto.setIDPR(respostasAtivas[numResposta].getID());
                voto.setTipo('R');

                if (arqVotos.read(voto.chaveSecundaria()) == null){
                    System.out.println("\n------ AVALIE ------");
                    System.out.println("\n1) GOSTEI\n2) NÃO GOSTEI\n\n0) Voltar");
                    System.out.print("\nOperação: ");
                    int op = leitor.nextInt();
                    
                    if (op!=0) {
                        
                        System.out.print("\n\nSEU VOTO: ");
                        if (op == 1) System.out.print(" GOSTEI");
                        else if (op == 2) System.out.print(" NÃO GOSTEI");

                        System.out.println("\n\nConfirmar voto?");

                        System.out.println("\n1) Sim\n2) Não");
                        System.out.print("\nOperação: ");

                        int conf = leitor.nextInt();

                        if (conf == 1) {
                            if (op == 1){
                                voto.setVoto(true);
                            } else if (op == 2){
                                voto.setVoto(false);
                            } else {
                                System.out.println("Operação cancelada!");
                            }
                                
                            voto.setIDUsuario(IDLogado);
                            arqVotos.create(voto);

                            Resposta r = arqRespostas.read(voto.getIDPR());
                            if (voto.getVoto() == true) {
                                r.setNota(r.getNota() + 1);
                            } else r.setNota(r.getNota() - 1);

                            arqRespostas.update(r);

                            System.out.println("\nVOTO CONFIRMADO!");

                        } else System.out.println("\nOperação Cancelada");
                    }
                }

            } else {
                System.out.println("\nVocê já votou nessa Resposta!");
            }     

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}