package org.graviton.service.impl;

import org.graviton.data.access.CsvReader;
import org.graviton.service.api.PackageAndServiceInfoLoader;
import org.graviton.service.api.UsageDataService;
import org.graviton.service.api.model.Service;
import org.graviton.service.api.model.usage.UsageData;
import org.graviton.service.api.model.usage.UsageDataLineItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Currently this service will read the file with usage logs and populate the usage in memory data,
 * so that we can use it to create transaction summary;
 */
public class UsageDataServiceImpl implements UsageDataService {
    public static final String CSV_DELIMITER = ",";
    public static final int USAGE_LOG_COLUMNS = 3;
    private static final int TIME_INDEX = 0;
    private static final int USERID_INDEX = 1;
    private static final int SERVICE_NAME_INDEX = 2;

    private UsageData usageData;
    private PackageAndServiceInfoLoader packageService;
    private CsvReader csvReader;

    public UsageDataServiceImpl(PackageAndServiceInfoLoader packageService, CsvReader csvReader) {
        this.packageService = packageService;
        this.csvReader = csvReader;
        usageData = new UsageData();
    }

    public void populateUsageDataFromLogs(String filePath) {
        try(Stream<String> linesWithoutComment = csvReader.readCSV(filePath);) {
            linesWithoutComment.forEach(this::lineToData);
        } catch (IOException e) {
            //log
            throw new RuntimeException(e);
        }
    }

    private void lineToData(String line) {
        String[] values = line.split(CSV_DELIMITER);
        if(values.length == USAGE_LOG_COLUMNS) {
            long time = Long.parseLong(values[TIME_INDEX]);
            Service service = packageService.getServiceByName(values[SERVICE_NAME_INDEX]);
            if(service != null) {
                UsageDataLineItem dataLineItem = new UsageDataLineItem(time, values[USERID_INDEX], service);
                List<UsageDataLineItem> usageDataLineItems = usageData.getUsageDataForUser().
                        computeIfAbsent(dataLineItem.getUserId(), k -> new ArrayList<>());
                usageDataLineItems.add(dataLineItem);
            } else {
                // log invalid service
            }
        }
    }

    public UsageData getUsageData() {
        return usageData;
    }
}
