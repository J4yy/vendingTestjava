import org.junit.Ignore;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VendingMachineTest {
    private static VendingMachine vm;

    @BeforeClass
    public static void setUp() {
        vm = VendingMachineFactory.createVendingMachine();
    }

    @AfterClass
    public static void tearDown() {
        vm = null;
    }

    @Test
    public void testBuyItemWithExactPrice() {
        long price = vm.selectItemAndGetPrice(Item.COKE);
        assertEquals(Item.COKE.getPrice(), price);
        vm.insertCoin(Coin.TENK);
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        assertEquals(Item.COKE, item);
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithMorePrice() {
        long price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);
        vm.insertCoin(Coin.TWOHK);
        vm.insertCoin(Coin.TWOHK);
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        assertEquals(Item.SODA, item);
        assertTrue(!change.isEmpty());
        assertEquals(400000 - Item.SODA.getPrice(), getTotal(change));
    }

    @Test
    public void testRefund() {
        long price = vm.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        vm.insertCoin(Coin.TENK);
        vm.insertCoin(Coin.TWEK);
        vm.insertCoin(Coin.FIFK);
        vm.insertCoin(Coin.ONEHK);
        vm.insertCoin(Coin.TWOHK);
        assertEquals(290000, getTotal(vm.refund()));
    }

    @Test(expected = SoldOutException.class)
    public void testSoldOut() {
        for (int i = 0; i < 10; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.TWOHK);
            vm.collectItemAndChange();
        }
    }

    @Test(expected = NotSufficientChangeException.class)
    public void testNotSufficientChangeException() {
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.SODA);
            vm.insertCoin(Coin.TWOHK);
            vm.insertCoin(Coin.TWOHK);
            vm.collectItemAndChange();
            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.TWOHK);
            vm.insertCoin(Coin.TWOHK);
            vm.collectItemAndChange();
        }
    }

    @Test(expected = SoldOutException.class)
    public void testReset() {
        VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
        vmachine.reset();
        vmachine.selectItemAndGetPrice(Item.COKE);
    }

    @Ignore
    public void testVendingMachineImpl() {
        VendingMachineImpl vm = new VendingMachineImpl();
    }

    private long getTotal(List<Coin> change) {
        long total = 0;
        for (Coin c : change) {
            total = total + c.getDenomination();
        }
        return total;
    }
}

