/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MinuteTaskRunnable extends BukkitRunnable {
	private BuildAssist plugin = BuildAssist.plugin;
	private HashMap<UUID, PlayerData> playermap = BuildAssist.playermap;


	public MinuteTaskRunnable() {
	}

	public void run() {
		this.playermap = BuildAssist.playermap;
		this.plugin = BuildAssist.plugin;
		if (this.playermap.isEmpty()) {
			return;
		}
		for (PlayerData playerdata : this.playermap.values()) {
			if (!playerdata.isOffline()) {
				Player player = this.plugin.getServer().getPlayer(
						playerdata.uuid);
				//経験値変更用のクラスを設定
				ExperienceManager expman = new ExperienceManager(player);

				int mines = BuildBlock.calcBuildBlock(player);

				playerdata.levelupdata(player, mines);
				if (playerdata.flyflag) {
					int flytime = playerdata.flytime;
					if (flytime == 0) {
						player.sendMessage(ChatColor.GREEN + "Fly効果が終了しました");
						playerdata.flyflag = false;
						player.setFlying(false);
					} else if (!expman.hasExp(10)) {
						player.sendMessage(ChatColor.RED
								+ "Fly効果の発動に必要な経験値が不足しているため、");
						player.sendMessage(ChatColor.RED + "Fly効果を終了しました");
						playerdata.flytime = 0;
						playerdata.flyflag = false;
						player.setFlying(false);
					} else {
						player.setFlying(true);
						player.sendMessage(ChatColor.GREEN + "Fly効果はあと"
								+ flytime + "分です");
						playerdata.flytime -= 1;
						expman.changeExp(-10);
					}
				}
			}
		}
	}
}
