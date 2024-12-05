package backend.academy.fractal.generator;

import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import java.util.Optional;

public interface FractalGenerator {
    Optional<Pixel[][]> generate();
}
