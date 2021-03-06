package pokecube.legends.blocks.plants;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.PineFoliagePlacer;
import net.minecraftforge.common.IPlantable;
import pokecube.legends.init.BlockInit;

public class Ultra_Tree02 extends Tree {

	public static final TreeFeatureConfig ULTRA_TREE02_CONFIG = (new TreeFeatureConfig.Builder(
		   new SimpleBlockStateProvider(BlockInit.ULTRA_LOGUB02.get().getDefaultState()),
		   new SimpleBlockStateProvider(BlockInit.ULTRA_LEAVEUB02.get().getDefaultState()), 
		   new PineFoliagePlacer(3,0)))
			.baseHeight(11)
			.heightRandA(3)
			.heightRandB(2)
			.foliageHeight(3)
			.ignoreVines()
			.setSapling((IPlantable) BlockInit.ULTRA_SAPLING_UB02.get()).build();

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean b) {
		return Feature.NORMAL_TREE.withConfiguration(ULTRA_TREE02_CONFIG);
	}
	
}
