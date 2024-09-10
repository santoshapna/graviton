package org.graviton.service.api;

import org.graviton.service.api.model.usage.UsageData;

public interface UsageDataService {
    void populateUsageDataFromLogs(String filePath);
    UsageData getUsageData();
}
