package com.varentechnologies.pagingmissioncontrol;

import com.varentechnologies.pagingmissioncontrol.entity.TelemetryData;
import com.varentechnologies.pagingmissioncontrol.router.TelemetryDataContentBasedRouter;
import com.varentechnologies.pagingmissioncontrol.util.TelemetryDataDeserializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class InputFileLoader implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${filename}")
    private String filename;

    /**
     * Want to be able to conditionally disable automatic file processing to make testing easier.
     */
    @Value("${file.auto-process:true}")
    private Boolean autoProcessFile;

    private boolean processed = false;

    @Autowired
    private TelemetryDataContentBasedRouter router;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        process(filename, true);
    }

    public void process(String filename, boolean automatic) {
        if ((StringUtils.isNotBlank(filename) && automatic && autoProcessFile) || !automatic) {
            log.info("Loading data file {}...", filename);
            File file = new File(filename);
            List<TelemetryData> data = TelemetryDataDeserializerUtil.fromPipeDelimitedFile(file);
            router.process(data);
            processed = true;
        }
    }

    public boolean isProcessed() {
        return processed;
    }
}
