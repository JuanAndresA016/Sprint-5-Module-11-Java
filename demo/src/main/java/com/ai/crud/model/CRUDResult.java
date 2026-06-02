package com.ai.crud.model;

public class CRUDResult {

    private GeneratedCRUD crud;
    private String serviceTests;
    private String entityName;
    private boolean success;
    private String errorMessage;

    public CRUDResult() {}

    public CRUDResult(GeneratedCRUD crud, String serviceTests) {
        this.crud = crud;
        this.serviceTests = serviceTests;
        this.entityName = crud != null ? crud.getEntityName() : null;
        this.success = true;
    }

    public static CRUDResult error(String message) {
        CRUDResult result = new CRUDResult();
        result.success = false;
        result.errorMessage = message;
        return result;
    }

    public GeneratedCRUD getCrud() { return crud; }
    public void setCrud(GeneratedCRUD crud) { this.crud = crud; }

    public String getServiceTests() { return serviceTests; }
    public void setServiceTests(String serviceTests) { this.serviceTests = serviceTests; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}