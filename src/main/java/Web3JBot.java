import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;

// this class implements a hello world using web3j which is currently the most popular and stable Java Ethereum RPC client library
public class Web3JBot {

    public static void main(String[] args) {
        // Connect to your local Ethereum node
        Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8545")); // uses your node url, geth testnet or anvil (foundry eth suite)

        try {
            // Get the latest block number - (blockchain/web3 hello world)
            EthBlockNumber ethBlockNumber = web3.ethBlockNumber().send();
            System.out.println("Latest block number: " + ethBlockNumber.getBlockNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
