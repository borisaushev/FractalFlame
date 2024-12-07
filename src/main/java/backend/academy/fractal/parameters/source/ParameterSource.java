package backend.academy.fractal.parameters.source;

import backend.academy.fractal.parameters.FractalParameters;
import java.util.Optional;

/**
 * Interface for obtaining fractal parameters
 */
public interface ParameterSource {
    /**
     * Retrieves the parameters for generating a fractal.
     *
     * @return an {@code Optional} containing the fractal parameters if successfully obtained.
     *     Returns an empty {@code Optional} if the parameters are unavailable or invalid.
     */
    Optional<FractalParameters> getParameters();
}
