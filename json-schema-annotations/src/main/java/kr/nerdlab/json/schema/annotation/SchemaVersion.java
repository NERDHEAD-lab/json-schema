package kr.nerdlab.json.schema.annotation;

public enum SchemaVersion {

//    DRAFT_2020_12("https://json-schema.org/draft/2020-12/json-schema-core.html"),
//    DRAFT_2019_09("https://json-schema.org/draft/2019-09/draft-handrews-json-schema-02.html"),
    DRAFT_07("http://json-schema.org/draft-07/schema#"),
//    DRAFT_06("http://json-schema.org/draft-06/schema#"),
//    DRAFT_05("http://json-schema.org/draft-05/schema#"),
    ;

    private final String url;

    SchemaVersion(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
