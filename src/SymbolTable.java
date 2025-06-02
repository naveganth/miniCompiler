import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, String> table = new HashMap<>();

    public void declare (String name, String type) {
        if (table.containsKey(name)) {
            if (!table.get(name).isEmpty()) {
                // Chave declarada e com tipo
                throw new RuntimeException("Erro: variável '" + name + "' já declarada.");
            }
            // Chave declarada mas sem tipo
            table.remove(name);
            table.put(name, type);
        }
        // Chave não declarada e sem tipo
        table.put(name, type);
    }

    public void initialize (String name) { 
        if (table.containsKey(name)) {
            throw new RuntimeException("Erro: variável '" + name + "' já inicializada.");
        }
        table.put(name, new String());
    }
    public boolean isDeclared (String name) {
        if (table.containsKey(name)) {
            if (!table.get(name).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public boolean isInitialized (String name) {
        if (table.containsKey(name)) {
            return true;
        }
        return false;
    }
    public void printTable () {
        System.out.println("Tabela de simbolos:");
        for (Map.Entry<String, String> entry : table.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }

}
