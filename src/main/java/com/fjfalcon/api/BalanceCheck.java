package com.fjfalcon.api;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fjfalcon on 14.05.17.
 */
public class BalanceCheck {
    private final OkHttpClient client;

    private final Request wallet1;
    private final Request wallet2;
    private final Request wallet3;


    public BalanceCheck(OkHttpClient client) {
        this.client = client;
        wallet1 = new Request.Builder()
                .url("https://api.blockcypher.com/v1/btc/main/addrs/115p7UMMngoj1pMvkpHijcRdfJNXj6LrLn/balance")
                .build();

        wallet2 = new Request.Builder()
                .url("https://api.blockcypher.com/v1/btc/main/addrs/13AM4VW2dhxYgXeQepoHkHSQuy6NgaEb94/balance")
                .build();

        wallet3 = new Request.Builder()
                .url("https://api.blockcypher.com/v1/btc/main/addrs/12t9YDPgwueZ9NyMgw519p7AA8isjr6SMw/balance")
                .build();
    }

    public BigDecimal getBalanceCheck() {
        try {
            return getBalance(wallet1).add(getBalance(wallet2)).add(getBalance(wallet3)).divide(new BigDecimal(100000000),BigDecimal.ROUND_HALF_UP, 4);
        } catch (IOException e) {
            System.out.println("API CALL EXCEPTION");
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getBalance(Request request) throws IOException{
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string()).getBigDecimal("balance");
    }
}
