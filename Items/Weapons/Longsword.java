package Items.Weapons;
public class Longsword extends Weapon{
    
    Longsword()
    {
        super(attackDamageModerate,attackSpeedHigh,rangeHigh);
        this.itemID = "34";
        this.name = "Longsword";

    }


    @Override
    void upgrade() {

        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageModerate + 5);

    }
}
