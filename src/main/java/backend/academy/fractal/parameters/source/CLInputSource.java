package backend.academy.fractal.parameters.source;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class CLInputSource {
    private final static Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }
}
