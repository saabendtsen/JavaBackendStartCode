package dtos;

import entities.CoinOrder;

public class CoinOrderDTO {

    private Long id;
    private String coinName;
    private int amount;
    private long buyPrice;
    private String user;
    private String currentPrice;


    public CoinOrderDTO() {
    }

    public CoinOrderDTO(String coinName, int amount, long buyPrice,String user) {
        this.coinName = coinName;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoinOrderDTO(CoinOrder coinOrder) {
        if(coinOrder.getId() != null){
            this.id = coinOrder.getId();
        }
        this.coinName = coinOrder.getCoinName();
        this.amount = coinOrder.getAmount();
        this.buyPrice = coinOrder.getBuyPrice();
        this.user = coinOrder.getUser().getUsername();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(long buyPrice) {
        this.buyPrice = buyPrice;
    }
}
