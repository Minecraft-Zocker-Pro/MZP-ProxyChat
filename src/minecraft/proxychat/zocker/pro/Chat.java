package minecraft.proxychat.zocker.pro;

import java.util.ArrayList;
import java.util.List;

public class Chat {

	private static final List<Chat> CHAT_LIST = new ArrayList<>();

	private final String key;
	private final String permissionWrite;
	private final String permissionRead;
	private final String prefix;
	private final String suffix;
	private final String format;

	public Chat(String key, String permissionWrite, String permissionRead, String prefix, String suffix, String format) {
		this.key = key;
		this.permissionWrite = permissionWrite;
		this.permissionRead = permissionRead;
		this.prefix = prefix;
		this.suffix = suffix;
		this.format = format;

		CHAT_LIST.add(this);
	}

	public String getKey() {
		return key;
	}

	public String getPermissionWrite() {
		return permissionWrite;
	}

	public String getPermissionRead() {
		return permissionRead;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getFormat() {
		return format;
	}

	public static void buildChats() {
		for (String chatName : Main.CHAT_CHATS.getSection("chat").getKeys()) {
			new Chat(
				Main.CHAT_CHATS.getString("chat." + chatName + ".key"),
				Main.CHAT_CHATS.getString("chat." + chatName + ".permission.write"),
				Main.CHAT_CHATS.getString("chat." + chatName + ".permission.read"),
				Main.CHAT_CHATS.getString("chat." + chatName + ".prefix"),
				Main.CHAT_CHATS.getString("chat." + chatName + ".suffix"),
				Main.CHAT_CHATS.getString("chat." + chatName + ".format"));
		}

		System.out.println("Added " + CHAT_LIST.size() + " custom chats");
	}

	public static List<Chat> getChatList() {
		return CHAT_LIST;
	}
}
