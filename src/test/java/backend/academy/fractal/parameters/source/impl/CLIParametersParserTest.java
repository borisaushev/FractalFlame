package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.FrameParameters;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.CLInputSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import backend.academy.fractal.transformation.impl.NonLinearTransformation;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static backend.academy.fractal.transformation.color.TransformationColor.GOLD;
import static backend.academy.fractal.transformation.impl.NonLinearTransformation.SINUSOIDAL;
import static backend.academy.fractal.transformation.impl.NonLinearTransformation.SPHERICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CLIParametersParserTest {
    @Mock
    private CLInputSource clReaderMock;

    @Spy
    private ParametersGenerator generator;

    @InjectMocks
    private CLIParametersParser parser;

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
        FractalParameters fractalParameters = params.orElseThrow();
        assertEquals(800, params.orElseThrow().frameParameters().width());
        assertEquals(600, params.orElseThrow().frameParameters().height());
        assertEquals(2, fractalParameters.transformations().size());
        assertEquals(FractalParameters.MIN_ITERATIONS, fractalParameters.iterations());
    }

    @DisplayName("parsing iterations")
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
        assertEquals(FractalParameters.MIN_ITERATIONS, parsedVal1.orElseThrow());
        assertTrue(parsedVal2.isEmpty());
        assertTrue(parsedVal3.isEmpty());
        assertTrue(parsedVal4.isEmpty());
    }

    @DisplayName("parsing transformations list")
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
        assertEquals(ParametersGenerator.DEFAULT_FUNCTIONS_COUNT, transformations1.orElseThrow().size());
        assertTrue(transformations2.isEmpty());
        assertTrue(transformations3.isEmpty());
        assertTrue(transformations4.isPresent());
        assertEquals(2, transformations4.orElseThrow().size());
    }

    @DisplayName("parsing frame parameters")
    @Test
    void getFrameParameters() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("800 600") // Valid input
            .thenReturn("800") // Missing second parameter
            .thenReturn("invalid input"); // Non-numeric input

        // when
        Optional<FrameParameters> frame1 = parser.getFrameParameters();
        Optional<FrameParameters> frame2 = parser.getFrameParameters();
        Optional<FrameParameters> frame3 = parser.getFrameParameters();

        // then
        assertTrue(frame1.isPresent());
        assertEquals(800, frame1.orElseThrow().width());
        assertEquals(600, frame1.orElseThrow().height());
        assertTrue(frame2.isEmpty());
        assertTrue(frame3.isEmpty());
    }

    @DisplayName("parsing a single affine transformation")
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
        assertEquals(0.1, transformation1.orElseThrow().a());
        assertEquals(0.2, transformation1.orElseThrow().b());
        assertEquals(0.3, transformation1.orElseThrow().c());
        assertEquals(0.4, transformation1.orElseThrow().d());
        assertEquals(0.5, transformation1.orElseThrow().e());
        assertEquals(0.6, transformation1.orElseThrow().f());
        assertTrue(transformation2.isPresent());
        assertTrue(transformation3.isEmpty());
        assertTrue(transformation4.isEmpty());
    }

    @DisplayName("parsing transformation color")
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
        assertEquals(GOLD, color1.orElseThrow());
        assertTrue(color2.isEmpty());
        assertTrue(color3.isEmpty());
        assertTrue(color4.isEmpty());
    }

    @DisplayName("parsing transformations list")
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
        assertEquals(2, list1.orElseThrow().size());
        assertEquals(SINUSOIDAL, list1.orElseThrow().getFirst());
        assertEquals(SPHERICAL, list1.orElseThrow().get(1));
        assertTrue(list2.isPresent());
        assertTrue(list3.isEmpty());
        assertTrue(list4.isEmpty());
        assertTrue(list5.isEmpty());
    }

    @DisplayName("parsing thread count")
    @Test
    void getThreadCount() {
        // given
        when(clReaderMock.nextLine())
            .thenReturn("4")
            .thenReturn("14a")
            .thenReturn("")
            .thenReturn("0")
            .thenReturn("-1");

        // when
        Optional<Integer> parsedVal1 = parser.getThreadCount();
        Optional<Integer> parsedVal2 = parser.getThreadCount();
        Optional<Integer> parsedVal3 = parser.getThreadCount();
        Optional<Integer> parsedVal4 = parser.getThreadCount();
        Optional<Integer> parsedVal5 = parser.getThreadCount();

        // then
        assertTrue(parsedVal1.isPresent());
        assertEquals(4, parsedVal1.orElseThrow());
        assertTrue(parsedVal2.isEmpty());
        assertTrue(parsedVal3.isPresent());
        assertEquals(Runtime.getRuntime().availableProcessors(), parsedVal3.orElseThrow());
        assertTrue(parsedVal4.isEmpty());
        assertTrue(parsedVal5.isEmpty());
    }
}
