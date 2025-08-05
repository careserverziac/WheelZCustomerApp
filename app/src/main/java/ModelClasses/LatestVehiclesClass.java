package ModelClasses;

public class LatestVehiclesClass {

    private String latestmfgname,vehmas_code,latestvimage,latestvname,latestvcategory,latestvcolour,latestvdate,latestvcc,latestvcomname,latestvcommob,latestvcomaltermob,
            latestvcomemail,latestvcomcity,latestdealername,createdby,vart_code,num_plate,own_type,
            man_year,vart_name,trans_name,km_drvn,fuel_name,fuel_code,sell_price,vmodel_code,reg_no,insc_type,lis_type,city_code,state_code;


    public LatestVehiclesClass(){

    }

    public LatestVehiclesClass(String latestmfgname, String vehmas_code, String latestvimage, String latestvname,
                               String own_type, String man_year, String vart_name,
                               String trans_name, String latestvcolour, String km_drvn,
                               String fuel_name, String latestvdate, String latestvcc, String sell_price,
                               String latestvcategory, String latestvcomname, String latestvcommob, String latestvcomaltermob,
                               String latestvcomemail, String latestvcomcity, String latestdealername){


        this.latestmfgname= latestmfgname;
        this.latestdealername= latestdealername;
        this.latestvcommob = latestvcommob;
        this.latestvcomaltermob = latestvcomaltermob;
        this.latestvcomemail = latestvcomemail;
        this.latestvcomcity = latestvcomcity;
        this.vehmas_code = vehmas_code;
        this.latestvimage=latestvimage;
        this.latestvname=latestvname;
        this.own_type = own_type;
        this.man_year = man_year;
        this.vart_name = vart_name;
        this.trans_name = trans_name;
        this.latestvcolour=latestvcolour;
        this.km_drvn = km_drvn;
        this.fuel_name = fuel_name;
        this.latestvdate=latestvdate;
        this.latestvcc=latestvcc;
        this.sell_price = sell_price;
        this.latestvcategory=latestvcategory;

    }

    public String getFuel_code() {
        return fuel_code;
    }

    public void setFuel_code(String fuel_code) {
        this.fuel_code = fuel_code;
    }

    public String getNum_plate() {
        return num_plate;
    }

    public void setNum_plate(String num_plate) {
        this.num_plate = num_plate;
    }

    public String getVart_code() {
        return vart_code;
    }

    public void setVart_code(String vart_code) {
        this.vart_code = vart_code;
    }

    public String getLatestdealername() {
        return latestdealername;
    }

    public void setLatestdealername(String latestdealername) {
        this.latestdealername = latestdealername;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getLatestmfgname() {
        return latestmfgname;
    }

    public void setLatestmfgname(String latestmfgname) {
        this.latestmfgname = latestmfgname;
    }

    public String getLatestvcomaltermob() {
        return latestvcomaltermob;
    }

    public void setLatestvcomaltermob(String latestvcomaltermob) {
        this.latestvcomaltermob = latestvcomaltermob;
    }

    public String getLatestvcomname() {
        return latestvcomname;
    }

    public void setLatestvcomname(String latestvcomname) {
        this.latestvcomname = latestvcomname;
    }

    public String getLatestvcommob() {
        return latestvcommob;
    }

    public void setLatestvcommob(String latestvcommob) {
        this.latestvcommob = latestvcommob;
    }

    public String getLatestvcomemail() {
        return latestvcomemail;
    }

    public void setLatestvcomemail(String latestvcomemail) {
        this.latestvcomemail = latestvcomemail;
    }

    public String getLatestvcomcity() {
        return latestvcomcity;
    }

    public void setLatestvcomcity(String latestvcomcity) {
        this.latestvcomcity = latestvcomcity;
    }

    public String getLatestvcategory() {
        return latestvcategory;
    }

    public void setLatestvcategory(String latestvcategory) {
        this.latestvcategory = latestvcategory;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        int pricevalue=(int) Double.parseDouble(sell_price);
        this.sell_price = String.valueOf(pricevalue);
    }


    public String getLatestvcc() {
        return latestvcc;
    }

    public void setLatestvcc(String latestvcc) {
        this.latestvcc = latestvcc;
    }

    public String getVehmas_code() {return vehmas_code;}

    public void setVehmas_code(String vehmas_code) {this.vehmas_code = vehmas_code;}

    public String getLatestvimage() {
        return latestvimage;
    }

    public void setLatestvimage(String latestvimage) {
        this.latestvimage = latestvimage;
    }

    public String getLatestvname() {
        return latestvname;
    }

    public void setLatestvname(String latestvname) {
        this.latestvname = latestvname;
    }

    public String getOwn_type() {
        return own_type;
    }

    public void setOwn_type(String own_type) {
        this.own_type = own_type;
    }

    public String getMan_year() {
        return man_year;
    }

    public void setMan_year(String veh_year)  {int yearvalue = (int) Double.parseDouble(veh_year);
        this.man_year = String.valueOf(yearvalue);}


    public String getKm_drvn() {
        return km_drvn;
    }

    public void setKm_drvn(String veh_km) {
        int kilometervalue = (int) Double.parseDouble(veh_km);
        this.km_drvn = String.valueOf(kilometervalue);}

    public String getFuel_name() {
        return fuel_name;
    }



    public String getVart_name() {
        return vart_name;
    }

    public void setVart_name(String vart_name) {
        this.vart_name = vart_name;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public String getLatestvcolour() {
        return latestvcolour;
    }

    public void setLatestvcolour(String latestvcolour) {
        this.latestvcolour = latestvcolour;
    }


    public void setFuel_name(String fuel_name) {
        this.fuel_name = fuel_name;
    }

    public String getLatestvdate() {
        return latestvdate;
    }

    public void setLatestvdate(String latestvdate) {
        this.latestvdate = latestvdate;
    }

    public String getVmodel_code() {
        return vmodel_code;
    }

    public void setVmodel_code(String vmodel_code) {
        this.vmodel_code = vmodel_code;
    }

    public String getReg_no() {return reg_no;}

    public void setReg_no(String reg_no) {this.reg_no = reg_no;}

    public String getInsc_type() {return insc_type;}

    public void setInsc_type(String insc_type) {this.insc_type = insc_type;}

    public String getLis_type() {return lis_type;}

    public void setLis_type(String lis_type) {this.lis_type = lis_type;}

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
}
