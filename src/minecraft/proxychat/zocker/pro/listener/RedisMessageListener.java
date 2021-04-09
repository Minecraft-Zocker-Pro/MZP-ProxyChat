package minecraft.proxychat.zocker.pro.listener;

import minecraft.proxychat.zocker.pro.Chat;
import minecraft.proxycore.zocker.pro.event.RedisMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;

public class RedisMessageListener implements Listener {

	@EventHandler
	public void onRedisMessage(RedisMessageEvent event) {
		if (!event.getReceiverName().equalsIgnoreCase("MZP-ProxyChat")) return;

		try {
			JSONObject packet = event.getPacket();
			if (packet.isNull("identify")) return;

			String identify = event.getPacket().getString("identify");
			if (identify.length() <= 0) return;

			if (identify.equalsIgnoreCase("SERVER_MESSAGE_CHAT")) {

				String message = packet.getString("message");
				if (message.length() <= 0) return;

				for (Chat chat : Chat.getChatList()) {
					String messageKey = message.substring(0, chat.getKey().length());
					if (messageKey.equalsIgnoreCase(chat.getKey())) {

						String messageFormatted = message.substring(0, chat.getKey().length() - 1) + message.substring(chat.getKey().length());

						for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
							if (proxiedPlayer.hasPermission(chat.getPermissionRead())) {
								proxiedPlayer.sendMessage(TextComponent.fromLegacyText(messageFormatted));
							}
						}

						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}