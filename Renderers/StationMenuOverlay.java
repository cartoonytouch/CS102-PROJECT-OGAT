package Renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import Entities.Characters.Player;
import Items.Item;
import Items.ItemCatalog;
import Items.Weapons.Weapon;
import Map.Room.BuyStation;
import Map.Room.Station;
import Map.Room.UpgradeStation;

public class StationMenuOverlay {

    private static final Color PANEL_FILL = new Color(88, 88, 88, 238);
    private static final Color PANEL_BORDER = new Color(193, 193, 193, 245);
    private static final Color CARD_FILL = new Color(74, 74, 74, 235);
    private static final Color BUTTON_FILL = new Color(112, 112, 112, 235);
    private static final Color BUTTON_DISABLED = new Color(76, 76, 76, 220);
    private static final Color EQUIPPED_ACCENT = new Color(226, 199, 120, 255);

    private final Map<String, BufferedImage> itemIconCache = new HashMap<>();
    private final BufferedImage coinIcon;

    private Station activeStation;
    private String feedbackMessage = "";
    private long feedbackMessageUntil = 0L;

    public StationMenuOverlay()
    {
        coinIcon = loadImage("Assets/ItemAssets/Coin.png");
    }

    public boolean isOpen()
    {
        return activeStation != null;
    }

    public void open(Station station, Player player)
    {
        activeStation = station;
        feedbackMessage = "";
        feedbackMessageUntil = 0L;

        if (station instanceof BuyStation)
        {
            ((BuyStation) station).ensureOffers(player);
        }
    }

    public void close()
    {
        activeStation = null;
        feedbackMessage = "";
        feedbackMessageUntil = 0L;
    }

    public boolean handleClick(int mouseX, int mouseY, Player player, int screenWidth, int screenHeight)
    {
        if (!isOpen())
        {
            return false;
        }

        Rectangle panelBounds = getPanelBounds(screenWidth, screenHeight);
        if (getCloseButtonBounds(panelBounds).contains(mouseX, mouseY))
        {
            close();
            return true;
        }

        if (activeStation instanceof BuyStation)
        {
            BuyStation buyStation = (BuyStation) activeStation;

            for (int slot = 0; slot < buyStation.getOfferCount(); slot++)
            {
                if (getBuyButtonBounds(panelBounds, slot).contains(mouseX, mouseY))
                {
                    setFeedback(buyStation.purchaseOffer(slot, player));
                    return true;
                }
            }
        }
        else if (activeStation instanceof UpgradeStation)
        {
            UpgradeStation upgradeStation = (UpgradeStation) activeStation;

            if (getUpgradeButtonBounds(panelBounds).contains(mouseX, mouseY))
            {
                setFeedback(upgradeStation.upgradeEquippedWeapon(player));
                return true;
            }

            if (getMergeButtonBounds(panelBounds).contains(mouseX, mouseY))
            {
                setFeedback(upgradeStation.mergeWeapons(player));
                return true;
            }
        }

        return panelBounds.contains(mouseX, mouseY);
    }

    public void draw(Graphics2D g2, Player player, int screenWidth, int screenHeight)
    {
        if (!isOpen())
        {
            return;
        }

        Graphics2D overlayGraphics = (Graphics2D) g2.create();
        overlayGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle panelBounds = getPanelBounds(screenWidth, screenHeight);

        overlayGraphics.setColor(new Color(0, 0, 0, 120));
        overlayGraphics.fillRect(0, 0, screenWidth, screenHeight);

        overlayGraphics.setColor(PANEL_FILL);
        overlayGraphics.fillRoundRect(panelBounds.x, panelBounds.y, panelBounds.width, panelBounds.height, 36, 36);
        overlayGraphics.setColor(PANEL_BORDER);
        overlayGraphics.setStroke(new BasicStroke(3f));
        overlayGraphics.drawRoundRect(panelBounds.x, panelBounds.y, panelBounds.width, panelBounds.height, 36, 36);

        drawCurrency(overlayGraphics, player, panelBounds);
        drawCloseButton(overlayGraphics, panelBounds);

        if (activeStation instanceof BuyStation)
        {
            drawBuyStationMenu(overlayGraphics, (BuyStation) activeStation, player, panelBounds);
        }
        else if (activeStation instanceof UpgradeStation)
        {
            drawUpgradeStationMenu(overlayGraphics, (UpgradeStation) activeStation, player, panelBounds);
        }

        drawFeedback(overlayGraphics, panelBounds);
        overlayGraphics.dispose();
    }

    private void drawBuyStationMenu(Graphics2D g2, BuyStation buyStation, Player player, Rectangle panelBounds)
    {
        for (int slot = 0; slot < buyStation.getOfferCount(); slot++)
        {
            Rectangle cardBounds = getBuyCardBounds(panelBounds, slot);
            Rectangle buttonBounds = getBuyButtonBounds(panelBounds, slot);
            Item item = buyStation.getOfferItem(slot);
            int price = buyStation.getOfferPrice(slot);
            boolean canBuy = buyStation.canBuyOffer(slot, player);

            drawCardBackground(g2, cardBounds, slot == 0 ? new Color(170, 170, 170, 170) : new Color(150, 150, 150, 150));

            if (item != null)
            {
                drawItemIcon(g2, item, cardBounds.x + 34, cardBounds.y + 28, cardBounds.width - 68, 112);
                drawPriceLine(g2, price, cardBounds.x + (cardBounds.width / 2), cardBounds.y + 166);
            }
            else
            {
                drawCenteredString(g2, "SOLD", cardBounds, new Color(215, 215, 215, 230));
            }

            drawButton(g2, buttonBounds, item != null ? "BUY" : "SOLD", item != null && canBuy);
        }
    }

    private void drawUpgradeStationMenu(Graphics2D g2, UpgradeStation upgradeStation, Player player, Rectangle panelBounds)
    {
        for (int slot = 0; slot < 2; slot++)
        {
            Rectangle cardBounds = getWeaponCardBounds(panelBounds, slot);
            Weapon weapon = upgradeStation.getWeaponAtSlot(player, slot);
            boolean equipped = player != null
                && player.getInventory() != null
                && player.getInventory().getChoosedWeaponIndex() == slot;

            drawCardBackground(g2, cardBounds, equipped ? EQUIPPED_ACCENT : new Color(160, 160, 160, 140));

            if (weapon != null)
            {
                drawItemIcon(g2, weapon, cardBounds.x + 28, cardBounds.y + 24, cardBounds.width - 56, 114);
                g2.setColor(Color.WHITE);
                g2.drawString("LV " + weapon.getUpgradeLevel(), cardBounds.x + 24, cardBounds.y + cardBounds.height - 32);

                if (equipped)
                {
                    g2.setColor(EQUIPPED_ACCENT);
                    g2.drawString("EQUIPPED", cardBounds.x + 24, cardBounds.y + cardBounds.height - 12);
                }
            }
            else
            {
                drawCenteredString(g2, "EMPTY", cardBounds, new Color(205, 205, 205, 215));
            }
        }

        Rectangle upgradeButton = getUpgradeButtonBounds(panelBounds);
        Rectangle mergeButton = getMergeButtonBounds(panelBounds);

        drawButton(g2, upgradeButton, "UPGRADE", upgradeStation.canUpgradeEquippedWeapon(player));
        drawPriceLine(g2, UpgradeStation.UPGRADE_COST, upgradeButton.x + (upgradeButton.width / 2), upgradeButton.y + upgradeButton.height + 26);

        drawButton(g2, mergeButton, "MERGE", upgradeStation.canMerge(player));
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, "FREE", new Rectangle(mergeButton.x, mergeButton.y + mergeButton.height + 6, mergeButton.width, 22), Color.WHITE);
    }

    private void drawCardBackground(Graphics2D g2, Rectangle bounds, Color outlineColor)
    {
        g2.setColor(CARD_FILL);
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 24, 24);
        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 24, 24);
    }

    private void drawButton(Graphics2D g2, Rectangle bounds, String label, boolean enabled)
    {
        g2.setColor(enabled ? BUTTON_FILL : BUTTON_DISABLED);
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 18, 18);
        g2.setColor(PANEL_BORDER);
        g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 18, 18);
        drawCenteredString(g2, label, bounds, enabled ? Color.WHITE : new Color(200, 200, 200, 180));
    }

    private void drawCurrency(Graphics2D g2, Player player, Rectangle panelBounds)
    {
        int currency = (player != null) ? player.getCurrency() : 0;
        int iconSize = 24;
        int x = panelBounds.x + panelBounds.width - 120;
        int y = panelBounds.y + 22;

        if (coinIcon != null)
        {
            g2.drawImage(coinIcon, x, y, iconSize, iconSize, null);
        }

        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(currency), x + iconSize + 10, y + 18);
    }

    private void drawCloseButton(Graphics2D g2, Rectangle panelBounds)
    {
        Rectangle closeBounds = getCloseButtonBounds(panelBounds);
        g2.setColor(new Color(108, 108, 108, 230));
        g2.fillRoundRect(closeBounds.x, closeBounds.y, closeBounds.width, closeBounds.height, 14, 14);
        g2.setColor(PANEL_BORDER);
        g2.drawRoundRect(closeBounds.x, closeBounds.y, closeBounds.width, closeBounds.height, 14, 14);
        drawCenteredString(g2, "X", closeBounds, Color.WHITE);
    }

    private void drawItemIcon(Graphics2D g2, Item item, int x, int y, int maxWidth, int maxHeight)
    {
        BufferedImage icon = getItemIcon(item);
        if (icon == null)
        {
            return;
        }

        int drawWidth = icon.getWidth();
        int drawHeight = icon.getHeight();
        double scale = Math.min((double) maxWidth / drawWidth, (double) maxHeight / drawHeight);

        drawWidth = Math.max(1, (int) Math.round(drawWidth * scale));
        drawHeight = Math.max(1, (int) Math.round(drawHeight * scale));

        int drawX = x + Math.max(0, (maxWidth - drawWidth) / 2);
        int drawY = y + Math.max(0, (maxHeight - drawHeight) / 2);

        g2.drawImage(icon, drawX, drawY, drawWidth, drawHeight, null);
    }

    private BufferedImage getItemIcon(Item item)
    {
        if (item == null)
        {
            return null;
        }

        String iconPath = ItemCatalog.getIconPath(item.getItemID());
        if (iconPath == null)
        {
            return null;
        }

        if (!itemIconCache.containsKey(iconPath))
        {
            itemIconCache.put(iconPath, loadImage(iconPath));
        }

        return itemIconCache.get(iconPath);
    }

    private BufferedImage loadImage(String path)
    {
        try
        {
            return ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private void drawPriceLine(Graphics2D g2, int price, int centerX, int baselineY)
    {
        String priceText = String.valueOf(price);
        FontMetrics metrics = g2.getFontMetrics();
        int totalWidth = metrics.stringWidth(priceText) + 8 + 18;
        int startX = centerX - (totalWidth / 2);

        g2.setColor(Color.WHITE);
        g2.drawString(priceText, startX, baselineY);

        if (coinIcon != null)
        {
            g2.drawImage(coinIcon, startX + metrics.stringWidth(priceText) + 8, baselineY - 14, 18, 18, null);
        }
    }

    private void drawFeedback(Graphics2D g2, Rectangle panelBounds)
    {
        if (feedbackMessage == null || feedbackMessage.isBlank())
        {
            return;
        }

        if (System.currentTimeMillis() > feedbackMessageUntil)
        {
            feedbackMessage = "";
            return;
        }

        Rectangle feedbackBounds = new Rectangle(panelBounds.x + 30, panelBounds.y + panelBounds.height - 48, panelBounds.width - 60, 24);
        drawCenteredString(g2, feedbackMessage, feedbackBounds, Color.WHITE);
    }

    private void setFeedback(String message)
    {
        feedbackMessage = message;
        feedbackMessageUntil = System.currentTimeMillis() + 2500L;
    }

    private Rectangle getPanelBounds(int screenWidth, int screenHeight)
    {
        int width = 720;
        int height = 360;
        return new Rectangle((screenWidth - width) / 2, (screenHeight - height) / 2, width, height);
    }

    private Rectangle getCloseButtonBounds(Rectangle panelBounds)
    {
        return new Rectangle(panelBounds.x + panelBounds.width - 52, panelBounds.y + 18, 30, 30);
    }

    private Rectangle getBuyCardBounds(Rectangle panelBounds, int slotIndex)
    {
        int cardWidth = 230;
        int cardHeight = 238;
        int gap = 30;
        int totalWidth = (cardWidth * 2) + gap;
        int startX = panelBounds.x + ((panelBounds.width - totalWidth) / 2);
        int x = startX + (slotIndex * (cardWidth + gap));
        int y = panelBounds.y + 62;
        return new Rectangle(x, y, cardWidth, cardHeight);
    }

    private Rectangle getBuyButtonBounds(Rectangle panelBounds, int slotIndex)
    {
        Rectangle cardBounds = getBuyCardBounds(panelBounds, slotIndex);
        return new Rectangle(cardBounds.x + 52, cardBounds.y + cardBounds.height - 56, cardBounds.width - 104, 38);
    }

    private Rectangle getWeaponCardBounds(Rectangle panelBounds, int slotIndex)
    {
        int cardWidth = 220;
        int cardHeight = 200;
        int gap = 26;
        int totalWidth = (cardWidth * 2) + gap;
        int startX = panelBounds.x + ((panelBounds.width - totalWidth) / 2);
        int x = startX + (slotIndex * (cardWidth + gap));
        int y = panelBounds.y + 54;
        return new Rectangle(x, y, cardWidth, cardHeight);
    }

    private Rectangle getUpgradeButtonBounds(Rectangle panelBounds)
    {
        return new Rectangle(panelBounds.x + 136, panelBounds.y + 278, 170, 42);
    }

    private Rectangle getMergeButtonBounds(Rectangle panelBounds)
    {
        return new Rectangle(panelBounds.x + panelBounds.width - 306, panelBounds.y + 278, 170, 42);
    }

    private void drawCenteredString(Graphics2D g2, String text, Rectangle bounds, Color color)
    {
        FontMetrics metrics = g2.getFontMetrics();
        int x = bounds.x + ((bounds.width - metrics.stringWidth(text)) / 2);
        int y = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
}
