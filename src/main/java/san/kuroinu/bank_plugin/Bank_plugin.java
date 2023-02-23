package san.kuroinu.bank_plugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import san.kuroinu.bank_plugin.Commands.atm;
import san.kuroinu.bank_plugin.Commands.bank;

public final class Bank_plugin extends JavaPlugin {
    public static JavaPlugin plugin;
    private static Economy econ = null;

    public static String prefix = ChatColor.AQUA + "[Bank]" + ChatColor.RESET;
    private Listeners listeners;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        this.listeners = new Listeners();
        Bukkit.getPluginManager().registerEvents(this.listeners, this);
        getCommand("bank").setExecutor(new bank());
        getCommand("atm").setExecutor(new atm());
        super.onEnable();
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
