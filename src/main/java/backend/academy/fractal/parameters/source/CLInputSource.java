package backend.academy.fractal.parameters.source;

import lombok.Getter;
import java.util.Scanner;

public class CLInputSource {
    private final static Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }
}
