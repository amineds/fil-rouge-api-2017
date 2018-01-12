package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Account;

import java.io.IOException;

/**
 * A data repository to expose account-related entities.
 */
// See MoviesRepository for architecture details
public abstract class AccountsRepository {

    // Singleton pattern
    private static AccountsRepository sRepository;

    // Singleton pattern
    public static AccountsRepository getInstance() {
        if (sRepository == null) {
            //sRepository = new DummyAccountsRepository();
            sRepository = new DatabaseAccountsRepository();
        }
        return sRepository;
    }

    // Singleton pattern
    protected AccountsRepository() { }

    /**
     * Find an account with a given name.
     * The matching is case-insensitive.
     * @param username The name of the user.
     * @return The {@link Account} entity, or null if it does not exist.
     */
    public abstract Account getAccount(String username);

    // TODO: Add other movie-related methods here

}
