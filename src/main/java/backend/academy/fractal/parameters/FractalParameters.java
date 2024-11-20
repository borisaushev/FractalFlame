package backend.academy.fractal.parameters;

import backend.academy.fractal.grid.GridParameters;
import backend.academy.fractal.transformation.TransformationParameters;

public record FractalParameters(
    GridParameters gridParameters,
    TransformationParameters transformationParameters,
    int iterations
) {
}
