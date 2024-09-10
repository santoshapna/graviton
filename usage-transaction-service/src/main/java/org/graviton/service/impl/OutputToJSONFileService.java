package org.graviton.service.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graviton.service.api.model.OutputToFileService;
import org.graviton.service.api.model.output.OutPutJsonFields;
import org.graviton.service.api.model.output.TransactionLine;
import org.graviton.service.api.model.output.TransactionSummary;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Writes Transaction summary as JSON to a file
 * todo: It can be moved to the data access layer
 */
public class OutputToJSONFileService implements OutputToFileService {
    @Override
    public void writeOutputToFile(List<TransactionSummary> txnSummaryForUsers, String filePath) {
        writeTxnSummaryToOutputFile(txnSummaryForUsers, filePath);
    }
    private void writeTxnSummaryToOutputFile(List<TransactionSummary> txnSummaryForUsers, String outputFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
            JsonGenerator generator = mapper.createGenerator(outputStream);
            generator.writeStartArray();
            for (TransactionSummary txnSummary : txnSummaryForUsers) {
                generator.writeStartObject();
                generator.writeStringField(OutPutJsonFields.getOutputFiledForUser(), txnSummary.getUser());
                generator.writeFieldName(OutPutJsonFields.getOutputFiledForTxns());
                writeLineItemsArr(txnSummary.getTransactions(), generator);
                generator.writeEndObject();
            }
            generator.writeEndArray();

            generator.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeLineItemsArr(List<TransactionLine> transactions, JsonGenerator generator) {
        try {
            generator.writeStartArray();

            for (TransactionLine lineItem : transactions) {
                generator.writeStartObject();
                generator.writeNumberField(OutPutJsonFields.getOutputFiledForTime(), lineItem.getTime());
                generator.writeStringField(OutPutJsonFields.getOutputFiledForType(), lineItem.getType().name());
                if (lineItem.getService() != null) {
                    generator.writeStringField(OutPutJsonFields.getOutputFiledForService(), lineItem.getService());
                }
                if (lineItem.getCreditPackage() != null) {
                    generator.writeStringField(OutPutJsonFields.getOutputFiledForPackage(), lineItem.getCreditPackage());
                }
                if (lineItem.getStatus() != null) {
                    generator.writeStringField(OutPutJsonFields.getOutputFiledForStatus(), lineItem.getStatus().name());
                }
                if (lineItem.getQuantity() > 0) {
                    generator.writeNumberField(OutPutJsonFields.getOutputFiledForQuantity(), lineItem.getQuantity());
                }
                if (lineItem.getBalance() >= 0) {
                    generator.writeNumberField(OutPutJsonFields.getOutputFiledForBalance(), lineItem.getBalance());
                }
                generator.writeEndObject();
            }
            generator.writeEndArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
