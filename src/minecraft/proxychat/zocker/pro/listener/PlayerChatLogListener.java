package minecraft.proxychat.zocker.pro.listener;

import minecraft.proxychat.zocker.pro.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerChatLogListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChatLog(ChatEvent event) {
		if (event.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

			if (event.isCommand() || event.isProxyCommand()) {
				if (Main.CHAT_CONFIG.getBool("chat.log.command.enabled")) {
					System.out.println("§6§l[LOG]§f " + proxiedPlayer.getServer().getInfo().getName() + " - " + proxiedPlayer.getName() + " : " + event.getMessage());
					return;
				}

				return;
			}

			if (Main.CHAT_CONFIG.getBool("chat.log.message.enabled")) {
				System.out.println("§6§l[LOG]§f " + proxiedPlayer.getServer().getInfo().getName() + " - " + proxiedPlayer.getName() + " : " + event.getMessage());
			}
		}
	}
}
