package backend.academy.fractal.generator.impl;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.source.ParameterSource;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * A fractal generator with color correction.
 * Enhances the generated fractal image by applying gamma correction and adjusting pixel intensities.
 */
@Component("GeneratorWithColorCorrection")
public class GeneratorWithColorCorrection extends SingleThreadFractalGenerator implements FractalGenerator {
    /**
     * Gamma correction factor for color adjustment.
     */
    public static final double GAMMA = 1.5;

    /**
     * Adjusts the color of a single pixel using gamma correction.
     *
     * @param currentPixel  the pixel to correct
     * @param logMaxDensity the logarithm of the maximum pixel density
     */
    protected static void correctPixel(Pixel currentPixel, double logMaxDensity) {
        int hitCount = currentPixel.hitCount();
        double normalizedDensity = Math.log(hitCount) / logMaxDensity;
        double normalizedColorFactor = Math.pow(normalizedDensity, 1.0 / GAMMA);

        int red = (int) Math.min(255, currentPixel.red() * normalizedColorFactor);
        int green = (int) Math.min(255, currentPixel.green() * normalizedColorFactor);
        int blue = (int) Math.min(255, currentPixel.blue() * normalizedColorFactor);

        currentPixel.red(red);
        currentPixel.green(green);
        currentPixel.blue(blue);
    }

    /**
     * Finds the maximum pixel density in the given frame.
     *
     * @param frame the frame to analyze
     * @return the maximum hit count of any pixel in the frame
     */
    protected static int findMaxDensity(Frame frame) {
        int max = 0;
        for (int x = 0; x < frame.width(); x++) {
            for (int y = 0; y < frame.height(); y++) {
                Pixel curPixel = frame.getPixel(x, y);
                if (curPixel != null && curPixel.hitCount() > max) {
                    max = curPixel.hitCount();
                }
            }
        }
        return max;
    }

    /**
     * Generates a fractal and applies color correction to enhance the image.
     *
     * @param parameterSource the source providing fractal parameters
     * @return an {@link Optional} containing the generated and corrected {@link Frame}, or empty if parameters are not provided
     */
    @Override
    public Optional<Frame> generate(ParameterSource parameterSource) {
        Optional<Frame> optionalGrid = super.generate(parameterSource);
        if (optionalGrid.isEmpty()) {
            return Optional.empty();
        }

        applyColorCorrection(optionalGrid.get());
        return optionalGrid;
    }

    /**
     * Applies gamma-based color correction to the given frame.
     *
     * @param frame the frame to apply color correction to
     */
    protected void applyColorCorrection(Frame frame) {
        int maxDensity = findMaxDensity(frame);
        double logMaxDensity = Math.log(maxDensity);

        for (int y = 0; y < frame.height(); y++) {
            for (int x = 0; x < frame.width(); x++) {
                Pixel currentPixel = frame.getPixel(x, y);
                correctPixel(currentPixel, logMaxDensity);
            }
        }
    }
}
