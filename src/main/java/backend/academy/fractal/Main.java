package backend.academy.fractal;

import backend.academy.ApplicationConfig;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        FractalFlame generator = context.getBean(FractalFlame.class);
        generator.generateAndDisplay();
    }
}
