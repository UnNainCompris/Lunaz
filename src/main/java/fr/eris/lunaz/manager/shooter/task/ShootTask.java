package fr.eris.lunaz.manager.shooter.task;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.shoot.ShootBlockEvent;
import fr.eris.lunaz.event.shoot.ShootEntityEvent;
import fr.eris.lunaz.manager.playerdata.data.PlayerStatus;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.Tuple;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ShootTask {
    private final static HashMap<Player, Long> lastShoot = new HashMap<>();
    private long bulletLiveTick = 0;
    private final BukkitTask shootingTask;
    private final Player shooter;
    private final Weapon weapon;
    private final ItemStack weaponItem;
    private Location currentBulletLocation;
    private Location lastBulletLocation;
    private Vector bulletVector;
    private Particle.DustOptions trailOption;
    private List<Entity> loadedEntity;
    public ShootTask(Player shooter, Weapon weapon, ItemStack weaponItem) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.weaponItem = weaponItem;
        ShootTask.lastShoot.put(shooter, System.currentTimeMillis());
        setup();
        loadedEntity = currentBulletLocation.getWorld().getEntities();
        this.shootingTask = BukkitTasks.asyncTimer(this::update, 1, 1);
    }

    public static boolean canShoot(Player player) {
        if(!lastShoot.containsKey(player)) return true;
        return System.currentTimeMillis() - lastShoot.get(player) >= 50;
    }

    private void setup() {
        double pitchRad = ((shooter.getLocation().getPitch() + 90) * Math.PI) / 180;
        double yawRad  = ((shooter.getLocation().getYaw() + 90)  * Math.PI) / 180;
        Random random = new Random();
        double dispersion = getDispersion();
        bulletVector = new Vector(Math.sin(pitchRad) * Math.cos(yawRad), Math.cos(pitchRad), Math.sin(pitchRad) * Math.sin(yawRad));
        //bulletVector = applyDispersion(20, dispersion, bulletVector);
        bulletVector.multiply(weapon.getBulletSpeed());
        currentBulletLocation = shooter.getEyeLocation();
        lastBulletLocation = currentBulletLocation.clone();
        if(shooter.getTargetBlock(null, 150).getType() == Material.AIR ||
            shooter.getWorld().rayTraceEntities(shooter.getLocation(), shooter.getLocation().getDirection(), 150) == null)
                bulletLiveTick = 160;
        trailOption = new Particle.DustOptions(Color.fromRGB(255, 128, 0), 0.30F);
    }

    private double getDispersion() {
        double dispersion = weapon.getBulletDispersion();
        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(shooter.getUniqueId()).getStatus();
        if(shooter.isSneaking()) dispersion *= 0.75;
        if(shooter.isSprinting()) dispersion *= 1.5;
        if(playerStatus.isWeaponZooming()) dispersion *= 0.5;
        return dispersion;
    }

    private Vector applyDispersion(double dispersionCompensator, double dispersion, Vector defaultVector) {
        Random random = new Random();
        Vector nomalizedVector = defaultVector.clone().normalize();
        Vector updatedVector = defaultVector.clone();

        updatedVector.add(new Vector(nomalizedVector.getX() + random.nextDouble(-dispersion / dispersionCompensator, dispersion / dispersionCompensator),
            nomalizedVector.getX() + random.nextDouble(-dispersion / dispersionCompensator, dispersion / dispersionCompensator),
            nomalizedVector.getX() + random.nextDouble(-dispersion / dispersionCompensator, dispersion / dispersionCompensator)));

        return updatedVector;
    }

    private void update() {
        if(shootingTask.isCancelled()) {
            try {
                super.finalize();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(!LunaZ.isRunning()) {
            shootingTask.cancel();
            return;
        }

        updateBullet();
        updateTrail();
        if(collisionUpdate() || bulletLiveTick >= 200) {
            shootingTask.cancel();
            return;
        }
        lastBulletLocation = currentBulletLocation.clone();
        bulletLiveTick++;
    }

    private void updateBullet() {
        bulletVector.multiply(0.99f); // Use to slow down the bullet
        if(currentBulletLocation.getBlock().isLiquid()) {
            bulletVector.setX(bulletVector.getX() * 0.70f);
            bulletVector.setY(bulletVector.getY() * 0.90f);
            bulletVector.setZ(bulletVector.getZ() * 0.70f);
        }
        currentBulletLocation.add(bulletVector);
    }

    private void updateTrail() {
        updateTrailColor();
        spawnBulletTrail();
    }

    private void updateTrailColor() {
        Color color = trailOption.getColor();
        if(color.getRed() > 128) {
            trailOption = new Particle.DustOptions(Color.fromRGB(color.getRed() - 8, 128, color.getBlue() + 8),
                    Math.max(trailOption.getSize() - 0.01F, 0.10F));
        }
    }

    private void spawnBulletTrail() {
        World world = currentBulletLocation.getWorld();
        if(world == null) return;
        double distance = lastBulletLocation.distance(currentBulletLocation);
        Location particleLocation = lastBulletLocation.clone();
        /**
         * This vector is used to keep track of where the bullet goes so we can spawn the trail all along the simulated bullet trajectories
         */
        Vector vector = currentBulletLocation.toVector().clone().subtract(particleLocation.toVector()).normalize().multiply(0.4);

        for (double length = 0 ; length < Math.abs(distance); particleLocation.add(vector)) {
            if(!world.isChunkLoaded(currentBulletLocation.getChunk())) {
                continue;
            }
            if(isCollide(lastBulletLocation.getBlock())) break;
            world.spawnParticle(Particle.REDSTONE, particleLocation, 1, trailOption);
            length += 0.8;
        }
    }

    private boolean isCollide(Block block) {
        return !(block.isEmpty() || block.isLiquid() || block.isPassable());
    }

    private boolean collisionUpdate() {
        double blockTravel = lastBulletLocation.distance(currentBulletLocation);
        Location currentCheckingLocation = lastBulletLocation.clone();
        Vector vector = currentBulletLocation.toVector().clone().subtract(currentCheckingLocation.toVector()).normalize().multiply(0.2);
        for (double length = 0 ; length < Math.abs(blockTravel) ; length += 0.2) {
            currentCheckingLocation.add(vector);
            //System.out.println("Checking at " + currentCheckingLocation);
            List<LivingEntity> nearEntity = new ArrayList<>(getNearbyEntity(currentCheckingLocation, 0.5));
            for(LivingEntity livingEntity : nearEntity) {
                if(!livingEntity.equals(shooter)) {
                    return !collideWithEntity(livingEntity);
                }
                //weapon.damage(entity, shooter)
            }

            if(isCollide(currentCheckingLocation.getBlock())) {
                return !collideWithBlock(currentCheckingLocation.getBlock());
            }
        }
        return false;
    }

    public List<LivingEntity> getNearbyEntity(Location location, double distance) {
        List<LivingEntity> foundEntity = new ArrayList<>();
        //List<Chunk> chunkToCheck = getNearbyChunk(location, distance);
        /*for(Chunk chunk : chunkToCheck) {
            //System.out.println("Chunk check " + chunk.getX() + " - " + chunk.getZ());
            for(Entity entity : getEntityInChunk(chunk)) {
                if(!(entity instanceof LivingEntity)) {
                    continue;
                }
                LivingEntity livingEntity = (LivingEntity) entity;
                System.out.println("Entity " + entity.getLocation() + " -- " + entity + " - Distance - " + livingEntity.getLocation().distance(location));
                if(livingEntity.getLocation().distance(location) <= distance) foundEntity.add(livingEntity);
            }
        }*/
        if(loadedEntity == null) {
            return foundEntity;
        }
        for(Entity entity : loadedEntity) {
            if(!(entity instanceof LivingEntity)) {
                continue;
            }
            LivingEntity livingEntity = (LivingEntity) entity;
            //livingEntity.getBoundingBox().contains(location.toVector());
            //if(livingEntity.getLocation().distance(location) <= distance || livingEntity.getEyeLocation().distance(location) <= distance) foundEntity.add(livingEntity);
            if(livingEntity.getBoundingBox().contains(location.toVector())) foundEntity.add(livingEntity);
        }
        return foundEntity;
    }

    public List<Chunk> getNearbyChunk(Location location, double distance) {
        int[] offset = {-1,0,1};
        World world = location.getWorld();
        if(world == null) {
            return new ArrayList<>();
        }
        List<Chunk> nearbyChunk = new ArrayList<>();
        int baseX = location.getChunk().getX();
        int baseZ = location.getChunk().getZ();
        for(int x : offset) {
            for (int z : offset) {
                int newChunkX = (int) (Math.ceil((location.getX() + distance * x) / 16d) - 1d /* * 0.0625 = / 16 */);
                int newChunkY = (int) (Math.ceil((location.getZ() + distance * z) / 16d) - 1d /* * 0.0625 = / 16 */);
                if(!world.isChunkLoaded(newChunkX, newChunkY)) continue;
                Chunk chunk = world.getChunkAt(newChunkX, newChunkY);
                if(!containsChunk(chunk, nearbyChunk)) {
                    nearbyChunk.add(chunk);
                }
            }
        }

        return nearbyChunk;
    }

    public boolean containsChunk(Chunk toCheck, List<Chunk> listToCheck) {
        for(Chunk currentChunk : listToCheck) {
            return toCheck.getZ() == currentChunk.getZ() && toCheck.getX() == currentChunk.getX();
        }
        return false;
    }



    public boolean collideWithEntity(LivingEntity entity) {
        ShootEntityEvent event = new ShootEntityEvent(shooter, new Tuple<>(weapon, weaponItem), entity);
        LunaZ.getEventManager().postEventSync(event);
        return event.isCancelled();
    }

    public  List<Entity> getEntityInChunk(Chunk chunk) {
        List<Entity> entityInChunk = new ArrayList<>();
        if(loadedEntity == null) return entityInChunk;
        for(Entity entity : new ArrayList<>(loadedEntity)) {
            Chunk entityChunk = entity.getLocation().getChunk();
            if(entityChunk.getX() == chunk.getX() && entityChunk.getZ() == chunk.getZ()) {
                entityInChunk.add(entity);
            }
        }
        return entityInChunk;
    }

    public boolean collideWithBlock(Block block) {
        ShootBlockEvent event = new ShootBlockEvent(shooter, new Tuple<>(weapon, weaponItem), block);
        LunaZ.getEventManager().postEventSync(event);
        return event.isCancelled();
    }
}
