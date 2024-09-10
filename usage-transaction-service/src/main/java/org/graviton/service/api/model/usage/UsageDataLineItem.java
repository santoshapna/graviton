package org.graviton.service.api.model.usage;

import org.graviton.service.api.model.Service;

public class UsageDataLineItem {
    private long timeStamp;
    private String userId;
    private Service service;

    public UsageDataLineItem(long timeStamp, String userId, Service service) {
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.service = service;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
