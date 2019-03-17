package com.github.jayakumarc.workflowengine.rest;

import java.util.Date;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProcessEntityController {

    @Autowired
    private ProcessEngine camunda;

    public static class ProcessEntityRequest {
        public String entityId;
        public String userId;
        public String type;
        public String url;
        public Date date;
    }

    public static class ApprovalStatusRequest {
        public boolean approved;
    }

    @PostMapping("/process-entities")
    public void initiateEntityProcess(@RequestBody ProcessEntityRequest processEntityRequest) {
        camunda.getRuntimeService().startProcessInstanceByKey(//
                "process-entity", //
                Variables //
                        .putValue("entityId", processEntityRequest.entityId) //
                        .putValue("userId", processEntityRequest.userId) //
                        .putValue("type", processEntityRequest.type) //
                        .putValue("url", processEntityRequest.url) //
                        .putValue("date", processEntityRequest.date));
    }

    // TODO: FIXME - Should use workflowId instead of entityId
    @PutMapping("/process-entities/{id}")
    public void updateEntityStatus(@PathVariable(value = "id") String entityId,
            @RequestBody ApprovalStatusRequest request) {

        camunda.getRuntimeService().createMessageCorrelation("Message_ApprovalStatus") //
                .processInstanceVariableEquals("entityId", entityId) //
                .setVariable("approved", request.approved) //
                .correlateWithResult();
    }
}
