import java.util.List;

public class Dispensing extends VMState {

    private List<VendingMachine.Coin> coins;
    private final long refundDelay;

    public Dispensing (Long refundDelay, List<VendingMachine.Coin> coins){
        this.coins = coins;
        this.refundDelay = refundDelay;
    }

    @Override
    public synchronized void refund() {
        //refunding = true;
        //dispensable = false;
        for(VendingMachine.Coin coin : coins) {
            System.out.println("A " + coin.toString() + " pops out. *clink*");
            // realistic simulation!
            try {
                wait(refundDelay);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        coins.clear();
        //refunding = false;
    }

    @Override
    public synchronized void dispenseProduct() {
        System.out.println("Dispensing delicious goodies!");
        coins.clear();
        //dispensable = false;
    }

    @Override
    public synchronized void insertCoin(VendingMachine.Coin coin) {
        System.out.println("Can't insert more coins.");
    }
}
