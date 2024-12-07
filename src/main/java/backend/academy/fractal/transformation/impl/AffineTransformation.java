package backend.academy.fractal.transformation.impl;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.transformation.TransformationFunction;

/**
 * Represents an affine transformation applied to a BiUnitPoint.
 */
public record AffineTransformation(double a, double b, double c, double d, double e, double f)
    implements TransformationFunction {
    /**
     * Applies the affine transformation to a given {@link BiUnitPoint}
     *
     * @param p The point to transform.
     */
    @Override
    public void transform(BiUnitPoint p) {
        p.x(a * p.x() + b * p.y() + c);
        p.y(d * p.x() + e * p.y() + f);
    }
}
