package main;

import impl.FindDiff;
import impl.ListsOf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Ihar_Chekan on 5/10/2017.
 */
public class OSSLicenseCheckerMain extends Application {

  @Override
  public void start(Stage stage) throws Exception{
    stage.setTitle("OSS Licenses Checker");

    stage.setScene(
      createScene(
        loadMainPane()
      )
    );

    stage.show();
  }

  private Pane loadMainPane() throws IOException {
    FXMLLoader loader = new FXMLLoader();

    Pane mainPane = (Pane) loader.load(
      getClass().getResourceAsStream(
        Navigator.MAIN
      )
    );

    MainController mainController = loader.getController();

    Navigator.setMainController(mainController);
    Navigator.loadScene(Navigator.SCENE_1);

    return mainPane;
  }

  private Scene createScene( Pane mainPane) {
    Scene scene = new Scene(
      mainPane
    );

    return scene;
  }


  public static void main ( String[] args ) throws IOException {

    if (args.length == 0) {
      launch(args);
    }
    else if (args.length == 1) {
      ListsOf.pathToOSSLicenceFile = args[0];

      OSSLicenseCheckerRun ossLicenseCheckerRun = new OSSLicenseCheckerRun();
      ossLicenseCheckerRun.ossLicenseCheckerRun();

      System.exit( 0 );
    }
    else {
      System.err.println("Use UI version or " +
        "\\n usage: java -jar thisJar [Full Path To OSS License file]] " + "\\n");
      System.exit(-1);
    }
  }
}
