package backend.academy.fractal.generator;

import backend.academy.fractal.grid.Frame;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public interface FractalGenerator {
    int MIN_ITERATIONS = 0;
    Optional<Frame> generate();
}
