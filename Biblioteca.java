import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Biblioteca {
    
    /**
     * Verifica se o email é válido a partir de um "pattern"
     */
    public static boolean isEmail (String email){
        boolean isEmail = false;
         
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
                isEmail = true;
            }

        return isEmail;
    }

    /**
     * retira acentos, espaços, caracteres especiais de palavras para padronizar as palavras chave
     * @param word keyWord modificada
     * @return
     */
    public static String toKeyWord(String word){
        
        word = word.toLowerCase();
        
        word = word.replaceAll("á|à|ã|â|ä","a");
        word = word.replaceAll("é|è|ê","e");
        word = word.replaceAll("í|ì","i");
        word = word.replaceAll("ó|ò|ô|õ","o");
        word = word.replaceAll("ú|ù|û","u");
        word = word.replaceAll("ç","c");
        word = word.replaceAll(" ","");
        word = word.replaceAll("[.|:|>|<|!|?|'|,|@|$|%|&|*|-|_|=|+|°|º|ª|-|#]","");

        return word;
    }

    /**
     * Faz a interseção de vetores de inteiros
     * @param r  primeiro vetor
     * @param aux  segundo vetor
     * @return vetor resultante
     */
    public static int[] intersecao (int[] r, int[] aux){

        int[] resultante = new int[r.length];
        int k = 0;

        for (int i = 0; i < r.length; i++){
            for (int j = 0; j < aux.length; j++){
                if (r[i] == aux[j]){
                    resultante[k] = r[i];
                    k++;
                }
            }
        }
        return resultante;
    }

    /**
	* Algoritmo de ordenacao por insercao para o objeto Pergunta
	*/
	public static Pergunta[] ordenacao(int n, Pergunta[] array) {
		
        for (int i = 1; i < n; i++) {
			  Pergunta tmp = array[i];
            
            int j = i - 1;

            while ((j >= 0) && (array[j].getNota() < tmp.getNota())) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = tmp;
        }

        return array;
	}

}