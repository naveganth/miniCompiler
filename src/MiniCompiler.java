import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiniCompiler {

public static void main(String[] args) throws IOException {
    String fonte = LerFonte(Path.of("./src/MiniCalc"));
    Lexer l = new Lexer();
    l.CriarTokens(fonte);

    SymbolTable symbolTable = new SymbolTable();
    // IntermediateCodeGenerator codeGen = new IntermediateCodeGenerator();

    System.out.println("variables");
// Declarar variaveis na tabela
// TODO: Analisar expresses e gerar codigo intermediario
// TODO: Inicializar variaveis conforme uso
// TODO: Exibir tabela e codigo gerado
    }

    public static String LerFonte(Path local) throws IOException {
        String fonteCrua = Files.readString(local);
        System.out.println("Código fonte: " + fonteCrua);


        String buffer = fonteCrua.replaceAll("\\n", " ").replaceAll("\\s{2,}", " ");
        System.out.println("Código fonte bunito: " + buffer);
        return buffer;
    }

    public static class Token {
        String tipo;
        String valor;

        public Token(String tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    public static class Lexer {
        ArrayList<Token> tokens = new ArrayList<Token>();

        public void CriarTokens(String fonte) {
            Pattern reInt = Pattern.compile("^int");
            Pattern reMais = Pattern.compile("^\\+");
            Pattern reMenos = Pattern.compile("^\\-");
            Pattern reMult = Pattern.compile("^\\*");
            Pattern reDiv = Pattern.compile("^\\/");
            Pattern rePontoVirgula = Pattern.compile("^;");
            Pattern reVirgula = Pattern.compile("^,");
            Pattern reIgual = Pattern.compile("^=");
            Pattern reNum = Pattern.compile("^[0-9]+");
            Pattern reVar = Pattern.compile("^[a-zA-Z]");
            // ArrayList<Pattern> regexes = new ArrayList<Pattern>();
            HashMap<String, Pattern> regexes = new HashMap<String, Pattern>();
            regexes.put("int", reInt);
            regexes.put("mais", reMais);
            regexes.put("menos", reMenos);
            regexes.put("vezes", reMult);
            regexes.put("barra", reDiv);
            regexes.put("numero", reNum);
            regexes.put("ID", reVar);
            regexes.put("pontoevirgula", rePontoVirgula);
            regexes.put("virgula", reVirgula);
            regexes.put("igual", reIgual);

            int indiceFinal = fonte.length();
            int indiceAtual = 0;
            
            while (indiceAtual < indiceFinal) {
                // System.out.println("Código fonte restante: '" + fonte + "'");
                // System.out.println("Indice atual: " + indiceAtual);
                // System.out.println("Indice final: " + indiceFinal);
                // for (int i=0; i<regexes.size(); i++) {

                for (Map.Entry<String, Pattern> set : regexes.entrySet()) {
                    String chaveRegex = set.getKey();
                    // System.out.println("Chave regex: " + chaveRegex);

                    Pattern regex_atual = regexes.get(chaveRegex);
                    // System.out.println("iterando regex: " + regex_atual);

                    Matcher matcher = regex_atual.matcher(fonte);
                    
                    if (matcher.find()) {
                        String palavraEncontrada = matcher.group();
                        
                        // System.out.println("Palavra encontrada: "+ palavraEncontrada);
                        // System.out.println("Bateu no regex: '"+regex_atual+"'");

                        Token token = new Token(chaveRegex, palavraEncontrada);
                        tokens.add(token);

                        // System.out.println("TOKEN CRIADO: '" + token + "'");
                        indiceAtual += palavraEncontrada.length();
                        fonte = fonte.substring(palavraEncontrada.length());
                        if (fonte.startsWith(" ")) {
                            fonte = fonte.substring(1);
                            indiceAtual += 1;
                        }
                        break;
                    }
                }
            }
        }
    }
}

