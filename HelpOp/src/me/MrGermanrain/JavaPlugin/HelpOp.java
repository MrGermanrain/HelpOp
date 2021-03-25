package me.MrGermanrain.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class HelpOp extends JavaPlugin
{
    @Override
    public void onEnable() 
    {
    	this.getConfig().options().copyDefaults(true);
    	this.saveConfig();
        @SuppressWarnings("unused")
		PluginMetrics metrics = new PluginMetrics(this, 10617);
    }
    
    @Override
    public void onDisable() 
    {

    }
	
    public void broadcastMessage(Player player, String configName, String message)
    {
    	String broadcastText = this.getConfig().getString(configName);
    	broadcastText = broadcastText.replace("%player%", player.getName());
    	broadcastText = broadcastText.replace("%message%", message);
    	broadcastText = broadcastText.replace("%prefix%", this.getConfig().getString("Prefix"));
    	broadcastText = ChatColor.translateAlternateColorCodes('&', broadcastText);
    	
		Bukkit.broadcast(broadcastText, "helpop.staff");
		
		
		TextComponent recieverMessage = new TextComponent();
		
		TextComponent tp = new TextComponent();
		tp.setText(ChatColor.translateAlternateColorCodes('&', "&a[Teleport]"));
		tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
		
		tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "&aClick to teleport to player!"))));
		recieverMessage.addExtra(tp);
		
		
		for( Player p : Bukkit.getOnlinePlayers() ) 
		{
			if(p.hasPermission("helpop.staff"))
			{ 
				p.spigot().sendMessage(recieverMessage);
			}
		}
    }
    
    public void sendPlayerMessage(Player player, String configName, String message)
    {
    	String messageSent = this.getConfig().getString(configName);
    	messageSent = messageSent.replace("%player%", player.getName());
    	messageSent = messageSent.replace("%message%", message);
    	messageSent = messageSent.replace("%prefix%", this.getConfig().getString("Prefix"));
    	messageSent = ChatColor.translateAlternateColorCodes('&', messageSent);
    	    	
    	player.sendMessage(messageSent);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player) 
        {
        	Player player = (Player) sender;
        	if (cmd.getName().equalsIgnoreCase("HelpOp"))
        	{
        		//reload command
    			if(args.length == 1)
    			{
    				if (args[0].equalsIgnoreCase("reload"))
    				{
    	        		if(sender.hasPermission("helpop.reload"))
    	        		{
    	        			this.reloadConfig();
    	        			sendPlayerMessage(player, "ReloadComplete", "");
    	        			return true;
    	        		}
    	        		else
    	        		{
    	        			sendPlayerMessage(player, "NoPermission", "");
    	        			return true;
    	        		}
    				}
    			}
        		
        		//Checking if the player included a message.
				if (args.length > 0) 
				{
					int count = 0;
					
					//Counting staff online.
					for (Player onlinePlayers : Bukkit.getOnlinePlayers()) 
					{
						if(onlinePlayers.hasPermission("helpop.staff"))
						{
							count++;							
						}
					}
					
					//Getting the player's message.
					String message = "";
					for (int i = 0; i < args.length; ++i) 
					{
						message = message + args[i] + " ";
					}
					
					//Checking if more than 1 staff member is online, then sending the message.
					if(count > 0)
					{						
						broadcastMessage(player, "HelpOpMessage", message);
						sendPlayerMessage(player, "ConfirmSent", message);
					}
					else
					{
						sendPlayerMessage(player, "NoStaffOnline", message);
					}
				}
				else
				{
					sendPlayerMessage(player, "WrongSyntax", "");
				}

				return true;
        	}
        }
        return true;
    }

}
