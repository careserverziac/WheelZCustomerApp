package ModelClasses;

public class ServiceDetails {
    private String modelName;
       private String serviceType;
       private String totalAmount;
       private String kmDone;
       private String dateAndTime;
       private int imageResId;


       public ServiceDetails(String modelName, String serviceType, String totalAmount, String kmDone, String dateAndTime, int imageResId) {
           this.modelName = modelName;
           this.serviceType = serviceType;
           this.totalAmount = totalAmount;
           this.kmDone = kmDone;
           this.dateAndTime = dateAndTime;
           this.imageResId = imageResId;
       }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getKmDone() {
        return kmDone;
    }

    public void setKmDone(String kmDone) {
        this.kmDone = kmDone;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}

