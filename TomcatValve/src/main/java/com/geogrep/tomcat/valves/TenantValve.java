package com.geogrep.tomcat.valves;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Emulates 3Scale plugin by adding a pre-configured tenant_id and api_key parameters to the request if not present.
 * @author  Agustí Sánchez
 *
 */
public class TenantValve extends ValveBase {

    private Log logger = LogFactory.getLog(getClass());

    private Map<String, String> paramMap = new HashMap<>();

    /**
     * Initializes the parameter map with the parameters defined in server.xml.
     * @param params a comma-delimited lists of requests parameters to add; i.e use commas instead of ampersands.
     */
    public void setParams(String params) {
        StringTokenizer tokenizer = new StringTokenizer(params, ",");
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer paramTokenizer = new StringTokenizer(tokenizer.nextToken(), "=");
            paramMap.put(paramTokenizer.nextToken(), paramTokenizer.hasMoreTokens() ? paramTokenizer.nextToken() : null);
        }

        logger.info("Initialized with parameters" + paramMap);
    }

    public void setRequestmatches(String matches) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        if (logger.isDebugEnabled())
            logger.debug("Processing request \"" + request.getRequestURI() + "\".");

        for (String param : paramMap.keySet()) {
            if (request.getParameter(param) == null) {
                request.addParameter(param, new String[] { paramMap.get(param) });
            }
        }
        getNext().invoke(request, response);
    }

}
