# **PROJETO USUÁRIOS** #
## **GRUPO 10** - ALGORITIMOS E ESTRUTURAS DE DADOS III ##
*Lucas Bottrel*, *Fernanda Cirino*, *Vinicius Mendes* e *Juliana Granffild* 

## OVERVIEW ##

Esse projeto é um programa de perguntas e respostas, parecido com o StackOverflow. A ideia é que o usuário tenho um cadastro no programa e que ele crie suas perguntas, responda perguntas de outros usuários cadastrados no programa, comente e vote em perguntas e respostas relevantes. 

### Como foi o processo de desenvolvimentos ##

Foram em torno de 3 meses de desenvolvimento. Algumas tarefas foram mais simples que outras, mas todas foram refeitas algumas vezes buscando a segurança de dados e tratamento de casos e erros que poderiam (e ainda podem) ocorrer. Nosso maior desafio foi resolver problemas no CRUD e nos arquivos e indices, uma vez que essa organização causou alguns bugs e sobreposições de dados. 

### Aprendizado ###

Durante o processo de criação do programa e atualização, a palavra que define cada passo e cada etapa é "aprendizado". Muitas vezes tinhamos vontade de jogar tudo fora e recomeçar ou nem recomeçar, algumas vezes desanimamos e colocamos nós mesmos contra nós. Mas no fim, crescemos enquanto programadores e como profissionais, uma vez que aprendemos a lidar com frustrações e resolver problemas. E, afinal, ser um bom cientista da computação não seria justamente saber lidar com problemas desse tipo (Alan Turing que nos diga)?

### Conclusão ###

Foi um processo árduo, e ainda não é um programa 100% funcional, muito menos está pronto para ser utilizado em grande escala. Porém, é gratificante chegar até aqui e ver que a gente chegou em algum lugar. Adoramos ter feito o projeto, foram muitos domingos inteiros programando e procurando soluções, mas agora vemos que valeu muito a pena!

## CRUD INDEXADO ##

O *CRUD indexado* é responsável pela manipulação dos arquivos criados durante a execucação do programa, através de indices diretos e indiretos, utilizando estruturas de **ArvoreB+**  e **Hash Extensível**

Dessa forma, utiliza os seguintes métodos para executar as taregas nos arquivos: 
```
    public int create(T objeto);
    public T read(int idChave);
    public T read(String chaveSecundaria);
    public boolean update(T objetoNovo);
    public boolean delete(int ID);

```
No construtor da classe abrimos o arquivo, criamos os indices e escreve o novo cabeçalho (caso seja a primeira vez que o arquivo esteja sendo aberto).

Lembrando que, por ser um **CRUD Genérico**, não importa qual objeto seja enviado, os métodos recebem como objeto do tipo genérico T.

### CREATE ( ) ###

Este método cria o registro e escreve ele no arquivo, recebendo o próprio objeto como parâmetro. A primeira coisa a ser feita é mudar o seek para o inicio do arquivo e ler o cabeçalho, contendo o ultimo id escrito no arquivo, definindo assim o id do novo objeto. Depois disso, basta ir para o final do arquivo e escrever o registro, de forma que escreva a *lápide*, *tamanho do registro* e a *sequencia de bytes*, utilizando o método `toByteArray()` para transformar o objeto em um array de bytes que definem o objeto. 

### READ (ID) ###

O método read recupera as informações do arquivo e cria um objeto a partir dessas informações, de forma que recebe o id do objeto como parâmetro. Depois da criação de uma nova instância do objeto, o método read busca no indice direto a posição do arquivo em que se encontra o id. Com essa posição, lê-se o lápide, o tamanho do campo e cria-se o array de bytes a partir deste tamanho, depois basta usar o método `fromByteArray()` pra transformar o array de bytes em um objeto

### READ (CHAVE SECUNDÁRIA) ###

Este método é um antecessor do método `Read(ID)`, uma vez que tende a passar o ID obtido para o método read original através do index indireto, recebendo alguma outra informação, como o email.

### UPDATE ( ) ###

O método update é o mais complexo da nossa classe CRUD, já que possui algumas peculiaridades. Para sabermos onde vamos atualizar nosso arquivo, procuramos pela posição no indice direto, utilizando o id do novo objeto que deve ter o mesmo id do objeto antigo. Com isso, criamos o objeto antigo a partir da leitura dos dados no arquivo (mesmo processo do read()) e transformando o objeto novo em um array de bytes para recuperar o tamanho do registro.

Agora temos três situações:

- **Tamanho do registro novo é maior do que o tamanho do registro antigo**: Nessa situação temos que "matar" o registro antigo e escrever o novo objeto no final do arquivo. Aqui não podemos esquecer de atualizar o indice direto do id para sua nova posição.

- **Tamanho do registro novo menor ou igual ao tamanho do registro antigo**: Nessa situação, sobreescrevemos o novo array de bytes sobre o registro antigo, pulando o lapide e o id;

Por fim, se a chave secundária do novo objeto tiver mudado, temos que atualizar o nosso indice indireto com o novo par (chaveSecundaria, ID);

### DELETE ( ) ###

O método delete é responsável apenas por mudar o lápide do registro. É um método extremamente simples, já que, recebendo o id do objeto como parâmetro, basta busca a posição no arquivo através do indice direto e alterar o lápide. No fim. basta excluir o registro no indice direto e no indice indireto.



## PÁGINA INICIAL ##

Na tela inicial do nosso projeto, temos um menu de **Login** e **Cadastro** de usuários, além de uma opção de **Esqueci minha senha**. 

### CADASTRO ( ) ### 

O cadastro é feito da seguinte maneira: Pedimos o email do usuário e, através do método `isEmail()`, verificamos a validade do email. O programa pede na sequência o nome do usuário e a senha, estes sem nenhuma restrição (por enquanto). Confirmado os dados pelo usuário, o cadastro é registrado no arquivo. 

### LOGIN ( ) ###

O método login solicita ao usuário que entre com o email. Se o email já estiver no arquivo de usuários, o email será validado e será solicitada a senha. Caso contrário, aparecerá uma mensagem de erro de email não cadastrado. Supondo que o email está correto, recuperamos a senha do usuário e comparamos com a senha digitada pelo usuário. Criamos aqui um contador de tentativas para senhas incorretas (3 tentativas). Conferidos o email e a senha, o usuário é levada para o menu inicial do nosso programa.

### ESQUECI MINHA SENHA ( ) ###

O método esqueci minha senha simula uma recuperação de senha a partir do email enviado pelo usuário (caso ele seja válido).

## PÁGINA INICIAL DO LOGIN ##

Este é o menu de recepção do usuário. Como já foi feito outros menus durante o projeto vamos economizar nas explicações de como ele funciona. Este menu tem três opções para o usuário:

 - **Criação de perguntas**
 - **Consulta/Resposta de perguntas**
 - **Notificações**

## CRIAÇÃO DE PERGUNTAS ##  

Outo menu para o usuário, dessa vez trata de algumas operações. São elas: `Listar()`, `Incluir()`, `Alterar()` e `Arquivar()`.

### LISTAR() ###

Esse método é responsável por listar as perguntas ativas no sistema. Para isso, utilizamos o método read da ArvoreB+ que retorna um array com os ids das perguntas. Depois, bastou usar um laço de repetição para passar por cada posição do vetor e usar o `read()` do **CRUD** para resgatar os dados da pergunta.

### INCLUIR() ###

O método incluir se parece um pouco com outros métodos já utilizados, como o cadastro do usuário. A diferença é que só pedimos dois dados ao usuário: **A pergunta** e as **palavras chaves da pergunta**. Validando a pergunta, criamos um novo objeto *Pergunta* e definimos o atribuito pergunta com o que o usuário digitou. Após a confirmação do usuário, definimos a hora e data de criação, a nota (inicialmente como 0), as palavras chaves e deixamos ela ativa. Depois basta chamar o método `create()` do **CRUD** para registrar a pergunta e o método `create`da **ArvoreB+** para atualizar o índice.

### ALTERAR ( ) ###

Esse é o método mais complexo da criação de perguntas. Depois de listar as perguntas, pedimos ao usuário para escolher umas delas para alteração. . Selecionada a pergunta, o usuário digita a nova pergunta e as novas palavras chave, e através do método `update()` do **CRUD**, modificamos o registro.

### ARQUIVAR ( ) ### 

O método arquivar novamente lista as perguntas para que o usuário possa escolher qual pergunta ele irá arquivar. No final de muitos tratamentos de exceção, basta definer o atributo ativa da pergunta como *false* e excluir o registro no indice de palavras chaves;

## RESPOSTA/CONSULTA DE PERGUNTAS ##

Através de palavras chaves passadas pelo usuário, procuramos as perguntas no indice de palavras chaves e listamos as perguntas que possuem essas chaves. Depois disso, o usuário escolhe a pergunta. 

Ao escolher a pergunta, o menu da pergunta aparecerá, com o autor, a data de criação, a nota da pergunta, respostas e possíveis comentários. Neste momento, o usuário pode escolher entre **responder**, **comentar** ou **sair** da página. 

### RESPONDER ( ) ###

O método responder tem a função de criar respostas para aquela pergunta encontrada. Na verdade, não só responder. Podemos **Listar** nossas respostas, **Incluir** uma nova resposta, **Alterar** uma resposta do usuário e **Arquivar** a mesma.

#### LISTAR ( ) ####

A função de listagem simplesmente escreve na tela as respostas cadastradas pelo usuário para aquela pergunta. A implementação foi feita de forma que passamos por todas respostas cadastradas no arquivo de respostas, e recuperamos aquelas com o `IDUsuario()` logado. Por fim, bastou verificar se a resposta está ou não arquivada.

#### INCLUIR ( ) ####

A inclusão de uma nova resposta se da praticamente igual à de perguntas. Recuperamos o horário e data de criação, recebemos a String da resposta e, se tudo estiver correto, chamamos o método create() para incluir a resposta no arquivo.

#### ALTERAR ( ) ####

Para alterar uma pergunta, o usuário seleciona a resposta pela lista de respostas cadastradas e informa qual alteração deseja que seja efetuada. Lida a nova resposta, basta seguir alguns comandos dados pelo usuário para validar a alteração e chamar o método update() do CRUD, alterando o atributo `Resposta`

#### ARQUIVAR ( ) ####

Arquivar as respostas é um procedimento simples. Listadas as respostas, o usuário escolhe a resposta que deseja arquivar. Confirmado o arquivamento pelo usuário, basta chamarmos o método update() do CRUD, alterando o atributo `Ativa`.

### COMENTAR ( ) ###

O método comentar tem a função de criar comentarios para perguntas e respostas de usuários. Dessa forma, basta perguntar ao usuário se ele deseja comentar na pergunta ou em uma das respostas.

#### Comentando uma Pergunta ####

Para comentar a pergunta solicitamos primeiro o comentário propriamente dito. Após isso, criamos o objeto comentario e definimos os atributos de `IDUsuario`, `IDPR` e o `tipo`. Confirmada a inclusão pelo usuário, incluimos o comentario no arquivo e, utilizando a arvore de relacionamentos, relacionamos o ID do comentario com o ID da pergunta.

#### Comentando uma Resposta ####

Para comentar a resposta, a unica diferença é que precisamos solicitar ao usuário que informe o número da resposta. Definido este número, repetimos o processo de comentar uma pergunta.

### AVALIAR ( ) ###

Na avaliação, o usuário tem a opção de avaliar a pergunta ou alguma das respostas daquela pergunta, de forma que não é possível votar mais de uma vez em nenhuma delas. Dessa forma, perguntamos ao usuário se ele deseja avaliar uma *PERGUNTA* ou uma *RESPOSTA*

#### Avaliando uma Pergunta ####

Avaliar a pergunta é mais simples, uma vez que só temos uma opção. O usuário pode votar **positivamente** ou **negativamente** e, confirmada a operação pelo usuário, o voto é conectado com a pergunta através do `IDPR` (ID de pergunta ou resposta) e um caracter chamado `Tipo` ('P' para pergunta e 'R' para resposta). Por fim, atualizamos o arquivo de votos e concluímos a avaliação.

#### Avaliando uma Resposta ####

Avaliar a resposta é bem parecido com avaliar uma pergunta. A grande diferença é que o usuário precisa dizer qual das respostas (se houver mais de uma) ele quer avaliar. Escolhida a resposta, o processo se dá da mesma forma de avaliação de perguntas.


