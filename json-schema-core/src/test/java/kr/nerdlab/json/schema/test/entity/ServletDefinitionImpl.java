package kr.nerdlab.json.schema.test.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.nerdlab.json.schema.annotation.SchemaProperty;

import java.util.Map;

public class ServletDefinitionImpl implements ServletDefinition {

    @SchemaProperty(
            description = "A unique name for the servlet, used for mapping.",
            examples = "HelloWorldServlet",
            defaultValue = "HelloWorld",
            required = true
    )
    @JsonProperty("servlet-name")
    private String servletName;

    @SchemaProperty(
            description = "The fully qualified class name of the servlet. This class must implement the kr.nerdlab.server.servlet.Servlet interface.",
            examples = "com.example.HelloWorldServlet",
            defaultValue = "kr.nerdlab.demo.servlet.HelloWorldServlet",
            required = true
    )
    @JsonProperty("servlet-class")
    private String servletClass;

    @SchemaProperty(
            description = "Optional initialization parameters for the servlet, passed to the servlet's init() method.",
            examples = "{\"greeting\": \"Hello\"}"
    )
    @JsonProperty("init-params")
    private Map<String, String> initParams;

    // @formatter:off
    @Override public String getServletName() { return servletName; }
    public void setServletName(String servletName) { this.servletName = servletName; }
    @Override public String getServletClass() { return servletClass; }
    public void setServletClass(String servletClass) { this.servletClass = servletClass; }
    @Override public Map<String, String> getInitParams() { return initParams; }
    public void setInitParams(Map<String, String> initParams) { this.initParams = initParams; }
    // @formatter:on
}
