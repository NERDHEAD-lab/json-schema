package kr.nerdlab.json.schema.test.entity;

import java.util.List;

public interface WebAppConfiguration {

    String getDisplayName();

    List<ServletDefinition> getServlets();

    List<ServletMapping> getServletMappings();
}
