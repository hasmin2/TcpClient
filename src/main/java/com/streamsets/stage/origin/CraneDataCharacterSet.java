package com.streamsets.stage.origin;

import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;

@GenerateResourceBundle
public enum CraneDataCharacterSet implements Label {
    AUTO("Auto Select"),
    ISO8859_1("ISO8859-1"),
    EUC_KR("EUC-KR"),
    ASCII("ASCII Code"),
    UNICODE("UNI Code"),;

    private final String label;

    CraneDataCharacterSet(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}