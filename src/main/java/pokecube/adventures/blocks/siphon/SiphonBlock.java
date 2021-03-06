package pokecube.adventures.blocks.siphon;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import pokecube.adventures.blocks.genetics.extractor.ExtractorBlock;
import pokecube.core.blocks.InteractableBlock;
import pokecube.core.blocks.InteractableHorizontalBlock;

public class SiphonBlock extends InteractableHorizontalBlock implements IWaterLoggable
{
	private static final Map<Direction, VoxelShape> SIPHON  = new HashMap<>();
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty   FIXED  = BooleanProperty.create("fixed");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // Precise selection box
    static
    {// @formatter:off
    	SiphonBlock.SIPHON.put(Direction.NORTH,
          VoxelShapes.combineAndSimplify(Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
            VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 1, 1, 15, 7, 15),
              VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 12, 1, 15, 16, 15),
                VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2, 7, 2, 14, 12, 14),
                  VoxelShapes.combineAndSimplify(Block.makeCuboidShape(6.5, 1, 0, 9.5, 4, 16),
                    Block.makeCuboidShape(0, 1, 6.5, 16, 4, 9.5),
                                IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR),
                          IBooleanFunction.OR), IBooleanFunction.OR)
        );
    	SiphonBlock.SIPHON.put(Direction.EAST,
		VoxelShapes.combineAndSimplify(Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
        VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 1, 1, 15, 7, 15),
          VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 12, 1, 15, 16, 15),
            VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2, 7, 2, 14, 12, 14),
              VoxelShapes.combineAndSimplify(Block.makeCuboidShape(6.5, 1, 0, 9.5, 4, 16),
                Block.makeCuboidShape(0, 1, 6.5, 16, 4, 9.5),
                            IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR),
                      IBooleanFunction.OR), IBooleanFunction.OR)
        );
    	SiphonBlock.SIPHON.put(Direction.SOUTH,
		VoxelShapes.combineAndSimplify(Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
        VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 1, 1, 15, 7, 15),
          VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 12, 1, 15, 16, 15),
            VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2, 7, 2, 14, 12, 14),
              VoxelShapes.combineAndSimplify(Block.makeCuboidShape(6.5, 1, 0, 9.5, 4, 16),
                Block.makeCuboidShape(0, 1, 6.5, 16, 4, 9.5),
                            IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR),
                      IBooleanFunction.OR), IBooleanFunction.OR)
        );
    	SiphonBlock.SIPHON.put(Direction.WEST,
		VoxelShapes.combineAndSimplify(Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
        VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 1, 1, 15, 7, 15),
          VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1, 12, 1, 15, 16, 15),
            VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2, 7, 2, 14, 12, 14),
              VoxelShapes.combineAndSimplify(Block.makeCuboidShape(6.5, 1, 0, 9.5, 4, 16),
                Block.makeCuboidShape(0, 1, 6.5, 16, 4, 9.5),
                            IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR),
                      IBooleanFunction.OR), IBooleanFunction.OR)
        );
    }// @formatter:on

    // Precise selection box
    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
            final ISelectionContext context)
    {
        return SiphonBlock.SIPHON.get(state.get(SiphonBlock.FACING));
    }
    
    public SiphonBlock(final Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(SiphonBlock.FACING, Direction.NORTH).with(
        		SiphonBlock.FIXED, false).with(WATERLOGGED, false));
    }
    
    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(SiphonBlock.FACING);
        builder.add(SiphonBlock.FIXED);
        builder.add(SiphonBlock.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        boolean flag = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        return this.getDefaultState().with(SiphonBlock.FACING, context.getPlacementHorizontalFacing().getOpposite())
                .with(SiphonBlock.FIXED, false).with(WATERLOGGED, flag);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos,
            BlockPos facingPos) 
    {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IFluidState getFluidState(BlockState state) 
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
    {
        return new SiphonTile();
    }

    @Override
    public VoxelShape getRenderShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos)
    {
        return InteractableBlock.RENDERSHAPE;
    }

    @Override
    public boolean hasTileEntity(final BlockState state)
    {
        return true;
    }

}
