package storage;

public final class Barn extends Storage {
    public Barn(String name, double capacity) { super(name, capacity); }

    @Override
    public String getStorageType() { return "Амбар"; }
}
