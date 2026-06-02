package com.ai.crud.model;

public class EntitySpec {

    private String entityName;
    private String rawDescription;

    public EntitySpec() {}

    public EntitySpec(String entityName, String rawDescription) {
        this.entityName = entityName;
        this.rawDescription = rawDescription;
    }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getRawDescription() { return rawDescription; }
    public void setRawDescription(String rawDescription) { this.rawDescription = rawDescription; }

    @Override
    public String toString() {
        return "EntitySpec{entityName='" + entityName + "', rawDescription='" + rawDescription + "'}";
    }
}