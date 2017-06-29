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
public class BalanceCheck {
    private final OkHttpClient client;

    private BigDecimal wal1;
    private BigDecimal wal2;
    private BigDecimal wal3;
    private BigDecimal wal4;
    private Integer tx1;
    private Integer tx2;
    private Integer tx3;
    private Integer tx4;
    private final Request wallet1;
    private final Request wallet2;
    private final Request wallet3;
    private final Request petyaWallet;


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
        petyaWallet = new Request.Builder()
                .url("https://api.blockcypher.com/v1/btc/main/addrs/1Mz7153HMuxXTuR2R1t78mGSdzaAtNbBWX/balance")
                .build();


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::update, 0,30, TimeUnit.MINUTES);
    }

    public BigDecimal getBalanceCheck() {
            return wal1.add(wal2).add(wal3).divide(new BigDecimal(100000000),BigDecimal.ROUND_HALF_UP, 4);
    }

    public BigDecimal getPetyaBalance() {
        return wal4.divide(new BigDecimal(100000000),BigDecimal.ROUND_HALF_UP, 4);
    }

    public Integer getVictims() {
        return tx1+tx2+tx3;
    }

    public Integer getPetyaVictims() {
        return tx4;
    }


    public String getEthBalance(String wallet) {
        try {

            Response res = client.newCall(new Request.Builder().url("https://api.etherscan.io/api?module=account&action=balance&address="+wallet+"&tag=latest&apikey=Q6F699X2ABAPV9Q5SZDWAJN1N4UYK9S8M1").build()).execute();
            JSONObject object = new JSONObject(res.body().string());
            return "ETH: " + object.getBigDecimal("result").divide(new BigDecimal(1000000000000000000L),BigDecimal.ROUND_HALF_UP, 4).toString();
        } catch (IOException e) {
            return "API ERROR";
        }
   }

    public String getBtcBalance(String wallet) {
        try {
            Response res = client.newCall(new Request.Builder().url("https://api.blockcypher.com/v1/btc/main/addrs/"+wallet+"/balance")
                    .build()).execute();
            JSONObject object = new JSONObject(res.body().string());
            return "BTC: " + object.getBigDecimal("balance").divide(new BigDecimal(100000000),BigDecimal.ROUND_HALF_UP, 4).toString();
        } catch (IOException e) {
            return "API ERROR";
        }
    }



    private void update() {
        try {
            Response response = client.newCall(wallet1).execute();
            JSONObject object = new JSONObject(response.body().string());
            wal1 = object.getBigDecimal("balance");
            tx1 = object.getInt("final_n_tx");

            response = client.newCall(wallet2).execute();
            object = new JSONObject(response.body().string());
            wal2 = object.getBigDecimal("balance");
            tx2 = object.getInt("final_n_tx");

            response = client.newCall(wallet3).execute();
            object = new JSONObject(response.body().string());
            wal3 = object.getBigDecimal("balance");
            tx3 = object.getInt("final_n_tx");

            response = client.newCall(wallet3).execute();
            object = new JSONObject(response.body().string());
            wal3 = object.getBigDecimal("balance");
            tx3 = object.getInt("final_n_tx");

            response = client.newCall(petyaWallet).execute();
            object = new JSONObject(response.body().string());
            wal4 = object.getBigDecimal("balance");
            tx4 = object.getInt("final_n_tx");
        }
        catch (IOException e) {
            System.out.println("API CALL EXCEPTION");
            e.printStackTrace();
        }
    }
}
