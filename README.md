## using java 8 and vertx.io
- How to build the application :

mvn clean package 

- How to run

 open target folder and excute this line :  java -jar moneytransfer-1.0-SNAPSHOT.jar
 
- APIS

## Accounts : 
1-Retrieve all accounts 

* GET localhost:8080/api/accounts

2-Retrieve one account

* GET localhost:8080/api/accounts/1

3-create account
```
* POST localhost:8080/api/accounts
    {
        "name": "Ibrahim",
        "balance": "2558",
        "currency": "USD"
    }
```
4-Update  account
```
* PUT localhost:8080/api/accounts/1
    {
        "name": "Mo Salah",
        "currency": "USD"
    }
```
5-Delete account
```
* DELETE localhost:8080/api/accounts/1
```

## Transfers :

1-Create Transfer
```
POST localhost:8080/api/transfers
    {
        "senderAccountId": "0",
        "recieverAccountId": "1",
        "amount": "450",
        "currency": "EUR",
        "comment": "Rent"
    }
```
2- Excute Transfer
```
PUT localhost:8080/api/transfers/1
```