import character.Farmer;
import character.Robinson;
import enums.CropType;
import enums.GrainState;
import enums.ToolType;
import exception.InsufficientGrainException;
import exception.MissingEquipmentException;
import exception.StorageOverflowException;
import farm.Farm;
import farm.Field;
import model.Grain;
import model.YearlyFoodPlan;
import storage.Barn;
import storage.Basket;
import storage.Storage;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Farm farm = new Farm(
                List.of(new Field(8 + random.nextDouble() * 4, 30),
                        new Field(7 + random.nextDouble() * 3, 35)),
                List.of(new Basket("Большая корзина", 100), new Basket("Маленькая корзина", 70)),
                new Barn("Амбар", 300), Set.of(ToolType.SICKLE, ToolType.FLAIL));
        Robinson robinson = new Robinson("Робинзон", farm);

        try {
            farm.getBarn().store(new Grain(CropType.WHEAT, 10, GrainState.SEED));
            farm.getBarn().store(new Grain(CropType.BARLEY, 8, GrainState.SEED));
            CropType[] crops = {CropType.WHEAT, CropType.BARLEY};
            double[] seeds = {10, 8};
            System.out.println("Начало сезона. Урожайность полей зависит от погоды.");
            for (int i = 0; i < farm.getFields().size(); i++) {
                Field field = farm.getFields().get(i);
                System.out.println(field);
                Grain seed = farm.getBarn().retrieve(crops[i], GrainState.SEED, seeds[i]);
                robinson.sow(field, seed);
                System.out.println("Посев выполнен: " + field);
            }

            try {
                robinson.harvest(farm.getFields().getFirst());
            } catch (IllegalStateException e) {
                System.out.println("Ранняя уборка отклонена: " + e.getMessage());
            }
            farm.advanceTime(15);
            System.out.println("Прошло 15 дней: " + farm.getFields());
            farm.advanceTime(20);
            System.out.println("Прошло ещё 20 дней: " + farm.getFields());

            for (int i = 0; i < farm.getFields().size(); i++) {
                Field field = farm.getFields().get(i);
                Grain harvest = robinson.harvest(field);
                System.out.println("Собран урожай: " + harvest + ". " + field);
                Storage basket = farm.getBaskets().get(i);
                try {
                    basket.store(harvest);
                } catch (StorageOverflowException e) {
                    System.out.println(e.getMessage() + ". Переносим излишек в амбар.");
                    double excess = harvest.getAmount() - basket.getFreeCapacity();
                    Grain basketPortion = new Grain(harvest.getCropType(),
                            harvest.getAmount() - excess, harvest.getState());
                    Grain barnPortion = new Grain(harvest.getCropType(), excess, harvest.getState());
                    basket.store(basketPortion);
                    farm.getBarn().store(barnPortion);
                    harvest.consume();
                }
                System.out.println(basket);
                double amount = basket.getAmount(crops[i], GrainState.IN_EARS);
                if (amount > 0) {
                    robinson.transferGrain(basket, farm.getBarn(), crops[i], GrainState.IN_EARS, amount);
                }
                System.out.println("Зерно перенесено. " + basket);
            }
            System.out.println(farm.getBarn());

            robinson.thresh(farm.getBarn(), CropType.WHEAT,
                    farm.getBarn().getAmount(CropType.WHEAT, GrainState.IN_EARS));

            for (CropType crop : crops) {
                double ears = farm.getBarn().getAmount(crop, GrainState.IN_EARS);
                if (ears > 0) robinson.thresh(farm.getBarn(), crop, ears);
            }
            System.out.println("Обмолот завершён. " + farm.getBarn());

            try {
                robinson.consumeGrain(farm.getBarn(), CropType.WHEAT,
                        farm.getBarn().getAmount(CropType.WHEAT, GrainState.THRESHED) + 1);
            } catch (InsufficientGrainException e) {
                System.out.println(e.getMessage() + ". Робинзон уменьшил порцию.");
            }
            double dailyConsumption = 0.25 + random.nextDouble() * 0.1;
            robinson.consumeGrain(farm.getBarn(), CropType.WHEAT, dailyConsumption);
            System.out.printf("За день съедено %.2f кг. %s%n",
                    robinson.getConsumedGrain(), farm.getBarn());

            YearlyFoodPlan plan = robinson.planYear(dailyConsumption, 18);
            System.out.println(plan);
            double available = farm.getBarn().getAmount();
            if (available >= plan.totalRequired()) {
                System.out.println("Урожая достаточно для питания и следующего посева.");
            } else {
                System.out.printf("До выполнения годового плана не хватает %.2f кг: потребуется ещё урожай.%n",
                        plan.totalRequired() - available);
            }
            if (farm.getBarn().getAmount(CropType.WHEAT, GrainState.THRESHED) >= 10
                    && farm.getBarn().getAmount(CropType.BARLEY, GrainState.THRESHED) >= 8) {
                for (int i = 0; i < farm.getFields().size(); i++) {
                    Grain seed = farm.getBarn().retrieve(crops[i], GrainState.THRESHED, seeds[i]);
                    robinson.sow(farm.getFields().get(i), seed);
                }
                System.out.println("Следующий посев выполнен: " + farm.getFields());
            } else {
                System.out.println("Для нового посева недостаточно семян: посев отложен.");
            }
            System.out.println(robinson);
            System.out.println(farm);
            System.out.println("Сценарий завершён.");
        } catch (StorageOverflowException | MissingEquipmentException | InsufficientGrainException e) {
            System.out.println("Сезон прерван: " + e.getMessage());
        }
    }
}
