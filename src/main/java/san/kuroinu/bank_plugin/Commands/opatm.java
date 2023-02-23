package san.kuroinu.bank_plugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static san.kuroinu.bank_plugin.Bank_plugin.prefix;

public class opatm implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player e = (Player) sender;
        Inventory inv = e.getInventory();
        if (args.length != 1){
            sender.sendMessage(prefix + ChatColor.RED + "引数の数が違います");
            return true;
        }
        if (inv.firstEmpty() == -1) {
            e.sendMessage(prefix + ChatColor.RED + "インベントリに空きがありません");
            return true;
        }
        if (args.length > 1) {
            e.sendMessage(prefix + ChatColor.RED + "引数が多すぎます");
            return true;
        }
        //引数が数字かどうか
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            e.sendMessage(prefix + ChatColor.RED + "引数は数字で入力してください");
            return true;
        }
        //引数が0以下かどうか
        if (Integer.parseInt(args[0]) <= 0) {
            e.sendMessage(prefix + ChatColor.RED + "引数は0より大きい数字で入力してください");
            return true;
        }
        if (Integer.parseInt(args[0]) > 1000000) {
            e.sendMessage(prefix + ChatColor.RED + "お金は100万円までしか入れられません");
            return true;
        }
        if (!sender.isOp()){
            e.sendMessage(prefix + ChatColor.RED + "このコマンドはOPのみ実行できます");
            return true;
        }
        inv.addItem(createItemStack(Material.GOLD_INGOT, 1, ChatColor.GOLD + "通貨", args[0] + "円"));
        e.sendMessage(prefix + ChatColor.GREEN + "お金を入れました");
        return true;
    }
    public ItemStack createItemStack(Material material, int amount, String name, String lore){
        //ItemStackを作成するメソッド
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
