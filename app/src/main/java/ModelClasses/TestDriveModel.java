package ModelClasses;

public class TestDriveModel {
    private String modelName;
    private String companyName;
    private String testDriveDate;
    private String testDriveTime;
    private String status;
    private String customerName;
    private String contactNumber;

    // Constructor
    public TestDriveModel(String modelName, String companyName, String testDriveDate,
                          String testDriveTime, String status, String customerName,
                          String contactNumber) {
        this.modelName = modelName;
        this.companyName = companyName;
        this.testDriveDate = testDriveDate;
        this.testDriveTime = testDriveTime;
        this.status = status;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
    }

    // Getters
    public String getModelName() { return modelName; }
    public String getCompanyName() { return companyName; }
    public String getTestDriveDate() { return testDriveDate; }
    public String getTestDriveTime() { return testDriveTime; }
    public String getStatus() { return status; }
    public String getCustomerName() { return customerName; }
    public String getContactNumber() { return contactNumber; }
}
