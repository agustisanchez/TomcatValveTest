package com.geogrep.tomcat.valves;

import java.util.regex.Pattern;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Pattern pattern = Pattern.compile("/[^/]*/api/.*|/[^/]*/proxy/.*|.*\\.html");
        
        System.out.println(pattern.matcher("patata.html").matches());
        
    }

}
