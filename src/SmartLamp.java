public class SmartLamp extends SmartDevice{

    private int kelvin = 4000;
    private int brightness = 100;

    public SmartLamp(String name, String status, int kelvin, int brightness) {
        this.setName(name);
        this.kelvin = kelvin;
        this.brightness = brightness;
        this.setStatus(status);
    }

    public SmartLamp(String name, String status) {
        this.setName(name);
        this.setStatus(status);
    }

    public SmartLamp(String name) {
        this.setName(name);
    }

    public int getKelvin() {
        return kelvin;
    }

    public void setKelvin(int kelvin) {
        this.kelvin = kelvin;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void zreport(){
        String switchtime = "null";
        if(getSwitch_time() != null){
            switchtime = getSwitch_time().format(Command.formatter1);
        }
        else {
            switchtime = "null";
        }
        Global.filehandler.Write("Smart Lamp " + getName() + " is " + getStatus().toLowerCase() + " and its kelvin value is " + getKelvin() + "K with " + getBrightness() + "% brightness, and its time to switch its status is " + switchtime + ".");
    }
}
