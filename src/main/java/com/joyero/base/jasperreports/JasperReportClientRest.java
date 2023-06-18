package com.joyero.base.jasperreports;

import com.joyero.base.jsf.Config;
import com.joyero.base.jsf.JsfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportClientRest  {

    @Autowired
    protected Config config;

    public byte[] getReport(String reportName, Map<String, Object> parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<byte[]> request = new HttpEntity<>(headers);

        String urlJasper = config.getUrlRest() + "jasperreport/report";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlJasper);
        builder.queryParam("reportName", reportName);
        generateUriFilter(parameters, builder);

        String uriString = builder.build(false).toUriString();

        ResponseEntity<byte[]> response = restTemplate.exchange(uriString, HttpMethod.GET, request, byte[].class);
        return response.getBody();
    }

    public String getBase64Creds() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        String base64Creds = (String) sessionMap.get("btoa");

        base64Creds = (String) JsfUtils.getFromSession("btoa");

        return base64Creds;
    }

    protected void generateUriFilter(Map<String, Object> filters, UriComponentsBuilder builder) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (entry.getValue() instanceof Date) {
                Date date = (Date) entry.getValue();

                if (date.getHours() > 0 || date.getMinutes() > 0) {
                    builder.queryParam(entry.getKey(), dtf.format(entry.getValue()));
                } else {
                    builder.queryParam(entry.getKey(), df.format(entry.getValue()));
                }
            } else if (entry.getValue() instanceof Collection) {
                List<Object> values = (List<Object>) entry.getValue();
                boolean isDate = values.get(0) instanceof Date;

                for (Object value : values) {
                    if (isDate) {
                        value = df.format((Date) value);
                    }
                    builder.queryParam(entry.getKey(), value);
                }

            } else {
                builder.queryParam(entry.getKey(), entry.getValue());
            }

        }
    }

}
