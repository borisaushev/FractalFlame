package backend.academy.fractal.generator;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.source.ParameterSource;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface FractalGenerator {
    /**
     * Iterations described in fractal flame algorithm,
     * for that we don't update pixels
     */
    int PRE_ITERATIONS = 10;

    /**
     * @param parameterSource source of fractal parameters
     * @return generated fractal
     */
    Optional<Frame> generate(ParameterSource parameterSource);
}
