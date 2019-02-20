public interface VMState {

    void changeState();

    double calculateMoneyInserted();

    void refund();

    void dispenseProduct();

    void insertCoin();

}
