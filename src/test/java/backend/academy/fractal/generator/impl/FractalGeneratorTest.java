package backend.academy.fractal.generator.impl;

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
}
