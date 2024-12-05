package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.CLInputSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.NonLinearTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static backend.academy.fractal.transformation.NonLinearTransformation.SINUSOIDAL;
import static backend.academy.fractal.transformation.NonLinearTransformation.SPHERICAL;
import static backend.academy.fractal.transformation.color.TransformationColor.GOLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CLIParametersParserTest {

    @Mock
    private CLInputSource clReaderMock;

    @InjectMocks
    private CLIParametersParser parser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("getting all parameters")
    @Test
    void getParameters() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("800 600") // getFrame
            .thenReturn("2") // getTransformationsList
            .thenReturn("0 0 0 0 0 0") // getAffineTransformation
            .thenReturn("1 2") // getTransformationList
            .thenReturn("2") // getTransformationColors
            .thenReturn("") // getAffineTransformation
            .thenReturn("1 3") // getTransformationList
            .thenReturn("1") // getTransformationColors
            .thenReturn(String.valueOf(FractalParameters.MIN_ITERATIONS)); // getIterations

        // when
        Optional<FractalParameters> params = parser.getParameters();

        // then
        assertTrue(params.isPresent());
        FractalParameters fractalParameters = params.get();
        assertEquals(800, params.get().frame().width());
        assertEquals(600, params.get().frame().height());
        assertEquals(2, fractalParameters.transformations().size());
        assertEquals(FractalParameters.MIN_ITERATIONS, fractalParameters.iterations());
    }

    @Test
    void getIterations() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn(String.valueOf(FractalParameters.MIN_ITERATIONS))
            .thenReturn("14a")
            .thenReturn("")
            .thenReturn(String.valueOf(FractalParameters.MIN_ITERATIONS - 1));

        // when
        Optional<Integer> parsedVal1 = parser.getIterations();
        Optional<Integer> parsedVal2 = parser.getIterations();
        Optional<Integer> parsedVal3 = parser.getIterations();
        Optional<Integer> parsedVal4 = parser.getIterations();

        // then
        assertEquals(FractalParameters.MIN_ITERATIONS, parsedVal1.get());
        assertTrue(parsedVal2.isEmpty());
        assertTrue(parsedVal3.isEmpty());
        assertTrue(parsedVal4.isEmpty());
    }

    @Test
    void getTransformationsList() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("") // t1: Default 6 transformations
            .thenReturn("3") // t2: Valid count
            .thenReturn("-1") // t2: Invalid input
            .thenReturn("a") // t3: Invalid input
            .thenReturn("2") // t4: Valid count
            .thenReturn("") // t4: first affine transformation (auto)
            .thenReturn("") //t4: first nonlinear transformation list (auto)
            .thenReturn("1") //t4: first color
            .thenReturn("0.1 0.2 0.3 0.4 0.5 0.6") // t4: second affine transformation
            .thenReturn("1 2") //t4: second nonlinear transformation list
            .thenReturn("2"); //t4: second color

        // when
        Optional<List<FractalTransformation>> transformations1 = parser.getTransformationsList();
        Optional<List<FractalTransformation>> transformations2 = parser.getTransformationsList();
        Optional<List<FractalTransformation>> transformations3 = parser.getTransformationsList();
        Optional<List<FractalTransformation>> transformations4 = parser.getTransformationsList();

        // then
        assertTrue(transformations1.isPresent());
        assertEquals(6, transformations1.get().size());
        assertTrue(transformations2.isEmpty());
        assertTrue(transformations3.isEmpty());
        assertTrue(transformations4.isPresent());
        assertEquals(2, transformations4.get().size());
    }

    @Test
    void getFrame() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("800 600") // Valid input
            .thenReturn("800") // Missing second parameter
            .thenReturn("invalid input"); // Non-numeric input

        // when
        Optional<Frame> frame1 = parser.getFrame();
        Optional<Frame> frame2 = parser.getFrame();
        Optional<Frame> frame3 = parser.getFrame();

        // then
        assertTrue(frame1.isPresent());
        assertEquals(800, frame1.get().width());
        assertEquals(600, frame1.get().height());
        assertTrue(frame2.isEmpty());
        assertTrue(frame3.isEmpty());
    }

    @Test
    void getAffineTransformation() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("0.1 0.2 0.3 0.4 0.5 0.6") // Valid input
            .thenReturn("") // Default generation
            .thenReturn("0.1 0.2 0.3") // Insufficient parameters
            .thenReturn("2 0.2 0.3 0.4 0.5 0.6"); // Invalid range

        // when
        Optional<AffineTransformation> transformation1 = parser.getAffineTransformation();
        Optional<AffineTransformation> transformation2 = parser.getAffineTransformation();
        Optional<AffineTransformation> transformation3 = parser.getAffineTransformation();
        Optional<AffineTransformation> transformation4 = parser.getAffineTransformation();

        // then
        assertTrue(transformation1.isPresent());
        assertEquals(0.1, transformation1.get().a());
        assertEquals(0.2, transformation1.get().b());
        assertEquals(0.3, transformation1.get().c());
        assertEquals(0.4, transformation1.get().d());
        assertEquals(0.5, transformation1.get().e());
        assertEquals(0.6, transformation1.get().f());
        assertTrue(transformation2.isPresent());
        assertTrue(transformation3.isEmpty());
        assertTrue(transformation4.isEmpty());
    }

    @Test
    void getTransformationColors() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("1") // Valid input
            .thenReturn("invalid") // Invalid input
            .thenReturn("0") // Out of range
            .thenReturn(String.valueOf(TransformationColor.values().length + 1)); // Out of range

        // when
        Optional<TransformationColor> color1 = parser.getTransformationColors();
        Optional<TransformationColor> color2 = parser.getTransformationColors();
        Optional<TransformationColor> color3 = parser.getTransformationColors();
        Optional<TransformationColor> color4 = parser.getTransformationColors();

        // then
        assertTrue(color1.isPresent());
        assertEquals(GOLD, color1.get());
        assertTrue(color2.isEmpty());
        assertTrue(color3.isEmpty());
        assertTrue(color4.isEmpty());
    }

    @Test
    void getNLTransformationsList() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("1 2") // Valid input
            .thenReturn("") // Default generation
            .thenReturn("invalid input") // Non-numeric
            .thenReturn("-1") // Out of range
            .thenReturn(String.valueOf(NonLinearTransformation.values().length + 1)); // Out of range

        // when
        Optional<List<NonLinearTransformation>> list1 = parser.getNLTransformationsList();
        Optional<List<NonLinearTransformation>> list2 = parser.getNLTransformationsList();
        Optional<List<NonLinearTransformation>> list3 = parser.getNLTransformationsList();
        Optional<List<NonLinearTransformation>> list4 = parser.getNLTransformationsList();
        Optional<List<NonLinearTransformation>> list5 = parser.getNLTransformationsList();

        // then
        assertTrue(list1.isPresent());
        assertEquals(2, list1.get().size());
        assertEquals(SINUSOIDAL, list1.get().getFirst());
        assertEquals(SPHERICAL, list1.get().get(1));
        assertTrue(list2.isPresent());
        assertTrue(list3.isEmpty());
        assertTrue(list4.isEmpty());
        assertTrue(list5.isEmpty());
    }
}
