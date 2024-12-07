package backend.academy.fractal.generator.impl.parallel;

import backend.academy.fractal.generator.impl.FractalGeneratorTest;
import backend.academy.fractal.generator.impl.SingleThreadFractalGenerator;
import backend.academy.fractal.grid.Frame;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiThreadGeneratorTest extends FractalGeneratorTest {
    @InjectMocks
    MultiThreadGenerator multiThreadGenerator;

    @InjectMocks
    SingleThreadFractalGenerator singleThreadGenerator;

    @Test
    @DisplayName("No parameters provided")
    void generateWithNoParameters() {
        //Given
        when(parameterSource.getParameters()).thenReturn(Optional.empty());

        //When
        Optional<Frame> result = multiThreadGenerator.generate(parameterSource);

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
        Optional<Frame> result = multiThreadGenerator.generate(parameterSource);
        //Getting a frame without color correction to compare
        Optional<Frame> singleThreadResult = singleThreadGenerator.generate(parameterSource);

        //Then
        assertFalse(result.isEmpty());
        assertFalse(singleThreadResult.isEmpty());
        Frame singleThreadFrame = singleThreadResult.orElseThrow();
        Frame parallelThreadFrame = result.orElseThrow();
        assertEquals(fractalParameters.frameParameters().width(), parallelThreadFrame.width());
        assertEquals(fractalParameters.frameParameters().height(), parallelThreadFrame.height());
        super.assertNotEmpty(parallelThreadFrame);
        super.assertFramesAreDifferent(singleThreadFrame, parallelThreadFrame);
    }
}
