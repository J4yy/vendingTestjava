public enum Item {
    COKE("Coke",10000), PEPSI("Pepsi", 10000), SODA("Soda", 20000);

    private String name;
    private int price;

    private Item(String name, int price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public long getPrice(){
        return price;
    }
}
