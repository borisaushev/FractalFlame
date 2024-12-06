package backend.academy.fractal.display;

import backend.academy.fractal.grid.Frame;
import org.springframework.stereotype.Component;

@Component
public interface FractalProcessor {
    void processFractal(Frame frame);
}
