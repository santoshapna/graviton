package org.graviton.service.api.model;

import org.graviton.service.api.model.output.TransactionSummary;

import java.util.List;

public interface OutputToFileService {
    void writeOutputToFile(List<TransactionSummary> txnSummaryForUsers, String filePath);
}
