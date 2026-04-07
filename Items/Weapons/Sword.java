package Items.Weapons;

public class Sword extends Weapon {

    public Sword()
    {
        super(attackDamageModerate, attackSpeedModerate, rangeModerate, 18, 3);
        this.itemID = "31";
        this.name = "Sword";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/SwordBase.png");
    }

    @Override
    public void upgrade()
    {
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageModerate + 5);
    }
}
