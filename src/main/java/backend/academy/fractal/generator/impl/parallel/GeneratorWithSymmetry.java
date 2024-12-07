package backend.academy.fractal.generator.impl.parallel;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.FramePoint;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import org.springframework.stereotype.Component;

/**
 * Multi thread implementation of {@link FractalGenerator} with symmetry
 */
@SuppressWarnings("MagicNumber")
@Component("GeneratorWithSymmetry")
public class GeneratorWithSymmetry extends MultiThreadGenerator implements FractalGenerator {
    /**
     * Constant for applying symmetry correction
     */
    private static final int SYMMETRY_COUNT = 4;

    /**
     * generates a part of the fractal with symmetry, modifies already given frame
     *
     * @param frame      frame
     * @param parameters parameters
     * @param start      starting point
     * @param end        ending point
     */
    @Override
    protected void generatePart(Frame frame, FractalParameters parameters, int start, int end) {
        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        for (int i = start; i < Math.min(parameters.iterations(), end); i++) {
            FractalTransformation transform = selectTransform(parameters.transformations());

            transform.applyTo(currentPoint);
            for (int s = 0; s < SYMMETRY_COUNT; s++) {
                double angle = Math.toRadians(s * (360.0 / SYMMETRY_COUNT));

                double x = currentPoint.x();
                double y = currentPoint.y();

                double newX = x * Math.cos(angle) - y * Math.sin(angle);
                double newY = x * Math.sin(angle) + y * Math.cos(angle);
                currentPoint.x(newX);
                currentPoint.y(newY);
                FramePoint point = frame.convertToFramePoint(currentPoint);
                if (i > PRE_ITERATIONS && frame.pointInBounds(point)) {
                    Pixel curPixel = frame.getPixel(point);
                    TransformationColor transformationColor = transform.color();
                    updatePixel(curPixel, transformationColor);
                    curPixel.hit();

                }
            }
        }
    }
}
