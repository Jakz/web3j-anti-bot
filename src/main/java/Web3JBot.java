import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Flow.Subscription;

// this class implements a hello world using web3j which is currently the most popular and stable Java Ethereum RPC client library
public class Web3JBot
{
  public static String targetWallet = "0x8b451A051d0eF6c53696Fc36a8CA65B8F9b38123";
  public static String targetPrivateKey = "";
  
  public static String rescueWallet = "";
  public static String scammerWallet = "";
  
  public static String usdtContract = "0x55d398326f99059ff775485246999027b3197955";
  
  //USDT uses 6 decimal places, so 10 USDT is 10,000,000
  public static BigInteger amountToTransfer = BigInteger.valueOf(1_000_000);
  //public static BigInteger amountToTransfer = BigInteger.valueOf(14_200_000_000L);
  
  static Web3j web3j;
  static Credentials credentials;
  
  public static void setup()
  { 
    //new HttpService("http://127.0.0.1:8545")
    web3j = Web3j.build(new HttpService("https://bsc-dataseed.bnbchain.org"));
    
    if (!targetPrivateKey.isEmpty())
      credentials = Credentials.create(targetPrivateKey);
    
    RxJavaPlugins.setErrorHandler(e -> {
      if (e instanceof UndeliverableException)
        e.printStackTrace();
    });
  }
  
  public static void main(String[] args)
  {
    setup();
    
    new Thread(() -> {
      try
      {
        Disposable subscription = web3j.pendingTransactionFlowable().subscribe(tx -> {
          /*if (tx.getFrom().equals(scammerWallet))
          {
            BigInteger gasToBeat = tx.getGas();
            sendRescueTransaction(gasToBeat);
          }*/
          System.out.println(tx.getTransactionIndex());
        });
        
        while(true);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }).start();
  }
  
  public static void sendRescueTransaction(BigInteger gasToBeat) throws Exception
  {
    // https://docs.web3j.io/4.11.0/transactions/gas/
    
    final BigInteger GAS_PRICE = BigInteger.valueOf(4_100_000_000L); // max price per unit
    final BigInteger GAS_LIMIT = gasToBeat.add(BigInteger.valueOf(100)); // max gas to spend, should be > the one used by the scammer
    ERC20 contract = ERC20.load(usdtContract, web3j, credentials, new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
    
    TransactionReceipt receipt = contract.transfer(rescueWallet, amountToTransfer).send();
    
    if (receipt.isStatusOK())
      System.out.println("Rescue transaction is ok: " + receipt.getTransactionHash());
    else
      System.out.println("Something went wrong with rescue transaction");
  }
}
