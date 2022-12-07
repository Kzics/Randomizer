package fr.randomizer.listeners;

import fr.randomizer.Main;
import fr.randomizer.utils.ColorsUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class PlayerJoin implements Listener {



    private Main instance;

    public PlayerJoin(final Main instance){
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(hasWaitingItem(player)) {
            List<String> currentMaterialList = instance.getConfig().getStringList("Waiting." + player.getUniqueId() + ".materials");


            if(!(currentMaterialList.size() > 1)) {
                currentMaterialList.forEach((matString) -> {
                    Material mat = Material.getMaterial(matString);
                    if (mat == null) {
                        System.out.println("Item Not Found");
                        return;
                    }

                    ItemStack it = new ItemStack(mat);
                    Component comp = it.displayName();
                     String plain = PlainTextComponentSerializer.plainText().serialize(comp);


                    player.sendMessage(text().append(text(ColorsUtil.translate.apply("&aVous avez reçu un item aléatoire : ")))
                            .append(comp.color(TextColor.color(25,255,255))));

                    instance.sendMessageToConsole(plain,player);

                    if(instance.hasInventoryFull(player)) {
                        if(instance.canFillStack(player.getInventory(),it.getType())){

                            instance.fillStack(player.getInventory(),it.getType());
                            return;
                        }
                        player.getWorld().dropItemNaturally(player.getLocation().toBlockLocation(),it);
                        return;
                    }
                    instance.sendMessageToConsole(String.valueOf(new TranslatableComponent("block.minecraft.iron_ore")),player);
                    player.getInventory().addItem(it);

                });
            }else{
                player.sendMessage(text().append(text(ColorsUtil.translate.apply("&aVous avez reçu "+ currentMaterialList.size()+" items aléatoire : "))));

                currentMaterialList.forEach(matStr->{
                    Material mat = Material.getMaterial(matStr);
                    ItemStack it = new ItemStack(mat);
                    Component comp = it.displayName();
                    final String plain = PlainTextComponentSerializer.plainText().serialize(comp);

                    player.sendMessage(text().append(comp.color(TextColor.color(25,255,255))));

                    instance.sendMessageToConsole(plain,player);

                    if(instance.hasInventoryFull(player)) {
                        if(instance.canFillStack(player.getInventory(),it.getType())){
                            instance.fillStack(player.getInventory(),it.getType());
                            return;
                        }
                        player.getWorld().dropItemNaturally(player.getLocation().toBlockLocation(),it);
                        return;
                    }
                    player.getInventory().addItem(it);

                });
            }

            instance.getConfig().set("Waiting." + player.getUniqueId(),null);

            try {
                instance.getConfig().save(new File(instance.getDataFolder(),"config.yml"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean hasWaitingItem(Player player){
        return instance.getInstance().getConfig().get("Waiting." + player.getUniqueId() + ".materials") != null;
    }
}
