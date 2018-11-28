/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revolut.ibrahimfouad.monytransfer;

/**
 *
 * @author ibrahimfouad
 */
import com.revolut.ibrahimfouad.monytransfer.models.Account;
import com.revolut.ibrahimfouad.monytransfer.models.Transfer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Map;

public class Application extends AbstractVerticle {

    private final Map<Integer, Account>  accountsMap = new LinkedHashMap<>(); 
    private final Map<Integer, Transfer> transfersMap = new LinkedHashMap<>();


    public static void main(final String[] args) {
        Launcher.executeCommand("run", Application.class.getName());
    }

    @Override
    public void start(Future<Void> fut) {

        initData();
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>MoneyTransfer</h1>");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());
        // accounts
        router.get("/api/accounts").handler(this::getAllAccounts);
        router.get("/api/accounts/:id").handler(this::getAccount);
        router.post("/api/accounts").handler(this::addAccount);
        router.put("/api/accounts/:id").handler(this::updateAccount);
        router.delete("/api/accounts/:id").handler(this::deleteAccount);
        // transfers
        router.get("/api/transfers").handler(this::getAllTransfers);
        router.get("/api/transfers/:id").handler(this::getTransfer);
        router.post("/api/transfers").handler(this::addTransfer);
        router.put("/api/transfers/:id").handler(this::updateTransfer);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        8080,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

    private void getAllAccounts(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(accountsMap.values()));
    }

    private void getAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accountsMap.get(idAsInteger);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(account));
            }
        }
    }

    private void addAccount(RoutingContext routingContext) {
        try {
            final Account account = Json.decodeValue(routingContext.getBodyAsString(),
                    Account.class);
            accountsMap.put(account.getId(), account);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(account));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }

    private void updateAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accountsMap.get(idAsInteger);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                boolean updated = false;
                if (json.getString("name") != null && !json.getString("name").isEmpty()) {
                    account.setUsername(json.getString("name"));
                    updated = true;
                }
                if (json.getString("balance") != null && !json.getString("balance").isEmpty() && (new BigDecimal(json.getString("balance"))).compareTo(BigDecimal.ZERO) >= 0) {
                    account.setBalance(new BigDecimal(json.getString("balance")));
                    updated = true;
                }
                if (json.getString("currency") != null && !json.getString("currency").isEmpty()) {
                    try {
                        account.setCurrency(Currency.getInstance(json.getString("currency")));
                        updated = true;
                    } catch (Exception e) {
                        updated = false;
                    }
                }
                if (!updated) {
                    routingContext.response().setStatusCode(400).end();
                } else {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(account));
                }
            }
        }
    }

    private void deleteAccount(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else if (accountsMap.get(Integer.valueOf(id)) == null) {
            routingContext.response().setStatusCode(404).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            accountsMap.remove(idAsInteger);
            routingContext.response().setStatusCode(204).end();
        }
    }

    private void getAllTransfers(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(transfersMap.values()));
    }

    private void getTransfer(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Transfer transfer = transfersMap.get(idAsInteger);
            if (transfer == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(transfer));
            }
        }
    }

    private void addTransfer(RoutingContext routingContext) {
        try {
            final Transfer transfer = Json.decodeValue(routingContext.getBodyAsString(),
                    Transfer.class);
            transfersMap.put(transfer.getId(), transfer);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(transfer));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }

    private void updateTransfer(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Transfer transfer = transfersMap.get(idAsInteger);
            if (transfer == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                if (transfer.getStatus() != Transfer.TransferStatus.EXECUTED && transfer.getStatus() != Transfer.TransferStatus.FAILED && transfer.getAmount().compareTo(BigDecimal.ZERO) > 0 && accountsMap.get(transfer.getSourceAccountId()) != null && accountsMap.get(transfer.getDestinationAccountId()) != null && accountsMap.get(transfer.getSourceAccountId()).getBalance().compareTo(transfer.getAmount()) >= 0 && accountsMap.get(transfer.getSourceAccountId()).getCurrency().equals(accountsMap.get(transfer.getDestinationAccountId()).getCurrency()) && accountsMap.get(transfer.getSourceAccountId()).getCurrency().equals(transfer.getCurrency()) && accountsMap.get(transfer.getDestinationAccountId()).getCurrency().equals(transfer.getCurrency())) {
                    accountsMap.get(transfer.getSourceAccountId()).withdraw(transfer.getAmount());
                    accountsMap.get(transfer.getDestinationAccountId()).deposit(transfer.getAmount());
                    transfer.setStatus(Transfer.TransferStatus.EXECUTED);
                } else {
                    transfer.setStatus(Transfer.TransferStatus.FAILED);
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(transfer));
            }
        }
    }

    private void initData() {
        Account acc1 = new Account("Mark Jack", new BigDecimal("11245"), Currency.getInstance("USD"));
        accountsMap.put(acc1.getId(), acc1);
        Account acc2 = new Account("Mohamed Salah", new BigDecimal("4107"), Currency.getInstance("EUR"));
        accountsMap.put(acc2.getId(), acc2);
        Account acc3 = new Account("John Smith", new BigDecimal("14000"), Currency.getInstance("GBP"));
        accountsMap.put(acc3.getId(), acc3);
        Transfer tr1 = new Transfer(0, 1, new BigDecimal("650"), Currency.getInstance("EUR"), "Rent");
        transfersMap.put(tr1.getId(), tr1);
        Transfer tr2 = new Transfer(1, 2, new BigDecimal("300"), Currency.getInstance("USD"), "Happy birthday");
        transfersMap.put(tr2.getId(), tr2);
        Transfer tr3 = new Transfer(1, 0, new BigDecimal("50"), Currency.getInstance("EUR"), "Congaratulations");
        transfersMap.put(tr2.getId(), tr2);
    }

}
