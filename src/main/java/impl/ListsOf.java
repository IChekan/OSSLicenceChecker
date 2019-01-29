package impl;

import objects.JarFile;
import objects.JarToComponent;
import objects.LicenseComponent;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ihar_Chekan on 5/16/2017.
 */
public class ListsOf {

  final static Logger logger = Logger.getLogger(ListsOf.class);

  public static String pathToOSSLicenceFile;

  // components/jarnames;versions are stored as List with ";" as separator
  public static List<LicenseComponent> allFromLicenceFile = new ArrayList<>();
  public static List<JarFile> allFromFileNames = new ArrayList<>();
  // "=" as separator
  public static List<JarToComponent> jarToComponentList = new ArrayList<>();


  public void fillAllFromLicenceFile(  ) throws IOException {

    File input = new File( pathToOSSLicenceFile );
    Document doc = Jsoup.parse( input, "UTF-8", "http://example.com/" );

    Elements contents = doc.getElementsByTag( "tbody" );
    Element content = null;
    for ( Element element : contents ) {
      if ( element.childNodes().size() > 20 ) {
        content = element;
      }
    }
    List<Node> nodes = content.childNodes();
    for ( Node node : nodes ) {
      try {
        TextNode tempNode1 = (TextNode) node.childNode( 0 ).childNode( 0 );
        TextNode tempNode2 = (TextNode) node.childNode( 1 ).childNode( 0 );
        Node tempNode3 = node.childNode( 2 ).childNode( 0 );

        String component = tempNode1.text();
        String version = tempNode2.text();
        String webPage = tempNode3.attr( "href" );
        allFromLicenceFile.add( new LicenseComponent( component, version, webPage ) );

        TextNode tempNode4 = (TextNode) node.childNode( 3 ).childNode( 0 );
        if ( tempNode4.text().isEmpty() ) {
          logger.info( "License In Effect field is empty for '" + component + "' component." );
        }

      } catch ( IndexOutOfBoundsException e ) {
        TextNode tempNode1 = (TextNode) node.childNode( 0 ).childNode( 0 );
        logger.info( "Component in OSS file w/o version: " + tempNode1.text() );
      }
    }
  }

  public void fillAllFromFileNames(  ) throws IOException {

    File input = new File( pathToOSSLicenceFile );

    List<Path> allJarFiles = Files.find( Paths.get( input.getParentFile().toString() + File.separator + "lib" ), 3,
      ( p, bfa ) -> bfa.isRegularFile() && p.getFileName().toString()
        .matches( ".+?\\.jar" ) ).collect( Collectors.toList() );

    List<String> allFileNames = new ArrayList<>();
    for ( Path jar : allJarFiles ) {
      String fileWithoutExtention =
        jar.getFileName().toString().substring( 0, jar.getFileName().toString().length() - 4 );
      allFileNames.add( fileWithoutExtention );
    }

    for ( String fileName : allFileNames ) {
      String[] temp = fileName.split( "(?<=-)(?=[0-9])", 2 );
      try {
        allFromFileNames.add( new JarFile( temp[ 0 ].substring( 0, temp[ 0 ].length() - 1 ), temp[ 1 ] ) );
      } catch ( ArrayIndexOutOfBoundsException e ) {
        logger.info( "Jar file w/o version: " + fileName + ".jar" );
        //allFromFileNames.add( new JarFile( temp[ 0 ].substring( 0, temp[ 0 ].length() - 1 ), "" ) );
      }
    }
  }

  public void fillJarToComponentList() throws IOException, URISyntaxException {

    Path propFile = Paths.get(Paths.get(Paths.get( ListsOf.class.getProtectionDomain().getCodeSource().getLocation().toURI() )
      .getParent().toAbsolutePath().normalize().toString() + File.separator + "jarToComponent.properties").toAbsolutePath().normalize().toString());

    InputStreamReader inputStreamReader;

    if( Files.isRegularFile( propFile ) ) {
      File fileToLoad = new File( propFile.toString() );
      logger.info( "Found " + propFile.toString() + " , using it." );
      inputStreamReader = new FileReader( fileToLoad );
    }
    else {
      logger.info("File 'jarToComponent.properties' is not found on path: '" + propFile.toString() + "'. Using bundled .properties file");
      ClassLoader classLoader = getClass().getClassLoader();
      inputStreamReader = new InputStreamReader( classLoader.getResourceAsStream("jarToComponent.properties") ) ;
    }

    try {
      BufferedReader br = new BufferedReader( inputStreamReader );

      String line = br.readLine();
      while ( line != null ) {
        if ( !line.startsWith( "#" ) && !line.isEmpty() ) {
          try {
            jarToComponentList.add( new JarToComponent( line.split( "=" )[ 0 ].trim(), line.split( "=" )[ 1 ].trim() ) );
          } catch ( ArrayIndexOutOfBoundsException e ) {
            logger.info( "Something wrong in 'jarToComponent.properties' file, line: " + line );
          }
        }
        line = br.readLine();
      }
      br.close();
      inputStreamReader.close();
    } catch ( FileNotFoundException e ) {
      logger.info( e );
    }
  }


}
