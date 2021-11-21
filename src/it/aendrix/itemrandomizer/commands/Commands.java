package it.aendrix.itemrandomizer.commands;

import it.aendrix.itemrandomizer.main.Main;
import it.aendrix.itemrandomizer.main.utils;
import it.aendrix.itemrandomizer.obj.BaseBlock;
import it.aendrix.itemrandomizer.obj.Trail;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("itemrandomizer") || cmd.getName().equalsIgnoreCase("ir")) {
            if (!(sender instanceof Player)) {
                utils.sendMsg(sender, "&cSolo un giocatore può eseguire questo comando");
                return true;
            }

            if (!(sender.hasPermission("itemrandomizer"))) {
                utils.sendMsg(sender, "&cNon hai i permessi per eseguire questo comando");
                return true;
            }

            Player p = (Player) sender;

            if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                utils.sendMsg(p, "&7-------------------------------------------------");
                utils.sendMsg(p, "&b/ir addblock <nome> &8> &fAggiungi un blocco di base");
                utils.sendMsg(p, "&b/ir delblock <nome> &8> &fRimuovi un blocco di base");
                utils.sendMsg(p, "&b/ir adddestination <blocco> &8> &fAggiungi una destinazione");
                utils.sendMsg(p, "&b/ir deldestination <blocco> <id> &8> &fRimuovi una destinazione");
                utils.sendMsg(p, "&b/ir setrange <blocco> <raggio> &8> &fImposta il raggio di una trail");
                utils.sendMsg(p, "&b/ir setlife <blocco> <amount> &8> &fImposta la vita di una trail in secondi");
                utils.sendMsg(p, "&b/ir starttrail <blocco> &8> &fAvvia una trail");
                utils.sendMsg(p, "&7-------------------------------------------------");
            }

            if (args[0].equalsIgnoreCase("addblock")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                String blockname = args[1];

                if (Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cEsiste già un blocco con questo nome");
                    return true;
                }

                BaseBlock block = new BaseBlock();
                block.setBlock(new Location(p.getLocation().getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY()+0.5, p.getLocation().getBlockZ()+0.5));
                block.setLife(300);
                block.setMaxY(1);
                block.setDestinationBlocks(new ArrayList<Location>());

                Main.getBlocks().put(blockname.toUpperCase(), block);

                utils.sendMsg(p, "&bNuovo blocco di base impostato");
                return true;
            }

            if (args[0].equalsIgnoreCase("delblock")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                String blockname = args[1];

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                Main.getBlocks().remove(blockname.toUpperCase());

                utils.sendMsg(p, "&bBlocco di base rimosso");
                return true;
            }

            if (args[0].equalsIgnoreCase("adddestination")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                String blockname = args[1];

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                BaseBlock block = Main.getBlocks().get(blockname.toUpperCase());
                block.getDestinationBlocks().add(new Location(p.getLocation().getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY()+0.5, p.getLocation().getBlockZ()+0.5));

                utils.sendMsg(p, "&cNuova destinazione aggiunta");
                return true;
            }

            if (args[0].equalsIgnoreCase("deldestination")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                if (args.length < 3) {
                    utils.sendMsg(p, "&cInserisci un numero");
                    return true;
                }

                String blockname = args[1];

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                BaseBlock block = Main.getBlocks().get(blockname.toUpperCase());

                try {
                    block.getDestinationBlocks().remove(Integer.parseInt(args[2]));
                } catch (IndexOutOfBoundsException exc) {
                    System.out.println("&fSei andato fuori range");
                }


                utils.sendMsg(p, "&cDestinazione rimossa");
                return true;
            }

            if (args[0].equalsIgnoreCase("setrange")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                if (args.length < 3) {
                    utils.sendMsg(p, "&cInserisci un valore numerico intero");
                    return true;
                }

                String blockname = args[1];
                int value = 0;
                try {
                    value = Math.abs(Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    utils.sendMsg(p, "Inserisci un valore numerico intero valido");
                    return true;
                }

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                BaseBlock block = Main.getBlocks().get(blockname.toUpperCase());
                block.setMaxY(value);

                utils.sendMsg(p, "&bRaggio settato");
                return true;
            }

            if (args[0].equalsIgnoreCase("setlife")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                if (args.length < 3) {
                    utils.sendMsg(p, "&cInserisci un valore numerico intero");
                    return true;
                }

                String blockname = args[1];
                int value = 0;
                try {
                    value = Math.abs(Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    utils.sendMsg(p, "Inserisci un valore numerico intero valido");
                    return true;
                }

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                BaseBlock block = Main.getBlocks().get(blockname.toUpperCase());
                block.setLife(value);

                utils.sendMsg(p, "&bVita impostata");
                return true;
            }

            if (args[0].equalsIgnoreCase("starttrail")) {
                if (args.length < 2) {
                    utils.sendMsg(p, "&cInserisci un nome");
                    return true;
                }

                String blockname = args[1];

                if (!Main.getBlocks().containsKey(blockname.toUpperCase())) {
                    utils.sendMsg(p, "&cNon esiste nessun blocco con questo nome");
                    return true;
                }

                BaseBlock block = Main.getBlocks().get(blockname.toUpperCase());

                //Destinazione casuale:
                Location destination = utils.randomLocation(block.getDestinationBlocks());

                Trail trail = new Trail(Particle.CLOUD);
                trail.setDestination(utils.copyLocation(destination));
                trail.setStart(utils.copyLocation(block.getBlock()));
                trail.setPosition(utils.copyLocation(block.getBlock()));
                trail.setItem(utils.randomItem(Main.getItems()));
                trail.setSpeed((float)0.1);
                trail.setLife(block.getLife());
                trail.setRangeRotation(block.getMaxY());

                trail.start();
                return true;
            }



        }
        return true;
    }
}
