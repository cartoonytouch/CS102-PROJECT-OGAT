package Items.Weapons;
public class GreatAxe extends Weapon{

    GreatAxe()
    {
        super(attackDamageHigh, attackSpeedHigh, rangeModerate);
        this.itemID = "36";
        this.name = "GreatAxe";
    }


    @Override
    void upgrade() {
        
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
        
    }


    
}
