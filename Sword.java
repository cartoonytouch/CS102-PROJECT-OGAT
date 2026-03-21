public class Sword extends Weapon{

    

    Sword()
    {
        super(attackDamageModerate,attackSpeedModerate,rangeModerate);
        this.itemID = "31";
        this.name = "Sword";
        
    }

    @Override
    void upgrade() {
        
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageModerate + 5);
        
        
    }


    
}
