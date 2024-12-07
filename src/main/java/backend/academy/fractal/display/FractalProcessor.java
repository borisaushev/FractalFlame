package backend.academy.fractal.display;

import backend.academy.fractal.grid.Frame;
import org.springframework.stereotype.Component;

/**
 * interface used to handle generated fractal
 */
@Component
public interface FractalProcessor {
    /**
     * Processes given fractal based on implementation
     *
     * @param frame generated fractal ({@link Frame} instance)
     */
    void processFractal(Frame frame);
}
