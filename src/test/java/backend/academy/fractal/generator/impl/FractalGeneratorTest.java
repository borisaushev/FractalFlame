package backend.academy.fractal.generator.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.FrameParameters;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import static java.awt.Color.BLACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class FractalGeneratorTest {
    @Spy
    protected ParametersGenerator parametersGenerator;
    @Mock
    @Qualifier("CLIParametersParser")
    protected ParameterSource parameterSource;

    protected int width = 1000;
    protected int height = 1000;
    protected int iterations = 1_000_000;
    protected int threadCount;
    protected FrameParameters frameParameters;
    protected List<FractalTransformation> transformations;
    protected FractalParameters fractalParameters;

    @BeforeEach
    void setUp() {
        threadCount = parametersGenerator.generateThreadCount();
        frameParameters = new FrameParameters(height, width);
        transformations = parametersGenerator.generateTransformations();
        fractalParameters = new FractalParameters(frameParameters, transformations, iterations, threadCount);
    }

    protected void assertNotEmpty(Frame frame) {
        assertEquals(fractalParameters.frameParameters().width(), frame.width());
        assertEquals(fractalParameters.frameParameters().height(), frame.height());
        int notNullCount = 0;
        int notBlackCount = 0;
        for (int x = 0; x < frame.width(); x++) {
            for (int y = 0; y < frame.height(); y++) {
                Pixel pixel = frame.getPixel(x, y);
                if (pixel == null) {
                    continue;
                }
                notNullCount++;
                if (pixel.getRgb() != BLACK.getRGB()) {
                    notBlackCount++;
                }
            }
        }
        assertNotEquals(0, notNullCount);
        assertNotEquals(0, notBlackCount);
    }

    protected void assertFramesAreDifferent(Frame frame1, Frame frame2) {
        int nonEqualPixels = 0;
        for (int x = 0; x < frame1.width(); x++) {
            for (int y = 0; y < frame1.height(); y++) {
                Pixel pixel = frame1.getPixel(x, y);
                if (pixel == null) {
                    continue;
                }
                if (pixel.getRgb() != frame2.getPixel(x, y).getRgb()) {
                    nonEqualPixels++;
                }
            }
        }
        assertNotEquals(0, nonEqualPixels);
    }
}
