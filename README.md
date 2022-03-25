# ClientServerATM
client(ATM)-server(bank-server) REST service

#MVP
get balance of account

#Preparing for work:
1. Package module "client" and "server";
2. Run modules from JAR
3. Init data with cURL requests

        Create users:
        curl -X POST http://localhost:8082/host/create/user -H "Content-Type: application/json" -d {\"firstName\":\"Vladislav\",\"lastName\":\"Boikov\"}
        curl -X POST http://localhost:8082/host/create/user -H "Content-Type: application/json" -d {\"firstName\":\"Alexandra\",\"lastName\":\"Semennova\"}
        Create accounts:
        curl -X POST http://localhost:8082/host/create/account -H "Content-Type: application/json" -d {\"cardNumber\":\"1111000011110000\",\"scoreNumber\":\"0001\",\"amount\":\"1090.50\",\"pinCode\":\"1001\",\"user\":{\"id\":1}}
        curl -X POST http://localhost:8082/host/create/account -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"scoreNumber\":\"0002\",\"amount\":\"390.50\",\"pinCode\":\"1221\",\"user\":{\"id\":1}}
        curl -X POST http://localhost:8082/host/create/account -H "Content-Type: application/json" -d {\"cardNumber\":\"1111333311113333\",\"scoreNumber\":\"0003\",\"amount\":\"4444.50\",\"pinCode\":\"1221\",\"user\":{\"id\":2}}
4. Application now is ready for work

#Instruction for work:
1. Client url - http://localhost:8080/client/
2. For checking balance - send "card_number" and "pin_code" 
to ATM (client)
       

   Example:

           curl -X POST http://localhost:8080/client/balance -H "Content-Type: application/json" -d {\"cardNumber\":\"1111222211112222\",\"pinCode\":\"1221\"}



