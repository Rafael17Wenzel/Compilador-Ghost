package codegen;

public class CodeGenerator {
    private final StringBuilder out = new StringBuilder();

    public void start() {
        out.append("#include <iostream>\n#include <string>\nusing namespace std;\n\nint main() {\n");
    }

    public void emit(String line) {
        out.append("    ").append(line).append("\n");
    }

    public void end() {
        out.append("    return 0;\n}\n");
    }

    public String getCode() {
        return out.toString();
    }
}
