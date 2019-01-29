package main;

import impl.FindDiff;
import impl.ListsOf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.TextAreaAppender;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ihar_Chekan on 2/1/2017.
 */
public class MainPage {

    final static Logger logger = Logger.getLogger(MainPage.class);

    @FXML
    Button buttonStart;
    @FXML
    Button buttonOssLicenseFilePath;
    @FXML
    TextField ossLicenseFilePath;
    @FXML
    TextArea output;

    @FXML
    void buttonInit (ActionEvent event) {
        if (event.getTarget() instanceof Button) {
            if ( event.getTarget() == buttonStart )
                buttonStartAction();
            else if ( event.getTarget() == buttonOssLicenseFilePath )
                buttonOpenOssLicenseFilePathAction();
        }
    }

    public void buttonStartAction() {
        output.setText( "" );
        ListsOf.allFromLicenceFile.clear();
        ListsOf.allFromFileNames.clear();
        ListsOf.jarToComponentList.clear();

        TextAreaAppender.setTextArea(output);

        if ( ossLicenseFilePath.getText().isEmpty() ) {
            logger.info("The only field is empty!");
        }
        else {
            buttonStart.setDisable( true );
            ListsOf.pathToOSSLicenceFile = ossLicenseFilePath.getText();

            Thread thread = new Thread() {
                public void run() {
                    OSSLicenseCheckerRun ossLicenseCheckerRun = new OSSLicenseCheckerRun();
                    ossLicenseCheckerRun.ossLicenseCheckerRun();
                    buttonStart.setDisable( false );
                    logger.info( "Finished." );
                }
            };
            thread.start();
        }
    }

    public void buttonOpenOssLicenseFilePathAction() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose OSS License file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            ossLicenseFilePath.setText(file.getAbsolutePath());
        }
    }

}

