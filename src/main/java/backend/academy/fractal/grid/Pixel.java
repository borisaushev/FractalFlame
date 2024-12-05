package backend.academy.fractal.grid;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public Pixel() {
        red = 0;
        green = 0;
        blue = 0;
        hitCount = 0;
    }

    public void hit() {
        hitCount++;
    }

    public int getRgb() {
        return new Color(red, green, blue).getRGB();
    }
}
