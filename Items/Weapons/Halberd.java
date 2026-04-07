package Items.Weapons;

public class Halberd extends Weapon {

    public Halberd()
    {
        super(attackDamageHigh, attackSpeedModerate, rangeHigh, 12, 3);
        this.itemID = "35";
        this.name = "Halberd";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/HalbertBase.png");
    }

    @Override
    public void upgrade()
    {
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
    }
}
