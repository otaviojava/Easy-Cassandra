package org.easycassandra.bean.model.customcollection;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.junit.Test;

/**
 * bank dao.
 * @author otaviojava
 */
public class BankDAO {

    private static final int TEN = 10;
    private PersistenceDao<Bank, String> dao = new PersistenceDao<>(Bank.class);

    /**
     * test.
     */
    @Test
    public void insertTest() {
        Bank bank = getBank();
        dao.insert(bank);
    }

    /**
     * test.
     */
    @Test
    public void findTest() {
        Bank bank = dao.retrieve(1);
        Assert.assertNotNull(bank);
    }


    private Bank getBank() {
        Bank bank = new Bank();
        bank.setCodBank(1);
        UUID uuid = UUID.randomUUID();
        List<HistoryOperations> operations = new LinkedList<>();

        for (int i = 0; i < TEN; i++) {
            HistoryOperations operation = new HistoryOperations();
            operation.setDate(new Date());
            if (i % 2 == 0) {
                operation.setOperationCode(UUID.randomUUID());
            } else {
                operation.setOperationCode(uuid);
            }
            operation.setByUser("byUser" + i);
            operation.setFromUser("fromUser" + i);
            operation.setValue(new Random().nextDouble());
            operations.add(operation);
        }
        bank.setAccountsList(operations);
        bank.setAccountsSet(new HashSet<>(operations));
        Map<UserBank, HistoryOperations> map = new HashMap<>();
        for (HistoryOperations operation: operations) {
            UserBank userBank = new UserBank();
            userBank.setByUser(operation.getByUser());
            userBank.setFromUser(operation.getFromUser());
            map.put(userBank, operation);
        }
        bank.setMapBank(map);
        return bank;
    }

}
