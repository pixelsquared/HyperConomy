package regalowl.hyperconomy;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hs {
	Hs(String args[], Player player) {
		HyperConomy hc = HyperConomy.hc;
		LanguageFile L = hc.getLanguageFile();
		EconomyManager em = hc.getEconomyManager();
		int amount;
		try {
			if (player.getGameMode() == GameMode.CREATIVE && hc.gYH().gQFC("config").gB("block-selling-in-creative-mode")) {
				player.sendMessage(L.get("CANT_SELL_CREATIVE"));
				return;
			}
			HyperPlayer hp = em.getHyperPlayer(player.getName());
			HyperEconomy he = hp.getHyperEconomy();
			if (em.inAnyShop(player)) {
				if (hp.hasSellPermission(em.getShop(player))) {
					if (args.length == 0) {
						amount = 1;
					} else {
						try {
							amount = Integer.parseInt(args[0]);
							if (amount > 10000) {
								amount = 10000;
							}
						} catch (Exception e) {
							String max = args[0];
							if (max.equalsIgnoreCase("max")) {
								HyperItem hi = he.getHyperItem(player.getItemInHand());
								amount = hi.count(player.getInventory());
							} else {
								player.sendMessage(L.get("HS_INVALID"));
								return;
							}
						}
					}
					HyperObject ho = he.getHyperObject(player.getItemInHand(), em.getShop(player));
					if (ho == null) {
						player.sendMessage(L.get("CANT_BE_TRADED"));
					} else {
						ItemStack iinhand = player.getItemInHand();
						if (new HyperItemStack(iinhand).hasenchants() == false) {
							Shop s = em.getShop(player);
							if (!s.isBanned(ho.getName())) {
								PlayerTransaction pt = new PlayerTransaction(TransactionType.SELL);
								pt.setHyperObject(ho);
								pt.setAmount(amount);
								pt.setTradePartner(s.getOwner());
								TransactionResponse response = hp.processTransaction(pt);
								response.sendMessages();
							} else {
								player.sendMessage(L.get("CANT_BE_TRADED"));
							}
						} else {
							player.sendMessage(L.get("CANT_BUY_SELL_ENCHANTED_ITEMS"));
						}
					}
				} else {
					player.sendMessage(L.get("NO_TRADE_PERMISSION"));
				}
			} else {
				player.sendMessage(L.get("MUST_BE_IN_SHOP"));
			}
		} catch (Exception e) {
			player.sendMessage(L.get("HS_INVALID"));
		}
	}
}
