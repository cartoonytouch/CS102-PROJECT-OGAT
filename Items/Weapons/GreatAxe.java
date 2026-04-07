package Items.Weapons;

public class GreatAxe extends Weapon {

    public GreatAxe()
    {
        super(attackDamageHigh, attackSpeedHigh, rangeModerate, 16, 3);
        this.itemID = "36";
        this.name = "GreatAxe";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/GreatAxeBase.png");
    }

    @Override
    public void upgrade()
    {
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
    }
}
