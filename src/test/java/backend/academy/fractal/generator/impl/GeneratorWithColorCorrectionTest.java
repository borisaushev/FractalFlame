package backend.academy.fractal.generator.impl;

import backend.academy.fractal.grid.Frame;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratorWithColorCorrectionTest extends FractalGeneratorTest {
    @InjectMocks
    SingleThreadFractalGenerator singleThreadFractalGenerator;
    @InjectMocks
    GeneratorWithColorCorrection generatorWithColorCorrection;

    @Test
    @DisplayName("No parameters provided")
    void generateWithNoParameters() {
        //Given
        when(parameterSource.getParameters()).thenReturn(Optional.empty());

        //When
        Optional<Frame> result = generatorWithColorCorrection.generate(parameterSource);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Comparing to fractal without color correction")
    void generate() {
        //Given
        when(parameterSource.getParameters())
            .thenReturn(Optional.of(fractalParameters))
            .thenReturn(Optional.of(fractalParameters));

        //When
        Optional<Frame> correctedResult = generatorWithColorCorrection.generate(parameterSource);
        Optional<Frame> notCorrectedResult = singleThreadFractalGenerator.generate(parameterSource);

        //Then
        assertFalse(correctedResult.isEmpty());
        assertFalse(notCorrectedResult.isEmpty());
        Frame singleThreadFrame = notCorrectedResult.orElseThrow();
        Frame parallelFrame = correctedResult.orElseThrow();
        super.assertNotEmpty(parallelFrame);
        super.assertFramesAreDifferent(parallelFrame, singleThreadFrame);
    }
}
