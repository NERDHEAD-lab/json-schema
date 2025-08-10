package kr.nerdlab.json.schema.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kr.nerdlab.json.schema.annotation.SchemaDefinition;
import kr.nerdlab.json.schema.annotation.SchemaProperty;
import kr.nerdlab.json.schema.annotation.SchemaVersion;

import java.util.List;

@SchemaDefinition(
        version = SchemaVersion.DRAFT_07,
        title = "Simple Web Server Web Application manifest configuration schema (web.json)",
        description = "Schema for defining a web application in the Simple Web Server. " +
                "This configuration file is used to specify servlets and their mappings."
)
@JsonIgnoreProperties({"$schema"})
public class WebAppConfigurationImpl implements WebAppConfiguration {

    @SchemaProperty(
            description = "A human-readable name for the web application.",
            example = "My Awesome App",
            defaultValue = "Demo Application"
    )
    @JsonProperty("display-name")
    private String displayName;

    @SchemaProperty(
            description = "A list of servlet definitions for this web application."
    )
    @JsonProperty("servlets")
    @JsonDeserialize(contentAs = ServletDefinitionImpl.class)
    private List<ServletDefinition> servlets;

    @SchemaProperty(
            description = "A list of servlet mappings to URL patterns."
    )
    //map ServletMappingImpl
    @JsonProperty("servlet-mappings")
    @JsonDeserialize(contentAs = ServletMappingImpl.class)
    private List<ServletMapping> servletMappings;

    // @formatter:off
    @Override public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    @Override public List<ServletDefinition> getServlets() { return servlets; }
    public void setServlets(List<ServletDefinition> servlets) { this.servlets = servlets; }
    @Override public List<ServletMapping> getServletMappings() { return servletMappings; }
    public void setServletMappings(List<ServletMapping> servletMappings) { this.servletMappings = servletMappings; }
    // @formatter:on
}
