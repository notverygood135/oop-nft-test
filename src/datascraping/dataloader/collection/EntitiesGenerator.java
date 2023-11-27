package datascraping.dataloader.collection;

import datascraping.model.CollectionEntity;

import java.util.Collection;

public class EntitiesGenerator {
    private DataLoader[] loaders;
    public EntitiesGenerator(){
        loaders = new DataLoader[]{
            new JsonLoaderBinance("binance1d.json"),
            new JsonLoaderBinance("binance7d.json"),
            new JsonLoaderOpenSea("opensea1d.json"),
            new JsonLoaderOpenSea("opensea7d.json"),
            new JsonLoaderRarible("rarible1d.json"),
            new JsonLoaderRarible("rarible7d.json"),
            new JsonLoaderNiftyGateway("niftygateway1d.json"),
            new JsonLoaderNiftyGateway("niftygateway7d.json")
        };
    }
    public void generate(){
        for(DataLoader dataLoader : loaders){
            int dem = 0;
            Collection<CollectionEntity> test = dataLoader.load();
            String loaderClassName = dataLoader.getClass().getSimpleName();
            String label = loaderClassName.substring(0, loaderClassName.indexOf("Json"));

            for(CollectionEntity x : test){
                dem++;
                System.out.println("Thuc the thu " + dem);
                x.printDetail();
            }
            System.out.println("Thuc the: " + label + "co tong so luong la: " + dem);
        }
    }

    public static void main(String[] args) {
        new EntitiesGenerator().generate();
    }
}
