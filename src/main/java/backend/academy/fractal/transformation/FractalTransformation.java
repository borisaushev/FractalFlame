package backend.academy.fractal.transformation;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.util.List;

public record FractalTransformation(
    AffineTransformation affineTransformation,
    List<NonLinearTransformation> nonLinearTransformationList,
    TransformationColor color) {

    public void applyTo(BiUnitPoint point) {
        affineTransformation.transform(point);
        for(var nlTransformation : nonLinearTransformationList) {
            nlTransformation.transform(point);
        }
    }
}
