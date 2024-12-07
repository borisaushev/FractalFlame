package backend.academy.fractal;

import backend.academy.fractal.display.FractalProcessor;
import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.source.ParameterSource;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class used for generating fractal and handling the result
 */
@Component
public class FractalFlame {
    @Autowired
    @Qualifier("CLIParametersParser")
    protected ParameterSource parameterSource;
    @Autowired
    @Qualifier("MultiThreadGenerator")
    private FractalGenerator generator;
    @Autowired
    @Qualifier("SwingFractalDisplay")
    private FractalProcessor fractalProcessor;

    public void generateAndDisplay() {
        Optional<Frame> optionalFrame = generator.generate(parameterSource);
        if (optionalFrame.isEmpty()) {
            return;
        }
        fractalProcessor.processFractal(optionalFrame.orElseThrow());
    }
}
