public enum Coin {
    TENK(10000), TWEK(20000), FIFK(50000), ONEHK(10000),TWOHK(200000);
    private int denomination;
    private Coin(int denomination){
        this.denomination = denomination;

    }
    public int getDenomination(){
        return denomination;
    }
}
