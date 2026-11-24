# Диаграмма классов объектной модели

Показаны все 17 типов проекта и стандартный класс Exception. Указаны основные поля и методы; конструкторы, обычные геттеры и переопределения методов Object опущены для читаемости.

Сплошные стрелки обозначают хранимые ссылки, пунктирные — зависимости. Связи Farm с переданными в конструктор объектами показаны как ассоциации: их жизненный цикл не привязан к ферме.

```mermaid
classDiagram
    direction TB
    class Main {
        +main(String[] args)$ void
    }
    class Farmer {
        <<interface>>
        +harvest(Field field) Grain
        +sow(Field field, Grain grain) void
    }
    class Robinson {
        -String name
        -Farm farm
        -double consumedGrain
        -YearlyFoodPlan foodPlan
        +sow(Field field, Grain grain) void
        +harvest(Field field) Grain
        +transferGrain(Storage source, Storage destination, CropType type, GrainState state, double amount) void
        +thresh(Storage storage, CropType type, double amount) void
        +consumeGrain(Storage storage, CropType type, double amount) void
        +planYear(double dailyConsumption, double grainToSow) YearlyFoodPlan
        -requireEquipment(ToolType tool) void
    }
    class Farm {
        -List~Field~ fields
        -List~Basket~ baskets
        -Barn barn
        -Set~ToolType~ equipment
        +hasEquipment(ToolType tool) boolean
        +addEquipment(ToolType tool) void
        +advanceTime(int days) void
    }
    class Field {
        -CropType cropType
        -double harvestAmount
        -double yieldMultiplier
        -FieldState state
        -int growthTime
        -int elapsedGrowthTime
        +sow(Grain grain) void
        +advanceTime(int days) void
        +harvest() Grain
    }
    class Grain {
        -CropType cropType
        -double amount
        -GrainState state
        +consume() void
    }
    class YearlyFoodPlan {
        <<record>>
        -double yearlyConsumption
        -double grainToSow
        +yearlyConsumption() double
        +grainToSow() double
        +totalRequired() double
    }
    class Storage {
        <<abstract>>
        -String name
        -double capacity
        -List~Grain~ grain
        +getStorageType() String*
        +getAmount() double
        +getAmount(CropType type, GrainState state) double
        +getFreeCapacity() double
        +getContents() List~Grain~
        +store(Grain incoming) void
        +retrieve(CropType type, GrainState state, double amount) Grain
    }
    class Barn {
        +getStorageType() String
    }
    class Basket {
        +getStorageType() String
    }
    class CropType {
        <<enumeration>>
        RICE
        BARLEY
    }
    class FieldState {
        <<enumeration>>
        EMPTY
        GROWING
        READY_TO_HARVEST
    }
    class GrainState {
        <<enumeration>>
        SEED
        IN_EARS
        THRESHED
    }
    class ToolType {
        <<enumeration>>
        SICKLE
        FLAIL
    }
    class Exception {
        <<standard>>
    }
    class InsufficientGrainException {
        +getMessage() String
    }
    class MissingEquipmentException {
        +getMessage() String
    }
    class StorageOverflowException {
        +getMessage() String
    }

    Farmer <|.. Robinson
    Storage <|-- Barn
    Storage <|-- Basket
    Exception <|-- InsufficientGrainException
    Exception <|-- MissingEquipmentException
    Exception <|-- StorageOverflowException
    Robinson --> "1" Farm : farm
    Robinson --> "0..1" YearlyFoodPlan : foodPlan
    Farm --> "0..*" Field : fields
    Farm --> "0..*" Basket : baskets
    Farm --> "1" Barn : barn
    Farm --> "0..*" ToolType : equipment
    Storage --> "0..*" Grain : grain
    Grain --> "1" CropType
    Grain --> "1" GrainState
    Field --> "0..1" CropType
    Field --> "1" FieldState
    Field ..> Grain : sows / harvests
    Farmer ..> Field
    Farmer ..> Grain
    Farmer ..> MissingEquipmentException : throws
    Robinson ..> Storage
    Robinson ..> ToolType
    Robinson ..> MissingEquipmentException : throws
    Robinson ..> InsufficientGrainException : throws
    Robinson ..> StorageOverflowException : throws
    Storage ..> InsufficientGrainException : throws
    Storage ..> StorageOverflowException : throws
    Main ..> Farm
    Main ..> Robinson
    Main ..> Grain
    Main ..> YearlyFoodPlan
    Main ..> Field
    Main ..> Basket
    Main ..> Barn
```
`+` — public, `-` — private, `$` — static, `*` у метода — abstract.

`Farmer <|.. Robinson` — реализация интерфейса; `Storage <|-- Barn` и `Storage <|-- Basket` — наследование. Собственные исключения наследуются от Exception и являются проверяемыми. Метки `throws` обозначают зависимости от объявленных исключений.

Пакеты соответствуют каталогам src: character, farm, model, storage, enums, exception. Main находится в пакете по умолчанию. YearlyFoodPlan — record; его компоненты доступны через методы yearlyConsumption() и grainToSow().

