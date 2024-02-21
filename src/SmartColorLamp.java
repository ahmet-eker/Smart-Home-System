public class SmartColorLamp extends SmartLamp{
    private String mode = "white";
    private String color_code;

    public SmartColorLamp(String name, String status, int kelvin, int brightness) {
        super(name, status, kelvin, brightness);
    }

    public SmartColorLamp(String name, String status, String color_code, int brightness) {
        super(name, status);
        this.color_code = color_code;
        this.setBrightness(brightness);
    }

    public SmartColorLamp(String name, String status) {
        super(name, status);
    }

    public SmartColorLamp(String name) {
        super(name);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }
    @Override
    public void zreport(){
        if(mode.equals("white")){
            Global.filehandler.Write("Smart Color Lamp " + getName() + " is " + getStatus().toLowerCase() + " and its color value is " + getKelvin() + "K with " + getBrightness() + "% brightness, and its time to switch its status is " + getSwitch_time() + ".");
        }
        else if (mode.equals("color")){
            Global.filehandler.Write("Smart Color Lamp " + getName() + " is " + getStatus().toLowerCase() + " and its color value is " + getColor_code() + " with " + getBrightness() + "% brightness, and its time to switch its status is " + getSwitch_time() + ".");
        }
    }

}
