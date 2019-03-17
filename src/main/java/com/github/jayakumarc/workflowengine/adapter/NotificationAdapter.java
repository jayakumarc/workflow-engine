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
public class NotificationAdapter implements JavaDelegate {

    @Autowired
    RestTemplate rest;

    @Autowired
    DashboardService dashboardService;

    private String restEndpoint() {
        return dashboardService.baseUrl() + "/api/notifications";
    }

    public static class NotificationRequest {
        public String entityId;
        public String workflowId;
        public String message;
    }

    public static class NotificationResponse {
        public String id;
    }

    @Override
    public void execute(DelegateExecution ctx) throws Exception {
        String entityId = (String) ctx.getVariable("entityId");
        boolean approved = (boolean) ctx.getVariable("approved");

        NotificationRequest request = new NotificationRequest();
        request.entityId = entityId;
        request.workflowId = (String) ctx.getId();
        request.message = approved ? String.format("Entity %s has been approved", entityId)
                : String.format("Entity %s not approved", entityId);

        NotificationResponse response = rest.postForObject( //
                restEndpoint(), //
                request, //
                NotificationResponse.class);

        ctx.setVariable("notificationId", response.id);
    }

}
