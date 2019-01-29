package objects;

/**
 * Created by Ihar_Chekan on 5/17/2017.
 */
public class JarFile {

  private String jarFileName;
  private String jarFileVersion;

  public JarFile( String jarFileName, String jarFileVersion ) {
    this.jarFileName = jarFileName;
    this.jarFileVersion = jarFileVersion;
  }

  public String getJarFileName() {
    return jarFileName;
  }

  public String getJarFileVersion() {
    return jarFileVersion;
  }

}
