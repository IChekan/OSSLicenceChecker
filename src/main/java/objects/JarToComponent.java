package objects;

/**
 * Created by Ihar_Chekan on 5/17/2017.
 */
public class JarToComponent {

  private String jarName;
  private String componentName;

  public JarToComponent( String jarName, String componentName ) {
    this.jarName = jarName;
    this.componentName = componentName;
  }

  public String getJarName() {
    return jarName;
  }

  public String getComponentName() {
    return componentName;
  }

}
