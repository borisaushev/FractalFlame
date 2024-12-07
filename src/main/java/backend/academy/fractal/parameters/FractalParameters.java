package backend.academy.fractal.parameters;

import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;

/**
 * Represents the parameters needed to generate a fractal
 */
public record FractalParameters(
    FrameParameters frameParameters,
    List<FractalTransformation> transformations,
    int iterations
) {
    /**
     * Minimum number of iterations allowed for fractal generation.
     */
    public final static int MIN_ITERATIONS = 1000;
}
