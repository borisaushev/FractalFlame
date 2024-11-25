package backend.academy.fractal.grid;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BiUnitPoint {
    private double x;
    private double y;

    public BiUnitPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
