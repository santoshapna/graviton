package org.graviton.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.graviton.data.access.JsonReader;
import org.graviton.service.api.PackageAndServiceInfoLoader;
import org.graviton.service.api.model.CreditPackage;
import org.graviton.service.api.model.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageAndServiceInfoLoaderImpl implements PackageAndServiceInfoLoader {
    private Map<String, CreditPackage> packageDetails = new HashMap<>();
    private Map<String, Service> serviceDetails = new HashMap<>();
    private JsonReader jsonReader;

    public PackageAndServiceInfoLoaderImpl(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    @Override
    public void loadPackageAndServiceInfoFromFile(String path) {
        JsonNode jsonNode = jsonReader.readJsonNodeFromFile(path);
        ObjectMapper objectMapper = new ObjectMapper();
        populateServiceInfos(jsonNode, objectMapper);
        populatePackageInfos(jsonNode, objectMapper);
    }

    @Override
    public Service getServiceByName(String serviceName) {
        if (serviceName != null) {
            return serviceDetails.get(serviceName);
        }
        return null;
    }

    @Override
    public CreditPackage getPackageByName(String packageName) {
        if (packageName != null) {
            return packageDetails.get(packageName);
        }
        return null;
    }

    private void populatePackageInfos(JsonNode jsonNode, ObjectMapper objectMapper) {
        JsonNode packageJson = jsonNode.get("creditPackage");
        if(packageJson.isArray()) {
            List<CreditPackage> packages = null;
            ObjectReader reader = objectMapper.readerFor(new TypeReference<List<CreditPackage>>() {});
            try {
                packages = reader.readValue(packageJson);
                populatePackageDetails(packages);
            } catch (IOException e) {
                //log and throw exception
                throw new RuntimeException("Error while populating Service details from input file", e);
            }
        }
    }

    private void populatePackageDetails(List<CreditPackage> packages) {
        for (CreditPackage creditPackage : packages) {
            this.packageDetails.put(creditPackage.getName(), creditPackage);
        }
    }


    private void populateServiceInfos(JsonNode jsonNode, ObjectMapper objectMapper) {
        JsonNode serviceJson = jsonNode.get("servicePricing");
        if(serviceJson.isArray()) {
            List<Service> services = null;
            ObjectReader reader = objectMapper.readerFor(new TypeReference<List<Service>>() {});
            try {
                services = reader.readValue(serviceJson);
                populateServiceDetails(services);
            } catch (IOException e) {
                throw new RuntimeException("Error while populating Service details", e);
            }
        }
    }

    private void populateServiceDetails(List<Service> services) {
        for (Service service : services) {
            this.serviceDetails.put(service.getName(), service);
        }
    }
}
