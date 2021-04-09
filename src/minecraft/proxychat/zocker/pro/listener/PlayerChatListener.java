package minecraft.proxychat.zocker.pro.listener;

import minecraft.proxychat.zocker.pro.Chat;
import minecraft.proxychat.zocker.pro.Main;
import minecraft.proxycore.zocker.pro.storage.StorageManager;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisCacheManager;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketBuilder;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketIdentifyType;
import minecraft.proxycore.zocker.pro.storage.cache.redis.packet.server.RedisServerMessagePacket;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerChatListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(ChatEvent event) {
		if (event.isCancelled()) return;
		if (event.isProxyCommand() || event.isCommand()) return;

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

		for (Chat chat : Chat.getChatList()) {
			String messageKey = event.getMessage().substring(0, chat.getKey().length());
			if (messageKey.equalsIgnoreCase(chat.getKey())) {
				if (!proxiedPlayer.hasPermission(chat.getPermissionWrite())) continue;

				event.setCancelled(true);

				String message = chat.getFormat()
					.replace("%message%", event.getMessage().substring(1))
					.replace("%sender%", proxiedPlayer.getName())
					.replace("%prefix%", chat.getPrefix())
					.replace("%suffix%", chat.getSuffix());

				if (Main.HAS_LUCKPERMS_SUPPORT) {
					LuckPerms luckPerms = LuckPermsProvider.get();
					User user = luckPerms.getUserManager().getUser(proxiedPlayer.getUniqueId());

					if (user != null) {
						QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(proxiedPlayer);
						CachedMetaData cachedMetaData = user.getCachedData().getMetaData(queryOptions);

						if (cachedMetaData.getPrefix() != null) {
							message = message.replace("%player_prefix%", cachedMetaData.getPrefix());

							if (cachedMetaData.getSuffix() != null) {
								message = message.replace("%player_suffix%", cachedMetaData.getSuffix());
							}
						}
					}
				}

				message = message.replace("&", "§");

				TextComponent textComponent = new TextComponent(message);

				if (StorageManager.isRedis()) {
					RedisPacketBuilder redisPacketBuilder = new RedisPacketBuilder();
					redisPacketBuilder.setPluginName("MZP-ProxyChat");
					redisPacketBuilder.setSenderName(StorageManager.getServerName());
					redisPacketBuilder.setReceiverName("MZP-ProxyChat");
					redisPacketBuilder.setServerTargetName("PROXYCORE");

					redisPacketBuilder.addPacket(new RedisServerMessagePacket(messageKey + message, RedisPacketIdentifyType.SERVER_MESSAGE_CHAT));

					new RedisCacheManager().publish(redisPacketBuilder.build());
					return;
				}

				for (ProxiedPlayer proxiedPlayerReceiver : ProxyServer.getInstance().getPlayers()) {
					if (proxiedPlayerReceiver.hasPermission(chat.getPermissionRead())) {
						proxiedPlayerReceiver.sendMessage(textComponent);
					}
				}
			}
		}
	}
}
