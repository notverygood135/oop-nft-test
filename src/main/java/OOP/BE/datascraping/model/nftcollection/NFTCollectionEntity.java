package OOP.BE.datascraping.model.nftcollection;

import OOP.BE.datascraping.model.Entity;

public abstract class NFTCollectionEntity implements Entity {
    private static int numEntity = 0;
    protected  String id,name,url;
    protected double floorPrice,volume,volumeChange;
    protected int numOfSales,numOwners,totalSupply;

    public NFTCollectionEntity(String id, String name, String url, double floorPrice, double volume, double volumeChange, int numOfSales, int numOwners, int totalSupply) {
        this.numEntity++;
        this.id = id;
        this.name = name;
        this.url = url;
        this.floorPrice = floorPrice;
        this.volume = volume;
        this.volumeChange = volumeChange;
        this.numOfSales = numOfSales;
        this.numOwners = numOwners;
        this.totalSupply = totalSupply;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public double getFloorPrice() {
        return floorPrice;
    }

    public double getVolume() {
        return volume;
    }

    public double getVolumeChange() {
        return volumeChange;
    }

    public int getNumOfSales() {
        return numOfSales;
    }

    public int getNumOwners() {
        return numOwners;
    }

    public int getTotalSupply() {
        return totalSupply;
    }

    @Override
    public void printDetail(){
        System.out.println("Thuc the: \n"+"1,id: "+getId()+"\n2,name "+getName()+"\n 3,URL "+getUrl()+"\n4,Floor price: "
        +getFloorPrice()+"\n5,Volume: "+getVolume()+"\n6,Volume change: "+getVolumeChange()
        +"\n7,Numofsales: "+getNumOfSales()+"\n8,Numowners: "+getNumOwners()+"\n9,TotalSupply: "+getTotalSupply());
    }
}
