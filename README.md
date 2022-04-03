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

      Server (ADMIN authorities):
         Init API:
            Method POST: http://localhost:8082/host/create/user - use it for create new User in database
            Method POST: http://localhost:8082/host/create/account - use it for create new Account in database
            Method POST: http://localhost:8082/host/remove/user - use it for delete User from database
            Method POST: http://localhost:8082/host/remove/account - use it for delete Account from database

            Method DELETE: http://localhost:8082/host/remove/user/{id} - use it for delete User from database
            Method DELETE: http://localhost:8082/host/remove/account/{id} - use it for delete Account from database
         
      Debug API (USER,ADMIN authorities):
         Method GET: http://localhost:8082/host/users - get all Users in database at server;
         Method GET: http://localhost:8082/host/accounts - get all Accounts in database at server;

<h1>Available auth users:</h1>

Since work on the implementation of authentication and authorization continues, 3 users are currently available

Username  :  card_number;

Password  :  pin_code;

1. USER (USER authorities) 

         Username: "user";
         Password: "user";
2. USER (USER authorities)

         Username: "1111222211112222"; 
         Password: "1221";             

3. ADMIN (ADMIN authorities)

           Username: "admin";
           Password: "admin";


<h1>Require to install and work:</h1>

1. JDK 8

2. cURL



<h1>How to install:</h1>

1. Download and open the project
2. For submodules "client" and "server" should run - <b>*mvn clean package*</b> 
3. Run JAR by console : <b>*java -jar name_of_JAR*</b>



<h1>Preparing app data for work:</h1>

1. Init 2 test Users and 3 test Accounts with cURL requests

        Create users:
        curl -X POST http://localhost:8082/host/create/user -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d {\"firstName\":\"Vladislav\",\"lastName\":\"Boikov\"}
        curl -X POST http://localhost:8082/host/create/user -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d {\"firstName\":\"Alexandra\",\"lastName\":\"Semennova\"}
        Create accounts:
        curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d {\"cardNumber\":\"1111000011110000\",\"accountNumber\":\"40800000000000000001\",\"amount\":\"1090.50\",\"pinCode\":\"1001\",\"user\":{\"id\":1}}
        curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"accountNumber\":\"40800000000000000002\",\"amount\":\"390.50\",\"pinCode\":\"1221\",\"user\":{\"id\":1}}
        curl -X POST http://localhost:8082/host/create/account -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d {\"cardNumber\":\"1111333311113333\",\"accountNumber\":\"40800000000000000003\",\"amount\":\"4444.50\",\"pinCode\":\"1221\",\"user\":{\"id\":2}}
3. Application now is ready for work



<h1>Instruction for work:</h1>

1. For checking balance on card: send JSON with <b>"card_number"</b> and <b>"pin_code"</b> to Client API:

   
      Method POST: http://localhost:8080/client/balance 

   Example:

      Data:
         card_number: 1111222211112222
         pin_code:    1221

      Request:
         curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"pinCode\":\"1221\"}

Response should be like:


      CARD_NUMBER : 1111222211112222
      BALANCE : 390.50


2. If data has wong PIN-code


      Data:
         card_number: 1111222211112222
         pin_code:    9999

      Request:
         curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"pinCode\":\"9999\"}

Response should be like:


      WRONG PIN-CODE


3. If pin-code has not valid - pin=code should consist of 4 digits


      Data:
         card_number: 1111222211112222
         pin_code:    asd11235
   
      Request:
      curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"pinCode\":\"9999\"}

Response should be like:


     Invalid input data : check card_number and pin_code!