package me.thundercaster.sumoduelplugin;

import me.thundercaster.sumoduelplugin.Commands.SumoCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SumoDuelPlugin extends JavaPlugin {

    public List<Player> plist = new ArrayList<>();
    @Override
    public void onEnable() {

        // Calling the command
        getCommand("SumoDuel").setExecutor(new SumoCommand(this));

    }
    //checks if the player's inventory is empty
    public boolean inventorycheck(Player p){
        if (p.getInventory().isEmpty()){
            p.sendMessage(ChatColor.AQUA + "Your Inventory is Empty");
            return true;
        }
        p.sendMessage(ChatColor.RED + "You must Empty Your Inventory");
        return false;
    }
    //checks if the player's health is full
    public boolean healthcheck(Player p){
        if (p.getHealth() == p.getMaxHealth()){
            p.sendMessage(ChatColor.AQUA + "You are at full health");
            return true;
        }
        return false;
    }
    //checks if the player's saturation is full
    public boolean saturationcheck(Player p){
        if (p.getSaturation() == 14){
            p.sendMessage(ChatColor.AQUA + "Your Saturation is full");
            return true;
        }
        p.sendMessage(ChatColor.RED + "Your Should Saturation Be Full");
        return false;
    }
    //checks if all of the above is true
    public boolean overallcheck(Player p){
        return (saturationcheck(p) && healthcheck(p) && inventorycheck(p));
    }
    //resests the two players' health and saturation
    public void health_reset(Player p, Player p1){
        p.setHealth(20);
        p.setSaturation(14);
        p1.setHealth(20);
        p1.setSaturation(14);
        p.sendMessage(ChatColor.AQUA + "your health has been restored");
        p1.sendMessage(ChatColor.AQUA + "your health has been restored");
    }

}
