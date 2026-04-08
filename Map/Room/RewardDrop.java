package Map.Room;

import java.awt.Rectangle;

import Items.Item;

public class RewardDrop {
    public enum RewardType {
        COIN,
        ITEM
    }

    private final int coordX;
    private final int coordY;
    private final RewardType rewardType;
    private final int coinAmount;
    private final Item itemReward;

    public RewardDrop(int coordX, int coordY, int coinAmount)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.rewardType = RewardType.COIN;
        this.coinAmount = coinAmount;
        this.itemReward = null;
    }

    public RewardDrop(int coordX, int coordY, Item itemReward)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.rewardType = RewardType.ITEM;
        this.coinAmount = 0;
        this.itemReward = itemReward;
    }

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }

    public RewardType getRewardType()
    {
        return rewardType;
    }

    public int getCoinAmount()
    {
        return coinAmount;
    }

    public Item getItemReward()
    {
        return itemReward;
    }

    public boolean isCoinReward()
    {
        return rewardType == RewardType.COIN;
    }

    public boolean isItemReward()
    {
        return rewardType == RewardType.ITEM;
    }

    public Rectangle getBounds(int tileSize)
    {
        int drawSize = tileSize / 2;
        int drawX = (coordX * tileSize) + ((tileSize - drawSize) / 2);
        int drawY = (coordY * tileSize) + ((tileSize - drawSize) / 2);
        return new Rectangle(drawX, drawY, drawSize, drawSize);
    }
}
