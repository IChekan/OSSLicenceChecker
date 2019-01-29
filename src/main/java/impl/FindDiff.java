package impl;

import objects.JarFile;
import objects.JarToComponent;
import objects.LicenseComponent;
import org.apache.log4j.Logger;
import utils.UrlChecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Ihar_Chekan on 5/17/2017.
 */
public class FindDiff {

  final static Logger logger = Logger.getLogger(FindDiff.class);

  public void jarDiff() {

    for ( JarFile jarFile : ListsOf.allFromFileNames ) {
      boolean match = false;

      for ( JarToComponent jarToComponent : ListsOf.jarToComponentList ) {
        if ( jarFile.getJarFileName().equals( jarToComponent.getJarName() ) ) {
          // excluding pentaho components
          if ( jarToComponent.getComponentName().equals( "Pentaho Component Reuse" ) || jarFile.getJarFileName().contains( "pentaho" )) {
            match = true;
            break;
          }
          for ( LicenseComponent licComponent : ListsOf.allFromLicenceFile ) {
            if ( licComponent.getComponentName().contains( jarToComponent.getComponentName() ) ) {
              if ( licComponent.getComponentVersion().contains( jarFile.getJarFileVersion() ) ) {
                //it's a match!!!
                match = true;
                break;
              }
              else if ( jarFile.getJarFileVersion().contains( licComponent.getComponentVersion() ) ) {
                //it's a match!!!
                match = true;
                break;
              }
              // fix for jaxb version
              else if ( jarFile.getJarFileName().equals( "jaxb-impl" ) ) {
                if ( licComponent.getComponentVersion().split( "-" )[0].contains( jarFile.getJarFileVersion().split( "-" )[0] ) ) {
                  match = true;
                  break;
                }
              }
            }
          }
        }
      }

      if (!match) {
        logger.info( "Failed to find matching component for jar: " + jarFile.getJarFileName() + "-" + jarFile.getJarFileVersion() + ".jar" );
      }
    }
  }

  public void componentsDiff() {

    for ( LicenseComponent licComponent : ListsOf.allFromLicenceFile ) {
      boolean match = false;
      // excluding pentaho components
      if (licComponent.getComponentName().equals( "Pentaho Component Reuse" )) {
        match = true;
        break;
      }

      for ( JarToComponent jarToComponent : ListsOf.jarToComponentList ) {
        if ( licComponent.getComponentName().contains( jarToComponent.getComponentName() ) ) {
          for ( JarFile jarFile : ListsOf.allFromFileNames ) {
            if ( jarFile.getJarFileName().equals( jarToComponent.getJarName() ) ) {
              if ( licComponent.getComponentVersion().contains( jarFile.getJarFileVersion() ) ) {
                //it's a match!!!
                match = true;
                break;
              }
              else if ( jarFile.getJarFileVersion().contains( licComponent.getComponentVersion() ) ) {
                //it's a match!!!
                match = true;
                break;
              }
              // fix for jaxb version
              else if ( jarFile.getJarFileName().equals( "jaxb-impl" ) ) {
                if ( licComponent.getComponentVersion().split( "-" )[0].contains( jarFile.getJarFileVersion().split( "-" )[0] ) ) {
                  match = true;
                  break;
                }
              }
            }
          }
        }
      }
      if (!match) {
        logger.info( "Failed to find matching jar for component: '" + licComponent.getComponentName() + "' with version: " + licComponent.getComponentVersion() );
      }
    }
  }

  public void checkWebPage() {

    CountDownLatch latch = new CountDownLatch( ListsOf.allFromLicenceFile.size() );

    for ( LicenseComponent licComponent : ListsOf.allFromLicenceFile ) {

        Thread thread = new Thread() {
          public void run() {
            try {
              URL url = new URL( licComponent.getWebPage() );
              if ( !UrlChecker.urlChecker( url ) ) {
                logger.info(
                  "Url check failed: '" + licComponent.getWebPage() + "' from component '" + licComponent
                    .getComponentName()
                    + "'" );
              }
              latch.countDown();
            } catch ( MalformedURLException e ) {
              logger.info( e + " Thrown by Component: " + licComponent.getComponentName() + " Page: " + licComponent.getWebPage() );
              latch.countDown();
            }
          }
        };
        thread.start();
      }
      try {
        latch.await();
      } catch ( InterruptedException e ) {
      logger.info( e );
      }
  }

}
