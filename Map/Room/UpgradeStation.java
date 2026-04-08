package Map.Room;

import Entities.Characters.Player;
import Items.Item;
import Items.Weapons.GreatAxe;
import Items.Weapons.Halberd;
import Items.Weapons.Hammer;
import Items.Weapons.Longsword;
import Items.Weapons.Spear;
import Items.Weapons.Sword;
import Items.Weapons.Weapon;

public class UpgradeStation extends Station {

    public static final int UPGRADE_COST = 1;

    public UpgradeStation(int coordX, int coordY)
    {
        super(coordX, coordY, "Upgrade Station");
    }

    public Weapon getWeaponAtSlot(Player player, int slotIndex)
    {
        if (player == null || player.getInventory() == null)
        {
            return null;
        }

        Item item = player.getInventory().getItems()[slotIndex];
        if (!(item instanceof Weapon))
        {
            return null;
        }

        return (Weapon) item;
    }

    public Weapon getEquippedWeapon(Player player)
    {
        if (player == null || player.getInventory() == null)
        {
            return null;
        }

        return getWeaponAtSlot(player, player.getInventory().getChoosedWeaponIndex());
    }

    public boolean canUpgradeEquippedWeapon(Player player)
    {
        Weapon equippedWeapon = getEquippedWeapon(player);
        return equippedWeapon != null
            && equippedWeapon.canUpgrade()
            && player.getCurrency() >= UPGRADE_COST;
    }

    public String upgradeEquippedWeapon(Player player)
    {
        Weapon equippedWeapon = getEquippedWeapon(player);
        if (equippedWeapon == null)
        {
            return "Equip a weapon first.";
        }

        if (!equippedWeapon.canUpgrade())
        {
            return "That weapon is already level 5.";
        }

        if (player.getCurrency() < UPGRADE_COST)
        {
            return "You need 1 coin.";
        }

        player.changeCurrency(UPGRADE_COST);
        equippedWeapon.upgrade();
        return equippedWeapon.getName() + " is now level " + equippedWeapon.getUpgradeLevel() + ".";
    }

    public boolean canMerge(Player player)
    {
        Weapon firstWeapon = getWeaponAtSlot(player, 0);
        Weapon secondWeapon = getWeaponAtSlot(player, 1);

        return firstWeapon != null
            && secondWeapon != null
            && firstWeapon.getUpgradeLevel() == Weapon.MAX_UPGRADE_LEVEL
            && secondWeapon.getUpgradeLevel() == Weapon.MAX_UPGRADE_LEVEL
            && createMergedWeapon(checkMerge(firstWeapon, secondWeapon)) != null;
    }

    public int checkMerge(Weapon firstWeapon, Weapon secondWeapon)
    {
        if (firstWeapon == null || secondWeapon == null)
        {
            return 0;
        }

        if ((firstWeapon instanceof Sword && secondWeapon instanceof Spear)
            || (firstWeapon instanceof Spear && secondWeapon instanceof Sword))
        {
            return 1;
        }
        if ((firstWeapon instanceof Sword && secondWeapon instanceof Hammer)
            || (firstWeapon instanceof Hammer && secondWeapon instanceof Sword))
        {
            return 2;
        }
        if ((firstWeapon instanceof Hammer && secondWeapon instanceof Spear)
            || (firstWeapon instanceof Spear && secondWeapon instanceof Hammer))
        {
            return 3;
        }

        return 0;
    }

    public String mergeWeapons(Player player)
    {
        if (player == null || player.getInventory() == null)
        {
            return "Upgrade Station is idle.";
        }

        Weapon firstWeapon = getWeaponAtSlot(player, 0);
        Weapon secondWeapon = getWeaponAtSlot(player, 1);

        if (firstWeapon == null || secondWeapon == null)
        {
            return "You need two weapons to merge.";
        }

        if (firstWeapon.getUpgradeLevel() < Weapon.MAX_UPGRADE_LEVEL
            || secondWeapon.getUpgradeLevel() < Weapon.MAX_UPGRADE_LEVEL)
        {
            return "Both weapons must reach level 5.";
        }

        Weapon mergedWeapon = createMergedWeapon(checkMerge(firstWeapon, secondWeapon));
        if (mergedWeapon == null)
        {
            return "Those weapons cannot be merged.";
        }

        player.getInventory().remove(firstWeapon);
        player.getInventory().remove(secondWeapon);
        mergedWeapon.setPlayer(player);
        player.getInventory().add(mergedWeapon);
        player.getInventory().setChoosedWeaponIndex(0);
        player.discoverItem(mergedWeapon);

        return "Merged into " + mergedWeapon.getName() + ".";
    }

    private Weapon createMergedWeapon(int mergeValue)
    {
        if (mergeValue == 1)
        {
            return new Longsword();
        }
        if (mergeValue == 2)
        {
            return new GreatAxe();
        }
        if (mergeValue == 3)
        {
            return new Halberd();
        }
        return null;
    }

    @Override
    public String interact(Player player)
    {
        return "Opened Upgrade Station.";
    }
}
