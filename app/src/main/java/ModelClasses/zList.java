package ModelClasses;

 public  class zList
{
    private String name;
    private String code;
    private String wuser_code,com_code,vehhis_code,reg_no;

    public String getReg_no() {return reg_no;}

    public void setReg_no(String reg_no) {this.reg_no = reg_no;}

    public String getWuser_code() {return wuser_code;}

    public void setWuser_code(String wuser_code) {this.wuser_code = wuser_code;}

    public String getCom_code() {return com_code;}

    public void setCom_code(String com_code) {this.com_code = com_code;}

    public String getVehhis_code() {return vehhis_code;}

    public void setVehhis_code(String vehhis_code) {this.vehhis_code = vehhis_code;}

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }
    public String get_code() {
        return code;
    }
    public void set_code(String code) {
        this.code = code;
    }


}
