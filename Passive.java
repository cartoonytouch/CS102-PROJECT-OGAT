public class Passive extends Item{

    String buffType;
    float multiplier;

    

    Passive(String buffType,float multiplier)
    {
        this.buffType = buffType;
        this.multiplier = multiplier;

        if(buffType.equals("Speed"))
        {
            this.name = "Agility Passive";
            this.itemID = "21";
        }
        if(buffType.equals("Regeneration"))
        {
            this.name = "Regen Passive";
            this.itemID = "22";
        }
    }
    void applyPermanentBuff(Player p)
    {
        if(this.getItemID().equals("21"))
        {
            p.setSpeed(Math.round(p.getSpeed() * multiplier));
            System.out.println("Speed Up!");

        }
        if(this.getItemID().equals("22"))
        {

        }
    }
    // public static void main(String[] args) {
        

    //     Passive speedPassive = new Passive("Speed", 3);

    //     Player player = new Player(0, 0, "Lancer", 2);
    //     System.out.println("Speed: " +  player.getSpeed());
    //     speedPassive.applyPermanentBuff(player);
    //     System.out.println("Speed: "+ player.getSpeed());
        

        

    // }

    
}
