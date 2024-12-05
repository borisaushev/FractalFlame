package backend.academy.fractal.parameters;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;

public record FractalParameters(
    Frame frame,
    List<FractalTransformation> transformations,
    int iterations
) {
    public final static int MIN_ITERATIONS = 1000;
}
