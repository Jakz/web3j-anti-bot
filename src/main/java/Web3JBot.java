import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;

public class Web3JBot {

    public static void main(String[] args) {
        // Connect to the Ethereum node
        Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8545")); // Use your node URL (e.g., Infura or local Ganache)

        try {
            // Get the latest block number
            EthBlockNumber ethBlockNumber = web3.ethBlockNumber().send();
            System.out.println("Latest block number: " + ethBlockNumber.getBlockNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
