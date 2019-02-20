import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class VendingMachineTest {
    private static final long REFUND_DELAY = 100;
    private static final String NL = System.getProperty("line.separator");

    private VendingMachine underTest;
    private ByteArrayOutputStream bout;
    private PrintStream out;

    @Before
    public void runBeforeTests() {
        underTest = new VendingMachine(REFUND_DELAY);

        bout = new ByteArrayOutputStream();
        out = new PrintStream(bout);
        System.setOut(out);
    }

    @Test
    public void insertCoinAfterDispensable() {
        underTest.insertCoin(VendingMachine.Coin.DOLLAR);
        underTest.insertCoin(VendingMachine.Coin.NICKEL);

        assertExpectedOutput("Can't insert more coins.");
    }

    @Test
    public void insertCoinOverOneDollar() {
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);

        assertExpectedOutput("Exact change only.");
    }

    @Test
    public void refund() {
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.DIME);
        underTest.insertCoin(VendingMachine.Coin.NICKEL);

        underTest.refund();

        String expected = "A FIFTY_CENT_PIECE pops out. *clink*" + NL +
                "A QUARTER pops out. *clink*" + NL +
                "A DIME pops out. *clink*" + NL +
                "A NICKEL pops out. *clink*";
        assertExpectedOutput(expected);
    }

    @Test
    public void refundMashButton() throws InterruptedException {
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);

        Thread refund = new Thread( () -> {
            underTest.refund();
        });
        refund.start();
        underTest.refund();
        refund.join();

        String expected = "A FIFTY_CENT_PIECE pops out. *clink*" + NL +
                "Stop mashing the button!" + NL +
                "A QUARTER pops out. *clink*";
        assertExpectedOutput(expected);
    }

    @Test
    public void insertCoinRefunding() throws InterruptedException {
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.DIME);
        underTest.insertCoin(VendingMachine.Coin.NICKEL);

        Thread insert = new Thread( () -> {
            underTest.refund();
        });
        insert.start();
        Thread.sleep(REFUND_DELAY/2);
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);

        insert.join();

        String expected = "A QUARTER pops out. *clink*" + NL +
                "Can't insert coins while refunding in progress." + NL +
                "A DIME pops out. *clink*" + NL +
                "A NICKEL pops out. *clink*";
        assertExpectedOutput(expected);
    }

    @Test
    public void dispenseProductExactChange() {
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.DIME);
        underTest.insertCoin(VendingMachine.Coin.DIME);
        underTest.insertCoin(VendingMachine.Coin.NICKEL);

        underTest.dispenseProduct();

        assertExpectedOutput("Dispensing delicious goodies!");
    }

    @Test
    public void dispenseProductRefunding() throws InterruptedException {
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);
        underTest.insertCoin(VendingMachine.Coin.FIFTY_CENT_PIECE);

        Thread insert = new Thread( () -> {
            underTest.refund();
        });
        insert.start();
        Thread.sleep(REFUND_DELAY/2);
        underTest.dispenseProduct();

        insert.join();

        String expected = "A FIFTY_CENT_PIECE pops out. *clink*" + NL +
                "Can't purchase while refunding!" + NL +
                "A FIFTY_CENT_PIECE pops out. *clink*";
        assertExpectedOutput(expected);
    }

    @Test
    public void dispenseProductInsufficientFunds() {
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.QUARTER);
        underTest.insertCoin(VendingMachine.Coin.DIME);
        underTest.insertCoin(VendingMachine.Coin.DIME);

        underTest.dispenseProduct();

        assertExpectedOutput("Insufficient funds!");
    }

    private void assertExpectedOutput(String expectedNoNewline) {
        String expected = expectedNoNewline + NL;
        out.flush();
        byte[] data = bout.toByteArray();
        String actual = new String(data);

        assertEquals("Output does not match expected!", expected, actual);
    }
}