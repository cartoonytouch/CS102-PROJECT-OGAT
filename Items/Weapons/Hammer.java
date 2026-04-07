package Items.Weapons;

public class Hammer extends Weapon {

    public Hammer()
    {
        super(attackDamageHigh, attackSpeedModerate, rangeLow, 20, 3);
        this.itemID = "33";
        this.name = "Hammer";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/HammerBase.png");
    }

    @Override
    public void upgrade()
    {
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
    }
}
