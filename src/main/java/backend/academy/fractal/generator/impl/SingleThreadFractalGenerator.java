package backend.academy.fractal.generator.impl;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.FramePoint;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * Single-threaded implementation of {@link FractalGenerator}.
 * Generates a fractal based on provided parameters using a single thread.
 */
@Component
public class SingleThreadFractalGenerator implements FractalGenerator {

    private static final Random random = new Random();

    /**
     * Updates a pixel's color and hit count based on the transformation color.
     *
     * @param curPixel            the pixel to update
     * @param transformationColor the color transformation to apply
     */
    protected static void updatePixel(Pixel curPixel, TransformationColor transformationColor) {
        if (curPixel.hitCount() == 0) {
            curPixel.red(transformationColor.color().getRed());
            curPixel.green(transformationColor.color().getGreen());
            curPixel.blue(transformationColor.color().getBlue());
        } else {
            curPixel.red((curPixel.red() + transformationColor.color().getRed()) / 2);
            curPixel.green((curPixel.green() + transformationColor.color().getGreen()) / 2);
            curPixel.blue((curPixel.blue() + transformationColor.color().getBlue()) / 2);
        }
    }

    /**
     * Randomly selects a transformation from the given list of transformations.
     *
     * @param transformations the list of transformations to choose from
     * @return a randomly selected {@link FractalTransformation}
     */
    protected static FractalTransformation selectTransform(List<FractalTransformation> transformations) {
        int index = random.nextInt(transformations.size());
        return transformations.get(index);
    }

    /**
     * Generates a fractal based on the parameters provided by the {@link ParameterSource}.
     *
     * @param parameterSource the source providing fractal parameters
     * @return an {@link Optional} containing the generated {@link Frame}, or empty if parameters are not provided
     */
    @Override
    public Optional<Frame> generate(ParameterSource parameterSource) {
        Optional<FractalParameters> optionalParameters = parameterSource.getParameters();
        if (optionalParameters.isEmpty()) {
            return Optional.empty();
        }
        FractalParameters parameters = optionalParameters.get();
        Frame frame = generate(parameters);

        return Optional.of(frame);
    }

    /**
     * Generates a fractal frame using the given parameters.
     *
     * @param parameters the fractal generation parameters
     * @return a {@link Frame} containing the generated fractal
     */
    private Frame generate(FractalParameters parameters) {
        Frame frame = new Frame(parameters.frameParameters());

        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        for (int i = 0; i < parameters.iterations(); i++) {
            FractalTransformation transform = selectTransform(parameters.transformations());

            transform.applyTo(currentPoint);
            FramePoint framePoint = frame.convertToFramePoint(currentPoint);

            if (i > PRE_ITERATIONS && frame.pointInBounds(framePoint)) {
                Pixel curPixel = frame.getPixel(framePoint);
                TransformationColor transformationColor = transform.color();
                updatePixel(curPixel, transformationColor);
                curPixel.hit();
            }
        }

        return frame;
    }
}
