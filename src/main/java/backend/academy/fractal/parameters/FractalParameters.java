package backend.academy.fractal.parameters;

import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;

/**
 * Represents the parameters needed to generate a fractal
 */
public record FractalParameters(
    FrameParameters frameParameters,
    List<FractalTransformation> transformations,
    int iterations,
    int threadsCount
) {
    /**
     * Minimum number of iterations allowed for fractal generation.
     */
    public final static int MIN_ITERATIONS = 1000;
    /**
     * Recommended number of iterations for fractal generation.
     */
    public final static int RECOMMENDED_ITERATIONS = 10000000;
}
