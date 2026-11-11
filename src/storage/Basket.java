package storage;

public final class Basket extends Storage {
    public Basket(String name, double capacity) { super(name, capacity); }

    @Override
    public String getStorageType() { return "Корзина"; }
}
