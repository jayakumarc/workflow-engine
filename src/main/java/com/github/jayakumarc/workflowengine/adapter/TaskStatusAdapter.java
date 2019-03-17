package com.github.jayakumarc.workflowengine.adapter;

import com.github.jayakumarc.workflowengine.conf.DashboardService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties
public class TaskStatusAdapter implements JavaDelegate {

    @Autowired
    RestTemplate rest;

    @Autowired
    DashboardService dashboardService;

    private String restEndpoint(String id) {
        return String.format(dashboardService.baseUrl() + "/api/tasks/%s", id);
    }

    public static class TaskStatusRequest {
        public String status;
    }

    public static class TaskStatusResponse {
    }

    @Override
    public void execute(DelegateExecution ctx) throws Exception {
        TaskStatusRequest request = new TaskStatusRequest();
        request.status = "completed";
        String taskId = (String) ctx.getVariable("taskId");

        rest.put( //
                restEndpoint(taskId), //
                request, //
                TaskStatusResponse.class);
    }

}
