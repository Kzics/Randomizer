package fr.randomizer.commands;

import fr.randomizer.Main;
import fr.randomizer.utils.ColorsUtil;
import fr.randomizer.utils.MaterialChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.kyori.adventure.text.Component.text;

public class CommandRandomizer implements CommandExecutor {


    private final Main instance;
    public CommandRandomizer(final Main instance){
        this.instance = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        executeCommand(sender,args);

        return false;
    }




    private Material getRandomMaterial(){
        List<Material> matlist = instance.getRemasteredMaterial();

        int random = new Random().nextInt(matlist.size());

        return matlist.get(random);
    }

    public List<Material> getRandomMaterial(int amount){
        ArrayList<Material> materials = new ArrayList<>();
        int count = 0;
        while(count<amount){
            Material mat = getRandomMaterial();
            materials.add(mat);

            count++;
        }
        return materials;
    }

    private boolean isOnline(String playerString){
     return Bukkit.getOnlinePlayers().stream().anyMatch(player-> player.getName().equalsIgnoreCase(playerString));
    }

    private boolean isInteger(String str){
        try{

            Integer.parseInt(str);

            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    private boolean executeCommand(CommandSender player,String[] args){

        if(player instanceof Player) {
            if (!player.hasPermission("randomizer.admin")) {
                player.sendMessage(ColorsUtil.translate.apply("&cErreur: Vous n'avez pas la permission ! "));
                return false;
            }
        }

        if(args.length == 1){
            Material randomMat = getRandomMaterial();
            while(MaterialChecker.hasCheatItems(randomMat)){
                randomMat = getRandomMaterial();
            }
            String target = args[0];

            if(isOnline(target)) {
                Player targetPlayer = Bukkit.getPlayer(target);
                ItemStack it = new ItemStack(randomMat);
                Component comp = it.displayName();
                String plain = PlainTextComponentSerializer.plainText().serialize(comp);


                while(plain.equals("[Air]")){
                    randomMat = getRandomMaterial();
                    it = new ItemStack(randomMat);
                    comp = it.displayName();
                    plain = PlainTextComponentSerializer.plainText().serialize(comp);
                }

                System.out.println(plain);

                if(instance.hasInventoryFull(targetPlayer)) {
                    if(instance.canFillStack(targetPlayer.getInventory(),new ItemStack(randomMat).getType())){
                        targetPlayer.sendMessage(text().append(text(ColorsUtil.translate.apply("&aVous avez reçu un item aléatoire : ")))
                                .append(new ItemStack(randomMat).displayName().color(TextColor.color(25,255,255))));

                        instance.sendMessageToConsole(plain,targetPlayer);
                        instance.fillStack(targetPlayer.getInventory(),new ItemStack(randomMat).getType());
                        return false;
                    }
                    targetPlayer.sendMessage(text().append(text(ColorsUtil.translate.apply("&aVous avez reçu un item aléatoire : ")))
                            .append(new ItemStack(randomMat).displayName().color(TextColor.color(25,255,255))));

                    targetPlayer.getWorld().dropItemNaturally(targetPlayer.getLocation().toBlockLocation(),new ItemStack(randomMat));

                    instance.sendMessageToConsole(plain,targetPlayer);
                    return false;
                }

                targetPlayer.getInventory().addItem(new ItemStack(randomMat));

                instance.sendMessageToConsole(plain,targetPlayer);

                targetPlayer.sendMessage(text().append(text(ColorsUtil.translate.apply("&aVous avez reçu un item aléatoire : ")))
                        .append(new ItemStack(randomMat).displayName().color(TextColor.color(25,255,255))));

            }else{

                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

                if(player instanceof Player) {
                    player.sendMessage(ColorsUtil.translate.apply("&aLe joueur étant hors-ligne il recevra son item à sa prochaine connexion."));
                }else{
                    instance.getLogger().info("Le joueur étant hors-ligne il recevra son item à sa prochaine connexion.");
                }
                List<String> materialList = instance.getConfig().get("Waiting." + targetPlayer.getUniqueId() + ".materials") == null ?
                        new ArrayList<>() : instance.getConfig().getStringList("Waiting." + targetPlayer.getUniqueId() + ".materials");

                materialList.add(randomMat.name());
                instance.getConfig().set("Waiting." + targetPlayer.getUniqueId() + ".materials",materialList);

                instance.saveConfig();
            }

        }else if(args.length == 2){
            String target = args[0];
            int amountItem;
            if(isInteger(args[1])){
                amountItem = Integer.parseInt(args[1]);
            }else{
                if(player instanceof Player) {
                    player.sendMessage(ColorsUtil.translate.apply("&cVous devez entrer un entier valide !"));
                }else{
                    instance.getLogger().info("&aLe joueur étant hors-ligne il recevra son item à sa prochaine connexion.");
                }

                return false;
            }


            List<Material> materials = getRandomMaterial(amountItem);

            if(isOnline(target)){
                Player targetPlayer = Bukkit.getPlayer(target);

                if(player instanceof Player) {
                    player.sendMessage(ColorsUtil.translate.apply("&aVous avez envoyé " + amountItem + " items aléatoire à " + targetPlayer.getName()));
                }else{
                    instance.getLogger().info("Vous avez envoyé " + amountItem + " items aléatoire à " + targetPlayer.getName());
                }
                targetPlayer.sendMessage(ColorsUtil.translate.apply("&aVous avez reçu " + amountItem + " items: "));

                materials.forEach(mat->{
                    ItemStack it = new ItemStack(mat);
                    Component comp = it.displayName();


                    String plain = PlainTextComponentSerializer.plainText().serialize(comp);
                    while(plain.equals("[Air]")){
                        mat = getRandomMaterial();
                        it = new ItemStack(mat);
                        comp = it.displayName();
                        plain = PlainTextComponentSerializer.plainText().serialize(comp);
                    }

                    targetPlayer.sendMessage(text().append(comp.color(TextColor.color(25,255,255))));

                    instance.getInstance().sendMessageToConsole(plain,targetPlayer);

                    targetPlayer.getInventory().addItem(it);

                    if(instance.hasInventoryFull(targetPlayer)) {
                        if(instance.canFillStack(targetPlayer.getInventory(),it.getType())){
                            instance.fillStack(targetPlayer.getInventory(),it.getType());
                            return;
                        }

                        targetPlayer.getWorld().dropItem(targetPlayer.getLocation().toBlockLocation(),it);
                    }
                });



            }else{

                List<String> materialNames = new ArrayList<>();

                materials.forEach(mat-> {
                    ItemStack it = new ItemStack(mat);
                    Component comp = it.displayName();
                    String plain = PlainTextComponentSerializer.plainText().serialize(comp);

                    while(plain.equals("[Air]")){
                        mat = getRandomMaterial();
                        it = new ItemStack(mat);
                        plain = PlainTextComponentSerializer.plainText().serialize(it.displayName());
                    }
                    materialNames.add(mat.name());
                });

                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

                player.sendMessage(ColorsUtil.translate.apply("&aLe joueur étant hors-ligne il recevra "+amountItem +" items à sa prochaine connexion."));

                instance.getConfig().set("Waiting." + targetPlayer.getUniqueId() + ".materials",materialNames);

                instance.saveConfig();
            }


        }

        return false;
    }
}
