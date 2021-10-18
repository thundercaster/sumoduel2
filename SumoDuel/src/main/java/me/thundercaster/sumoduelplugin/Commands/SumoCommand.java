package me.thundercaster.sumoduelplugin.Commands;

import me.thundercaster.sumoduelplugin.SumoDuelPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SumoCommand implements CommandExecutor, Listener {

    SumoDuelPlugin plugin;

    public SumoCommand(SumoDuelPlugin plugin) {
        this.plugin = plugin;
    }
    public int number = 3;//to be used for the countdown
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender==null){
            return false;
        }
        if (sender instanceof Player p){
            if (!(args.length==1)){
                p.sendMessage(ChatColor.RED + "Please enter the name of the person that you want to duel");
            }
            else {
                Player p1 = plugin.getServer().getPlayer(args[0]);
                if (p1 == null){
                    p.sendMessage(ChatColor.RED + "couldn't find targeted player");
                }
                else {
                    if (plugin.overallcheck(p) & plugin.overallcheck(p1)){

                        final Location pl = p.getLocation(); //this will be the center of the fighting circle, arena
                        p1.teleport(pl);//teleports them on top of each other for a fair game
                        //adds the two to a list, if they are on the list they won't be able to move or hit (check event handlers)
                        plugin.plist.add(p);
                        plugin.plist.add(p1);
                        //creates the arena (call of a function)
                        WoolCircle(pl, 15, true);
                        //cd
                        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin,new Runnable(){
                            @Override
                            public void run() {
                                if (number!= -1){
                                    if (number!=0){
                                        p.sendMessage("" + number);
                                        p1.sendMessage("" + number);
                                        number--;

                                    }else{
                                        p.sendMessage(ChatColor.GOLD + "FIGHT!");
                                        p1.sendMessage("" + number);
                                        plugin.plist.remove(p);
                                        plugin.plist.remove(p1);
                                        number--;
                                    }


                                }
                            }
                        }, 0L, 20L);


                        //checks if someone left the circle
                        if (distance(p) - center(pl) > 15 )  {
                            WoolCircle(pl, 15, false);
                            p.sendMessage(ChatColor.RED + "you lost");
                            p1.sendMessage(ChatColor.GREEN + "you won");
                            plugin.health_reset(p,p1);

                        }
                        if (distance(p1) - center(pl) > 15){
                            WoolCircle(pl,15,false);
                            p1.sendMessage(ChatColor.RED + "you lost");
                            p.sendMessage(ChatColor.GREEN + "you won");
                            plugin.health_reset(p,p1);
                        }


                    }
                    else{
                        p.sendMessage(ChatColor.RED + "A verification isn't right");
                    }
                }


            }
        }
        return true;
    }
    @EventHandler
    public void freeze(PlayerMoveEvent e){
        Player p = e.getPlayer();

        while (plugin.plist.contains(p)){
            e.setCancelled(true);//freezes any player inside the list
        }


    }
    @EventHandler
    public void nohit(EntityDamageByEntityEvent e){
        if (e instanceof Player p){
            while (plugin.plist.contains(p)){
                if(e.getEntity()instanceof Player){
                    e.setCancelled(true);//stops any player inside the list from hitting another person
                }
            }
        }
    }
    //method to create a woold circle, if n is true then the circle will be created, and if n is false the circle will be removed and swaped to it's original state
    public void WoolCircle(Location l, float radius, boolean n){
        for (double t = 0; t<15; t+=0.5) {
            float x = radius*(float)Math.sin(t);
            float z = radius*(float)Math.cos(t);
            final Material m = l.getBlock().getType();
            if (n){
                l.getBlock().setType(Material.RED_WOOL);
            }
            if (!n) {
                l.getBlock().setType(m);
            }
        }

    }
    //calculate x**2 and z**2 to use later to calculate distance between the circle center and the player's position
    public float distance(Player p){
        float dx = (float) (p.getLocation().getX()*p.getLocation().getX());
        float dz = (float) (p.getLocation().getZ()*p.getLocation().getZ());
        float d = dx + dz;
        return d;
    }
    //calculate the distance for the center of the circle
    public float center(Location l){
        float dx = (float) ((float) l.getX()*l.getX());
        float dz = (float) (l.getZ()*l.getZ());
        float d = dx + dz;
        return d;
    }
}

