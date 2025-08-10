package kr.nerdlab.json.schema.test.entity;

import java.util.Map;

public interface ServletDefinition {

    String getServletName();

    String getServletClass();

    Map<String, String> getInitParams();
}
