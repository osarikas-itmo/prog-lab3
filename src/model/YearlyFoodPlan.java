package model;

public record YearlyFoodPlan(double yearlyConsumption, double grainToSow) {
    public YearlyFoodPlan {
        if (!Double.isFinite(yearlyConsumption) || yearlyConsumption < 0
                || !Double.isFinite(grainToSow) || grainToSow < 0) {
            throw new IllegalArgumentException("Годовой план содержит недопустимые количества зерна");
        }
    }

    public double totalRequired() { return yearlyConsumption + grainToSow; }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ROOT,
                "Годовой план: питание — %.2f кг, посев — %.2f кг, всего — %.2f кг",
                yearlyConsumption, grainToSow, totalRequired());
    }
}
