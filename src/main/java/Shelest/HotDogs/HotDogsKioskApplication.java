package Shelest.HotDogs;

import Shelest.HotDogs.persistence.Database;
import Shelest.HotDogs.persistence.JsonDatabase;
import Shelest.HotDogs.ui.CommandLineUserInteraction;
import Shelest.HotDogs.ui.UserInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HotDogsKioskApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(HotDogsKioskApplication.class);

    //Вхідна точка в програму
    public static void main(String[] args) {
        //Створення необхідних об'єктів
        Database database = new JsonDatabase();
        UserInteraction userInteraction = new CommandLineUserInteraction(database);

        //Запуск інтерфейсу взаємодії з користувачем
        while (true) {
            try {
                userInteraction.run();
            } catch (Exception e) {
                LOGGER.error("An exception occurred");
            }
        }
    }
}
