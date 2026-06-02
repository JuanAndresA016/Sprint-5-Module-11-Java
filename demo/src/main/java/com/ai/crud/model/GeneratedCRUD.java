package com.ai.crud.model;

public class GeneratedCRUD {

    private String entityName;
    private String entityCode;
    private String repositoryCode;
    private String serviceCode;
    private String controllerCode;
    private String rawResponse;

    public GeneratedCRUD() {}

    public GeneratedCRUD(String entityName, String entityCode, String repositoryCode,
                         String serviceCode, String controllerCode) {
        this.entityName = entityName;
        this.entityCode = entityCode;
        this.repositoryCode = repositoryCode;
        this.serviceCode = serviceCode;
        this.controllerCode = controllerCode;
    }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getEntityCode() { return entityCode; }
    public void setEntityCode(String entityCode) { this.entityCode = entityCode; }

    public String getRepositoryCode() { return repositoryCode; }
    public void setRepositoryCode(String repositoryCode) { this.repositoryCode = repositoryCode; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getControllerCode() { return controllerCode; }
    public void setControllerCode(String controllerCode) { this.controllerCode = controllerCode; }

    public String getRawResponse() { return rawResponse; }
    public void setRawResponse(String rawResponse) { this.rawResponse = rawResponse; }

    @Override
    public String toString() {
        return "GeneratedCRUD{entityName='" + entityName + "'}";
    }
}