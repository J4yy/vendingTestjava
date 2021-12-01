
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VendingMachineImpl implements VendingMachine {
    private Inventory<Coin> cashInventory = new Inventory<Coin>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public VendingMachineImpl() {
        initialize();
    }

    private void initialize() {
        //initialize machine with 5 coins of each denomination
        //and 5 cans of each Item
        for (Coin c : Coin.values()) {
            cashInventory.put(c, 10);
        }

        for (Item i : Item.values()) {
            itemInventory.put(i, 10);
        }

    }

    @Override
    public long selectItemAndGetPrice(Item item) {
        //check the item in inventory
        if (itemInventory.hasItem(item)) {
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please buy another item");
    }

    @Override
    public void insertCoin(Coin coin) {
        //add coin in machine
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }


    @Override
    public Bucket<Item, List<Coin>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        List<Coin> change = collectChange();

        return new Bucket<Item, List<Coin>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if (isFullPaid()) {
            if (hasSufficientChange()) {
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ",
                remainingBalance);
    }

    private List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    @Override
    public List<Coin> refund() {
        List<Coin> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }


    private boolean isFullPaid() {
        if (currentBalance >= currentItem.getPrice()) {
            return true;
        }
        return false;
    }


    private List<Coin> getChange(long amount) throws NotSufficientChangeException {
        List<Coin> changes = Collections.EMPTY_LIST;

        if (amount > 0) {
            changes = new ArrayList<Coin>();
            long balance = amount;
            while (balance > 0) {
                if (balance >= Coin.TWOHK.getDenomination()
                        && cashInventory.hasItem(Coin.TWOHK)) {
                    changes.add(Coin.TWOHK);
                    balance = balance - Coin.TWOHK.getDenomination();
                    continue;

                } else if (balance >= Coin.ONEHK.getDenomination()
                        && cashInventory.hasItem(Coin.ONEHK)) {
                    changes.add(Coin.ONEHK);
                    balance = balance - Coin.ONEHK.getDenomination();
                    continue;

                } else if (balance >= Coin.FIFK.getDenomination()
                        && cashInventory.hasItem(Coin.FIFK)) {
                    changes.add(Coin.FIFK);
                    balance = balance - Coin.FIFK.getDenomination();
                    continue;

                } else if (balance >= Coin.TWEK.getDenomination()
                        && cashInventory.hasItem(Coin.TWEK)) {
                    changes.add(Coin.TWEK);
                    balance = balance - Coin.TWEK.getDenomination();
                    continue;

                }
                else if (balance >= Coin.TENK.getDenomination()
                        && cashInventory.hasItem(Coin.TENK)) {
                    changes.add(Coin.TENK);
                    balance = balance - Coin.TENK.getDenomination();
                    continue;
                }
                else {
                    throw new NotSufficientChangeException("NotSufficientChange, Please try another product ");
                }
            }
        }

        return changes;
    }

    @Override
    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStats() {
        System.out.println("Total Sales : " + totalSales);
        System.out.println("Current Item Inventory : " + itemInventory);
        System.out.println("Current Cash Inventory : " + cashInventory);
    }


    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount) {
        boolean hasChange = true;
        try {
            getChange(amount);
        } catch (NotSufficientChangeException nsce) {
            return hasChange = false;
        }

        return hasChange;
    }

    private void updateCashInventory(List<Coin> change) {

        for (Coin c: change) {
            cashInventory.deduct(c);
        }
    }

    public long getTotalSales() {
        return totalSales;
    }
    public boolean GetFreeItem()
    {
        Random generator = new Random();
        int value=generator.nextInt(9)+1;
        int[] arr={1,2,3,4,5,6,7,8,9,10};
        for(int i=1;i<=10;i++)
        {
            if(arr[i]==value)
                return  true;
        }
        return  false;

    }

}


