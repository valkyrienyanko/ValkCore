package me.valk.valkcore.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.primitives.Ints;

public class GUINavigation extends GUI {
	public static final Map<UUID, GUINavigation> invNav = new HashMap<>();
	private final Player p;
	private final int size;
	private final String title;
	private int page;
	private boolean itemBuilder;
	private final List<ItemStack> items;

	public GUINavigation(Player p, List<ItemStack> items, int rows, String title) {
		this.p = p;
		//noinspection UnstableApiUsage
		this.size = Ints.constrainToRange(rows, 2, 6) * 9;
		this.title = title;
		this.items = items;
		this.page = 1;
		this.itemBuilder = false;
		invNav.put(p.getUniqueId(), this);
	}

	public Inventory getInv() {
		Inventory inv = Bukkit.createInventory(p, size, title);
		inv.setItem(size - 1, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		inv.setItem(size - 5, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		inv.setItem(size - 9, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

		final int PAGE_SIZE = size - 9;

		int n = PAGE_SIZE * (page - 1);

		for (int i = n; i < n + PAGE_SIZE; i++) {
			if (i < items.size()) {
				inv.setItem(i - n, new ItemStack(items.get(Math.min(items.size() - 1, i))));
			}
		}

		return inv;
	}

	public void setItemBuilder(boolean value) {
		itemBuilder = value;
	}

	public boolean isItemBuilder() {
		return itemBuilder;
	}

	void nextPage() {
		int maxPages = (int) Math.ceil(items.size() / (double) (size - 9));
		page = Math.min(maxPages, page + 1);
		openInv();
	}

	void prevPage() {
		page = Math.max(1, page - 1);
		openInv();
	}

	public void openInv() {
		p.openInventory(getInv());
	}

	public void closeInv() {
		p.closeInventory();
	}

	String getTitle() {
		return title;
	}

	int getSize() {
		return size;
	}

	protected int getItemSlot(int slot) {
		return (page - 1) * (size - 9) + slot;
	}

	int getPrevPageSlot() {
		return size - 9;
	}

	int getBackSlot() {
		return size - 5;
	}

	int getNextPageSlot() {
		return size - 1;
	}

	protected int getPage() {
		return page;
	}
}
