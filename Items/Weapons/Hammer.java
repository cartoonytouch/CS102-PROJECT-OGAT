package Items.Weapons;
public class Hammer extends Weapon{
    
    

    Hammer()
    {
        super(attackDamageHigh, attackSpeedModerate, rangeLow);
        this.itemID = "33";
        this.name = "Hammer";
        

    }



    @Override
    void upgrade() {
        
        this.setUpgradeLevel();
        this.setAttackDamage(attackDamageHigh + 5);
        
    }
    
}
