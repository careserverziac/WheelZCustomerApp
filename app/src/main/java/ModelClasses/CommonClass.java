package ModelClasses;

public class CommonClass {


    private String Image_path;
    public String category,manufacture,cc,bhp,topspeed,bodytype,fuelname,model_name,saleprice,chargingtime;
    public String com_code,state_code,city_code,com_name,com_address,city_name,state_name,com_pin,com_email,ctype_name,
            com_website,com_contact,com_contact_mobno,logo_image,com_phone,com_lng,com_lat,com_contact_email;




    public String vehhis_code,jobtype_name,Veh_modelname,total_amt,kms_done,jc_datec,cveh_code,imgdoc_path;
    public  String wuser_code,com_code1,engine_no,regis_no,registrationno,vehiclemodelname;

/*
    String wuser_code1 = jsonObject.getString("wuser_code");
    String com_code = jsonObject.getString("com_code");
    String engine_no = jsonObject.getString("engine_no");
    String chasis_no = jsonObject.getString("chasis_no");
    String regis_no = jsonObject.getString("reg_no");
    String Veh_modelname = jsonObject.getString("model_name");
*/

    public CommonClass(){}


    public CommonClass(String image_path, String category, String manufacture, String cc, String bhp, String topspeed, String bodytype,
                       String fuelname, String model_name, String saleprice, String chargingtime, String com_code, String state_code,
                       String city_code, String com_name, String com_address, String city_name, String state_name, String com_pin,
                       String com_email, String ctype_name, String com_website, String com_contact, String com_contact_mobno,
                       String logo_image, String com_phone, String com_lng, String com_lat, String com_contact_email,
                       String wuser_code,String com_code1,String registrationno,String vehiclemodelname,String engine_no,String regis_no) {



        this. Image_path = image_path;
        this.category = category;
        this.manufacture = manufacture;
        this.cc = cc;
        this.bhp = bhp;
        this.topspeed = topspeed;
        this.bodytype = bodytype;
        this.fuelname = fuelname;
        this.model_name = model_name;
        this.saleprice = saleprice;
        this.chargingtime = chargingtime;
        this.com_code = com_code;
        this.state_code = state_code;
        this.city_code = city_code;
        this.com_name = com_name;
        this.com_address = com_address;
        this.city_name = city_name;
        this.state_name = state_name;
        this.com_pin = com_pin;
        this.com_email = com_email;
        this.ctype_name = ctype_name;
        this.com_website = com_website;
        this.com_contact = com_contact;
        this.com_contact_mobno = com_contact_mobno;
        this.logo_image = logo_image;
        this.com_phone = com_phone;
        this.com_lng = com_lng;
        this.com_lat = com_lat;
        this.com_contact_email = com_contact_email;
        this.registrationno = registrationno;
        this.vehiclemodelname = vehiclemodelname;
        this. wuser_code = wuser_code;
        this. com_code1 = com_code1;
        this. engine_no = engine_no;
        this. regis_no = regis_no;


    }

    public String getImgdoc_path() {
        return imgdoc_path;
    }

    public void setImgdoc_path(String imgdoc_path) {
        this.imgdoc_path = imgdoc_path;
    }

    public String getCveh_code() {
        return cveh_code;
    }

    public void setCveh_code(String cveh_code) {
        this.cveh_code = cveh_code;
    }

    public String getJc_datec() {
        return jc_datec;
    }

    public void setJc_datec(String jc_datec) {
        this.jc_datec = jc_datec;
    }

    public String getJobtype_name() {
        return jobtype_name;
    }

    public void setJobtype_name(String jobtype_name) {
        this.jobtype_name = jobtype_name;
    }

    public String getVeh_modelname() {
        return Veh_modelname;
    }

    public void setVeh_modelname(String veh_modelname) {
        Veh_modelname = veh_modelname;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getKms_done() {
        return kms_done;
    }

    public void setKms_done(String kms_done) {
        this.kms_done = kms_done;
    }

    public String getVehhis_code() {
        return vehhis_code;
    }

    public void setVehhis_code(String vehhis_code) {
        this.vehhis_code = vehhis_code;
    }

    public String getWuser_code() {
        return wuser_code;
    }

    public void setWuser_code(String wuser_code) {
        this.wuser_code = wuser_code;
    }

    public String getCom_code1() {
        return com_code1;
    }

    public void setCom_code1(String com_code1) {
        this.com_code1 = com_code1;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getRegis_no() {
        return regis_no;
    }

    public void setRegis_no(String regis_no) {
        this.regis_no = regis_no;
    }

    public String getRegistrationno() {
        return registrationno;
    }

    public void setRegistrationno(String registrationno) {
        this.registrationno = registrationno;
    }

    public String getVehiclemodelname() {
        return vehiclemodelname;
    }

    public void setVehiclemodelname(String vehiclemodelname) {
        this.vehiclemodelname = vehiclemodelname;
    }

    public String getImage_path() {
        return Image_path;
    }

    public void setImage_path(String image_path) {
        Image_path = image_path;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getChargingtime() {
        return chargingtime;
    }

    public void setChargingtime(String chargingtime) {
        this.chargingtime = chargingtime;
    }

    public String getBhp() {
        return bhp;
    }

    public void setBhp(String bhp) {
        this.bhp = bhp;
    }

    public String getTopspeed() {
        return topspeed;
    }

    public void setTopspeed(String topspeed) {
        this.topspeed = topspeed;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getFuelname() {
        return fuelname;
    }

    public void setFuelname(String fuelname) {
        this.fuelname = fuelname;
    }

    public String getCom_code() {
        return com_code;
    }

    public void setCom_code(String com_code) {
        this.com_code = com_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getCom_address() {
        return com_address;
    }

    public void setCom_address(String com_address) {
        this.com_address = com_address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCom_pin() {
        return com_pin;
    }

    public void setCom_pin(String com_pin) {
        this.com_pin = com_pin;
    }

    public String getCom_email() {
        return com_email;
    }

    public void setCom_email(String com_email) {
        this.com_email = com_email;
    }

    public String getCtype_name() {
        return ctype_name;
    }

    public void setCtype_name(String ctype_name) {
        this.ctype_name = ctype_name;
    }

    public String getCom_website() {
        return com_website;
    }

    public void setCom_website(String com_website) {
        this.com_website = com_website;
    }

    public String getCom_contact() {
        return com_contact;
    }

    public void setCom_contact(String com_contact) {
        this.com_contact = com_contact;
    }

    public String getCom_contact_mobno() {
        return com_contact_mobno;
    }

    public void setCom_contact_mobno(String com_contact_mobno) {
        this.com_contact_mobno = com_contact_mobno;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }

    public String getCom_phone() {
        return com_phone;
    }

    public void setCom_phone(String com_phone) {
        this.com_phone = com_phone;
    }

    public String getCom_lng() {
        return com_lng;
    }

    public void setCom_lng(String com_lng) {
        this.com_lng = com_lng;
    }

    public String getCom_lat() {
        return com_lat;
    }

    public void setCom_lat(String com_lat) {
        this.com_lat = com_lat;
    }

    public String getCom_contact_email() {
        return com_contact_email;
    }

    public void setCom_contact_email(String com_contact_email) {
        this.com_contact_email = com_contact_email;
    }

    /*  public void setMobile_no(String mobile_no) {
        try {

            double mobileNoDouble = Double.parseDouble(mobile_no);
            long mobileNoLong = (long) mobileNoDouble;
            String formattedMobileNo = String.format("%010d", mobileNoLong);
            this.mobile_no = formattedMobileNo;
        } catch (NumberFormatException e) {
            System.err.println("Invalid mobile number format: " + mobile_no);
        }
    }*/



}
