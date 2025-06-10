import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiniCompiler {

public static void main(String[] args) throws IOException {
    String fonte = LerFonte(Path.of("./src/MiniCalc"));
    
    Lexer l = new Lexer();
    l.CriarTokens(fonte);
    ArrayList<Token> tokens = l.tokens;
    // System.out.println("Tokens criados: " + tokens);

    SymbolTable symbolTable = new SymbolTable();
    Sintatico s = new Sintatico(tokens, symbolTable);
    s.parse();
    symbolTable.printTable();
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

        @Override public String toString() {
            return "Token { Tipo: "+ tipo +", Valor: " + valor + " }"; 
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
            Pattern reVar = Pattern.compile("^(?!int)[a-zA-Z]");
            // ArrayList<Pattern> regexes = new ArrayList<Pattern>();
            HashMap<String, Pattern> regexes = new HashMap<String, Pattern>();
            regexes.put("ID", reVar);
            regexes.put("int", reInt);
            regexes.put("mais", reMais);
            regexes.put("menos", reMenos);
            regexes.put("vezes", reMult);
            regexes.put("barra", reDiv);
            regexes.put("numero", reNum);
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

    public static class Sintatico {
        private ArrayList<Token> tokens;
        private int indiceAtual;
        private int indiceMax;
        private Token lookahead;
        private SymbolTable symbolTable;

        public Sintatico(ArrayList<Token> tokens, SymbolTable symbolTable) {
            this.tokens = tokens;
            this.indiceAtual = 0;
            this.indiceMax = tokens.size();
            this.lookahead = this.tokens.get(indiceAtual);
            this.symbolTable = symbolTable;
        }

        public void consume(String esperado) throws IOException {
            if (lookahead == null) throw new RuntimeException("Fim inesperado");
            if (lookahead.tipo.equals(esperado)) {
                indiceAtual += 1;
                if (indiceAtual < indiceMax) {
                    lookahead = tokens.get(indiceAtual);
                } else {
                    lookahead = null;
                }
            } else {
                erro("Esperado: " + esperado + ", encontrado: " + lookahead.tipo);
            }
        }

        public void erro(String msg) {
            if (lookahead != null) {
                System.out.println("Erro sintático: " + msg + " (" + indiceAtual + ")");
            } else {
                System.out.println("Erro sintático: " + msg + " (-1)");
            }
        }

        public void parse() throws IOException {
            bloco();
            System.out.println("Análise sintática concluída com sucesso");
        }

        public void bloco() throws IOException {
            declaracao_variavel();
            expressao();
            expressao();
        }

        public void declaracao_variavel() throws IOException {
            consume("int");
            if (this.symbolTable.isInitialized(lookahead.valor)) {
                erro("Variável já declarada: " + lookahead.valor);
            }
            this.symbolTable.initialize(lookahead.valor);
            consume("ID");

            while (lookahead.tipo == "virgula") {
                consume("virgula");
                if (lookahead.tipo == "ID") {
                    if (this.symbolTable.isInitialized(lookahead.valor)) {
                        erro("Variável já inicializada: " + lookahead.valor);
                    }
                    this.symbolTable.initialize(lookahead.valor);
                    consume("ID");
                }
            }
            consume("pontoevirgula");
        }

        public void expressao() throws IOException {
            if (!symbolTable.isInitialized(lookahead.valor)) {
                erro("Variável não declarada: " + lookahead.valor);
            }
            consume("ID");
            consume("igual");
            expr();
            consume("pontoevirgula");
        }

        public void expr() throws IOException {
            termo();

            while (lookahead.tipo == "mais" || lookahead.tipo == "menos") {
                consume(lookahead.tipo);
                termo();
            }
        }

        public void termo() throws IOException {
            fator();

            while (lookahead.tipo == "vezes" || lookahead.tipo == "barra") {
                consume(lookahead.tipo);
                fator();
            }
        }

        public void fator() throws IOException {
            switch (lookahead.tipo) {
                case "ID":
                    if (!symbolTable.isInitialized(lookahead.valor)) {
                        erro("Variável não declarada: " + lookahead.valor);
                    }
                    consume("ID");
                    break;
                case "numero":
                    consume("numero");
                    break;
                default:
                    erro("Esperado: ID ou número, encontrado: " + lookahead.valor);
                    break;
            }
        }
    }
}

