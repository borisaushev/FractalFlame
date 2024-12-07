package backend.academy.fractal.generator.impl.parallel;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.generator.impl.GeneratorWithColorCorrection;
import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.FramePoint;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * Multi thread implementation of {@link FractalGenerator}
 */
@Component("MultiThreadGenerator")
public class MultiThreadGenerator extends GeneratorWithColorCorrection implements FractalGenerator {
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * applies color correction to a part of the frame
     *
     * @param frame         frame
     * @param logMaxDensity pre-calculated value of log10(maxDensity)
     * @param yStart        starting point
     * @param yEnd          ending point
     */
    protected static void correctPart(Frame frame, double logMaxDensity, int yStart, int yEnd) {
        for (int y = yStart; y < Math.min(yEnd, frame.height()); y++) {
            for (int x = 0; x < frame.width(); x++) {
                Pixel currentPixel = frame.getPixel(x, y);
                correctPixel(currentPixel, logMaxDensity);
            }
        }
    }

    /**
     * Gets parameters and generates the fractal.
     * Can only last an hour
     *
     * @param parameterSource source of fractal parameters
     * @return generated fractal
     */
    @Override
    public Optional<Frame> generate(ParameterSource parameterSource) {
        Optional<FractalParameters> optionalParameters = parameterSource.getParameters();
        if (optionalParameters.isEmpty()) {
            return Optional.empty();
        }
        FractalParameters parameters = optionalParameters.get();
        Frame frame = new Frame(parameters.frameParameters());

        try (ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT)) {
            int iterations = parameters.iterations();
            for (int i = 0; i < iterations; i += (iterations / THREAD_COUNT)) {
                int start = i;
                int end = i + iterations / THREAD_COUNT;
                service.submit(() -> generatePart(frame, parameters, start, end));
            }
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
            return Optional.empty();
        }
        applyColorCorrection(frame);

        return Optional.of(frame);
    }

    /**
     * applies color correction using multiple threads
     * Can only last an hour
     *
     * @param frame given frame to apply color correction
     */
    @Override
    protected void applyColorCorrection(Frame frame) {
        int maxDensity = findMaxDensity(frame);
        double logMaxDensity = Math.log(maxDensity);

        try (ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT)) {
            for (int y = 0; y < frame.height(); y += frame.height() / THREAD_COUNT) {
                int start = y;
                int end = y + frame.height() / THREAD_COUNT;
                service.execute(() -> correctPart(frame, logMaxDensity, start, end));
            }
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * generates a part of the fractal, modifies already given frame
     *
     * @param frame      frame
     * @param parameters parameters
     * @param start      starting point
     * @param end        ending point
     */
    protected void generatePart(Frame frame, FractalParameters parameters, int start, int end) {
        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        for (int i = start; i < Math.min(parameters.iterations(), end); i++) {
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
    }
}
