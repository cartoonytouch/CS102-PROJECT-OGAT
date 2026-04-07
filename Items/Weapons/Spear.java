package Items.Weapons;

public class Spear extends Weapon {

    public Spear()
    {
        super(attackDamageLow, attackSpeedHigh, rangeHigh, 8, 3);
        this.itemID = "32";
        this.name = "Spear";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/SpearBase.png");
    }

    @Override
    public void upgrade()
    {
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageLow + 5);
    }
}
