package backend.academy.fractal.parameters.source;

import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 * A utility class to read input from the command line (CLI).
 * Simply is a wrapper for a {@link Scanner} instance.
 */
@Component
public class CLInputSource {
    private final static Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }
}
