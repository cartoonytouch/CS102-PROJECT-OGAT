package Items.Weapons;

public class Longsword extends Weapon {

    public Longsword()
    {
        super(attackDamageModerate, attackSpeedHigh, rangeHigh, 14, 3);
        this.itemID = "34";
        this.name = "Longsword";
        loadSharedSprite("Assets/ItemAssets/WeaponAssets/LongswordBase.png");
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
    }
}
