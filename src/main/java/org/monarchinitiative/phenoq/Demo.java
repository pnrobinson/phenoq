package org.monarchinitiative.phenoq;

import org.monarchinitiative.phenoq.controller.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Demo extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        super.init();
        String[] args = getParameters().getRaw().toArray(String[]::new);
        context = new SpringApplicationBuilder(Demo.class).run(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent parent = loader.load();

        Scene scene = new Scene(parent, 800, 600);
        stage.setTitle("PhenoQuestionnaire Demo");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LOGGER.info("Shutting down...");
        context.close();
        LOGGER.info("Bye!");
    }

    public static void main(String[] args) {
        Application.launch(Demo.class, args);
    }
}

