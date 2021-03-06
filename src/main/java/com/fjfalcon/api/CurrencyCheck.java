package com.fjfalcon.api;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fjfalcon on 14.05.17.
 */
public class CurrencyCheck {

    private final BalanceCheck balanceCheck;
    private BigDecimal usdToBtc;
    private BigDecimal usdToRub;
    private String ethPrice;
    private String btcPrice;
    private final OkHttpClient client;
    private final Request exchangeRate;

    Request ethPriceRequest = new Request.Builder().url("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=BTC,USD,EUR,RUB").build();
    Request btcPriceRequest = new Request.Builder().url("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,EUR,RUB").build();


    public CurrencyCheck(String token) {
        this.client = new OkHttpClient();
        this.balanceCheck = new BalanceCheck(client);
        exchangeRate = new Request.Builder().url("http://apilayer.net/api/live?access_key="+token+"&source=USD&currencies=BTC,RUB").build();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::update, 0,60, TimeUnit.MINUTES);
    }

    public BigDecimal getUsdToRub() {
        return usdToRub;
    }

    public BigDecimal getUsdToBtc() {
        return usdToBtc;
    }

    public String getMessage() {
        BigDecimal btc = balanceCheck.getBalanceCheck();
        BigDecimal usd = btc.divide(usdToBtc,BigDecimal.ROUND_HALF_UP,2);
        BigDecimal rub = usd.multiply(usdToRub).setScale(2, BigDecimal.ROUND_HALF_UP);

        return String.format("BTC: %s, USD: %s, RUB: %s, Victims: %s",btc,usd,rub, balanceCheck.getVictims());
    }

    public String getPetyaMessage() {
        BigDecimal btc = balanceCheck.getPetyaBalance();
        BigDecimal usd = btc.divide(usdToBtc,BigDecimal.ROUND_HALF_UP,2);
        BigDecimal rub = usd.multiply(usdToRub).setScale(2, BigDecimal.ROUND_HALF_UP);

        return String.format("BTC: %s, USD: %s, RUB: %s, Victims: %s",btc,usd,rub, balanceCheck.getPetyaVictims());
    }

    private void update() {
        try {
            Response response = client.newCall(exchangeRate).execute();
            JSONObject object = new JSONObject(response.body().string()).getJSONObject("quotes");
            usdToBtc = object.getBigDecimal("USDBTC");
            usdToRub = object.getBigDecimal("USDRUB");
            ethPrice = client.newCall(ethPriceRequest).execute().body().string().replaceAll("[^a-zA-Z0-9,:.]", "");;
            btcPrice = client.newCall(btcPriceRequest).execute().body().string().replaceAll("[^a-zA-Z0-9,:.]", "");;

        } catch (IOException e) {
            System.out.println("api error");;
        }

    }
    public String getBtcBalance(String wallet) {
        return balanceCheck.getBtcBalance(wallet) + " " + btcPrice;
    }

    public String getEthBalance(String wallet) {
        return balanceCheck.getEthBalance(wallet) + " " + ethPrice;
    }

    public String getEthPrice() {
        return ethPrice;
    }

    public String getBtcPrice() {
        return btcPrice;
    }
}
