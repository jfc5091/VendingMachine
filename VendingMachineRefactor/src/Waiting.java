import java.util.ArrayList;
import java.util.List;

public class Waiting extends VMState {

    private List<VendingMachine.Coin> coins;
    private final long refundDelay;

    public Waiting(Long refundDelay, List<VendingMachine.Coin> coins){
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
        System.out.println("Insufficient funds!");
    }

    @Override
    public synchronized void insertCoin(VendingMachine.Coin coin) {
        double amountAlreadyInserted = this.calculateMoneyInserted();
        if(amountAlreadyInserted + coin.amount() > 1.00) {
            System.out.println("Exact change only.");
        }
        else {
            coins.add(coin);
            if(this.calculateMoneyInserted() == 1.00) {
                //dispensable = true;
            }
        }
    }
}
