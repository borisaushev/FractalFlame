package backend.academy.fractal.grid;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single pixel in the fractal frame.
 * <p>
 * Each pixel stores its RGB color values and a hit count, which indicates
 * how many times the pixel was "hit" during fractal generation.
 * </p>
 */
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pixel {
    private AtomicInteger red;
    private AtomicInteger green;
    private AtomicInteger blue;
    private AtomicInteger hitCount;

    /**
     * Default constructor initializes the pixel with no color (black)
     * and a hit count of zero.
     */
    public Pixel() {
        red = new AtomicInteger(0);
        green = new AtomicInteger(0);
        blue = new AtomicInteger(0);
        hitCount = new AtomicInteger(0);
    }

    /**
     * Increments the hit count of the pixel.
     * <p>
     * This method is used during fractal generation to track how many times
     * a pixel was affected by the algorithm.
     * </p>
     */
    public void hit() {
        hitCount.incrementAndGet();
    }

    /**
     * Computes the RGB color value of the pixel as a single integer.
     *
     * @return the RGB value of the pixel in the format used by {@link Color#getRGB()}
     */
    public int getRgb() {
        return new Color(red.intValue(), green.intValue(), blue.intValue()).getRGB();
    }

    public void green(int green) {
        this.green.set(green);
    }

    public void blue(int blue) {
        this.blue.set(blue);
    }

    public void red(int red) {
        this.red.set(red);
    }

    public int red() {
        return this.red.get();
    }

    public int blue() {
        return this.blue.get();
    }

    public int green() {
        return this.green.get();
    }

    public int hitCount() {
        return this.hitCount.get();
    }
}
