package fr.randomizer;

import fr.randomizer.commands.CommandRandomizer;
import fr.randomizer.listeners.PlayerJoin;
import fr.randomizer.utils.MaterialChecker;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.print.Paper;
import java.util.*;

public class Main extends JavaPlugin {

    private Main instance;

    private List<Material> remasteredMaterialList;


    @Override
    public void onEnable() {
        instance = this;

        registerCommands();
        registerListeners();

        removeBad();


        if(remasteredMaterialList.contains(Material.BUNDLE)){
            System.out.println("Unfortunately oui");
        }
    }

    private void registerCommands(){
        getCommand("randomizer").setExecutor(new CommandRandomizer(this));
    }


    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerJoin(instance),this);


    }

    public Main getInstance() {
        return instance;
    }

    public boolean hasInventoryFull(Player player){
        return player.getInventory().firstEmpty() == -1;
    }

    public void fillStack(Inventory inv, Material materials){
        for(ItemStack it: inv.getContents()){
            if(it == null){
                return;
            }
            if(materials.equals(it.getType())){
                it.setAmount(it.getAmount()+1);
                break;
            }
        }
    }

    public boolean canFillStack(Inventory inv,Material material){
        for(ItemStack it : inv.getContents()){
            if(it == null){
                continue;
            }
            if(material.equals(it.getType())){
                return true;
            }
        }
        return false;
    }

    private void removeBad(){
        List<Material> matlist = new ArrayList<>(Arrays.asList(Material.values()));
                matlist.removeIf(MaterialChecker::hasCheatItems);
            this.remasteredMaterialList = matlist;
        System.out.println("Done");
        }


    public void sendMessageToConsole(String item, Player player){
        getLogger().info(("Le joueur " + player.getName() + " a reçu un item aléatoire : " + item));
    }
    public void sendMessageToConsole(TranslatableComponent item, Player player){
        Bukkit.getConsoleSender().sendMessage("Le joueur " + player.getName() + " a reçu un item aléatoire : " + item);
    }

    public List<Material> getRemasteredMaterial(){
        return remasteredMaterialList;
    }
}
