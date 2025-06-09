import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MiniCompiler {

public static void main(String[] args) throws IOException {
    String fonte = LerFonte(Path.of("./src/MiniCalc"));

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

    public class Token {
        String tipo;
        String valor;

        public Token(String tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    public static void CriarTokens() {

    }
}

