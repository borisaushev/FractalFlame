package backend.academy.fractal.grid;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single pixel in the fractal frame.
 * <p>
 * Each pixel stores its RGB color values and a hit count, which indicates
 * how many times the pixel was "hit" during fractal generation.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pixel {
    private int red;
    private int green;
    private int blue;
    private int hitCount;

    /**
     * Default constructor initializes the pixel with no color (black)
     * and a hit count of zero.
     */
    public Pixel() {
        red = 0;
        green = 0;
        blue = 0;
        hitCount = 0;
    }

    /**
     * Increments the hit count of the pixel.
     * <p>
     * This method is used during fractal generation to track how many times
     * a pixel was affected by the algorithm.
     * </p>
     */
    public void hit() {
        hitCount++;
    }

    /**
     * Computes the RGB color value of the pixel as a single integer.
     *
     * @return the RGB value of the pixel in the format used by {@link Color#getRGB()}
     */
    public int getRgb() {
        return new Color(red, green, blue).getRGB();
    }
}
