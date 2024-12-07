package backend.academy.fractal.generator.impl.parallel;

import backend.academy.fractal.generator.impl.FractalGeneratorTest;
import backend.academy.fractal.grid.Frame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GeneratorWithSymmetryTest extends FractalGeneratorTest {
    @InjectMocks
    GeneratorWithSymmetry generator;

    @Test
    void generatePart() {
        //Given
        Frame frame = new Frame(frameParameters);

        //When
        Executable executable =
            () -> generator.generatePart(frame, fractalParameters, 0, fractalParameters.iterations());

        //Then
        assertDoesNotThrow(executable);
    }
}
