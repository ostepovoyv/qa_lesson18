package in.reqres.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

public class ProjectConfig {

    public static final PropertiesInterface PROPS = ConfigFactory.create(PropInterfaceTest.class, System.getProperties());

    @Config.LoadPolicy(Config.LoadType.MERGE)
    @Config.Sources({"system:properties", "classpath:test.properties"})
    interface PropInterfaceTest extends PropertiesInterface {
    }

    public interface PropertiesInterface extends Config {

        @Key("token")
        String getToken();

        @Key("email")
        String getEmail();

        @Key("password")
        String getPassword();
    }

}
