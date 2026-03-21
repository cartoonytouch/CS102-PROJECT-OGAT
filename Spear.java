public class Spear extends Weapon{
    
    

    Spear()
    {
        super(attackDamageLow, attackSpeedHigh, rangeModerate);
        this.itemID = "32";
        this.name = "Spear";
        

    }



    @Override
    void upgrade() {
        
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageLow + 5);
        
    }










}
