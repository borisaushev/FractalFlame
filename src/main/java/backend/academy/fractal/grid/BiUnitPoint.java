package backend.academy.fractal.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to withhold a point with both x and y in [-1; 1]
 * The actual values are not checked to fit in ths range
 */
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
