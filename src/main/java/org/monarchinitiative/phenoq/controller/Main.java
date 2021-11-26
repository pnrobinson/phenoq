package org.monarchinitiative.phenoq.controller;


import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenoq.phenoitem.PhenoItem;
import org.monarchinitiative.phenoq.phenoitem.PhenoItemDemoGenerator;
import org.monarchinitiative.phenoq.qtable.PhenoqTable;
import org.monarchinitiative.phenoq.qtable.Qphenorow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class Main {

    @FXML
    private VBox vbox;
    @FXML
    private Button startQ;

    private PhenoqTable table = null;

    private Ontology ontology;

    private List< Qphenorow> phenoRows = null;

    /**
     * This returns all Phenoitems with a known (Observed or excluded) answer, and ignores Phenoitems
     * that are unkwown (e.g., because they were not answered by the user)
     * @return List of all observed/excluded phenoitems.
     */
    public List<PhenoItem> getPhenoItems() {
       if (phenoRows == null) return List.of();
       else return phenoRows.stream().map(Qphenorow::toPhenoItem).filter(Predicate.not(PhenoItem::isUnknown))
               .collect(Collectors.toList());
    }

    @Autowired
    public void Main(Ontology ontology) {
        this.ontology = ontology;
    }


    @FXML
    private void initialize() {
        this.vbox.setAlignment(Pos.CENTER);
    }


    @FXML
    public void startQuestionnaire() {
        VBox root = new VBox();
        this.phenoRows = getDemoItems();
        table = new PhenoqTable(this.phenoRows);
        root.getChildren().add(table);
        HBox buttonBox = new HBox();
        buttonBox.setMinWidth(1000);
        buttonBox.setMaxHeight(20);
        Button cancelButton = new Button("Cancel");
        Button acceptButton = new Button("Done");
        cancelButton.setOnAction((e) -> {
            this.phenoRows.forEach(Qphenorow::reset);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
        acceptButton.setOnAction((e) -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
        buttonBox.getChildren().add(cancelButton);
        buttonBox.getChildren().add(acceptButton);
        root.getChildren().add(buttonBox);
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.setTitle("HPO-Based Phenotype Questionnaire");
        stage.showAndWait();
        ListView<String> listView = new ListView<>();
        for (var pitem : this.phenoRows) {
            listView.getItems().add(pitem.toPhenoItem().toString());
        }
        listView.setMinWidth(990);
        listView.setMinHeight(650);
        HBox hbox = new HBox(listView);
        Scene scene2 = new Scene(hbox, 1000, 700);
        stage.setScene(scene2);
        stage.setTitle("Answers");
        stage.show();
    }



    private List<Qphenorow> getDemoItems() {
        PhenoItemDemoGenerator generator = new PhenoItemDemoGenerator(this.ontology);
        List<Qphenorow> phenomap = new ArrayList<>();
        for (PhenoItem item : generator.generateItems()) {
            phenomap.add(new Qphenorow(item));
        }
        return phenomap;
    }

}
