package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Account;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A concrete {@link AccountsRepository} backed by an in-memory list of static {@link Account} entities.
 */
public class DatabaseAccountsRepository extends AccountsRepository {

    // Hold entities in a simple list.
    private List<Account> mAccounts = new ArrayList<Account>();

    DatabaseAccountsRepository()  {
        String accountDir;
        //todo : find way to avoid setting hard the director
        accountDir = "C:\\Users\\benhamza\\AppData\\Local\\NoBackup\\Perso\\CentraleSupelec\\Java\\fil-rouge-api-2017\\src\\main\\java\\fr\\centralesupelec\\sio\\data\\rawdata\\accounts.csv";
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);
        Path accountPath = Paths.get(accountDir);
        List<String> accountsRecords = null;
        try {
            accountsRecords = Files.readAllLines(accountPath);
            accountsRecords.remove(0);
            for (String accountsRecord : accountsRecords) {
                Account ac = new Account();
                String[] accountInfos = accountsRecord.split(";");
                ac.setUsername(accountInfos[0]); //user name is found in the first column
                ac.setPasswordHash(accountInfos[2]); //hashpass found in the second column
                mAccounts.add(ac);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(mAccounts.size());
    }

    @Override
    public Account getAccount(String username) {
        System.out.println(username);
        for (Account mAccount : mAccounts) {
            System.out.println(mAccount.getUsername());
            System.out.println(mAccount.getPasswordHash());
        }
        return mAccounts.stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // Below are (unused) variants of getAccount(username)

    // A simple loop with index
    public Account getAccountWithIndexedLoop(String username) {
        for (int i = 0; i < mAccounts.size(); i++) {
            Account a = mAccounts.get(i);
            if (a.getUsername().equalsIgnoreCase(username)) {
                return a;
            }
        }
        return null;
    }

    // A better iteration syntax for collections, more readable
    public Account getAccountWithIteration(String username) {
        for (Account a: mAccounts) {
            if (a.getUsername().equalsIgnoreCase(username)) {
                return a;
            }
        }
        return null;
    }

    // A newer "stream" manipulation syntax, with method chaining
    public Account getAccountWithStream(String username) {
        return mAccounts
                // Obtain a streamable view of the list
                .stream()
                // Keep only items matching a predicate (function that return a boolean)
                .filter(new Predicate<Account>() {
                    @Override
                    public boolean test(Account account) {
                        return account.getUsername().equalsIgnoreCase(username);
                    }
                })
                // Get the first item (returns an Optional<Movie>)
                .findFirst()
                // If not found, return null
                .orElse(null);
    }

    // Same version as above, where the Predicate is replaced by a lambda
    public Account getAccountWithStreamLambda(String username) {
        return mAccounts.stream()
                .filter(account -> account.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

}
