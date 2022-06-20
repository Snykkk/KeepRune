package Snykkk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Rune extends JavaPlugin implements Listener {
	
		File configF;
		FileConfiguration config;
		public static Plugin plugin;
		
		@Override
		public void onEnable() {
		    this.configF = new File(getDataFolder(), "config.yml");
		    this.config = YamlConfiguration.loadConfiguration(this.configF);
		    setDefault();
		    
		    Bukkit.getPluginManager().registerEvents(this, this);
		    

			Bukkit.getConsoleSender().sendMessage("§b|===========================================|");
			Bukkit.getConsoleSender().sendMessage("§b|              KeepRune MCFAMILY            |");
			Bukkit.getConsoleSender().sendMessage("§b|                    v1.0                   |");
			Bukkit.getConsoleSender().sendMessage("§b|                  _DinhThi_                |");
			Bukkit.getConsoleSender().sendMessage("§b|                sv.mcfamily.vn             |");
			Bukkit.getConsoleSender().sendMessage("§b|===========================================|");

		    
		    plugin = this;
		}
		
		public void onDisable() {
			Bukkit.getConsoleSender().sendMessage("§cKeepRune disable ...");
		}
		
		public void saveConfigF() {
		    try {
		      this.config.save(this.configF);
		    } catch (IOException e) {
		      
		      e.printStackTrace();
		    } 
		}
		
		public void loadConfigF() {
		    try {
		      this.config.load(this.configF);
		    }
		    catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
		      e.printStackTrace();
		    } 
		}
		  
		public void setDefault() {
		    if (!this.configF.exists()) {
		      ArrayList<String> lore = new ArrayList<String>();
		      lore.add(0, "&fKhông bị mất đồ khi chết nhưng");
		      lore.add(1, "&fmất &a1 &fviên ngọc.");
		      this.config.set("Lore", lore);
		      this.config.set("Material", "EMERALD");
		      this.config.set("RuneName", "&eNgọc thế thân");
		      this.config.set("Version", Double.valueOf(1.2D));
		      saveConfigF();
		    } 
		}
		  
		public void getDefault() {
		    ArrayList<String> lore = new ArrayList<String>();
		    lore.add(0, "&fKhông bị mất đồ khi chết nhưng");
		    lore.add(1, "&fmất &a1 &fviên ngọc.");
		    this.config.set("Lore", lore);
		    this.config.set("Material", "EMERALD");
		    this.config.set("RuneName", "&eNgọc thế thân");
		    this.config.set("Version", Double.valueOf(1.2D));
		    saveConfigF();
		}

		  
		public ItemStack getRune(int Amount) {
		    ItemStack item = null;
		    try {
		      item = new ItemStack(Material.valueOf(this.config.getString("Material")));
		    } catch (Exception e) {
		      getDefault();
		      loadConfigF();
		      saveConfig();
		    } 

		    
		    ArrayList<String> lore = new ArrayList<String>();
		    ItemMeta i = item.getItemMeta();
		    i.setDisplayName(this.config.getString("RuneName").replaceAll("&", "§"));
		    lore.addAll(this.config.getStringList("Lore"));

		    
		    ArrayList<String> lorelist = new ArrayList<String>();
		    for (String s : lore) {
		      lorelist.add(s.replaceAll("&", "§"));
		    }



		    
		    i.setLore(lorelist);
		    i.addEnchant(Enchantment.DURABILITY, 1, false);
		    
		    i.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		    item.setItemMeta(i);
		    item.setAmount(Amount);
		    return item;
		}
		
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		    if (cmd.getName().equalsIgnoreCase("keeprune")) {
		      
		      if (sender instanceof Player) {
		        Player p = (Player)sender;
		        if (!p.hasPermission("KeepRune.admin")) {
		          p.sendMessage("§cLệnh không tồn tại, sử dụng §e/help§c để xem danh sách câu lệnh!");
		          return true;
		        } 
		      } 
		      
		      if (args.length > 3) {
		        sender.sendMessage("§cLệnh không tồn tại, sử dụng §e/help§c để xem danh sách câu lệnh!");
		        return true;
		      } 

		      
		      if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
		        
		        this.setDefault();
		        this.loadConfigF();
		        this.saveConfigF();
		        
		        sender.sendMessage("§7[§eMCFAMILY§7] §aReload config thành công!");
		        return true;
		      } 
		      
		      if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
		        sender.sendMessage("§7§m--§6§m|-------§e§l §a?§e§lKeepRune§a?  §6§m-------|§7§m--");
		        sender.sendMessage("§aPhiên bản: §f " + this.config.getString("Version").toString());
		        sender.sendMessage("");
		        sender.sendMessage("§6Lệnh:");
		        sender.sendMessage("§6/keeprune give <tên player> <số lượng> §f: Trao ngọc thế thân cho player");
		        sender.sendMessage("§6/keeprune reload§f : load lại config");
		        sender.sendMessage("");
		        sender.sendMessage("§7§m                                              ");
		        return true;
		      } 
		      
		      if (args[0].equalsIgnoreCase("give")) {
		        if (Bukkit.getServer().getPlayer(args[1]) != null) {
		          int Amount = 0;
		          if (args.length != 2)
		          { try {
		              Integer.parseInt(args[2]);
		            } catch (NumberFormatException e) {
		              sender.sendMessage("§cLệnh không tồn tại, sử dụng §e/help§c để xem danh sách câu lệnh!");
		              return true;
		            } 
		            Amount = Integer.parseInt(args[2]);
		            
		            if (Amount == 0) Amount = 1;  } else { Amount = 1; }





		          
		          ItemStack item = this.getRune(Amount);


		          
		          Player target = Bukkit.getPlayer(args[1]);
		          target.getInventory().addItem(new ItemStack[] { item });
		          sender.sendMessage("§7[§eKeepRune§7] §eĐã trao " + Amount + " " + item.getItemMeta().getDisplayName().toString() + "§e cho §f" + target.getDisplayName());
		          target.sendMessage("§7[§eMCFAMILY§7] §eBạn nhận được " + Amount + " " + item.getItemMeta().getDisplayName().toString());
		          return true;
		        } 

		        
		        sender.sendMessage("§7[§eKeepRune§7] §cNgười chơi " + args[1] + " §ckhông online");
		        return true;
		      } 


		      
		      sender.sendMessage("§cLệnh không tồn tại, sử dụng §e/help§c để xem danh sách câu lệnh!");
		      return true;
		    } 

		    
		    return false;
		  }
		
		@EventHandler
		  public void onDeath(PlayerDeathEvent event) {
		    Player p = event.getEntity();
		    if (p.getWorld().getGameRuleValue("keepInventory").contains("true")) {
		      return;
		    }




		    
		    ItemStack item = this.getRune(0);



		    
		    PlayerInventory playerInventory = p.getInventory();
		    int check = 0;
		    for (int a = 0; a < p.getInventory().getSize(); a++) {

		      
		      ItemStack itemcheck = playerInventory.getItem(a);
		      
		      if (itemcheck != null && 
		        item.getItemMeta().equals(itemcheck.getItemMeta())) {
		        int itemcount = itemcheck.getAmount();
		        itemcount--;
		        check = 1;
		        item.setAmount(itemcount);
		        playerInventory.setItem(a, item);
		        if (item.getAmount() <= 0) playerInventory.setItem(a, null); 
		        event.setKeepInventory(true);
		        event.setKeepLevel(true);
		        p.sendMessage("§cBạn đã chết! §e1 viên " + item.getItemMeta().getDisplayName().toString() + " §eđã được dùng!");
		        
		        break;
		      } 
		    } 
		    if (check == 0)
		      p.sendMessage("§cBạn đã chết! §eHãy mua " + item.getItemMeta().getDisplayName().toString() + "§e để tránh mất đồ!"); 
		  }


}
