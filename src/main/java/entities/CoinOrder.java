package entities;

import dtos.CoinOrderDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "coin_order")
@Entity
public class CoinOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String coinName;
    private int amount;
    private long buyPrice;

    @ManyToOne
    private User user;

    public CoinOrder() {
    }


    public CoinOrder(CoinOrderDTO coinOrderDTO) {
        this.coinName = coinOrderDTO.getCoinName();
        this.amount = coinOrderDTO.getAmount();
        this.buyPrice = coinOrderDTO.getBuyPrice();

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        user.addOrders(this);
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static List<CoinOrderDTO> toDtos(List<CoinOrder> coinOrders) {
        List<CoinOrderDTO> dtoList = new ArrayList();
        for (CoinOrder c : coinOrders) {
            dtoList.add(new CoinOrderDTO(c));
        }
        return dtoList;

    }
}