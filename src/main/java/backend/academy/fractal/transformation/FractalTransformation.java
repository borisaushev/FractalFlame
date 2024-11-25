package backend.academy.fractal.transformation;

import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.util.List;

public record FractalTransformation(
    AffineTransformation affineTransformation,
    List<NonLinearTransformation> nonLinearTransformationList,
    TransformationColor color) {

}
