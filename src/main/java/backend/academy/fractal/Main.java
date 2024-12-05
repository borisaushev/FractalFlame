package backend.academy.fractal;

import backend.academy.ApplicationConfig;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        SwingFractalDisplay display = context.getBean(SwingFractalDisplay.class); // Получаем бин Main из контекста

        display.generateAndDisplay();
    }
}
