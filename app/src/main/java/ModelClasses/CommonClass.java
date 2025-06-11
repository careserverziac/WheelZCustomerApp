package ModelClasses;

public class CommonClass {


    private String Image_path;
    public String category,manufacture,cc,bhp,topspeed,bodytype,fuelname,model_name,model_code,saleprice,chargingtime;
    public String com_code,state_code,city_code,com_name,com_address,city_name,state_name,com_pin,com_email,ctype_name,
            com_website,com_contact,com_contact_mobno,logo_image,com_phone,com_lng,com_lat,com_contact_email;

    public String chassis_no,batt_no,vcol_name,prv_serdt,nxt_serdt,image;

    public String service_code,service_date,service_time,service_type,pick_flag,drop_flag,additional_info
            ,location_map,service_address;


    public String vehhis_code,jobtype_name,Veh_modelname,total_amt,kms_done,jc_datec,cveh_code,imgdoc_path,imgdoc_code,file_type;
    public  String wuser_code,com_code1,engine_no,regis_no,registrationno,vehiclemodelname,mfg_name;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel_code() {
        return model_code;
    }

    public void setModel_code(String model_code) {
        this.model_code = model_code;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getPick_flag() {
        return pick_flag;
    }

    public void setPick_flag(String pick_flag) {
        this.pick_flag = pick_flag;
    }

    public String getDrop_flag() {
        return drop_flag;
    }

    public void setDrop_flag(String drop_flag) {
        this.drop_flag = drop_flag;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getLocation_map() {
        return location_map;
    }

    public void setLocation_map(String location_map) {
        this.location_map = location_map;
    }

    public String getService_address() {
        return service_address;
    }

    public void setService_address(String service_address) {
        this.service_address = service_address;
    }

    public String getChassis_no() {
        return chassis_no;
    }

    public void setChassis_no(String chassis_no) {
        this.chassis_no = chassis_no;
    }

    public String getBatt_no() {
        return batt_no;
    }

    public void setBatt_no(String batt_no) {
        this.batt_no = batt_no;
    }

    public String getVcol_name() {
        return vcol_name;
    }

    public void setVcol_name(String vcol_name) {
        this.vcol_name = vcol_name;
    }

    public String getPrv_serdt() {
        return prv_serdt;
    }

    public void setPrv_serdt(String prv_serdt) {
        this.prv_serdt = prv_serdt;
    }

    public String getNxt_serdt() {
        return nxt_serdt;
    }

    public void setNxt_serdt(String nxt_serdt) {
        this.nxt_serdt = nxt_serdt;
    }

    public String getMfg_name() {
        return mfg_name;
    }

    public void setMfg_name(String mfg_name) {
        this.mfg_name = mfg_name;
    }

    public String getImgdoc_code() {
        return imgdoc_code;
    }

    public void setImgdoc_code(String imgdoc_code) {
        this.imgdoc_code = imgdoc_code;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
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
