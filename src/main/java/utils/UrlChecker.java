package utils;

// import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ihar_Chekan on 5/24/2017.
 */
public class UrlChecker {

  // final static Logger logger = Logger.getLogger(UrlChecker.class);
  static int responseCode;

  public static boolean urlChecker ( URL url ) {
    try {
      HttpURLConnection huc = (HttpURLConnection) url.openConnection();
      huc.addRequestProperty("User-Agent", "Mozilla/5.01");
      responseCode = huc.getResponseCode();
      if (300 <= huc.getResponseCode() && huc.getResponseCode() <= 399) {
        urlChecker( new URL (huc.getHeaderField( "Location" ) ) );
      }
      return (responseCode == HttpURLConnection.HTTP_OK);
    } catch ( IOException e ) {
      // logger.info( "urlChecker throws IOExeption: " + e );
      return false;
    }
  }
}
