package backend.academy.fractal.parameters.source;

import backend.academy.fractal.parameters.FractalParameters;
import java.util.Optional;

public interface ParameterSource {
    Optional<FractalParameters> getParameters();
}
