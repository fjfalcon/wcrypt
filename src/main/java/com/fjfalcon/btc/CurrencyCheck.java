package com.fjfalcon.btc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fjfalcon on 14.05.17.
 */
public class CurrencyCheck implements Runnable {

    private final BalanceCheck balanceCheck;

    private BigDecimal usdToBtc;
    private BigDecimal usdToRub;
    private final OkHttpClient client;
    private final Request exchangeRate;

    public CurrencyCheck(String token) {
        this.client = new OkHttpClient();
        this.balanceCheck = new BalanceCheck(client);
        exchangeRate = new Request.Builder().url("http://apilayer.net/api/live?access_key="+token+"&source=USD&currencies=BTC,RUB").build();
        new Thread(this).start();
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

        return "BTC: " + btc.toString() + " USD: " +usd.toString() + " RUB: " +rub.toString();
    }

    public void run() {
        while (true) {
            update();
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        try {
            Response response = client.newCall(exchangeRate).execute();
            JSONObject object = new JSONObject(response.body().string()).getJSONObject("quotes");
            usdToBtc = object.getBigDecimal("USDBTC");
            usdToRub = object.getBigDecimal("USDRUB");
        } catch (IOException e) {
            System.out.println("api error");;
        }

    }
}
