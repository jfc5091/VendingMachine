import java.util.List;

public class Refunding extends VMState{

    private List<VendingMachine.Coin> coins;
    private final long refundDelay;

    public Refunding (Long refundDelay, List<VendingMachine.Coin> coins){
        this.coins = coins;
        this.refundDelay = refundDelay;
    }

    @Override
    public synchronized void refund() {
        System.out.println("Stop mashing the button!");
    }

    @Override
    public synchronized void dispenseProduct() {
        System.out.println("Can't purchase while refunding!");
    }

    @Override
    public synchronized void insertCoin(VendingMachine.Coin coin) {
        System.out.println("Can't insert coins while refunding in progress.");
    }
}
