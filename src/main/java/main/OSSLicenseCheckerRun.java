package main;

import impl.FindDiff;
import impl.ListsOf;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Ihar_Chekan on 5/18/2017.
 */
public class OSSLicenseCheckerRun {

  final static Logger logger = Logger.getLogger(OSSLicenseCheckerRun.class);

  public void ossLicenseCheckerRun() {
    try {
      ListsOf listsOf = new ListsOf();
      listsOf.fillJarToComponentList();
      listsOf.fillAllFromFileNames();
      listsOf.fillAllFromLicenceFile();

      FindDiff findDiff = new FindDiff();
      findDiff.jarDiff();
      findDiff.componentsDiff();
      findDiff.checkWebPage();
    } catch ( IOException e ) {
      logger.info( "IOException: " + e );
    } catch ( URISyntaxException use ) {
      logger.info( use );
    }

  }
}
