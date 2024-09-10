package org.graviton.service.api;

import org.graviton.service.api.model.CreditPackage;
import org.graviton.service.api.model.Service;

public interface PackageAndServiceInfoLoader {
    void loadPackageAndServiceInfoFromFile(String path);
    Service getServiceByName(String serviceName);
    CreditPackage getPackageByName(String packageName);
}
