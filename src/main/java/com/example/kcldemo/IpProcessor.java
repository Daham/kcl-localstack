package com.example.kcldemo;


import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpProcessor implements IRecordProcessor {


    @Override
    public void initialize(InitializationInput initializationInput) {
        //Add the implementation
    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {

        processRecordsInput
                .getRecords()
                .forEach(record -> log.info("consumed the record: {}", new String(record.getData().array())));
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        //Add the implementation
    }
}
