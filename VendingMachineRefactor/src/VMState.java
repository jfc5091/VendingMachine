import java.util.List;

abstract class VMState {

    private List<VendingMachine.Coin> coins;

    void refund(){}

    void dispenseProduct(){}

    public void insertCoin(VendingMachine.Coin coin){}

    public double calculateMoneyInserted() {
        double amount = 0.0;

        for(VendingMachine.Coin coin : this.coins) {
            amount += coin.amount();
        }
        return amount;
    }

}