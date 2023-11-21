package datascraping.model;

public class Binance extends CollectionEntity{

    public Binance(String id, String name, String url, double floorPrice, double volume, double volumeChange, int numOfSales, int numOwners, int totalSupply) {
        super(id, name, url, floorPrice, volume, volumeChange, numOfSales, numOwners, totalSupply);
    }
}
