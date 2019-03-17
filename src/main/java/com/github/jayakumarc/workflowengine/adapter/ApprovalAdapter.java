package com.github.jayakumarc.workflowengine.adapter;

import java.util.Date;
import com.github.jayakumarc.workflowengine.conf.DashboardService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties
public class ApprovalAdapter implements JavaDelegate {

    @Autowired
    RestTemplate rest;

    @Autowired
    DashboardService dashboardService;

    private String restEndpoint() {
        return dashboardService.baseUrl() + "/api/tasks";
    }

    public static class TaskRequest {
        public String entityId;
        public String userId;
        public String type;
        public String url;
        public Date date;
        public String workflowId;
    }

    public static class TaskResponse {
        public String id;
    }

    @Override
    public void execute(DelegateExecution ctx) throws Exception {
        TaskRequest request = new TaskRequest();
        request.entityId = (String) ctx.getVariable("entityId");
        request.userId = (String) ctx.getVariable("userId");
        request.type = (String) ctx.getVariable("type");
        request.url = (String) ctx.getVariable("url");
        request.date = (Date) ctx.getVariable("date");
        request.workflowId = (String) ctx.getId();

        TaskResponse response = rest.postForObject( //
                restEndpoint(), //
                request, //
                TaskResponse.class);

        ctx.setVariable("taskId", response.id);
    }

}
