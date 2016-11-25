package com.srgood.reasons.app;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.config.ConfigPersistenceUtils;
import com.srgood.reasons.utils.config.ConfigUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import net.dv8tion.jda.core.entities.Guild;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static com.srgood.reasons.ReasonsMain.jda;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane x1;

    @FXML TitledPane aSTab;

    @FXML
    private Button PowerButton;

    @FXML
    private TextArea console;

    @FXML
    private TextArea errConsole;

    @FXML
    private Button loadXMLButton;

    @FXML
    private Button saveXMLButton;

    @FXML
    private TextArea xmlEditArea;

    @FXML TextArea servers;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'sample.fxml'.";

        PowerButton.setOnAction(event -> jda.shutdown());

        loadXMLButton.setOnAction(event -> loadXMLToEditArea());

        saveXMLButton.setOnAction(event -> {
            try {
                ConfigUtils.initFromStream(new ByteArrayInputStream(xmlEditArea.getText().getBytes(StandardCharsets.UTF_8))); // see http://stackoverflow.com/questions/782178/how-do-i-convert-a-string-to-an-inputstream-in-java
                loadXMLToEditArea();
                ConfigPersistenceUtils.writeXML();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        StringBuilder sb = new StringBuilder();

        for (Guild guild : jda.getGuilds()) {
            sb.append(guild.getName());
            sb.append("\n");
        }
        servers.setText(sb.toString());
        aSTab.setText("Active Servers (" + jda.getGuilds().size() + ")");
    }

    private void loadXMLToEditArea() {
        try {
            xmlEditArea.setText(ConfigPersistenceUtils.generateCleanXML());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateConsole() {
        console.setText(ReasonsMain.out.toString());
    }

    @FXML
    public void updateErrConsole() {
        errConsole.setText(ReasonsMain.errOut.toString());
    }





}