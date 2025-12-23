package com.hackathon.backend.domain.gikbti.entity;

public enum GikbtiCategory {
    MORNING_EVENING("아침형/저녁형"),
    CLEAN_DIRTY("깔끔형/자유형"),
    SENSITIVE_DULL("예민형/둔감형"),
    EXTRO_INTRO("외향형/내향형");

    private final String description;

    GikbtiCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

