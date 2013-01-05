package tomcatvalve;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class TestValve extends ValveBase {

    private Map<String, String> paramMap = new HashMap<>();

    public void setParams(String params) {
        StringTokenizer tokenizer = new StringTokenizer(params, ",");
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer paramTokenizer = new StringTokenizer(tokenizer.nextToken(), "=");
            paramMap.put(paramTokenizer.nextToken(), paramTokenizer.hasMoreTokens() ? paramTokenizer.nextToken() : null);
        }

        System.out.println("Initialized with " + paramMap);
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.printf("Processing request %s for context %s with referrer %s.\n", request.getRequestURI(), request.getContextPath(), request.getHeader("referer"));

        for (String param : paramMap.keySet()) {
            if (request.getParameter(param) == null) {
                request.addParameter(param, new String[] { paramMap.get(param) });
            }
        }
        getNext().invoke(request, response);
    }

}
