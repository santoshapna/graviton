package org.graviton.service.api.model.usage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsageData {

    private final Map<String, List<UsageDataLineItem>> usageDataForUser;

    public UsageData() {
        this.usageDataForUser = new HashMap<>();
    }

    public Map<String, List<UsageDataLineItem>> getUsageDataForUser() {
        return usageDataForUser;
    }

}
