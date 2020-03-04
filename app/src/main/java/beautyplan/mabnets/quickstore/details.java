package beautyplan.mabnets.quickstore;

public class details {
    private String email;
    private String phone;
    private String adress;

    public details(String email, String phone, String adress) {
        this.email = email;
        this.phone = phone;
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }
}
