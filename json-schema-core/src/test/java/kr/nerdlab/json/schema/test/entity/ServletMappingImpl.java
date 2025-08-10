package kr.nerdlab.json.schema.test.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.nerdlab.json.schema.annotation.SchemaProperty;

public class ServletMappingImpl implements ServletMapping {

    @SchemaProperty(
            description = "The name of the servlet to map. This must match a 'servlet-name' from the servlets list.",
            example = "HelloWorldServlet",
            defaultValue = "HelloWorld",
            required = true
    )
    @JsonProperty("servlet-name")
    private String servletName;

    @SchemaProperty(
            description = "The URL pattern to map to the servlet. It must start with a '/'.",
            example = "/hello",
            defaultValue = "/hello",
            pattern = "^/.*",
            required = true
    )
    @JsonProperty("url-pattern")
    private String urlPattern;

    // @formatter:off
    @Override public String getServletName() { return servletName; }
    public void setServletName(String servletName) { this.servletName = servletName; }
    @Override public String getUrlPattern() { return urlPattern; }
    public void setUrlPattern(String urlPattern) { this.urlPattern = urlPattern; }
    // @formatter:on
}