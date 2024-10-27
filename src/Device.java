public abstract class Device {
    protected int number;
    protected SmsSystem system;

    public Device(int number, SmsSystem system) {
        this.number = number;
        this.system = system;
    }

    public int getNumber() {
        return number;
    }
    public abstract void terminate();
}
