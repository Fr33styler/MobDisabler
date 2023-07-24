package ro.fr33styler.mobdisabler;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobDisabler extends JavaPlugin implements Listener {

    private final Set<UUID> disabledWorlds = new HashSet<>();

    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().runTask(this, () -> {
            for (String name : getConfig().getStringList("DisabledWorlds")) {
                World world = getServer().getWorld(name);
                if (world != null) {
                    disabledWorlds.add(world.getUID());
                }
            }
        });
    }

    public void onDisable() {
        disabledWorlds.clear();
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        UUID worldUID = event.getEntity().getLocation().getWorld().getUID();
        if (disabledWorlds.contains(worldUID) && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(true);
        }
    }

}
