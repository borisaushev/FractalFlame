package backend.academy.fractal.grid;

import backend.academy.fractal.parameters.FrameParameters;
import lombok.Getter;

@Getter
public final class Frame {
    private final int height;
    private final int width;
    private final Pixel[][] pixelGrid;

    public Frame(FrameParameters frameParameters) {
        this.height = frameParameters.height();
        this.width = frameParameters.width();
        this.pixelGrid = new Pixel[height][width];
    }

    public FramePoint convertToFramePoint(BiUnitPoint point) {
        int px = (int) ((point.x()) / 4.0 * width() + (double) width() / 2);
        int py = (int) ((point.y()) / 4.0 * height() + (double) height() / 2);
        return new FramePoint(px, py);
    }

    public Pixel getPixel(int x, int y) {
        if(pixelGrid()[y][x] == null) {
            pixelGrid()[y][x] = new Pixel();
        }
        return pixelGrid()[y][x];
    }

    public Pixel getPixel(FramePoint point) {
        return getPixel(point.x(), point.y());
    }

    public boolean pointInBounds(FramePoint point) {
        return point.x() >= 0 && point.x() < width()
            && point.y() >= 0 && point.y() < height();
    }
}
