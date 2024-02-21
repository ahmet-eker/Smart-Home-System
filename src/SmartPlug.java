import java.time.Duration;
import java.time.LocalDateTime;

public class SmartPlug extends SmartDevice{
    private double ampere;
    private boolean plug = false;
    private double consumed_power;
    private int voltage = 220;
    private LocalDateTime start_time;

    public SmartPlug(String name, String status, double ampere) {
        this.ampere = ampere;
        this.setStatus(status);
        this.setName(name);
        this.plug = true;
        if(getStatus().equals("On")){
            start_time = Global.global_time;
        }
    }

    public SmartPlug(String name, String status) {
        this.setStatus(status);
        this.setName(name);
        this.plug = false;
    }

    public SmartPlug(String name) {
        this.setName(name);
        this.plug = false;
    }

    public double getAmpere() {
        return ampere;
    }

    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }

    public boolean isPlug() {
        return plug;
    }

    public void setPlug(boolean plug) {
        this.plug = plug;
        if(plug && getStatus().equals("On")){
            start_time = Global.global_time;
        }
        else if (!plug && getStatus().equals("On")){
            add_power(voltage, getAmpere());
            start_time = null;
        }
    }

    public void add_power(int voltage, double ampere){
        double time = Duration.between(start_time, Global.global_time).toMinutes()/60.00;
        consumed_power += voltage*ampere*(time);
    }

    public double getConsumed_power() {
        return consumed_power;
    }

    public void setConsumed_power(double consumed_power) {
        this.consumed_power = consumed_power;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStatus(String status) {
        if(status.equals("On") && isPlug()){
            start_time = Global.global_time;
        } else if (status.equals("Off") && isPlug()) {
            add_power(voltage, ampere);
            start_time = null;
        }
        super.setStatus(status);
    }

    public void zreport(){
        String switchtime = "null";
        if(getSwitch_time() != null){
            switchtime = getSwitch_time().format(Command.formatter1);
        }
        else {
            switchtime = "null";
        }
        Global.filehandler.Write("Smart Plug " + getName() + " is " + getStatus().toLowerCase() + " and consumed " + Global.df.format(getConsumed_power()).replace(".",",") + "W so far (excluding current device), and its time to switch its status is " + switchtime + ".");
    }

}
