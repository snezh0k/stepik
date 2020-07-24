package org.stepik;

import org.stepik.database_service.DBException;
import org.stepik.database_service.DBService;
import org.stepik.database_service.datasets.UsersDataSet;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        try {
            long userId = dbService.addUser("tully");
            System.out.println("Added user id: " + userId);

            userId = dbService.getUserId("tully");
            System.out.println("UserdId: " + userId);

            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.println("User data set: " + dataSet);

        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
