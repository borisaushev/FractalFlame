package backend.academy.fractal.parameters;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;

public record FractalParameters(
    Frame gridParameters,
    //key - affine transformation, value - list of nonlinear transformations to be applied
    List<FractalTransformation> transformations,
    int iterations
) {
    public final static int MIN_ITERATIONS = 1000;
}
