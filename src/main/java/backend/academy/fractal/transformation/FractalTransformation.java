package backend.academy.fractal.transformation;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import backend.academy.fractal.transformation.impl.NonLinearTransformation;
import java.util.List;

/**
 * Represents a fractal transformation, consisting of an affine transformation,
 * a list of non-linear transformations, and a color.
 */
public record FractalTransformation(
    AffineTransformation affineTransformation,
    List<NonLinearTransformation> nonLinearTransformationList,
    TransformationColor color) {

    /**
     * Applies the affine and non-linear transformations to a given point.
     *
     * @param point The point to apply the transformations on.
     */
    public void applyTo(BiUnitPoint point) {
        affineTransformation.transform(point);
        for (var nlTransformation : nonLinearTransformationList) {
            nlTransformation.transform(point);
        }
    }
}
