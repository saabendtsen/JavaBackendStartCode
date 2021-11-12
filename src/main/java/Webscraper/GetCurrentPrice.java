package Webscraper;

import dtos.CoinOrderDTO;
import utils.HttpUtils;

import java.io.IOException;

public class GetCurrentPrice {

    private String currentPrice;
    private CoinOrderDTO dto;
    private boolean isCalled = false;
    private String url;

    public GetCurrentPrice(CoinOrderDTO coin) {
        this.dto = coin;
        this.url = "https://api.coingecko.com/api/v3/simple/price?ids=" + coin.getCoinName() + "&vs_currencies=usd&include_market_cap=true&include_24hr_change=true";
    }


    public void doWork() throws IOException {
        if (isCalled) {
            return; //Tag values allready set
        }

        try {
            this.dto.setCurrentPrice(HttpUtils.fetchData(this.url));
            isCalled = true;
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public CoinOrderDTO getDto() {
        return dto;
    }

    public void setDto(CoinOrderDTO dto) {
        this.dto = dto;
    }
}
