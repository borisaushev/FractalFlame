package backend.academy.fractal.generator.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.FrameParameters;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import static java.awt.Color.BLACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratorWithColorCorrectionTest {
    @Mock
    @Qualifier("CLIParametersParser")
    private ParameterSource parameterSource;

    @InjectMocks
    GeneratorWithColorCorrection generator;

    @Spy
    ParametersGenerator parametersGenerator;

    @InjectMocks
    private SingleThreadFractalGenerator singleThreadFractalGenerator;

    @Test
    @DisplayName("No parameters provided")
    void generateWithNoParameters() {
        //Given
        when(parameterSource.getParameters()).thenReturn(Optional.empty());

        //When
        Optional<Frame> result = generator.generate();

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("All parameters provided")
    void generate() {
        //Given
        int width = 1000;
        int height = 1000;
        FrameParameters frameParameters = new FrameParameters(height, width);
        List<FractalTransformation> transformations = parametersGenerator.generateTransformations();
        int iterations = 1_000_000;
        FractalParameters fractalParameters = new FractalParameters(frameParameters, transformations, iterations);
        when(parameterSource.getParameters())
            .thenReturn(Optional.of(fractalParameters))
            .thenReturn(Optional.of(fractalParameters));

        //When
        Optional<Frame> result = generator.generate();
        //Getting a frame without color correction to compare
        Optional<Frame> singleThreadResult = singleThreadFractalGenerator.generate();

        //Then
        assertFalse(result.isEmpty());
        assertFalse(singleThreadResult.isEmpty());
        Frame singleThreadFrame = singleThreadResult.get();
        Frame frame = result.get();
        assertEquals(width, frame.width());
        assertEquals(height, frame.height());
        int notNullCount = 0;
        int notBlackCount = 0;
        int nonEqualPixels = 0;
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
                if(pixel.getRgb() != singleThreadFrame.getPixel(x, y).getRgb()) {
                    nonEqualPixels++;
                }
            }
        }
        assertNotEquals(0, notNullCount);
        assertNotEquals(0, notBlackCount);
        assertNotEquals(0, nonEqualPixels);
    }
}
