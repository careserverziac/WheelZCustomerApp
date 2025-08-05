package ModelClasses;

public class LessDrivenClass {

    private String ld_mfgname,ld_vmodelcode,ld_vimg,ld_vname,ld_vcolor, man_year,ld_vcategory,vart_name,
            trans_name, own_type,ld_vcc,km_drvn,fuel_name, sell_price,ld_dname,ld_dmob,ld_altermob,
            ld_dmail,ld_dcity,createdby,vart_code,num_plate,fuel_code,vmodel_code,vehmas_code,reg_no,
            insc_type,lis_type,city_code,state_code;



    public LessDrivenClass(){

    }

    public LessDrivenClass(String ld_mfgname, String ld_vimg, String ld_vmodelcode, String ld_vname, String ld_vcolor, String ld_vcategory,
                           String man_year, String vart_name, String trans_name,
                           String own_type, String ld_vcc, String km_drvn, String fuel_name, String sell_price,
                           String ld_dname, String ld_dmob, String ld_altermob, String ld_dmail, String ld_dcity){

        this.ld_mfgname = ld_mfgname;
        this.ld_dname = ld_dname;
        this.ld_dmob = ld_dmob;
        this.ld_altermob = ld_altermob;
        this.ld_dmail = ld_dmail;
        this.ld_dcity = ld_dcity;
        this.sell_price = sell_price;
        this.ld_vmodelcode = ld_vmodelcode;
        this.ld_vimg = ld_vimg;
        this.ld_vname = ld_vname;
        this.ld_vcolor=ld_vcolor;
        this.man_year = man_year;
        this.ld_vcategory=ld_vcategory;
        this.vart_name = vart_name;
        this.trans_name = trans_name;
        this.own_type = own_type;
        this.ld_vcc=ld_vcc;
        this.km_drvn = km_drvn;
        this.fuel_name = fuel_name;

    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getLd_mfgname() {
        return ld_mfgname;
    }

    public void setLd_mfgname(String ld_mfgname) {
        this.ld_mfgname = ld_mfgname;
    }

    public String getLd_altermob() {
        return ld_altermob;
    }

    public void setLd_altermob(String ld_altermob) {
        this.ld_altermob = ld_altermob;
    }

    public String getLd_dname() {
        return ld_dname;
    }

    public void setLd_dname(String ld_dname) {
        this.ld_dname = ld_dname;
    }

    public String getLd_dmob() {
        return ld_dmob;
    }

    public void setLd_dmob(String ld_dmob) {
        this.ld_dmob = ld_dmob;
    }

    public String getLd_dmail() {
        return ld_dmail;
    }

    public void setLd_dmail(String ld_dmail) {
        this.ld_dmail = ld_dmail;
    }

    public String getLd_dcity() {
        return ld_dcity;
    }

    public void setLd_dcity(String ld_dcity) {
        this.ld_dcity = ld_dcity;
    }

    public String getSell_price() {
        return sell_price;
    }
    public void setSell_price(String sell_price) {
        int sprice=(int) Double.parseDouble(sell_price);
        this.sell_price = String.valueOf(sprice);}




    public String getLd_vmodelcode() {
        return ld_vmodelcode;
    }

    public void setLd_vmodelcode(String ld_vmodelcode) {
        this.ld_vmodelcode = ld_vmodelcode;
    }

    public String getLd_vcategory() {
        return ld_vcategory;
    }

    public void setLd_vcategory(String ld_model) {
        this.ld_vcategory = ld_model;
    }

    public String getLd_vimg() {
        return ld_vimg;
    }

    public void setLd_vimg(String ld_vimg) {
        this.ld_vimg = ld_vimg;
    }

    public String getLd_vname() {
        return ld_vname;
    }

    public void setLd_vname(String ld_vname) {
        this.ld_vname = ld_vname;
    }

    public String getLd_vcolor() {
        return ld_vcolor;
    }

    public void setLd_vcolor(String ld_vcolor) {
        this.ld_vcolor = ld_vcolor;
    }

    public String getMan_year() {
        return man_year;
    }


    public void setMan_year(String veh_year) {int yearvalue = (int) Double.parseDouble(veh_year);this.man_year = String.valueOf(yearvalue);}

    public String getVart_name() {
        return vart_name;
    }

    public void setVart_name(String ld_vtype) {
        this.vart_name = ld_vtype;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public String getOwn_type() {
        return own_type;
    }

    public void setOwn_type(String own_type) {
        this.own_type = own_type;
    }

    public String getLd_vcc() {
        return ld_vcc;
    }

    public void setLd_vcc(String ld_vcc) {
        this.ld_vcc = ld_vcc;
    }

    public String getKm_drvn() {
        return km_drvn;
    }

    //  public void setKm_drvn(String km_drvn) {this.km_drvn = km_drvn;}
    public void setKm_drvn(String veh_kms) {int kmvalues = (int) Double.parseDouble(veh_kms);this.km_drvn = String.valueOf(kmvalues);}

    public String getFuel_name() {
        return fuel_name;
    }

    public void setFuel_name(String fuel_name) {
        this.fuel_name = fuel_name;
    }

    public String getVart_code() {
        return vart_code;
    }

    public void setVart_code(String vart_code) {
        this.vart_code = vart_code;
    }

    public String getNum_plate() {
        return num_plate;
    }

    public void setNum_plate(String num_plate) {
        this.num_plate = num_plate;
    }

    public String getFuel_code() {
        return fuel_code;
    }

    public String getVmodel_code() {
        return vmodel_code;
    }

    public void setVmodel_code(String vmodel_code) {
        this.vmodel_code = vmodel_code;
    }

    public void setFuel_code(String fuel_code) {this.fuel_code = fuel_code;}

    public String getVehmas_code() {return vehmas_code;}
    public void setVehmas_code(String vehmas_code) {
        this.vehmas_code = vehmas_code;
    }

    public String getReg_no() {return reg_no;}

    public void setReg_no(String reg_no) {this.reg_no = reg_no;}

    public String getInsc_type() {return insc_type;}

    public void setInsc_type(String insc_type) {this.insc_type = insc_type;}

    public String getLis_type() {return lis_type;}

    public void setLis_type(String lis_type) {this.lis_type = lis_type;}

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
}

