package backend.academy.fractal.generator.impl;

import backend.academy.fractal.generator.impl.parallel.MultiThreadGenerator;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.FrameParameters;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.MAX_PRIORITY;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceTest {
    @Mock
    @Qualifier("CLIParametersParser")
    private ParameterSource parameterSource;

    @Spy
    ParametersGenerator parametersGenerator;

    @InjectMocks
    volatile MultiThreadGenerator multiThreadGenerator;

    @InjectMocks
    volatile GeneratorWithColorCorrection generatorWithColorCorrection;

    @DisplayName("Testing multi thread advantage")
    @RepeatedTest(10)
    void multiThreadTest() throws InterruptedException {
        //Given
        int width = 1000;
        int height = 1000;
        FrameParameters frameParameters = new FrameParameters(height, width);
        List<FractalTransformation> transformations = parametersGenerator.generateTransformations();
        int iterations = 2_000_000;
        FractalParameters fractalParameters = new FractalParameters(frameParameters, transformations, iterations);
        when(parameterSource.getParameters())
            .thenReturn(Optional.of(fractalParameters))
            .thenReturn(Optional.of(fractalParameters));

        //when
        long start1 = currentTimeMillis();
        Thread.ofPlatform()
            .name("SingleThreadGenerator")
            .priority(MAX_PRIORITY)
            .start(() -> {
                assert generatorWithColorCorrection.generate().isPresent();
            })
            .join();
        long time1 = currentTimeMillis() - start1;

        long start2 = currentTimeMillis();
        Thread.ofPlatform()
            .name("MultiThreadGenerator")
            .priority(MAX_PRIORITY)
            .start(() -> {
                assert multiThreadGenerator.generate().isPresent();
            })
            .join();
        long time2 = currentTimeMillis() - start2;

        //Then
        System.out.println("Threads count: " + MultiThreadGenerator.THREAD_COUNT);
        System.out.printf("single : %d --- multi : %d (millis)%n", time1, time2);
        assertTrue(time1 > time2);
    }
}
