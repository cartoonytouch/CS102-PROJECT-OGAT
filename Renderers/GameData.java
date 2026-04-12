package Renderers;

public class GameData {
    public String mapSeed;
    public int playerX;
    public int playerY;
    public int playerHealth;
    public int playerMana;
    public int playerCurrency;
    public String[] inventoryItemIds;
    public int[] inventoryWeaponLevels;
    public int choosedWeaponIndex;
    public String playerClass;
    public String[] discoveredItemIds;
    public String difficulty;


    public int gridX;
    public int gridY;

    public RoomState[] rooms;

    public static class RoomState
    {
        public int gridX;
        public int gridY;
        public boolean isDiscovered;
        public boolean isCleared;
        public EnemyState[] enemies;
        public RewardState rewardDrop;
        public BuyStationState buyStation;
    }

    public static class EnemyState
    {
        public String enemyType;
        public int xCoord;
        public int yCoord;
        public int health;
    }

    public static class RewardState
    {
        public int coordX;
        public int coordY;
        public String rewardType;
        public int coinAmount;
        public String itemId;
    }

    public static class BuyStationState
    {
        public String[] offerItemIds;
        public int[] offerPrices;
    }
}
