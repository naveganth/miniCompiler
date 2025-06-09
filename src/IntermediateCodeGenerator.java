import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IntermediateCodeGenerator {

    private int tempCounter = 0;
    private SymbolTable symbolTable;

    public IntermediateCodeGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<String> generate(String expression) {
        List<String> code = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        // Divide a expressão em tokens
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) {
                continue;
            }

            if (isOperator(token)) {
                if (stack.size() >= 2) {
                    String op2 = stack.pop();
                    String op1 = stack.pop();
                    String temp = "t" + (++tempCounter);
                    code.add(temp + " = " + op1 + " " + token + " " + op2);
                    stack.push(temp);
                }
            } else {
                stack.push(token);
            }
        }

        return code;
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    public List<String> optimize(List<String> code) {
        // TODO: aplicar otimizações simples (ex: + 0, * 1)
        return code;
    }

    // Exibe o código linha por linha
    public void printCode(List<String> code) {
        for (String line : code) {
            System.out.println(line);
        }
    }
}
