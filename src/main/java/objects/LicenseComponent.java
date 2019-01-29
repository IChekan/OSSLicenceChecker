package objects;

/**
 * Created by Ihar_Chekan on 5/17/2017.
 */
public class LicenseComponent {

  private String componentName;
  private String componentVersion;
  private String componentWebPage;

  public LicenseComponent( String componentName, String componentVersion, String webPage ) {
    this.componentName = componentName;
    this.componentVersion = componentVersion;
    this.componentWebPage = webPage;
  }

  public String getComponentName() {
    return componentName;
  }

  public String getComponentVersion() {
    return componentVersion;
  }

  public String getWebPage() {
    return componentWebPage;
  }

}
