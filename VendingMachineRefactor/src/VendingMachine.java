import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertstjacquesjr on 2/22/17.
 */
public class VendingMachine {

    public enum Coin {
        NICKEL (0.05),
        DIME (0.10),
        QUARTER (0.25),
        FIFTY_CENT_PIECE (0.5),
        DOLLAR (1.00);

        private double amount;

        Coin(double amount) {
            this.amount = amount;
        }

        public double amount() {
            return amount;
        }
    }

    private List<Coin> coins;
    private boolean refunding;
    private boolean dispensable;

    private final long refundDelay;

    public VendingMachine() {
        this(500);
    }

    VendingMachine(long refundDelay) {
        coins = new ArrayList<>();
        refunding = false;
        dispensable = false;

        this.refundDelay = refundDelay;
    }

    public synchronized void insertCoin(Coin coin) {
        double amountAlreadyInserted = calculateMoneyInserted();
        if(dispensable) {
            System.out.println("Can't insert more coins.");
        }
        else if(amountAlreadyInserted + coin.amount() > 1.00) {
            System.out.println("Exact change only.");
        }
        else if(refunding) {
            System.out.println("Can't insert coins while refunding in progress.");
        }
        else {
            coins.add(coin);
            if(calculateMoneyInserted() == 1.00) {
                dispensable = true;
            }
        }
    }

    public synchronized void refund() {
        if(refunding) {
            System.out.println("Stop mashing the button!");
        }
        else {
            refunding = true;
            dispensable = false;
            for(Coin coin : coins) {
                System.out.println("A " + coin.toString() + " pops out. *clink*");
                // realistic simulation!
                try {
                    wait(refundDelay);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            coins.clear();
            refunding = false;
        }
    }

    public synchronized void dispenseProduct() {
        if(refunding) {
            System.out.println("Can't purchase while refunding!");
        }
        else if(dispensable) {
            System.out.println("Dispensing delicious goodies!");
            coins.clear();
            dispensable = false;
        }
        else {
            System.out.println("Insufficient funds!");
        }
    }

    private double calculateMoneyInserted() {
        double amount = 0.0;

        for(Coin coin : coins) {
            amount += coin.amount();
        }
        return amount;
    }
}