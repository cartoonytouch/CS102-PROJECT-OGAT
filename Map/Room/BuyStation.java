package Map.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Entities.Characters.Player;
import Items.Inventory;
import Items.Item;
import Items.ItemCatalog;
import Items.Passive;
import Items.Weapons.Weapon;

public class BuyStation extends Station {

    public static final int OFFER_COUNT = 2;

    private final Random random;
    private final List<Item> offerItems = new ArrayList<>();
    private final List<Integer> offerPrices = new ArrayList<>();
    private boolean offersGenerated;

    public BuyStation(int coordX, int coordY, Random random)
    {
        super(coordX, coordY, "Buy Station");
        this.random = (random != null) ? random : new Random();
    }

    public void ensureOffers(Player player)
    {
        if (offersGenerated)
        {
            return;
        }

        List<String> candidatePool = new ArrayList<>();
        if (player != null)
        {
            candidatePool.addAll(player.getDiscoveredShopItemIds());
        }

        if (candidatePool.size() < OFFER_COUNT)
        {
            for (String itemId : ItemCatalog.getDefaultShopPool())
            {
                if (!candidatePool.contains(itemId))
                {
                    candidatePool.add(itemId);
                }
            }
        }

        Collections.shuffle(candidatePool, random);

        for (String itemId : candidatePool)
        {
            Item item = ItemCatalog.createItem(itemId);
            if (item == null)
            {
                continue;
            }

            offerItems.add(item);
            offerPrices.add(generateCost(item));

            if (offerItems.size() >= OFFER_COUNT)
            {
                break;
            }
        }

        offersGenerated = true;
    }

    private int generateCost(Item item)
    {
        if (item instanceof Weapon)
        {
            return random.nextInt(3) + 4;
        }
        if (item instanceof Passive)
        {
            return random.nextInt(3) + 3;
        }
        return random.nextInt(3) + 2;
    }

    public int getOfferCount()
    {
        return offerItems.size();
    }

    public boolean hasGeneratedOffers()
    {
        return offersGenerated;
    }

    public String[] getOfferItemIds()
    {
        String[] itemIds = new String[offerItems.size()];
        for (int i = 0; i < offerItems.size(); i++)
        {
            Item item = offerItems.get(i);
            itemIds[i] = (item != null) ? item.getItemID() : null;
        }
        return itemIds;
    }

    public int[] getOfferPrices()
    {
        int[] prices = new int[offerPrices.size()];
        for (int i = 0; i < offerPrices.size(); i++)
        {
            prices[i] = offerPrices.get(i);
        }
        return prices;
    }

    public void restoreOffers(String[] itemIds, int[] prices)
    {
        offerItems.clear();
        offerPrices.clear();

        if (itemIds != null)
        {
            for (String itemId : itemIds)
            {
                offerItems.add(itemId != null ? ItemCatalog.createItem(itemId) : null);
            }
        }

        if (prices != null)
        {
            for (int price : prices)
            {
                offerPrices.add(price);
            }
        }

        while (offerPrices.size() < offerItems.size())
        {
            offerPrices.add(0);
        }

        offersGenerated = true;
    }

    public Item getOfferItem(int slotIndex)
    {
        if (slotIndex < 0 || slotIndex >= offerItems.size())
        {
            return null;
        }
        return offerItems.get(slotIndex);
    }

    public int getOfferPrice(int slotIndex)
    {
        if (slotIndex < 0 || slotIndex >= offerPrices.size())
        {
            return 0;
        }
        return offerPrices.get(slotIndex);
    }

    public boolean isOfferSold(int slotIndex)
    {
        return getOfferItem(slotIndex) == null;
    }

    public boolean canBuyOffer(int slotIndex, Player player)
    {
        Item item = getOfferItem(slotIndex);
        if (item == null || player == null)
        {
            return false;
        }

        Inventory inventory = player.getInventory();
        return inventory != null
            && inventory.canAdd(item)
            && player.getCurrency() >= getOfferPrice(slotIndex);
    }

    public String purchaseOffer(int slotIndex, Player player)
    {
        Item item = getOfferItem(slotIndex);
        if (player == null || item == null)
        {
            return "That offer is no longer available.";
        }

        Inventory inventory = player.getInventory();
        if (inventory == null || !inventory.canAdd(item))
        {
            return "That slot in your inventory is occupied.";
        }

        int price = getOfferPrice(slotIndex);
        if (player.getCurrency() < price)
        {
            return "You need more coins.";
        }

        item.setPlayer(player);
        inventory.add(item);
        player.discoverItem(item);
        player.changeCurrency(price);
        offerItems.set(slotIndex, null);
        return "Purchased " + item.getName() + ".";
    }

    @Override
    public String interact(Player player)
    {
        ensureOffers(player);
        return "Opened Buy Station.";
    }
}
