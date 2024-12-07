package backend.academy.fractal.generator.impl;

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
class SingleThreadFractalGeneratorTest extends FractalGeneratorTest {
    @InjectMocks
    SingleThreadFractalGenerator generator;

    @Test
    @DisplayName("No parameters provided")
    void generateWithNoParameters() {
        //Given
        when(parameterSource.getParameters()).thenReturn(Optional.empty());

        //When
        Optional<Frame> result = generator.generate(parameterSource);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("All parameters provided")
    void generate() {
        //Given
        when(parameterSource.getParameters()).thenReturn(Optional.of(fractalParameters));

        //When
        Optional<Frame> result = generator.generate(parameterSource);

        //Then
        assertFalse(result.isEmpty());
        Frame parallelFrame = result.orElseThrow();
        assertEquals(fractalParameters.frameParameters().width(), parallelFrame.width());
        assertEquals(fractalParameters.frameParameters().height(), parallelFrame.height());
        super.assertNotEmpty(parallelFrame);
    }
}
