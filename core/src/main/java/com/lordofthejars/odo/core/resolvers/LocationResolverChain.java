package com.lordofthejars.odo.core.resolvers;

import com.lordofthejars.odo.api.LocationResolver;
import com.lordofthejars.odo.api.OdoConfiguration;
import java.nio.file.Path;

public class LocationResolverChain {

    static final String ODO_VERSION = "odo.version";
    OperatingSystemConfig operatingSystemConfig;

    public LocationResolverChain() {
        this.operatingSystemConfig = new OperatingSystemConfig(System.getProperty("os.name"));
    }

    public LocationResolver getLocationResolver(OdoConfiguration odoConfiguration) {

        if (odoConfiguration.isLocalOdoSet()) {
            return getLocalLocationResolver(odoConfiguration.getLocalOdo());
        }

        final String odoBinary = this.operatingSystemConfig.resolveDiferenciaBinary();

        if (System.getProperty(ODO_VERSION) != null) {
            return getUrlLocationResolver(odoBinary, System.getProperty(ODO_VERSION));
        }

        if (odoConfiguration.isVersionSet()) {
            return getUrlLocationResolver(odoBinary, odoConfiguration.getVersion());
        }

        return getClasspathLocationResolver(odoBinary);
    }

    private LocationResolver getClasspathLocationResolver(String odoBinary) {
        return new ClasspathLocationResolver(odoBinary);
    }

    private LocationResolver getUrlLocationResolver(String odoBinary, String version) {
        return new UrlLocationResolver(odoBinary, version);
    }

    private LocationResolver getLocalLocationResolver(Path path) {
        return new LocalLocationResolver(path);
    }

}