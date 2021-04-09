package minecraft.proxychat.zocker.pro;

import minecraft.proxychat.zocker.pro.listener.PlayerChatListener;
import minecraft.proxychat.zocker.pro.listener.PlayerChatLogListener;
import minecraft.proxychat.zocker.pro.listener.RedisMessageListener;
import minecraft.proxycore.zocker.pro.CorePlugin;
import minecraft.proxycore.zocker.pro.config.Config;
import minecraft.proxycore.zocker.pro.storage.StorageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;

public class Main extends CorePlugin {

	public static Config CHAT_CONFIG;
	public static Config CHAT_CHATS;
	public static boolean HAS_LUCKPERMS_SUPPORT;

	@Override
	public void onEnable() {
		super.setPluginName("MZP-ProxyChat");
		super.onEnable();

		this.buildConfig();
		this.registerCommand();
		this.registerListener();

		Chat.buildChats();

		HAS_LUCKPERMS_SUPPORT = ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms") != null;
	}

	@Override
	public void registerCommand() {
	}

	@Override
	public void registerListener() {
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

		pluginManager.registerListener(this, new PlayerChatListener());

		if (Main.CHAT_CONFIG.getBool("chat.log.enabled")) {
			pluginManager.registerListener(this, new PlayerChatLogListener());
		}

		if (StorageManager.isRedis()) {
			pluginManager.registerListener(this, new RedisMessageListener());
		}
	}

	@Override
	public void buildConfig() {
		// Config
		CHAT_CONFIG = new Config("config.yml", getPluginName());

		CHAT_CONFIG.set("chat.log.enabled", true, "0.0.1");
		CHAT_CONFIG.set("chat.log.message.enabled", true, "0.0.1");
		CHAT_CONFIG.set("chat.log.command.enabled", true, "0.0.1");

		CHAT_CONFIG.setVersion("0.0.1", true);

		// Chats
		CHAT_CHATS = new Config("chats.yml", getPluginName());

		CHAT_CHATS.set("chat.staff.key", "#", "0.0.1");
		CHAT_CHATS.set("chat.staff.permission.write", "group.trialstaff", "0.0.1");
		CHAT_CHATS.set("chat.staff.permission.read", "group.trialstaff", "0.0.1");
		CHAT_CHATS.set("chat.staff.prefix", "&6&l[Team]&7", "0.0.1");
		CHAT_CHATS.set("chat.staff.suffix", "", "0.0.1");
		CHAT_CHATS.set("chat.staff.format", "%prefix% %player_prefix% &7%sender% &6>>&f %message%", "0.0.1");

		CHAT_CHATS.setVersion("0.0.1", true);
	}
}
