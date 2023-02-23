package san.kuroinu.bank_plugin.Commands;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class bank implements CommandExecutor {

    private static Economy econ = null;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String prefix = ChatColor.AQUA + "[Bank]" + ChatColor.RESET;
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
        OfflinePlayer p = Bukkit.getOfflinePlayer(e.getName());
        double r = econ.getBalance(p);
        if (args.length == 0) {
            e.sendMessage(prefix + ChatColor.GREEN + "あなたの所持金は" + r + "です");
            return true;
        }else{
            e.sendMessage(prefix + ChatColor.RED + "引数が多すぎます");
        }
        return false;
    }
}
