public class Bicycle {
    private String frame;
    private String groupset;
    private String brakes;
    private String chainring;

    public void setFrame(String frame) { this.frame = frame; }
    public void setGroupset(String groupset) { this.groupset = groupset; }
    public void setBrakes(String brakes) { this.brakes = brakes; }
    public void setChainring(String chainring) { this.chainring = chainring; }

    @Override
    public String toString() {
        return String.format("Bicycle [Frame = %s, Groupset = %s, Brakes = %s, Chainring = %s]",
                frame, groupset, brakes, chainring);
    }
}