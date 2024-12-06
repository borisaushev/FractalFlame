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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import static java.lang.System.currentTimeMillis;

@Component
public class SingleThreadFractalGenerator implements FractalGenerator {
    @Autowired
    @Qualifier("CLIParametersParser")
    protected ParameterSource parameterSource;

    private static final Random random = new Random();

    public Optional<Frame> generate() {
        Optional<FractalParameters> optionalParameters = parameterSource.getParameters();
        if (optionalParameters.isEmpty()) {
            return Optional.empty();
        }
        FractalParameters parameters = optionalParameters.get();
        Frame frame = generate(parameters);

        return Optional.of(frame);
    }

    private Frame generate(FractalParameters parameters) {
        Frame frame = new Frame(parameters.frameParameters());

        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        for (int i = 0; i < parameters.iterations(); i++) {
            FractalTransformation transform = selectTransform(parameters.transformations());

            transform.applyTo(currentPoint);
            FramePoint framePoint = frame.convertToFramePoint(currentPoint);

            if (i > MIN_ITERATIONS && frame.pointInBounds(framePoint)) {
                Pixel curPixel = frame.getPixel(framePoint);
                TransformationColor transformationColor = transform.color();
                updatePixel(curPixel, transformationColor);
                curPixel.hit();
            }
        }

        return frame;
    }

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

    protected static FractalTransformation selectTransform(List<FractalTransformation> transformations) {
        int index = random.nextInt(transformations.size());
        return transformations.get(index);
    }
}
