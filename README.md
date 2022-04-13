# ClientServerATM
Client (ATM) - Server (bank-server) 
#Description:
The application represents a model of interaction between an ATM and a bank server. 

The application allows the user to find out the balance on the card.

Client and Server are two Spring Boot Web apps, which connect by REST.

---
Stack:

--Spring Boot

--Spring MVC

--Spring Data - JPA

--Spring Security



<h1>MVP (Minimum Viable Product)</h1>

Get balance on the card.

---
<h1>Available APIs:</h1>

Client URL: http://localhost:8080/

Server URL: http://localhost:8082/

      Client (USER,ADMIN authorities):
         Main projected API:
            Method POST: http://localhost:8080/client/balance - use it for check balance!
            Method POST: http://localhost:8080/client/money/transfer - use it for transfer money card_To_card!

      Server (ADMIN authorities):
         Init API:
            Method POST: http://localhost:8082/host/create/user - use it for create new User in database
            Method POST: http://localhost:8082/host/create/account - use it for create new Account in database
            Method POST: http://localhost:8082/host/remove/user - use it for delete User from database
            Method POST: http://localhost:8082/host/remove/account - use it for delete Account from database

            Method DELETE: http://localhost:8082/host/remove/user/{id} - use it for delete User from database
            Method DELETE: http://localhost:8082/host/remove/account/{id} - use it for delete Account from database
         
      Debug API (USER,ADMIN authorities):
         Method GET : http://localhost:8082/host/users - get all Users in database at server;
         Method GET : http://localhost:8082/host/accounts - get all Accounts in database at server;
      Debug API (ADMIN authorities):
         Method POST: http://localhost:8082/host/money/transfer - send money from card to card;

<h1>Available auth users:</h1>

From the moment the application starts, by default, two users with different permissions are available :

Username  :  card_number;

Password  :  pin_code;

1. USER (USER authorities) 

         Username: "1111222211112222";
         Password: "1221";

2. ADMIN (ADMIN authorities)

           Username: "1111333311113333";
           Password: "1221";


<h1>Require to install and work:</h1>

1. JDK 8

2. cURL / POSTMAN



<h1>How to install:</h1>

1. Download and open the project
2. For submodules "client" and "server" should run - <b>*mvn clean package*</b> 
3. Run JAR by console : <b>*java -jar name_of_JAR*</b>



<h1>Preparing app data for work:</h1>

1. Init 2 test Users and 3 test Accounts with cURL requests



      Create users:
         curl -X POST http://localhost:8082/host/create/user -H "Authorization: Basic MTExMTMzMzMxMTExMzMzMzoxMjIx" -H "Content-Type: application/json" -d {\"firstName\":\"Vladislav\",\"lastName\":\"Boikov\"}
         curl -X POST http://localhost:8082/host/create/user -H "Authorization: Basic MTExMTMzMzMxMTExMzMzMzoxMjIx" -H "Content-Type: application/json" -d {\"firstName\":\"Alexandra\",\"lastName\":\"Semennova\"}
      Create accounts:
         curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic MTExMTMzMzMxMTExMzMzMzoxMjIx" -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000011111111\",\"accountNumber\":\"40800000000000000003\",\"amount\":\"1090.50\",\"pinCode\":\"$2a$12$Dcvi1lTmeCaAzyOwVax9eO/YkXz.DHXOgQQNys2RC3B5nguh1CQRS\",\"user\":{\"id\":3}} 
         curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic MTExMTMzMzMxMTExMzMzMzoxMjIx" -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000022222222\",\"accountNumber\":\"40800000000000000004\",\"amount\":\"390.50\",\"pinCode\":\"$2a$12$RdHqoZDm4KF5NmVlAkgMNunBabRTLgfXk.YI9fZZSxolAO/8fbTCG\",\"user\":{\"id\":3}}
         curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic MTExMTMzMzMxMTExMzMzMzoxMjIx" -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000033333333\",\"accountNumber\":\"40800000000000000005\",\"amount\":\"4444.50\",\"pinCode\":\"$2a$12$RdHqoZDm4KF5NmVlAkgMNunBabRTLgfXk.YI9fZZSxolAO/8fbTCG\",\"user\":{\"id\":4}}



2. Application now is ready for work



<h1>Instruction for work:</h1>

<h2>1.Get balance</h2>

1. For checking balance on card: send JSON with <b>"card_number"</b> and <b>"pin_code"</b> to Client API:


            Method POST: http://localhost:8080/client/balance 

   Example:


            Data:
                  card_number: 0000000022222222
                  pin_code:    1221    
            Request:
                  curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000022222222\",\"pinCode\":\"1221\"}

Response should be like:


      CARD_NUMBER : 0000000022222222
      BALANCE : 390.50


2. Case: wrong PIN-code


       Data:
          card_number: 0000000022222222
          pin_code:    9999
       Request:
          curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000022222222\",\"pinCode\":\"9999\"}

Response should be like:


      WRONG PIN-CODE


3. Case: ATM do not have connection with server



            Data:
               card_number: 0000000022222222
               pin_code:    1221
            
            Request:
               curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"0000000022222222\",\"pinCode\":\"9999\"}

Response should be like:


     Don`t have connection with server


<h2>2.Send money by card_number</h2>

1. To transfer money from card to card should send : 

-card_holder_data: <b>"card_number"</b> and <b>"pin_code"</b> 

-card_number to transfer: <b>"card_number_to"</b>

-amount to transfer: <b>"amount_to_transfer"</b>

to Client API:


            Method POST: http://localhost:8080/client/money/transfer 

Example:


            Data:
               card_number_from : 0000000033333333
               pin_code:    1221   
               car_number_to : 0000000022222222
               amount_to_transfer : 100
            Request:
                  curl -X POST http://localhost:8080/client/money/transfer -H "Content-Type: application/json" -d {\"accountFrom\":{\"cardNumber\":\"0000000033333333\",\"pinCode\":\"1221\"},\"cardNumberTo\":\"0000000022222222\",\"amountToTransfer\":\"100\"} 

Response should be like:


      Transfer success!
      Balance BEFORE: 4444.50
      Balance AFTER: 4344.50


2. Case  : wrong PIN-code


       Data:
         card_number_from : 0000000033333333
         pin_code:    0000 
         car_number_to : 0000000022222222
         amount_to_transfer : 100
       Request:
          curl -X POST http://localhost:8080/client/money/transfer -H "Content-Type: application/json" -d {\"accountFrom\":{\"cardNumber\":\"0000000033333333\",\"pinCode\":\"1221\"},\"cardNumberTo\":\"0000000022222222\",\"amountToTransfer\":\"100\"}

Response should be like:


      WRONG PIN-CODE


3. Case  : don`t have enough money to transfer


         Data:
            card_number_from : 0000000033333333
            pin_code:    1221   
            car_number_to : 0000000022222222
            amount_to_transfer : 100000000
         Request:
            curl -X POST http://localhost:8080/client/money/transfer -H "Content-Type: application/json" -d {\"accountFrom\":{\"cardNumber\":\"1111333311113333\",\"pinCode\":\"1221\"},\"cardNumberTo\":\"0000000022222222\",\"amountToTransfer\":\"100000000\"} 

Response should be like:


     Don`t have enough amount to transfer!
4. Case  : ATM do not have connection with server


         Data:
               card_number_from : 0000000033333333
               pin_code:    1221   
               car_number_to : 0000000022222222
               amount_to_transfer : 100
            Request:
                  curl -X POST http://localhost:8080/client/money/transfer -H "Content-Type: application/json" -d {\"accountFrom\":{\"cardNumber\":\"0000000033333333\",\"pinCode\":\"1221\"},\"cardNumberTo\":\"0000000022222222\",\"amountToTransfer\":\"100\"} 

Response should be like:


     Don`t have connection with server



