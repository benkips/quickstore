package beautyplan.mabnets.quickstore;

public class cartz {
    private String id;
    private String product ;
    private String photo;
    private  Integer price;


    public cartz(String idd, String product1, String photo1,Integer price1) {
        this.id = idd;
        this.product = product1;
        this.photo = photo1;
        this.price = price1;
    }

    public String getIdd() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getPrice() {
        return price;
    }

}

