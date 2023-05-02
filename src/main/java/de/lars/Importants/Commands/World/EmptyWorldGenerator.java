package de.lars.Importants.Commands.World;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class EmptyWorldGenerator extends ChunkGenerator {

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        // Setze einen Block bei den Chunk-Koordinaten (0,0,0)
        if (x == 0 && z == 0) {
            chunkData.setBlock(0, 0, 0, Material.STONE);
        }

        // Setze alle anderen Blöcke auf Luft
        for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 256; yy++) {
                for (int zz = 0; zz < 16; zz++) {
                    if (xx != 0 || yy != 0 || zz != 0) {
                        chunkData.setBlock(xx, yy, zz, Material.AIR);
                    }
                }
            }
        }

        return chunkData;
    }

}