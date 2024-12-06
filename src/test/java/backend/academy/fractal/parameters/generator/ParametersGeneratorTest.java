package backend.academy.fractal.parameters.generator;

import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.impl.NonLinearTransformation;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParametersGeneratorTest {

    @Spy
    ParametersGenerator parametersGenerator;

    @DisplayName("generating FractalTransformation list")
    @Test
    void generateTransformations() {
        //When
        List<FractalTransformation> transformations = parametersGenerator.generateTransformations();

        //Then
        assertNotNull(transformations);
        assertEquals(ParametersGenerator.DEFAULT_FUNCTIONS_COUNT, transformations.size());
        for (FractalTransformation transformation : transformations) {
            assertNotNull(transformation.affineTransformation());
            assertNotNull(transformation.color());
            assertNotNull(transformation.nonLinearTransformationList());
            assertNotNull(transformation);
        }
    }

    @DisplayName("generating NonLinearTransformation list")
    @Test
    void generateNonLinearTransformationList() {
        //When
        List<NonLinearTransformation> transformations = parametersGenerator.generateNonLinearTransformationList();

        //Then
        assertNotNull(transformations);
        assertEquals(ParametersGenerator.DEFAULT_FUNCTIONS_COUNT, transformations.size());
        for (NonLinearTransformation transformation : transformations) {
            assertNotNull(transformation);
        }
    }

    @DisplayName("generating single AffineTransformation")
    @Test
    void generateAffineTransformation() {
        //When
        AffineTransformation affineTransformation = parametersGenerator.generateAffineTransformation();

        //Then
        assertNotNull(affineTransformation);
    }

    @DisplayName("generating random stuff at once")
    @Test
    void generateAll() {
        //When
        AffineTransformation affineTransformation = parametersGenerator.generateAffineTransformation();
        List<NonLinearTransformation> NLTransformations = parametersGenerator.generateNonLinearTransformationList();
        List<FractalTransformation> transformations = parametersGenerator.generateTransformations();

        //Then
        assertNotNull(affineTransformation);
        assertNotNull(NLTransformations);
        assertNotNull(transformations);
    }
}
