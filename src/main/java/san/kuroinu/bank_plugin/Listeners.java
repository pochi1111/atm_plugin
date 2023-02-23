package san.kuroinu.bank_plugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;
import static san.kuroinu.bank_plugin.Bank_plugin.prefix;

public class Listeners implements Listener {
    private static Economy econ = null;
    @EventHandler
    public void atmClose(InventoryCloseEvent event) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
        Inventory inv = event.getInventory();
        if (event.getView().getTitle().equals(prefix+ChatColor.GREEN+"お金を入れて下さい") && inv.getSize() == 9) {
            //お金を入れて下さい、というタイトルのインベントリを閉じた時の処理
            //econを定義
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return;
            }
            econ = rsp.getProvider();
            event.getPlayer().sendMessage(prefix + ChatColor.GREEN + "お金を入れて下さい、というインベントリを閉じました");
            //お金を入れる
            ItemStack memo = null;
            int addMoney = 0;
            String lore = null;
            for (int i=0;i < 9;i++){
                memo = inv.getItem(i);
                if (memo == null) continue;
                if (!memo.getType().equals(Material.GOLD_INGOT)) continue;
                if(memo.getItemMeta().getLore() == null) continue;
                lore = memo.getLore().toString();
                event.getPlayer().sendMessage(lore);
                lore = lore.substring(1,lore.length()-2);
                event.getPlayer().sendMessage(lore);
                try {
                    Integer.parseInt(lore);
                } catch (NumberFormatException ex) {
                    event.getPlayer().sendMessage(prefix + ChatColor.RED + "loreについてのエラー");
                    return;
                }
                event.getPlayer().sendMessage(prefix + ChatColor.GREEN + "お金を"+lore+"入れました");
                addMoney += Integer.parseInt(lore)*memo.getAmount();
            }
            OfflinePlayer off_p = Bukkit.getOfflinePlayer(event.getPlayer().getName());
            EconomyResponse r = econ.depositPlayer(off_p,addMoney);
            if(r.transactionSuccess()) {
                event.getPlayer().sendMessage(prefix + ChatColor.GREEN + "お金を"+Double.toString(r.amount)+"入れました 残高は"+Double.toString(r.balance)+"円です");
            }else{
                event.getPlayer().sendMessage(prefix + ChatColor.RED + "お金を入れるのに失敗しました");
            }
        }
        return;
    }
}
