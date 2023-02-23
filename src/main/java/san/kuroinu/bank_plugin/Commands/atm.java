package san.kuroinu.bank_plugin.Commands;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;
import static san.kuroinu.bank_plugin.Bank_plugin.prefix;

public class atm implements CommandExecutor {
    private static Economy econ = null;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.RED + "このコマンドはプレイヤーから実行してください");
            return true;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
        Player e = (Player) sender;
        EconomyResponse r = econ.bankBalance(e.getName());
        if(args.length == 0){
            //お金を入れて下さい、というタイトルのインベントリを表示
            Inventory inv = e.getServer().createInventory(null, 9, prefix+ChatColor.GREEN+"お金を入れて下さい");
            e.openInventory(inv);
    }else{
            //プレイヤーのインベントリに空きがあるかどうか
            //空きがある場合はお金を入れる
            //空きがない場合はお金を入れられない
            //お金を入れたらお金を入れたことを表示
            Inventory inv = e.getInventory();
            if(inv.firstEmpty() == -1){
                e.sendMessage(prefix+ChatColor.RED+"インベントリに空きがありません");
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
            if(Integer.parseInt(args[0]) <= 0){
                e.sendMessage(prefix + ChatColor.RED + "引数は0より大きい数字で入力してください");
                return true;
            }
            if(Integer.parseInt(args[0]) > 1000000){
                e.sendMessage(prefix + ChatColor.RED + "お金は100万円までしか入れられません");
                return true;
            }
            if (r.balance > Integer.parseInt(args[0])){
                e.sendMessage(prefix + ChatColor.RED + "お金が足りません");
                return true;
            }
            r = econ.withdrawPlayer(e.getName(), Integer.parseInt(args[0]));
            if(r.transactionSuccess()) {
                inv.addItem(createItemStack(Material.GOLD_INGOT, 1, ChatColor.GOLD+"通貨", ChatColor.GOLD+args[0]+"円"));
            } else {
                sender.sendMessage(String.format("エラー: %s :運営に報告してください", r.errorMessage));
            }
            e.sendMessage(prefix+ChatColor.GREEN+"お金を入れました");
        }
        return false;
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
