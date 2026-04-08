package HelperClasses;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed , downPressed , leftPressed , rightPressed, spacePressed;
    public boolean jPressed, kPressed, ePressed, cPressed, lPressed;
    public boolean attackPressed, switchWeaponPressed, rPressed;
    public boolean escPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
        if(code == KeyEvent.VK_R){
            rPressed = true;
        }

        if(code == KeyEvent.VK_J){
            jPressed = true;
            attackPressed = true;
        }
        if(code == KeyEvent.VK_K){
            kPressed = true;
        }
        if(code == KeyEvent.VK_E){
            ePressed = true;
        }
        if(code == KeyEvent.VK_C){
            cPressed = true;
        }
        if(code == KeyEvent.VK_L){
            lPressed = true;
            switchWeaponPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            escPressed = true;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }
        if(code == KeyEvent.VK_R){
            rPressed = false;
        }

        if(code == KeyEvent.VK_J){
            jPressed = false;
        }
        if(code == KeyEvent.VK_K){
            kPressed = false;
        }
        if(code == KeyEvent.VK_E){
            ePressed = false;
        }
        if(code == KeyEvent.VK_C){
            cPressed = false;
        }
        if(code == KeyEvent.VK_L){
            lPressed = false;
        }
    }

    
}
