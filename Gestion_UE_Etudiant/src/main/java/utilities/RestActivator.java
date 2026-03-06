package utilities;

import filtres.AuthenticationFilter;
import ressources.AuthenticationEndPoint;
import ressources.ModuleRessources;
import ressources.UERessources;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestActivator extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AuthenticationEndPoint.class);
        resources.add(ModuleRessources.class);
        resources.add(UERessources.class);
        resources.add(AuthenticationFilter.class);
        return resources;
    }
}
