package Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Items.Weapons.GreatAxe;
import Items.Weapons.Halberd;
import Items.Weapons.Hammer;
import Items.Weapons.Longsword;
import Items.Weapons.Spear;
import Items.Weapons.Sword;

public final class ItemCatalog {

    private static final List<String> DEFAULT_SHOP_POOL = Arrays.asList(
        "31", "32", "33",
        "21", "22", "23", "24", "25",
        "11", "12", "13", "14", "15"
    );

    private ItemCatalog()
    {
    }

    public static List<String> getDefaultShopPool()
    {
        return new ArrayList<>(DEFAULT_SHOP_POOL);
    }

    public static boolean isShopEligible(String itemId)
    {
        return itemId != null && DEFAULT_SHOP_POOL.contains(itemId);
    }

    public static Item createItem(String itemId)
    {
        if (itemId == null || itemId.isBlank())
        {
            return null;
        }

        switch (itemId)
        {
            case "31":
                return new Sword();
            case "32":
                return new Spear();
            case "33":
                return new Hammer();
            case "34":
                return new Longsword();
            case "35":
                return new Halberd();
            case "36":
                return new GreatAxe();
            default:
                if (itemId.startsWith("2"))
                {
                    return new Passive(itemId);
                }
                if (itemId.startsWith("1"))
                {
                    return new Consumable(itemId);
                }
                return null;
        }
    }

    public static String getIconPath(String itemId)
    {
        if (itemId == null || itemId.isBlank())
        {
            return null;
        }

        switch (itemId)
        {
            case "11":
                return "Assets/ItemAssets/ConsumableAssets/HealthPotion.png";
            case "12":
                return "Assets/ItemAssets/ConsumableAssets/StaminaPotion.png";
            case "13":
                return "Assets/ItemAssets/ConsumableAssets/CoinBag.png";
            case "14":
                return "Assets/ItemAssets/FullHeart.png";
            case "15":
                return "Assets/ItemAssets/ConsumableAssets/StaminaPotion.png";
            case "21":
                return "Assets/ItemAssets/PassiveAssets/UsefulBag.png";
            case "22":
                return "Assets/ItemAssets/PassiveAssets/BloodySword.png";
            case "23":
                return "Assets/ItemAssets/PassiveAssets/AngelAttack.png";
            case "24":
                return "Assets/ItemAssets/PassiveAssets/BloodySword.png";
            case "25":
                return "Assets/ItemAssets/PassiveAssets/AngelMovement.png";
            case "31":
                return "Assets/ItemAssets/WeaponAssets/SwordBase.png";
            case "32":
                return "Assets/ItemAssets/WeaponAssets/SpearBase.png";
            case "33":
                return "Assets/ItemAssets/WeaponAssets/HammerBase.png";
            case "34":
                return "Assets/ItemAssets/WeaponAssets/LongswordBase.png";
            case "35":
                return "Assets/ItemAssets/WeaponAssets/HalbertBase.png";
            case "36":
                return "Assets/ItemAssets/WeaponAssets/GreatAxeBase.png";
            default:
                return null;
        }
    }
}
