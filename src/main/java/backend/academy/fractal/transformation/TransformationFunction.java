package backend.academy.fractal.transformation;

import backend.academy.fractal.grid.BiUnitPoint;

/**
 * Represents a transformation function that can be applied to a BiUnitPoint.
 */
public interface TransformationFunction {

    /**
     * Transforms the given point according to the transformation's logic.
     *
     * @param point The point to transform.
     */
    void transform(BiUnitPoint point);
}
