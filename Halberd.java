public class Halberd extends Weapon{

    Halberd()
    {
        super(attackDamageHigh,attackSpeedModerate,rangeHigh);
        this.itemID = "35";
        this.name = "Halberd";

    }

    @Override
    void upgrade() {
        
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
        
    }


    
}
