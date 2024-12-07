package backend.academy.fractal.generator;

import backend.academy.fractal.generator.impl.FractalGeneratorTest;
import backend.academy.fractal.generator.impl.GeneratorWithColorCorrection;
import backend.academy.fractal.generator.impl.parallel.MultiThreadGenerator;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.ParameterSource;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Performance test(need to be ran by hand)")
public class PerformanceTest extends FractalGeneratorTest {
    @Spy
    ParametersGenerator parametersGenerator;
    @InjectMocks
    volatile MultiThreadGenerator multiThreadGenerator;
    @InjectMocks
    volatile GeneratorWithColorCorrection generatorWithColorCorrection;
    @Mock
    @Qualifier("CLIParametersParser")
    private ParameterSource parameterSource;

    @DisplayName("Testing multi-threaded advantage over single-threaded")
    @RepeatedTest(10)
    @Disabled
    synchronized void multiThreadTest() {
        when(parameterSource.getParameters()).thenReturn(Optional.of(fractalParameters));

        // Warm-up phase
        generatorWithColorCorrection.generate(parameterSource);
        multiThreadGenerator.generate(parameterSource);

        // Measure single-threaded performance
        long singleThreadTime = measureExecutionTime(() ->
            generatorWithColorCorrection.generate(parameterSource)
        );

        // Measure multi thread performance
        long multiThreadTime = measureExecutionTime(() ->
            multiThreadGenerator.generate(parameterSource)
        );

        // Log results for debugging
        System.out.println("Threads count: " + fractalParameters.threadsCount());
        System.out.printf("Single-thread: %d ms, Multi-thread: %d ms%n", singleThreadTime, multiThreadTime);

        // Assert that the multi thread implementation is faster
        assertTrue(singleThreadTime > multiThreadTime);
    }

    /**
     * Measures the execution time of the given task in milliseconds.
     *
     * @param task Task to measure.
     * @return Execution time in milliseconds.
     */
    private long measureExecutionTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return Duration.ofNanos(end - start).toMillis();
    }
}
