package backend.academy.fractal.parameters;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.transformation.NonLinearTransformation;
import backend.academy.fractal.transformation.TransformationFunction;
import java.util.HashMap;
import java.util.List;

public record FractalParameters(
    Frame gridParameters,
    //key - affine transformation, value - list of nonlinear transformations to be applied
    HashMap<TransformationFunction, List<NonLinearTransformation>> transformations,
    int iterations
) {
}
