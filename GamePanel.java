
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Map.Room.Room;
import Renderers.RoomRenderPanel;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 5;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 13;
    final int maxScreenRow = 9;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    public Room[][] mapGrid;  /// bu yeni
    public int curGridX;
    public int curGridY;

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    Player player = new Player(this,keyH);
    MeleeEnemy enemy1 = null; //new MeleeEnemy(this);
    LongRangeEnemy enemy2 = null; //new LongRangeEnemy(this);

    public Heart heart = null; //new Heart(this);

    public InteractableObjects chest = null;//new Chest();

    public Room currentRoom;
    public RoomRenderPanel roomRenderer;


    public GamePanel(Room[][] grid, Room startRoom){ //// grid ve start room alıyorum artık ////

        this.mapGrid = grid;
        this.curGridX = startRoom.gridX; /// mevcut konumu ///
        this.curGridY = startRoom.gridY; /// mevcut konumu ///
        this.currentRoom = startRoom;


        if(this.currentRoom != null){ //// burdada işte room renderer çağırıp ikisini birleştiriyorum ////
            this.roomRenderer = new RoomRenderPanel(this.currentRoom);
            this.roomRenderer.setSize(screenWidth,screenHeight);
        }

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        player.xCoord = (screenWidth/2)-(tileSize/2);
        player.yCoord = (screenHeight/2)-(tileSize/2);

        revealRooms();  /// bu mini map için ///

    }

    public void changeRoom(int targetX, int targetY, String direction){ ///// bu yeni metod oda değişimi ////

        Room nextRoom = null;

        for (int i = 0; i < mapGrid.length; i++) {
            for (int j = 0; j < mapGrid[i].length; j++) {
                Room r = mapGrid[i][j];
                if (r != null && r.gridX == targetX && r.gridY == targetY) {
                    nextRoom = r;
                    // break;
                }
            }
        }

        if (nextRoom != null) {
            // If tehre is a door and room
            curGridX = targetX;
            curGridY = targetY;
            currentRoom = nextRoom;
            roomRenderer.setActiveRoom(currentRoom);

            revealRooms();


            int roomPixelWidth = currentRoom.width*tileSize;
            int roomPixelHeight = currentRoom.height*tileSize;


            if(direction.equals("right")){
                player.xCoord = tileSize +20; 
            }
            if(direction.equals("left")){
                player.xCoord = roomPixelWidth - (tileSize*2)-20;
            } 
            if(direction.equals("up")){
                player.yCoord = roomPixelHeight - (tileSize*2)-20;
            }    
            if(direction.equals("down")){
                player.yCoord = tileSize+20;
            } 
            
        } else {
            // If there is no door
            System.out.println("Borders. Theer is no door here.");
            if(direction.equals("right")){
                player.xCoord -= 20;
            } 
            if(direction.equals("left")){
                player.xCoord += 20;
            }  
            if(direction.equals("up")){
                player.yCoord += 20;
            }    
            if(direction.equals("down")){
                player.yCoord -= 20;
            } 
        }
    }

    public void revealRooms() { //// mini map'te sadece gittiğim odalar açılsın diye ////
        if(currentRoom == null) return;
        
        for (int i = 0; i < mapGrid.length; i++) {
            for (int j = 0; j < mapGrid[i].length; j++) {
                Room r = mapGrid[i][j];
                if (r != null) {

                    int distance = Math.abs(r.gridX - currentRoom.gridX) + Math.abs(r.gridY - currentRoom.gridY);
                    if (distance <= 1) {
                        r.isDiscovered = true;
                    }
                }
            }
        }
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;

        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        
        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                drawCount++;
                delta--;
            }

            if(timer >= 1000000000){

                System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update(){

        player.update();
        //enemy1.update();
        //enemy2.update();

        // int playerTileX = (player.xCoord + tileSize / 2) / tileSize;
        // int playerTileY = (player.yCoord + tileSize / 2) / tileSize;

        
        // for (Map.Room.Door d : currentRoom.placedDoors) {
            
        //     // right door check
        //     if (player.xCoord > screenWidth - tileSize && playerTileY == d.coordY) {
        //         changeRoom(curGridX + 1, curGridY, "right");
        //         break;
        //     }
        //     // left door check
        //     else if (player.xCoord < 0 && playerTileY == d.coordY) {
        //         changeRoom(curGridX - 1, curGridY, "left");
        //         break;
        //     }
        //     // up door check
        //     else if (player.yCoord < 0 && playerTileX == d.coordX) {
        //         changeRoom(curGridX, curGridY - 1, "up");
        //         break;
        //     }
        //     // down door check
        //     else if (player.yCoord > screenHeight - tileSize && playerTileX == d.coordX) {
        //         changeRoom(curGridX, curGridY + 1, "down");
        //         break;
        //     }
        // }

        // if(currentRoom != null && currentRoom.localEnemies != null){
        //     for(int i=0; i<currentRoom.localEnemies.size(); i++){
        //         //currentRoom.localEnemies.get(i).update();
        //     }
        // }
    }

    // To render miniMap
    public void drawMinimap(Graphics2D g2) {
        int miniTile = 18; 
        int mapX = 20; 
        int mapY = screenHeight - 200;
        
        for (int i = 0; i < mapGrid.length; i++) {
            for (int j = 0; j < mapGrid[i].length; j++) {
                Room r = mapGrid[i][j];
                if (r != null && r.isDiscovered) {
                    
                    int drawX = mapX + (r.gridX * miniTile);
                    int drawY = mapY + (r.gridY * miniTile);
                    
                    
                    if (r == currentRoom){
                        g2.setColor(Color.YELLOW);// current room
                    } 
                    else if (r.type.equals("Start")){
                        g2.setColor(Color.GREEN); // start room
                    } 
                    else if (r.type.equals("Boss")){
                        g2.setColor(Color.RED); // Boss room
                    } 
                    else if (r.type.equals("Shop")){
                        g2.setColor(Color.MAGENTA); // shop
                    } 
                    else{
                        g2.setColor(new Color(100, 100, 100, 200)); // empty room
                    }
                    
                    g2.fillRect(drawX, drawY, miniTile - 2, miniTile - 2);
                    g2.setColor(Color.WHITE);
                    g2.drawRect(drawX, drawY, miniTile - 2, miniTile - 2);
                }
            }
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if(roomRenderer != null){
            roomRenderer.paintComponent(g2);
        }
        player.draw(g2);

        //enemy1.draw(g2);
        //enemy2.draw(g2);

        //chest.draw(g2, tileSize);

        //heart.draw(g2);

        //g2.setColor(Color.RED);
        //g2.fillRect(50, 50, 100, 100);

        // if(currentRoom != null && currentRoom.localEnemies != null){
        //     for(int i=0; i < currentRoom.localEnemies.size(); i++){
        //         //currentRoom.localEnemies.get(i).draw(g2);
        //     }
        // }

        drawMinimap(g2); // şu da yeni ki çizdirsin //
        g2.dispose();
    }

}
